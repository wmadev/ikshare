/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client;

import ikshare.client.threads.ServerConversationThread;
import ikshare.client.threads.ShareSynchronisationThread;
import ikshare.domain.IKShareFile;
import ikshare.domain.Peer;
import ikshare.domain.SearchResult;
import ikshare.domain.SharedItem;
import ikshare.domain.Transfer;
import ikshare.domain.TransferState;
import ikshare.protocol.command.CreateAccountCommando;
import ikshare.protocol.command.DownloadInformationRequestCommand;
import ikshare.protocol.command.DownloadInformationResponseCommand;
import ikshare.protocol.command.FindBasicCommando;
import ikshare.protocol.command.LogOffCommando;
import ikshare.protocol.command.LogOnCommando;
import ikshare.protocol.command.StartShareSynchronisationCommando;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public void getDownloadInformationForResult(SearchResult rs) {
        DownloadInformationRequestCommand dirc = new DownloadInformationRequestCommand();
        dirc.setAccountName(rs.getOwner());
        dirc.setFileName(rs.getName());
        dirc.setFileSize(rs.getSize());
        dirc.setFolderId(rs.getParentId());
        serverConversation.sendCommand(dirc);
    }

    public Transfer getTransferForDownload(DownloadInformationResponseCommand dirc) throws UnknownHostException {
        Transfer t = new Transfer();
        t.setId(String.valueOf(new Date().getTime()));
        t.setFile(new IKShareFile(dirc.getPath(), dirc.getName()));
        t.setPeer(new Peer(dirc.getAccountName(), InetAddress.getByName(dirc.getIp()), dirc.getPort()));
        t.setState(TransferState.DOWNLOADING);
        return t;
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
    
    public boolean share(String accountName,File root) throws IOException{
        executorService.execute(new ShareSynchronisationThread(accountName,root));
        return true;
    }
    
    public void stopServerConversation() {
        serverConversation.stop();
    }
}
