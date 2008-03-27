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
    private String databaseDriver;
    private String databaseUser;
    private String databasePassword;
    private String databaseURL;
    
    public ServerConfiguration(){
        
    }

    public String getDatabaseDriver() {
        return databaseDriver;
    }

    public void setDatabaseDriver(String databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public String getDatabaseURL() {
        return databaseURL;
    }

    public void setDatabaseURL(String databaseURL) {
        this.databaseURL = databaseURL;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    
    
    public void setDatabaseType(int databaseType) {
        this.databaseType = databaseType;
    }
    
    public int getDatabaseType() {
        return databaseType;
    }
}
