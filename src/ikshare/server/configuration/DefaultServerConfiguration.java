package ikshare.server.configuration;

public class DefaultServerConfiguration extends ServerConfiguration{
        public DefaultServerConfiguration(){
            super.setDatabaseType("ORACLE");
            super.setDatabaseDriver("oracle.jdbc.driver.OracleDriver");
            super.setDatabaseUser("SYSTEM");
            super.setDatabasePassword("e=mc**2");
            super.setDatabaseURL("jdbc:oracle:thin:@//localhost:1521/ikshare");
        }

}
