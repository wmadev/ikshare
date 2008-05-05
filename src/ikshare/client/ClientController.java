package ikshare.client;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.client.threads.ChatServerConversationThread;
import ikshare.client.threads.ServerConversationThread;
import ikshare.client.threads.ShareSynchronisationThread;
import ikshare.domain.IKShareFile;
import ikshare.domain.Peer;
import ikshare.domain.PeerFacade;
import ikshare.domain.SearchResult;
import ikshare.domain.Transfer;
import ikshare.domain.TransferState;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.ServerConversationListener;
import ikshare.domain.exception.NoServerConnectionException;
import ikshare.exceptions.ConfigurationException;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.CreateAccountCommando;
import ikshare.protocol.command.DownloadInformationRequestCommand;
import ikshare.protocol.command.DownloadInformationResponseCommand;
import ikshare.protocol.command.FindAdvancedFileCommando;
import ikshare.protocol.command.FindAdvancedFolderCommando;
import ikshare.protocol.command.FindBasicCommando;
import ikshare.protocol.command.LogNiLukNiCommando;
import ikshare.protocol.command.LogOffCommando;
import ikshare.protocol.command.LogOnCommando;
import ikshare.protocol.command.WelcomeCommando;
import ikshare.protocol.command.chat.ChatCreateRoomCommando;
import ikshare.protocol.command.chat.ChatEnterRoomCommando;
import ikshare.protocol.command.chat.ChatLeaveRoomCommando;
import ikshare.protocol.command.chat.ChatLogOffCommando;
import ikshare.protocol.command.chat.ChatLogOnCommando;
import ikshare.protocol.command.chat.ChatMessageCommando;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientController implements ServerConversationListener{
    private static ClientController instance;
    private ExecutorService executorService;
    private ServerConversationThread serverConversation;
    private ChatServerConversationThread chatServerConversation;
    
    private ClientController(){
        EventController.getInstance().addServerConversationListener(this);
        executorService = Executors.newCachedThreadPool();
    }
    
    public static ClientController getInstance(){
        if(instance == null)
            instance = new ClientController();
        return instance;
    }

    public void createAccount(String accountName, String accountPassword, String accountEmail) throws NoServerConnectionException, ConfigurationException {

        CreateAccountCommando cac = new CreateAccountCommando();
        cac.setAccountName(accountName);
        cac.setPassword(md5encrypt(accountPassword));
        cac.setEmail(accountEmail);
        sendCommand(cac);
    }

   
    public void getDownloadInformationForResult(SearchResult rs) throws NoServerConnectionException {
        DownloadInformationRequestCommand dirc = new DownloadInformationRequestCommand();
        dirc.setAccountName(rs.getOwner());
        dirc.setFileName(rs.getName());
        dirc.setFileSize(rs.getSize());
        dirc.setFolderId(rs.getFolderId());
        sendCommand(dirc);
    }

    public Transfer getTransferForDownload(DownloadInformationResponseCommand dirc) throws UnknownHostException {
        Transfer t = new Transfer();
        t.setId(String.valueOf(new Date().getTime()));
        t.setFile(new IKShareFile(dirc.getPath(), dirc.getName()));
        t.setPeer(new Peer(dirc.getAccountName(), InetAddress.getByName(dirc.getIp()), dirc.getPort()));
        t.setState(TransferState.DOWNLOADING);
        return t;
    }

    public void logoff(String accountName, String password, int port) throws NoServerConnectionException, ConfigurationException {
        LogOffCommando loc = new LogOffCommando();
        loc.setAccountName(accountName);
        loc.setPassword(md5encrypt(password));
        loc.setPort(port);
        sendCommand(loc);
    }
    
    public void startServerConversation() throws IOException{
        if(serverConversation == null){
            serverConversation = new ServerConversationThread();
            executorService.execute(serverConversation);
        }
    }
    
    public void startChatServerConversion() throws IOException{
    	if(chatServerConversation==null)
    	{
    		chatServerConversation = new ChatServerConversationThread();
    		executorService.execute(chatServerConversation);
    	}
    }

    public boolean logon(String accountname,String password,int port) throws NoServerConnectionException, ConfigurationException{
        LogOnCommando loc = new LogOnCommando();
        loc.setAccountName(accountname);
        loc.setPassword(md5encrypt(password));
        loc.setPort(port);
        sendCommand(loc);
        return true;
    }
    public boolean findBasic(String searchId,String keyword) throws NoServerConnectionException{
        FindBasicCommando fbc = new FindBasicCommando();
        fbc.setKeyword(keyword);
        fbc.setSearchID(searchId);
        sendCommand(fbc);
        return true;
    }
    
    private void sendCommand(Commando c) throws NoServerConnectionException{
        if(serverConversation == null){
            
                throw new NoServerConnectionException("No connection with the server");
            
            
        }
        else{
            serverConversation.sendCommand(c);
        }
        
        
    }
    
    public void findAdvancedFile(String searchId, String text, boolean keywordAnd, int typeID, long minBytes, long maxBytes) throws NoServerConnectionException {
        FindAdvancedFileCommando fafc = new FindAdvancedFileCommando();
        fafc.setSearchID(searchId);
        fafc.setKeyword(text);
        fafc.setTextAnd(keywordAnd);
        fafc.setTypeID(typeID);
        fafc.setMinSize(minBytes);
        fafc.setMaxSize(maxBytes);
        sendCommand(fafc);
        
    }

    public void findAdvancedFolder(String searchId, String text, boolean keywordAnd, long minBytes, long maxBytes) throws NoServerConnectionException {
        FindAdvancedFolderCommando fafc = new FindAdvancedFolderCommando();
        fafc.setSearchID(searchId);
        fafc.setKeyword(text);
        fafc.setTextAnd(keywordAnd);
        fafc.setMinSize(minBytes);
        fafc.setMaxSize(maxBytes);
        sendCommand(fafc);
    }

    public boolean share(String accountName,File root) throws IOException{
        executorService.execute(new ShareSynchronisationThread(accountName,root));
        return true;
    }
    
    public boolean chatLogon(String nickName) {
    	try
    	{
    		startChatServerConversion();
    		ChatLogOnCommando CLOCommand = new ChatLogOnCommando();
    		CLOCommand.setNickName(nickName);
    		CLOCommand.setPort(ClientConfigurationController.getInstance().getConfiguration().getChatServerPort());
    		chatServerConversation.sendCommand(CLOCommand);
    		return true;
    	}
    	catch(IOException e){
    		return false;
    	}
    }
    
    public void chatLogoff(String nickName){
    	ChatLogOffCommando CLOCommando = new ChatLogOffCommando();
    	CLOCommando.setNickName(nickName);
    	chatServerConversation.sendCommand(CLOCommando);
    	stopChatServerConversation();
    }
    
    public void chatEnterRoom(String roomName, String password){
    	ChatEnterRoomCommando CERCommando = new ChatEnterRoomCommando();
    	CERCommando.setRoomName(roomName);
    	if(password == null)
    		CERCommando.setPassword("");
    	else
    		CERCommando.setPassword(password);
    	chatServerConversation.sendCommand(CERCommando);
    }
    
    public void chatLeaveRoom(String roomName){
    	ChatLeaveRoomCommando CLRCommando = new ChatLeaveRoomCommando();
    	CLRCommando.setRoomName(roomName);
    	chatServerConversation.sendCommand(CLRCommando);
    }
    
    public void chatMessage(String message, String recipient, boolean privateMessage, String nickName){
    	ChatMessageCommando CMCommando = new ChatMessageCommando();
    	CMCommando.setPrivateMessage(privateMessage);
    	CMCommando.setRecipient(recipient);
    	CMCommando.setSender(nickName);
    	CMCommando.setText(message);
    	chatServerConversation.sendCommand(CMCommando);
    }

	public void chatCreateRoom(String roomName, String roomPassword, boolean publicRoom) 
	{
		ChatCreateRoomCommando CCRCommando = new ChatCreateRoomCommando();
		CCRCommando.setRoomName(roomName);
		if(roomPassword!=null && !roomPassword.equals(""))
			CCRCommando.setPassword(roomPassword);
		else
			CCRCommando.setPassword("");
		CCRCommando.setPrivateRoom(!publicRoom);
		chatServerConversation.sendCommand(CCRCommando);
	}
    
    public void stopServerConversation() {
        serverConversation.stop();
        serverConversation = null;
    }
    
    public void stopChatServerConversation() {
    	if(chatServerConversation!=null)
    	{
    		chatServerConversation.stop();
    		chatServerConversation = null;
    	}
    }
    private String md5encrypt(String text) throws ConfigurationException {
        String md5 = "";
        try {
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte[] md5bytes = md.digest();
            for (byte b : md5bytes) {
                md5 += Integer.toHexString( b & 0xff );
            }
            return md5;
        } catch (NoSuchAlgorithmException ex) {
            throw new ConfigurationException(ClientConfigurationController.getInstance().getString("configurationException"),ex);
        }
    }
    
    
    public void receivedCommando(Commando c) {
        if( c instanceof WelcomeCommando){
            try {
                WelcomeCommando wc = (WelcomeCommando) c;
                Peer p = new Peer(wc.getAccountName(), InetAddress.getByName(wc.getIpAddress()));
                PeerFacade.getInstance().setPeer(p);
                EventController.getInstance().triggerLoggedOnEvent();
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
                // TODO betere afhandeling
            }
        }
        else if (c instanceof LogNiLukNiCommando){
            EventController.getInstance().triggerLogOnFailedEvent(((LogNiLukNiCommando)c).getMessage());
        }
    }
}
