/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server;

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
    
    private ServerController(){
        executorService = Executors.newFixedThreadPool(1);
        server = new IkshareServer();
    }
    
    public static ServerController getInstance(){
        if(instance == null)
            instance = new ServerController();
        return instance;
    }
    public void startServer(){
        executorService.execute(server);
    }
    public void stopServer(){
        server.stop();
        executorService.shutdown();
    }
}
