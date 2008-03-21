/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data.oracle;

import ikshare.server.data.ObjectPool;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author awosy
 */
public class OracleConnectionPool extends ObjectPool<Connection>{
    private String dsn, usr, pwd;
    public OracleConnectionPool(String driver, String dsn, String usr,
                               String pwd) {
        super();
        try {
            Class.forName(driver).newInstance();
        } catch( Exception e) {
            e.printStackTrace();
        }
        this.dsn = dsn;
        this.usr = usr;
        this.pwd = pwd;
    }

    @Override
    protected Connection create() {
        Connection o = null;
        try {
            o = (Connection)(DriverManager.getConnection(dsn, usr, pwd));
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            return (null);
         }
    }

    @Override
    public void expire(Connection o) {
        try {
            ((Connection)o).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validate(Connection o) {
        boolean validate = false;
        try {

            validate = !o.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return validate;
     
        }
}
