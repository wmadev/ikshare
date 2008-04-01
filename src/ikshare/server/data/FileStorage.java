/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data;

import ikshare.domain.SharedFolder;

/**
 *
 * @author awosy
 */
public interface FileStorage {
    public boolean addShares(String accountName,SharedFolder root) throws DatabaseException;
}
