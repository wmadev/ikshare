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
    public boolean createAccount(Peer newUser);
    public boolean deleteAccount(int userID);
    public boolean updateAccount(Peer user);
    public boolean login(Peer user);
    public boolean logout(Peer user);
}
