/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data;

import ikshare.domain.Peer;

/**
 *
 * @author awosy
 */
public interface AccountStorage {
    public boolean createAccount(Peer newUser) throws DatabaseException;
    public boolean checkAccountName(Peer newUser) throws DatabaseException;
    public boolean checkPassword(Peer user) throws DatabaseException;
    public boolean deleteAccount(int userID) throws DatabaseException;
    public boolean updateAccount(Peer user) throws DatabaseException;
    public boolean logon(Peer user) throws DatabaseException;
    public boolean logoff(Peer user) throws DatabaseException;
}
