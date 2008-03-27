/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.configuration;

/**
 *
 * @author awosy
 */
public class DefaultServerConfiguration extends ServerConfiguration{
        public DefaultServerConfiguration(){
            super.setDatabaseType("ORACLE");
            super.setDatabaseDriver("oracle.jdbc.driver.OracleDriver");
            super.setDatabaseUser("SYSTEM");
            super.setDatabasePassword("e=mc**2");
            super.setDatabaseURL("jdbc:oracle:thin:@127.0.0.1:1521:ikshare");
        }



}
