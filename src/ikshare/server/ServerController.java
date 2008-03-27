/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server;

import ikshare.domain.Peer;
import ikshare.server.data.DatabaseException;
import ikshare.server.data.DatabaseFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author awosy
 */
public class ServerController {
    private static ServerController instance;
    private ExecutorService executorService;
    private IkshareServer server;
    private DatabaseFactory databaseFactory;
    
    private ServerController(){
        executorService = Executors.newFixedThreadPool(1);
        server = new IkshareServer();
        databaseFactory = databaseFactory.getFactory();
    }
    
    public static ServerController getInstance(){
        if(instance == null)
            instance = new ServerController();
        return instance;
    }

    public boolean checkPassword(Peer user) throws DatabaseException {
        return databaseFactory.getAccountStorage().checkPassword(user);
    }

    public boolean createAccount(Peer newUser) throws DatabaseException {
        return databaseFactory.getAccountStorage().createAccount(newUser);
    }
    public boolean checkAccount(Peer newUser) throws DatabaseException{
        return databaseFactory.getAccountStorage().checkAccountName(newUser);
    }

    public boolean logoff(Peer user) throws DatabaseException {
        return databaseFactory.getAccountStorage().logoff(user);
    }

    public boolean logon(Peer user) throws DatabaseException {
        return databaseFactory.getAccountStorage().logon(user);
    }
    
    public void startServer(){
        executorService.execute(server);
    }
    public void stopServer(){
        server.stop();
        executorService.shutdown();
    }
}
