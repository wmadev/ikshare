 package ikshare.server.data.oracle;

import ikshare.domain.DownloadInformation;
import ikshare.domain.SearchResult;
import ikshare.server.data.DatabaseException;
import ikshare.server.data.FileStorage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class OracleFileStorage implements FileStorage {
    private static OracleFileStorage instance;
    private ResourceBundle bundle;
    private String audioExtensions[] = {".mp3",".wma",".ogg",".wav",".mid",".m4a"};
    private String videoExtensions[]= {".avi", ".dvix", ".xvid", ".mpeg", ".mp4"};
    private String textExtensions[] = {".txt", ".doc", ".pdf"};
    
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
                        ,result.getString(bundle.getString("accountname")),result.getLong(bundle.getString("filesize")),false,0,result.getInt(bundle.getString("parentid")));
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
    
    public List<SearchResult> advancedFileSearch(String keyword, boolean textAnd, int typeID, long minSize, long maxSize) throws DatabaseException {
        ArrayList<SearchResult> results = null;
        String searchString="WITH y AS (SELECT x.USERNAME FROM ONLINE_USERS x WHERE x.ACTION='LOGON' AND x.USERNAME NOT IN ( SELECT USERNAME FROM ONLINE_USERS WHERE ACTION = 'LOGOFF' AND TIMESTAMP> x.TIMESTAMP AND USERNAME=x.USERNAME ) ) " +
                "SELECT fi.FOLDERID,FILENAME,ACCOUNTNAME,FILESIZE FROM SHAREDFILES fi JOIN SHAREDFOLDERS fo ON fo.FOLDERID=fi.FOLDERID JOIN y ON y.USERNAME=fo.ACCOUNTNAME and ( lower(FILENAME) LIKE '";
        keyword = keyword.toLowerCase();
        StringTokenizer tokenizer = new StringTokenizer(keyword," ");
        searchString+="%"+tokenizer.nextToken()+"%' ";
        if(textAnd){
            while(tokenizer.hasMoreTokens()){
                searchString+= "AND lower(FILENAME) LIKE '%"+tokenizer.nextToken()+"%' ";
            }
        }
        else{
            while(tokenizer.hasMoreTokens()) {
                searchString+= "OR lower(FILENAME) LIKE '%"+tokenizer.nextToken()+"%' ";
            }               
        }
        switch (typeID){
            case 1:             //audio
                if(audioExtensions.length!=0){
                    searchString+="AND (lower(FILENAME) LIKE '%"+audioExtensions[0]+"' ";
                    for( int i=1;i<audioExtensions.length;i++) {
                    searchString+= "OR lower(FILENAME) LIKE '%"+audioExtensions[i]+"' ";
                    }
                    searchString+=") ";
                }
                break;
            case 2:             //video
                if(videoExtensions.length!=0){
                    searchString+="AND (lower(FILENAME) LIKE '%"+videoExtensions[0]+"' ";
                    for( int i=1;i<videoExtensions.length;i++) {
                    searchString+= "OR lower(FILENAME) LIKE '%"+videoExtensions[i]+"' ";
                    }
                    searchString+=") ";
                }
                break;
            case 3:             //text
                if(textExtensions.length!=0){
                    searchString+="AND (lower(FILENAME) LIKE '%"+textExtensions[0]+"' ";
                    for( int i=1;i<textExtensions.length;i++) {
                    searchString+= "OR lower(FILENAME) LIKE '%"+textExtensions[i]+"' ";
                    }
                    searchString+=") ";
                }
                break;
            case 4:
                if(audioExtensions.length!=0){
                    searchString+="AND (lower(FILENAME) NOT LIKE '%"+audioExtensions[0]+"' ";
                }
                for( int i=1;i<audioExtensions.length;i++) {
                searchString+= "AND lower(FILENAME) NOT LIKE '%"+audioExtensions[i]+"' ";
                }
                if(audioExtensions.length==0 && videoExtensions.length!=0){
                    searchString+="AND (lower(FILENAME) NOT LIKE '%"+videoExtensions[0]+"' ";
                }
                for( int i=0;i<videoExtensions.length;i++) {
                searchString+= "AND lower(FILENAME) NOT LIKE '%"+videoExtensions[i]+"' ";
                }
                if(audioExtensions.length==0 && videoExtensions.length==0 && textExtensions.length!=0){
                    searchString+="AND (lower(FILENAME) NOT LIKE '%"+textExtensions[0]+"' ";
                }
                for( int i=0;i<textExtensions.length;i++) {
                searchString+= "AND lower(FILENAME) NOT LIKE '%"+textExtensions[i]+"' ";
                }
                if(audioExtensions.length!=0 || videoExtensions.length!=0 || textExtensions.length!=0){
                    searchString+=") ";
                }
                break;
        }
        if(minSize!=0 && maxSize!=0){
            searchString+= "AND FILESIZE BETWEEN "+minSize+" AND "+maxSize+" ";
        }
        else if (minSize!=0){
            searchString+= "AND FILESIZE>"+minSize+" ";
             }
        else if (maxSize!=0){
            searchString+= "AND FILESIZE<"+maxSize+" ";
        }
        searchString+=" )";
              
        Connection conn = OracleDatabaseFactory.getConnection();
        try{
            Statement stmt=conn.createStatement();
            System.out.println("QUERY: "+searchString);
            ResultSet result=stmt.executeQuery(searchString);
            results =new ArrayList<SearchResult>();
            while(result.next()){
                SearchResult sr=new SearchResult(result.getString(bundle.getString("filename"))
                        ,result.getString(bundle.getString("accountname")),result.getLong(bundle.getString("filesize")),false,0,result.getInt(bundle.getString("parentid")));
                results.add(sr);
            }
            result.close();
            stmt.close();
            
        }
        catch (SQLException e){
            results=null;
            e.printStackTrace();
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        }
        finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
        
        return results;
    }

    public List<SearchResult> advancedFolderSearch(String keyword, boolean textAnd, long minSize, long maxSize) throws DatabaseException {
        ArrayList<SearchResult> results = null;
        String searchString="WITH y AS (SELECT x.USERNAME FROM ONLINE_USERS x WHERE x.ACTION='LOGON' AND " +
              "x.USERNAME NOT IN ( SELECT USERNAME FROM ONLINE_USERS WHERE ACTION = 'LOGOFF' " +
              "AND TIMESTAMP> x.TIMESTAMP AND USERNAME=x.USERNAME ) ), " +
              "w as (SELECT distinct(fo.folderid), foldername, parentfolderid, accountname, " +
              "sum(fi.filesize) over(partition by fo.folderid) as foldersize, connect_by_isleaf blad " +
              "from sharedfolders fo join y on y.username=accountname join sharedfiles fi on fi.folderid=fo.folderid " +
              "where parentfolderid<>0 start with lower(foldername) like '";
        keyword = keyword.toLowerCase();
        StringTokenizer tokenizer = new StringTokenizer(keyword," ");
        searchString+="%"+tokenizer.nextToken()+"%' ";
        if(textAnd){
            while(tokenizer.hasMoreTokens()){
                searchString+= "AND lower(FOLDERNAME) LIKE '%"+tokenizer.nextToken()+"%' ";
            }
        }
        else{
            while(tokenizer.hasMoreTokens()) {
                searchString+= "OR lower(FOLDERNAME) LIKE '%"+tokenizer.nextToken()+"%' ";
            }               
        }
        searchString+="connect by prior folderid = parentfolderid) ," +
                "v as (select folderid, foldername, parentfolderid, accountname, connect_by_root( foldersize) as foldersize " +
		"from w	start with blad=1 connect by prior parentfolderid = folderid), " +
                "z as (select folderid, foldername, parentfolderid, sum( foldersize) as foldersize, accountname " +
		"from v group by folderid, foldername, parentfolderid, accountname ";

        if(minSize!=0 && maxSize!=0){
            searchString+= "HAVING FOLDERSIZE BETWEEN "+minSize+" AND "+maxSize+" ";
        }
        else if (minSize!=0){
            searchString+= "HAVING FOLDERSIZE>"+minSize+" ";
             }
        else if (maxSize!=0){
            searchString+= "HAVING FOLDERSIZE<"+maxSize+" ";
        }
        searchString+=") " +
                "select folderid, foldername, parentfolderid, foldersize, accountname from z " +
                "union all select fi.folderid, filename, 0, fi.filesize, accountname " +
                "from sharedfiles fi join z on z.folderid = fi.folderid " +
                "order by folderid, parentfolderid desc";
              
        Connection conn = OracleDatabaseFactory.getConnection();
        try{
            Statement stmt=conn.createStatement();
            ResultSet result=stmt.executeQuery(searchString);
            results =new ArrayList<SearchResult>();
            while(result.next()){
                SearchResult sr=new SearchResult(result.getString(bundle.getString("foldername"))
                        ,result.getString(bundle.getString("accountname")),result.getLong(bundle.getString("filesize")),result.getInt(bundle.getString("parentfolderid"))!=0,result.getInt(bundle.getString("parentfolderid")), result.getInt(bundle.getString("folderid")));
                results.add(sr);
            }
            result.close();
            stmt.close();
            
        }
        catch (SQLException e){
            results=null;
            e.printStackTrace();
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        }
        finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
        
        return results;
    }
}
