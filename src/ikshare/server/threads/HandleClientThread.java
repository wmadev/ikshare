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
import java.util.List;
import java.util.ResourceBundle;

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
                    else if( c instanceof FindAdvancedFileCommando){
                        handleFindAdvancedFile(c);
                    }
                    else if( c instanceof FindAdvancedFolderCommando){
                        handleFindAdvancedFolder(c);
                    }
                    else if( c instanceof DownloadInformationRequestCommand){
                        handleDownloadInformationRequest((DownloadInformationRequestCommand)c);
                    }
                     
                    
                }
            }
            clientSocket.close();
            System.out.println("Client afgesloten");
        } catch (Exception ex) {
            ex.printStackTrace();
            running = false;
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

    private void handleDownloadInformationRequest(DownloadInformationRequestCommand c) {
        try {
            DownloadInformation di = ServerController.getInstance().getDownloadInformation(c.getAccountName(), c.getFileName(), c.getFileSize(), c.getFolderId());
            if (di != null) {
                DownloadInformationResponseCommand dirc = new DownloadInformationResponseCommand();
                dirc.setAccountName(di.getAccountName());
                dirc.setIp(di.getIp());
                dirc.setName(di.getName());
                dirc.setPath(di.getPath());
                dirc.setPort(di.getPort());
                outputWriter.println(dirc.toString());
            }
        } catch (DatabaseException ex) {
            ServerErrorCommando sec = new ServerErrorCommando();
            sec.setMessage(ex.getMessage());
            outputWriter.println(sec.toString());
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
                frc.setSearchKeyword(fbc.getKeyword());
                frc.setFolder(result.isFolder());
                frc.setAccountName(result.getOwner());
                frc.setName(result.getName());
                frc.setSize(result.getSize());
                frc.setParentId(result.getParentId());
                frc.setFolderId(result.getFolderId());
                outputWriter.println(frc.toString());
            }
        }else{
            NoResultsFoundCommando nrfc = new NoResultsFoundCommando();
            nrfc.setSearchID(fbc.getSearchID());
            nrfc.setKeyword(fbc.getKeyword());
            outputWriter.println(nrfc.toString());
        }
    }
    
    private void handleFindAdvancedFile(Commando c) {
        List<SearchResult> results = null;
        FindAdvancedFileCommando fafc = (FindAdvancedFileCommando)c;
        try{
            results = ServerController.getInstance().findAdvancedFile(fafc.getKeyword(), fafc.isTextAnd(), fafc.getTypeID(), fafc.getMinSize(), fafc.getMaxSize());
        }
        catch (DatabaseException ex) {
            ServerErrorCommando sec = new ServerErrorCommando();
            sec.setMessage(ex.getMessage());
            outputWriter.println(sec.toString());
        }
        if(results!= null && results.size()>=1){
            for(SearchResult result : results){
                FoundResultCommando frc = new FoundResultCommando();
                frc.setSearchID(fafc.getSearchID());
                frc.setSearchKeyword(fafc.getKeyword());
                frc.setFolder(result.isFolder());
                frc.setAccountName(result.getOwner());
                frc.setName(result.getName());
                frc.setSize(result.getSize());
                frc.setParentId(result.getParentId());
                frc.setFolderId(result.getFolderId());
                outputWriter.println(frc.toString());
            }
        }else{
            NoResultsFoundCommando nrfc = new NoResultsFoundCommando();
            nrfc.setSearchID(fafc.getSearchID());
            nrfc.setKeyword(fafc.getKeyword());
            outputWriter.println(nrfc.toString());
        }
    }

    private void handleFindAdvancedFolder(Commando c) {
            List<SearchResult> results = null;
        FindAdvancedFolderCommando fafc = (FindAdvancedFolderCommando)c;
        try{
            results = ServerController.getInstance().findAdvancedFolder(fafc.getKeyword(), fafc.isTextAnd(), fafc.getMinSize(), fafc.getMaxSize());
        }
        catch (DatabaseException ex) {
            ServerErrorCommando sec = new ServerErrorCommando();
            sec.setMessage(ex.getMessage());
            outputWriter.println(sec.toString());
        }
        if(results!= null && results.size()>=1){
            for(SearchResult result : results){
                FoundResultCommando frc = new FoundResultCommando();
                frc.setSearchID(fafc.getSearchID());
                frc.setSearchKeyword(fafc.getKeyword());
                frc.setFolder(result.isFolder());
                frc.setAccountName(result.getOwner());
                frc.setName(result.getName());
                frc.setSize(result.getSize());
                frc.setParentId(result.getParentId());
                frc.setFolderId(result.getFolderId());
                outputWriter.println(frc.toString());
            }
        }else{
            NoResultsFoundCommando nrfc = new NoResultsFoundCommando();
            nrfc.setSearchID(fafc.getSearchID());
            nrfc.setKeyword(fafc.getKeyword());
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
                    wc.setIpAddress(clientSocket.getInetAddress().getHostAddress());
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
