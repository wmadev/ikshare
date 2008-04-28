package ikshare.server;

import ikshare.domain.DownloadInformation;
import ikshare.domain.Peer;
import ikshare.domain.SearchResult;
import ikshare.server.data.DatabaseException;
import ikshare.server.data.DatabaseFactory;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {
    private static ServerController instance;
    private ExecutorService executorService;
    private IkshareServer server;
    private DatabaseFactory databaseFactory;
    
    private ServerController(){
        executorService = Executors.newFixedThreadPool(1);
        server = new IkshareServer();
        databaseFactory = databaseFactory.getFactory();
    }
    
    public static ServerController getInstance(){
        if(instance == null)
            instance = new ServerController();
        return instance;
    }

    public boolean addSharedFile(int parentFolderID, String name, long size) throws DatabaseException {
        return databaseFactory.getFileStorage().addSharedFile(parentFolderID,name,size);
    }

    public int addSharedFolder(String path, String accountName, String name, int parentFolderId) throws DatabaseException {
        return databaseFactory.getFileStorage().addSharedFolder(path,accountName,name,parentFolderId);
    }

    public boolean checkPassword(Peer user) throws DatabaseException {
        return databaseFactory.getAccountStorage().checkPassword(user);
    }

    public boolean createAccount(Peer newUser) throws DatabaseException {
        return databaseFactory.getAccountStorage().createAccount(newUser);
    }
    public boolean checkAccount(Peer newUser) throws DatabaseException{
        return databaseFactory.getAccountStorage().checkAccountName(newUser);
    }

    public boolean deleteSharedFile(int parentFolderID, String name, long size) throws DatabaseException{
        return databaseFactory.getFileStorage().deleteSharedFile(parentFolderID,name,size);
    }

    public boolean deleteSharedFolder(int folderId) throws DatabaseException{
        return databaseFactory.getFileStorage().deleteSharedFolder(folderId);
    }

    public List<SearchResult> findAdvancedFile(String keyword, boolean textAnd, int typeID, long minSize, long maxSize) throws DatabaseException{
        return databaseFactory.getFileStorage().advancedFileSearch(keyword, textAnd, typeID, minSize, maxSize);
    }

    public List<SearchResult> findAdvancedFolder(String keyword, boolean textAnd, long minSize, long maxSize) throws DatabaseException {
        return databaseFactory.getFileStorage().advancedFolderSearch(keyword, textAnd, minSize, maxSize);
    }

    public List<SearchResult> findBasic(String keyword) throws DatabaseException {
        return databaseFactory.getFileStorage().basicSearch(keyword);
    }

    public DownloadInformation getDownloadInformation(String accountName, String fileName, long fileSize, int folderId) throws DatabaseException {
        return databaseFactory.getFileStorage().getDownloadInformation(accountName,fileName,fileSize,folderId);
    }

    public boolean logoff(Peer user) throws DatabaseException {
        return databaseFactory.getAccountStorage().logoff(user);
    }

    public boolean logon(Peer user) throws DatabaseException {
        return databaseFactory.getAccountStorage().logon(user);
    }
    
    public void startServer(){
        executorService.execute(server);
    }
    public void stopServer(){
        server.stop();
        executorService.shutdown();
    }
}
