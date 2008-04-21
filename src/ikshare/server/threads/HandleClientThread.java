/**
 * This class represents a thread that handles a client that connects with the server.
 */
package ikshare.server.threads;

import ikshare.domain.*;
import ikshare.protocol.command.*;
import ikshare.server.*;
import ikshare.server.data.*;
import java.io.*;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author awosy
 */
public class HandleClientThread implements Runnable{
    private Socket clientSocket;
    private boolean running = false;
    private PrintWriter outputWriter;
    private BufferedReader incomingReader;
    private ResourceBundle bundle;
    private String accountName;
        
    public HandleClientThread(Socket socket){
        try {
            clientSocket = socket;
            outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            incomingReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bundle = ResourceBundle.getBundle("ikshare.server.server");
            running = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void run() {
        try {
            while(running){
                String inputLine = incomingReader.readLine();
                if (inputLine != null) {
                    System.out.println(inputLine);
                    Commando c = CommandoParser.getInstance().parse(inputLine);
	            if (c instanceof CreateAccountCommando) {
                        handleCreateAccountCommando(c);
	            }
                    else if( c instanceof LogOnCommando){
                        handleLogonCommando(c);
                    }
                    else if( c instanceof LogOffCommando){
                        handleLogoffCommando(c);
                    }
                    else if( c instanceof StartShareSynchronisationCommando){
                        handleShareSynchronisation(c);
                    }
                    else if(c instanceof AddShareCommando){
                        handleAddShareCommando(c);
                    }
                    else if(c instanceof DeleteShareCommando){
                        handleDeleteShareCommando(c);
                    }
                    else if( c instanceof EndShareSynchronisationCommando){
                        handleEndShareSynchronisation(c);
                    }
                    else if( c instanceof FindBasicCommando){
                        handleFindBasicCommando(c);
                    }
                        
                    
                }
            }
            clientSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }

    private String getString(String key,String[] params){
        String text = bundle.getString(key);
        if(params!=null && text!=null) {
            MessageFormat format = new MessageFormat(text);
            text=format.format(params);
        }
        return text;
    }

    private void handleCreateAccountCommando(Commando c) {
        CreateAccountCommando cac = (CreateAccountCommando)c;
        Peer newUser = new Peer(cac.getAccountName(),cac.getPassword(),cac.getEmail());
        try {
            if(!ServerController.getInstance().checkAccount(newUser)){
                if (ServerController.getInstance().createAccount(newUser)) {
                    CreatedAccountCommando cdac = new CreatedAccountCommando();
                    cdac.setAccountName(newUser.getAccountName());
                    outputWriter.println(cdac.toString());
                }
            }
            else {
                InvalidRegisterCommando irc = new InvalidRegisterCommando();
                String[] param = {newUser.getAccountName()};
                irc.setMessage(getString("accountNameAlreadyExists",param));
                outputWriter.println(irc.toString());
            }
        }
        catch (DatabaseException ex) {
            ServerErrorCommando sec = new ServerErrorCommando();
            sec.setMessage(ex.getMessage());
            outputWriter.println(sec.toString());
        }
    }

    private void handleDeleteShareCommando(Commando c) {
        DeleteShareCommando dsc = (DeleteShareCommando)c;
        if(dsc.isDirectory()){
            try {
                ServerController.getInstance().deleteSharedFolder(dsc.getFolderId());
            } catch (DatabaseException ex) {
                ServerErrorCommando sec = new ServerErrorCommando();
                sec.setMessage(ex.getMessage());
                outputWriter.println(sec.toString());
            }
        }
        else{
             try {
                ServerController.getInstance().deleteSharedFile(dsc.getParentFolderID(),dsc.getName(), dsc.getSize());
            } catch (DatabaseException ex) {
                ServerErrorCommando sec = new ServerErrorCommando();
                sec.setMessage(ex.getMessage());
                outputWriter.println(sec.toString());
            }
        }
    }

    private void handleEndShareSynchronisation(Commando c) {
        running = false;
    }

    private void handleFindBasicCommando(Commando c) {
        List<SearchResult> results = null;
        FindBasicCommando fbc = (FindBasicCommando)c;
        try{
            results = ServerController.getInstance().findBasic(fbc.getKeyword());
        }
        catch (DatabaseException ex) {
            ServerErrorCommando sec = new ServerErrorCommando();
            sec.setMessage(ex.getMessage());
            outputWriter.println(sec.toString());
        }
        if(results!= null && results.size()>=1){
            for(SearchResult result : results){
                FoundResultCommando frc = new FoundResultCommando();
                frc.setSearchID(fbc.getSearchID());
                frc.setFolder(result.isFolder());
                frc.setAccountName(result.getOwner());
                frc.setName(result.getName());
                frc.setSize(result.getSize());
                frc.setParentId(result.getParentId());
                outputWriter.println(frc.toString());
            }
        }else{
            NoResultsFoundCommando nrfc = new NoResultsFoundCommando();
            nrfc.setSearchID(fbc.getSearchID());
            nrfc.setKeyword(fbc.getKeyword());
            outputWriter.println(nrfc.toString());
        }
    }

    private void handleLogoffCommando(Commando c) {
        LogOffCommando lo = (LogOffCommando)c;
        Peer user = new Peer(lo.getAccountName(),lo.getPassword(),clientSocket.getInetAddress(),lo.getPort());
                     
        try {
            ServerController.getInstance().logoff(user);
        }
        catch (DatabaseException ex) {
            ServerErrorCommando sec = new ServerErrorCommando();
            sec.setMessage(ex.getMessage());
            outputWriter.println(sec.toString());
        }
        running = false;
    }

    private void handleLogonCommando(Commando c) {
        LogOnCommando lc = (LogOnCommando)c;
        Peer user = new Peer(lc.getAccountName(),lc.getPassword(),clientSocket.getInetAddress(),lc.getPort());
                     
        try {
            if(ServerController.getInstance().checkPassword(user)){
                if (ServerController.getInstance().logon(user)) {
                    WelcomeCommando wc = new WelcomeCommando();
                    wc.setAccountName(user.getAccountName());
                    outputWriter.println(wc.toString());
                }
            }
            else {
                LogNiLukNiCommando lnlnc = new LogNiLukNiCommando();
                String[] param = {user.getAccountName()};
                lnlnc.setAccountName(user.getAccountName());
                lnlnc.setMessage(getString("logonFailed",param));
                outputWriter.println(lnlnc.toString());
            }
        }
        catch (DatabaseException ex) {
            ServerErrorCommando sec = new ServerErrorCommando();
            sec.setMessage(ex.getMessage());
            outputWriter.println(sec.toString());
        }
    }

    /*private void handleShareCommando(Commando c) {
        ShareCommando sc = (ShareCommando)c;
        StringTokenizer tokenizer = new StringTokenizer(sc.getShares(),";");
        String token = tokenizer.nextToken();
        String[] split = token.split(":");
        SharedFolder root = new SharedFolder(true, sc.getAccountName(),split[0]);
        parseShares(root,Integer.parseInt(split[2]),tokenizer);
        try {
            ServerController.getInstance().addShares(sc.getAccountName(), root);
            printTree(root);
        }
        catch (DatabaseException ex) {
            ServerErrorCommando sec = new ServerErrorCommando();
            sec.setMessage(ex.getMessage());
            outputWriter.println(sec.toString());
        }
    }
        private static void printTree(SharedItem root) {
        if(root.isFolder()){
            System.out.println("Folder: "+root.getState()+" : "+
                    ((SharedFolder)root).getFolderID()+" : "+
                    ((SharedFolder)root).getParentID()+" : "+
                    ((SharedFolder)root).getName());
            for(SharedItem item:root.getSharedItems()){
                printTree((SharedItem)item);    
            }
            
        }
        else {
            System.out.println("File: "+root.getState()+" : "+((SharedFile)root).getFolderID()+" : "+((SharedFile)root).getName());
        }
    }
    
    private void parseShares(SharedFolder root,int number,StringTokenizer tokenizer){
        while(number>0){
            String token = tokenizer.nextToken();
            String[] split = token.split(":");
            if(split[1].equalsIgnoreCase("DIR") && Integer.parseInt(split[2])>0){
                SharedFolder folder = new SharedFolder(true, root.getAccountName(),
                        split[0].substring(split[0].indexOf(System.getProperty("file.separator"),2)));
                root.getSharedItems().add(folder);
                parseShares(folder, Integer.parseInt(split[2]), tokenizer);
            }
            else{
                if(Long.parseLong(split[2])>0){
                    root.getSharedItems().add(new SharedFile(false,split[0],Long.parseLong(split[2])));
                }
            }
            number--;
        }
    }*/

    private void handleShareSynchronisation(Commando c) {
        StartShareSynchronisationCommando sssc = (StartShareSynchronisationCommando)c;
        accountName = sssc.getAccountName();
    }
    private void handleAddShareCommando(Commando c) {
        AddShareCommando asc = (AddShareCommando)c;
        if(asc.isDirectory()){
            try {
                int id = ServerController.getInstance().addSharedFolder(asc.getPath(), accountName, asc.getName(), asc.getParentFolderID());
                ReceiveFolderIdCommando rfic = new ReceiveFolderIdCommando();
                rfic.setFolderId(id);
                outputWriter.println(rfic.toString());
            } catch (DatabaseException ex) {
                ServerErrorCommando sec = new ServerErrorCommando();
                sec.setMessage(ex.getMessage());
                outputWriter.println(sec.toString());
            }
        }
        else{
             try {
                ServerController.getInstance().addSharedFile(asc.getParentFolderID(),asc.getName(), asc.getSize());
            } catch (DatabaseException ex) {
                ServerErrorCommando sec = new ServerErrorCommando();
                sec.setMessage(ex.getMessage());
                outputWriter.println(sec.toString());
            }
        }
    }
    
}
