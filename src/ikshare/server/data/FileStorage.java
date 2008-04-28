package ikshare.server.data;

import ikshare.domain.DownloadInformation;
import ikshare.domain.SearchResult;
import java.util.List;

public interface FileStorage {

    public boolean addSharedFile(int parentFolderID, String name, long size) throws DatabaseException;

    public int addSharedFolder(String path, String accountName, String name, int parentFolderId) throws DatabaseException;

    public List<SearchResult> advancedFolderSearch(String keyword, boolean textAnd, long minSize, long maxSize) throws DatabaseException;

    public List<SearchResult> advancedFileSearch(String keyword, boolean textAnd, int typeID, long minSize, long maxSize) throws DatabaseException;
    
    public List<SearchResult> basicSearch(String name) throws DatabaseException;
    
    public boolean deleteSharedFile(int parentFolderID, String name, long size) throws DatabaseException;

    public boolean deleteSharedFolder(int folderId) throws DatabaseException;

    public DownloadInformation getDownloadInformation(String accountName, String fileName, long fileSize, int folderId)throws DatabaseException;
}
