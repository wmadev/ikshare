/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data.oracle;

import ikshare.domain.Peer;
import ikshare.server.data.AccountStorage;
import ikshare.server.data.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    
    public synchronized boolean checkAccountName(Peer newUser) throws DatabaseException {
        boolean exists = false;
        Connection conn = OracleDatabaseFactory.getConnection();
        try {
            PreparedStatement stmtCheckAccount = conn.prepareStatement(bundle.getString("checkAccount"));
            stmtCheckAccount.setString(1, newUser.getAccountName());
            ResultSet result = stmtCheckAccount.executeQuery();
            if(result.next()){
                exists = true;
            }
            result.close();
            stmtCheckAccount.close();
        } catch (SQLException e) {
            exists = false;
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        } finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
        return exists;
    }
    
    public boolean createAccount(Peer newUser) throws DatabaseException {
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
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        } finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
        return success;
    }
    
    public boolean checkPassword(Peer user) throws DatabaseException{
        boolean correct = false;
        Connection conn = OracleDatabaseFactory.getConnection();
        try {
            PreparedStatement stmtCheckPassword = conn.prepareStatement(bundle.getString("checkPassword"));
            stmtCheckPassword.setString(1, user.getAccountName());
            stmtCheckPassword.setString(2, user.getPassword());
            ResultSet result = stmtCheckPassword.executeQuery();
            if(result.next()){
                correct = true;
            }
            result.close();
            stmtCheckPassword.close();
        } catch (SQLException e) {
            correct = false;
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        } finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
        return correct;
    }

    public boolean deleteAccount(int userID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean updateAccount(Peer user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean logon(Peer user) throws DatabaseException {
        boolean success = false;
        Connection conn = OracleDatabaseFactory.getConnection();
        try {
            PreparedStatement stmtLogon = conn.prepareStatement(bundle.getString("logon"));
            stmtLogon.setString(1, user.getAccountName());
            stmtLogon.setString(2, user.getInternetAddress().getHostAddress());
            stmtLogon.setInt(3, user.getPort());
            stmtLogon.executeUpdate();
            stmtLogon.close();
            success = true;
        } catch (SQLException e) {
            success = false;
            e.printStackTrace();
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        } finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
        return success;
    }

    public boolean logoff(Peer user) throws DatabaseException {
        boolean success = false;
        Connection conn = OracleDatabaseFactory.getConnection();
        try {
            PreparedStatement stmtLogoff = conn.prepareStatement(bundle.getString("logoff"));
            stmtLogoff.setString(1, user.getAccountName());
            stmtLogoff.setString(2, user.getInternetAddress().getHostAddress());
            stmtLogoff.setInt(3, user.getPort());
            stmtLogoff.executeUpdate();
            stmtLogoff.close();
            success = true;
        } catch (SQLException e) {
            success = false;
            e.printStackTrace();
            throw new DatabaseException(bundle.getString("ERROR_Database"));
        } finally {
            OracleDatabaseFactory.freeConnection(conn);
        }
        return success;
    }

   

}
