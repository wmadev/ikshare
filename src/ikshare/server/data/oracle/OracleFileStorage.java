/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data.oracle;

import ikshare.domain.DownloadInformation;
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
                        ,result.getString(bundle.getString("accountname")),result.getLong(bundle.getString("filesize")),false,result.getInt(bundle.getString("parentid")));
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

    public boolean deleteSharedFile(int parentFolderID, String name, long size) throws DatabaseException {
        boolean success =false;
        Connection conn = OracleDatabaseFactory.getConnection();
        try {
            PreparedStatement stmtDeleteFile = conn.prepareStatement(bundle.getString("deletefile"));
            stmtDeleteFile.setInt(1, parentFolderID);
            stmtDeleteFile.setString(2, name);
            stmtDeleteFile.setLong(3, size);
            stmtDeleteFile.executeUpdate();
            stmtDeleteFile.close();
            success = true;
        } catch (SQLException e) {
            success = false;
            e.printStackTrace();
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        } finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
       return  success;
    }

    public boolean deleteSharedFolder(int folderId) throws DatabaseException {
        boolean success =false;
        Connection conn = OracleDatabaseFactory.getConnection();
        try {
            PreparedStatement stmtDeleteFolder = conn.prepareStatement(bundle.getString("deletefolder"));
            stmtDeleteFolder.setInt(1, folderId);
            stmtDeleteFolder.setInt(2, folderId);
            stmtDeleteFolder.executeUpdate();
            stmtDeleteFolder.close();
            success = true;
        } catch (SQLException e) {
            success = false;
            e.printStackTrace();
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        } finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
       return  success;
    }

    public DownloadInformation getDownloadInformation(String accountName, String fileName, long fileSize, int folderId) throws DatabaseException {
        DownloadInformation di = new DownloadInformation();
        di.setAccountName(accountName);
        di.setName(fileName);
        Connection conn = OracleDatabaseFactory.getConnection();
        try {
            PreparedStatement stmtDownloadInformation = conn.prepareStatement(bundle.getString("getdownloadinformation"));
            stmtDownloadInformation.setString(1, fileName);
            stmtDownloadInformation.setString(2, accountName);
            stmtDownloadInformation.setLong(3, fileSize);
            stmtDownloadInformation.setInt(4, folderId);
            ResultSet result = stmtDownloadInformation.executeQuery();
            result.next();
            di.setPath(result.getString(bundle.getString("path")));
            stmtDownloadInformation = conn.prepareStatement(bundle.getString("getPortAndAddress"));
            stmtDownloadInformation.setString(1, accountName);
            result = stmtDownloadInformation.executeQuery();
            result.next();
            di.setIp(result.getString(bundle.getString("ip")));
            di.setPort(result.getInt(bundle.getString("port")));
            result.close();
            stmtDownloadInformation.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        } finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
       return di;
    }
}
