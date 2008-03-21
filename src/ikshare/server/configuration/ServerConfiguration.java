/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.configuration;

/**
 *
 * @author awosy
 */
public class ServerConfiguration {
    
    private int databaseType;
    
    public ServerConfiguration(){
        
    }

    public void setDatabaseType(int databaseType) {
        this.databaseType = databaseType;
    }
    
    public int getDatabaseType() {
        return databaseType;
    }
}
