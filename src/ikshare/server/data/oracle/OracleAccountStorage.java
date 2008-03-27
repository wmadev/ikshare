/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data.oracle;

import ikshare.domain.Peer;
import ikshare.server.data.AccountStorage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *
 * @author awosy
 */
public class OracleAccountStorage implements AccountStorage {
    private static OracleAccountStorage instance;
    private ResourceBundle bundle;
    
    private OracleAccountStorage(){
        bundle = ResourceBundle.getBundle("ikshare.server.data.oracle.DBConstants");
    }
    public static OracleAccountStorage getInstance(){
        if(instance == null){
            instance = new OracleAccountStorage();
        }
        return instance;
    }
    
    public boolean createAccount(Peer newUser) {
        boolean success = false;
        Connection conn = OracleDatabaseFactory.getConnection();
        try {
            PreparedStatement stmtCreateAccount = conn.prepareStatement(bundle.getString("createAccount"));
            stmtCreateAccount.setString(1, newUser.getAccountName());
            stmtCreateAccount.setString(2, newUser.getPassword());
            stmtCreateAccount.setString(3, newUser.getEmail());
            stmtCreateAccount.executeUpdate();
            stmtCreateAccount.close();
            success = true;
        } catch (SQLException e) {
            success = false;
            e.printStackTrace();
        } finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
        return success;
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
