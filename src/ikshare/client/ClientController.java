/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client;

import ikshare.client.threads.ServerConversationThread;
import ikshare.protocol.command.CreateAccountCommando;
import ikshare.protocol.command.LogOffCommando;
import ikshare.protocol.command.LogOnCommando;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author awosy
 */
public class ClientController {
    private static ClientController instance;
    private ExecutorService executorService;
    private ServerConversationThread serverConversation;
    
    private ClientController(){
        executorService = Executors.newCachedThreadPool(); 
    }
    
    public static ClientController getInstance(){
        if(instance == null)
            instance = new ClientController();
        return instance;
    }

    public void createAccount(String accountName, String accountPassword, String accountEmail) {

        CreateAccountCommando cac = new CreateAccountCommando();
        cac.setAccountName(accountName);
        cac.setPassword(accountPassword);
        cac.setEmail(accountEmail);
        startServerConversation();
        serverConversation.sendCommand(cac);
    }

    public void logoff(String accountName, String password, int port) {
        LogOffCommando loc = new LogOffCommando();
        loc.setAccountName(accountName);
        loc.setPassword(password);
        loc.setPort(port);
        serverConversation.sendCommand(loc);
    }
    
    public void startServerConversation(){
        serverConversation = new ServerConversationThread();
        executorService.execute(serverConversation);
    }

    public boolean logon(String accountname,String password,int port){
        LogOnCommando loc = new LogOnCommando();
        loc.setAccountName(accountname);
        loc.setPassword(password);
        loc.setPort(port);
        serverConversation.sendCommand(loc);
        return true;
    }

    public void stopServerConversation() {
        serverConversation.stop();
    }
}
