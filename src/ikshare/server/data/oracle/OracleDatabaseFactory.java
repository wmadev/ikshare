package ikshare.server.data.oracle;

import ikshare.server.configuration.ServerConfiguration;
import ikshare.server.data.AccountStorage;
import ikshare.server.data.DatabaseFactory;
import ikshare.server.data.FileStorage;
import java.sql.Connection;

public class OracleDatabaseFactory extends DatabaseFactory {
    private static OracleDatabaseFactory instance;
    
    private static OracleConnectionPool connectionPool;
    private ServerConfiguration config;
    
    private OracleDatabaseFactory(ServerConfiguration configuration){
        config = configuration;
        connectionPool = new OracleConnectionPool(config.getDatabaseDriver(), config.getDatabaseURL(),
                config.getDatabaseUser(), config.getDatabasePassword());
    }
    
    public static DatabaseFactory getInstance(ServerConfiguration configuration) {
        if(instance == null){
            instance = new OracleDatabaseFactory(configuration);
        }
        return instance;
    }
   
    
    public static Connection getConnection()  {
       return connectionPool.checkOut();
    }

    public static void freeConnection(Connection con) {
       connectionPool.checkIn(con);
    }
    

    @Override
    public AccountStorage getAccountStorage() {
        return OracleAccountStorage.getInstance();
    }
    
    @Override
    public FileStorage getFileStorage(){
        return OracleFileStorage.getInstance();
    }

}
