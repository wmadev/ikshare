/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data.oracle;

import ikshare.domain.SharedFile;
import ikshare.domain.SharedFolder;
import ikshare.domain.SharedItem;
import ikshare.server.data.DatabaseException;
import ikshare.server.data.FileStorage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *
 * @author awosy
 */
public class OracleFileStorage implements FileStorage {
    private static OracleFileStorage instance;
    private ResourceBundle bundle;
    
    private OracleFileStorage(){
        bundle = ResourceBundle.getBundle("ikshare.server.data.oracle.DBConstants");
    }
    public static OracleFileStorage getInstance(){
        if(instance == null){
            instance = new OracleFileStorage();
        }
        return instance;
    }

    public boolean addShares(String accountName,SharedFolder root) throws DatabaseException {
        boolean success = false;
        Connection conn = OracleDatabaseFactory.getConnection();
        try {
            conn.setAutoCommit(false);
            addFolder(root,conn,0);
            conn.commit();
        } catch (SQLException e) {
            success = false;
            try{
                conn.rollback();
            }
            catch(SQLException ex){
                ex.printStackTrace();
                throw new DatabaseException(bundle.getString("ERROR_Database"));
            }
            e.printStackTrace();
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        } finally {
            try{
                conn.setAutoCommit(true);
            }
            catch(SQLException ex){
                ex.printStackTrace();
                throw new DatabaseException(bundle.getString("ERROR_Database"));
            }
            OracleDatabaseFactory.freeConnection(conn);
        }
        return success;
    }
    private void addFolder(SharedItem root, Connection conn,int parent) throws SQLException {
        if(root.isFolder() && root.getSharedItems().size()>0){
            PreparedStatement stmtAddFolder = conn.prepareStatement(bundle.getString("addFolder"));
            stmtAddFolder.setString(1,((SharedFolder)root).getPath());
            stmtAddFolder.setString(2, ((SharedFolder)root).getAccountName());
            stmtAddFolder.setInt(3, parent);
            stmtAddFolder.setString(4, ((SharedFolder)root).getName());
            stmtAddFolder.executeUpdate();
            stmtAddFolder = conn.prepareStatement(bundle.getString("getLastFolderId"));
            ResultSet result = stmtAddFolder.executeQuery();
            result.next();
            int p = result.getInt(1);
            ((SharedFolder)root).setFolderID(p);
            result.close();
            stmtAddFolder.close();
            for(SharedItem item :root.getSharedItems()){
                addFolder(item,conn,p);
            }
        }else if(!root.isFolder()){
            ((SharedFile)root).setFolderID(parent);
            addFile((SharedFile)root,conn);
        }
        
    }
    private void addFile(SharedFile file,Connection conn) throws SQLException{
        
        PreparedStatement stmtAddFile = conn.prepareStatement(bundle.getString("addFile"));
        stmtAddFile.setInt(1, file.getFolderID());
        stmtAddFile.setString(2, file.getName());
        stmtAddFile.setLong(3, file.getSize());
        stmtAddFile.executeUpdate();
        stmtAddFile.close();
    }
    
    
   

}
