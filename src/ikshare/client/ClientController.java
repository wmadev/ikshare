/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client;

import ikshare.client.threads.ServerConversationThread;
import ikshare.client.threads.ShareSynchronisationThread;
import ikshare.domain.SharedItem;
import ikshare.protocol.command.CreateAccountCommando;
import ikshare.protocol.command.FindBasicCommando;
import ikshare.protocol.command.LogOffCommando;
import ikshare.protocol.command.LogOnCommando;
import ikshare.protocol.command.StartShareSynchronisationCommando;
import java.io.File;
import java.io.IOException;
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

    public void createAccount(String accountName, String accountPassword, String accountEmail) throws IOException {

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
    
    public void startServerConversation() throws IOException{
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
    public boolean findBasic(String searchId,String keyword){
        FindBasicCommando fbc = new FindBasicCommando();
        fbc.setKeyword(keyword);
        fbc.setSearchID(searchId);
        serverConversation.sendCommand(fbc);
        return true;
    }
    
    private SharedItem generateShareTree(File root){
        return null;
    }
    
    public boolean share(String accountName,File root) throws IOException{
        executorService.execute(new ShareSynchronisationThread(accountName,root));
        return true;
    }
    
    private String voegtoe(File file){
        String line = "";
        if(file.isDirectory()){
            File[] files = file.listFiles(); 
            line+=file.getPath()+":DIR:"+files.length+";";
            for(int i = 0;i<files.length;i++){
                line+=voegtoe(files[i]);
            }
        }else{
           line+=file.getName()+":FILE:"+file.length()+";";
        }
        return line;
    }

    public void stopServerConversation() {
        serverConversation.stop();
    }
}
