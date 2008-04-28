package ikshare.client;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.client.threads.ChatServerConversationThread;
import ikshare.client.threads.ServerConversationThread;
import ikshare.client.threads.ShareSynchronisationThread;
import ikshare.domain.IKShareFile;
import ikshare.domain.Peer;
import ikshare.domain.SearchResult;
import ikshare.domain.Transfer;
import ikshare.domain.TransferState;
import ikshare.protocol.command.CreateAccountCommando;
import ikshare.protocol.command.DownloadInformationRequestCommand;
import ikshare.protocol.command.DownloadInformationResponseCommand;
import ikshare.protocol.command.FindAdvancedFileCommando;
import ikshare.protocol.command.FindAdvancedFolderCommando;
import ikshare.protocol.command.FindBasicCommando;
import ikshare.protocol.command.LogOffCommando;
import ikshare.protocol.command.LogOnCommando;
import ikshare.protocol.command.chat.ChatEnterRoomCommando;
import ikshare.protocol.command.chat.ChatLeaveRoomCommando;
import ikshare.protocol.command.chat.ChatLogOffCommando;
import ikshare.protocol.command.chat.ChatLogOnCommando;
import ikshare.protocol.command.chat.ChatMessageCommando;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientController {
    private static ClientController instance;
    private ExecutorService executorService;
    private ServerConversationThread serverConversation;
    private ChatServerConversationThread chatServerConversation;
    
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
    
    public void startChatServerConversion() throws IOException{
    	if(chatServerConversation==null)
    	{
    		chatServerConversation = new ChatServerConversationThread();
    		executorService.execute(chatServerConversation);
    	}
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
    
    public void findAdvancedFile(String searchId, String text, boolean keywordAnd, int typeID, long minBytes, long maxBytes) {
        FindAdvancedFileCommando fafc = new FindAdvancedFileCommando();
        fafc.setSearchID(searchId);
        fafc.setKeyword(text);
        fafc.setTextAnd(keywordAnd);
        fafc.setTypeID(typeID);
        fafc.setMinSize(minBytes);
        fafc.setMaxSize(maxBytes);
        serverConversation.sendCommand(fafc);
        
    }

    public void findAdvancedFolder(String searchId, String text, boolean keywordAnd, long minBytes, long maxBytes) {
        FindAdvancedFolderCommando fafc = new FindAdvancedFolderCommando();
        fafc.setSearchID(searchId);
        fafc.setKeyword(text);
        fafc.setTextAnd(keywordAnd);
        fafc.setMinSize(minBytes);
        fafc.setMaxSize(maxBytes);
        serverConversation.sendCommand(fafc);
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
    
    public void chatEnterRoom(String roomName, String password, boolean privateRoom){
    	ChatEnterRoomCommando CERCommando = new ChatEnterRoomCommando();
    	CERCommando.setRoomName(roomName);
    	if(password == null)
    		CERCommando.setPassword("");
    	else
    		CERCommando.setPassword(password);
    	CERCommando.setPrivateRoom(privateRoom);
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
    
    public void stopServerConversation() {
        serverConversation.stop();
    }
    
    public void stopChatServerConversation() {
    	if(chatServerConversation!=null)
    	{
    		chatServerConversation.stop();
    		chatServerConversation = null;
    	}
    }
}
