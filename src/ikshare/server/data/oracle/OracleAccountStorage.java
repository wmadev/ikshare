/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data.oracle;

import ikshare.domain.Peer;
import ikshare.server.data.AccountStorage;

/**
 *
 * @author awosy
 */
public class OracleAccountStorage implements AccountStorage {
    private static OracleAccountStorage instance;
    
    private OracleAccountStorage(){
        
    }
    public static OracleAccountStorage getInstance(){
        if(instance == null){
            instance = new OracleAccountStorage();
        }
        return instance;
    }
    
    public boolean createAccount(Peer newUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean deleteAccount(int userID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean updateAccount(Peer user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean login(Peer user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean logout(Peer user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
