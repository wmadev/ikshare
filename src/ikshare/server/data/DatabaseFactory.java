/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data;

import ikshare.server.configuration.ServerConfiguration;
import ikshare.server.configuration.ServerConfigurationController;
import ikshare.server.data.oracle.OracleDatabaseFactory;

/**
 *
 * @author awosy
 */
public abstract class DatabaseFactory {
    
    private static ServerConfiguration configuration;
    
    // Supported Storage factories (Now only ORACLE, can be expanded to support more)
    private static final int ORACLE = 1;
    
    public static DatabaseFactory getFactory(){
        if(configuration == null)
            configuration = ServerConfigurationController.getInstance().getConfiguration();    
        switch (configuration.getDatabaseType()) {
        case ORACLE:
            return OracleDatabaseFactory.getInstance(configuration);
        default:
            return null;
        }
    }
    public abstract AccountStorage getAccountStorage();
    
}
