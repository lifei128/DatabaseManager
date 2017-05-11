package Backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PipelineManager {

    public static ResultSet getColumnsForTable(Connection connection, String schema, String tableName) throws SQLException{
        Statement stmt = connection.createStatement();
        
        return stmt.executeQuery("select * from information_schema.columns where table_name = '" + tableName + "' and table_schema='" + schema + "';");
    }

    public static ResultSet getColumnsForIndex(Connection connection, String schema, String indexName) throws SQLException{
        Statement stmt = connection.createStatement();
    
        return stmt.executeQuery("select * from information_schema.columns;");
    }

    static ResultSet getSchemasFor(Connection connection, String user) throws SQLException{
        Statement stmt = connection.createStatement();
        return stmt.executeQuery("select pn.nspname as schema_name from pg_namespace pn inner join pg_user pu on pu.usesysid= pn.nspowner where pu.usename = '" + user + "';");
    }

    static ResultSet getTablesFor(Connection connection, String user) throws SQLException{
        Statement stmt = connection.createStatement();
        return stmt.executeQuery("select * from information_schema.tables;");
    }

    static ResultSet getIndexesFor(Connection connection, String user) throws SQLException{
        Statement stmt = connection.createStatement();
        return stmt.executeQuery("select pi.schemaname as schemaname, pi.indexname as indexname from pg_indexes pi inner join pg_tables pt on pi.tablename = pt.tablename;");
    }
}
