package Backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class ConnectionData {
    public final Connection connection;
    private final String url;
    private final String database;
    private final String user;
    public final String connectionName;
    private DefaultMutableTreeNode treeNode;
    private HashMap<String, DefaultMutableTreeNode> schemasMap;
    
    public ConnectionData(String connectionName, String user, String database, String url, Connection connection) {
        this.connectionName = connectionName;
        this.user = user;
        this.database = database;
        this.url = url;
        this.connection = connection;
        this.treeNode = new DefaultMutableTreeNode(connectionName);
        schemasMap = new HashMap<String,DefaultMutableTreeNode>();
        this.initDataBaseObjects();
    }
    
    public MutableTreeNode getTreeNode() {
        return treeNode;
    }
    
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

    private void initDataBaseObjects() {
        initSchemas();
        initTables();
        initIndexes();
    }
    
    private void initSchemas() {
        DefaultMutableTreeNode schemas = new DefaultMutableTreeNode("Schemas");
        treeNode.add(schemas);
        try(ResultSet rs = PipelineManager.getSchemasFor(connection, user);) {
            while (rs.next()) {
                String schemaName = rs.getString("schema_name");
                schemasMap.put(schemaName, new DefaultMutableTreeNode(schemaName));
                schemas.add(schemasMap.get(schemaName));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTables() {
        DefaultMutableTreeNode tables = null;
        try(ResultSet rs = PipelineManager.getTablesFor(connection, user);) {
            String schemaName = "";
            while (rs.next()) {
                String nextSchemaName = rs.getString("table_schema");
                DefaultMutableTreeNode schema = schemasMap.get(nextSchemaName);
                if(!schemaName.equals(nextSchemaName))
                {
                    schemaName = nextSchemaName;
                    tables = new DefaultMutableTreeNode("Tables");
                    schema.add(tables);
                }
                if(tables != null)
                    tables.add(new DefaultMutableTreeNode(rs.getString("table_name")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initIndexes() {
        DefaultMutableTreeNode indexes = null;
        try(ResultSet rs = PipelineManager.getIndexesFor(connection, user);) {
            String schemaName = "";
            while (rs.next()) {
                String nextSchemaName = rs.getString("schemaname");
                DefaultMutableTreeNode schema = schemasMap.get(nextSchemaName);
                if(!schemaName.equals(nextSchemaName))
                {
                    schemaName = nextSchemaName;
                    indexes = new DefaultMutableTreeNode("Indexes");
                    schema.add(indexes);
                }
                if(indexes!=null)
                    indexes.add(new DefaultMutableTreeNode(rs.getString("indexname")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
