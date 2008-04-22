/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data;

import ikshare.domain.DownloadInformation;
import ikshare.domain.SearchResult;
import ikshare.domain.SharedFolder;
import java.util.List;

/**
 *
 * @author awosy
 */
public interface FileStorage {

    public boolean addSharedFile(int parentFolderID, String name, long size) throws DatabaseException;

    public int addSharedFolder(String path, String accountName, String name, int parentFolderId) throws DatabaseException;
    public List<SearchResult> basicSearch(String name) throws DatabaseException;
    public boolean deleteSharedFile(int parentFolderID, String name, long size) throws DatabaseException;

    public boolean deleteSharedFolder(int folderId) throws DatabaseException;

    public DownloadInformation getDownloadInformation(String accountName, String fileName, long fileSize, int folderId)throws DatabaseException;
}
