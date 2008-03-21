/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data;

/**
 *
 * @author awosy
 */
public abstract class DatabaseFactory {
    
    private static Configuration configuration;
    
    // Supported Storage factories (Now only MySQL, can be expanded to support more)
    private static final int MYSQL = 1;
    public DatabaseFactory getFactory(){
        return null;
    }
    public abstract AccountStorage getAccountStorage();
    
}
