/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data.oracle;

import ikshare.domain.SearchResult;
import ikshare.domain.SharedFile;
import ikshare.domain.SharedFolder;
import ikshare.domain.SharedItem;
import ikshare.domain.SharedItemState;
import ikshare.server.data.DatabaseException;
import ikshare.server.data.FileStorage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public synchronized int addSharedFolder(String path, String accountName, String name, int parentFolderId) throws DatabaseException{
        int id = 0;
        Connection conn = OracleDatabaseFactory.getConnection();
        try {
            PreparedStatement stmtAddFolder = conn.prepareStatement(bundle.getString("addFolder"));
            stmtAddFolder.setString(1, path);
            stmtAddFolder.setString(2, accountName);
            stmtAddFolder.setInt(3, parentFolderId);
            stmtAddFolder.setString(4, name);
            stmtAddFolder.executeUpdate();
            stmtAddFolder = conn.prepareStatement(bundle.getString("getLastFolderId"));
            ResultSet result = stmtAddFolder.executeQuery();
            result.next();
            id = result.getInt(1);
            result.close();
            stmtAddFolder.close();
       } catch (SQLException e) {
            id = 0;
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        } finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
        return id;
    }
    
     public boolean addSharedFile(int parentFolderID, String name, long size) throws DatabaseException {
        boolean success = false;
        Connection conn = OracleDatabaseFactory.getConnection();
        try {
            PreparedStatement stmtAddFile = conn.prepareStatement(bundle.getString("addFile"));
            stmtAddFile.setInt(1, parentFolderID);
            stmtAddFile.setString(2, name);
            stmtAddFile.setLong(3, size);
            stmtAddFile.executeUpdate();
            stmtAddFile.close();
            success = true;
       } catch (SQLException e) {
            success = false;
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        } finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
        return success;
    }

    public synchronized List<SearchResult> basicSearch(String name) throws DatabaseException {
        ArrayList<SearchResult> results = null;
        Connection conn = OracleDatabaseFactory.getConnection();
        try {
            PreparedStatement stmtBasicSearch = conn.prepareStatement(bundle.getString("basicSearch"));
            
            stmtBasicSearch.setString(1, "%"+name.toLowerCase()+"%");
            ResultSet result = stmtBasicSearch.executeQuery();
            results =new ArrayList<SearchResult>();
            while(result.next()){
                SearchResult sr=new SearchResult(result.getString(bundle.getString("filename"))
                        ,result.getString(bundle.getString("accountname")),Long.parseLong(result.getString(bundle.getString("filesize"))),false);
                results.add(sr);
            }
            result.close();
            stmtBasicSearch.close();
        } catch (SQLException e) {
            results = null;
            e.printStackTrace();
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        } finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
       return  results;
    }
}
