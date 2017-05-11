package UI;
import Backend.PipelineManager;
import Backend.ConnectionData;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class Manager extends javax.swing.JFrame {
    private ArrayList<ConnectionData> connectionsList;
    private DefaultMutableTreeNode rootObjectsNode;
    private JTree ConnectionsTree;
    private JTextPane sqlPane;
    private JScrollPane columnsScrollPane;
    private JScrollPane dataScrollPane;
    private JScrollPane ddlPane;
    private JTable columnsPane;
    private JScrollPane scrollpaneColumns;
    
    /**
     * Creates new form Manager
     */
    public Manager() {
        this.connectionsList = new ArrayList<>();
        initComponents();
        initObjectsTree();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        objectsTree = new javax.swing.JTree();
        MainPane = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        nuevaConexionMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jScrollPane1.setViewportView(objectsTree);

        jSplitPane1.setLeftComponent(jScrollPane1);
        jSplitPane1.setRightComponent(MainPane);

        jMenu1.setText("Conexiones");

        nuevaConexionMenuItem.setText("Nueva Conexión");
        nuevaConexionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevaConexionMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(nuevaConexionMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1749, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nuevaConexionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevaConexionMenuItemActionPerformed
        ConnectionForm c = new ConnectionForm(this);
        c.setVisible(true);
    }//GEN-LAST:event_nuevaConexionMenuItemActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        for(ConnectionData c : connectionsList){
            try {
                c.connection.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Manager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane MainPane;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuItem nuevaConexionMenuItem;
    private javax.swing.JTree objectsTree;
    // End of variables declaration//GEN-END:variables

    void addConnection(ConnectionData connectionData) {
        try{
            this.connectionsList.add(connectionData);
            rootObjectsNode.add(connectionData.getTreeNode());
            ((DefaultTreeModel)ConnectionsTree.getModel()).reload(rootObjectsNode);
        }catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    ConnectionData getConnectionFromList(TreeNode[] connectionName){
        for(ConnectionData con : connectionsList){
            if(con.connectionName.equals(connectionName[1].toString())){
                return con;
            }
        }
        
        return null;
    }
    
    private void updateColumnsTables() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)ConnectionsTree.getLastSelectedPathComponent();
        if(node != null && node.getLevel() == 5){
                        System.out.println("parent: "+node.getParent());
            System.out.println("Node: "+node);
            for(TreeNode t : node.getPath())
                System.out.println("--> "+t);
            
            System.out.println(node.getLevel());

            if("Tables".equals(node.getParent().toString())){
                ConnectionData conn = getConnectionFromList(node.getPath());
                
                try(ResultSet rs = PipelineManager.getColumnsForTable(conn.connection, node.getPath()[3].toString(), node.getPath()[5].toString())){
                    columnsPane = new JTable(ConnectionData.buildTableModel(rs));
                    columnsScrollPane.setViewportView(columnsPane);
                }catch(SQLException ex){
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Something went wrong",JOptionPane.ERROR_MESSAGE);
                }
            }else if("Indexes".equals(node.getParent().toString())){
                ConnectionData conn = getConnectionFromList(node.getPath());
                try(ResultSet rs = PipelineManager.getColumnsForIndex(conn.connection, node.getPath()[3].toString(),node.getPath()[5].toString());){
                    columnsPane = new JTable(ConnectionData.buildTableModel(rs));
                    columnsScrollPane.setViewportView(columnsPane);
                }catch(SQLException ex){
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Something went wrong",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void updateShowSQL() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)ConnectionsTree.getLastSelectedPathComponent();
    }
    

    private void initObjectsTree() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        rootObjectsNode = new DefaultMutableTreeNode("Connections");

        //create the tree by passing in the root node
        ConnectionsTree = new JTree(rootObjectsNode);
        ConnectionsTree.addTreeSelectionListener((TreeSelectionEvent tse) -> {
            updateColumnsTables();
            updateShowSQL();
        });
        
        jScrollPane1.setViewportView(ConnectionsTree);
        sqlPane = new JTextPane();
        
        columnsScrollPane = new JScrollPane();
        dataScrollPane = new JScrollPane();
        ddlPane = new JScrollPane();
        ddlPane.setViewportView(sqlPane);
        MainPane.addTab("Columns", columnsScrollPane);
        MainPane.addTab("Data", dataScrollPane);
        MainPane.addTab("DDL", ddlPane);
    }
}
