package ikshare.client.gui.panels;

import ikshare.client.ClientController;
import ikshare.client.chatComponents.ChatRoom;
import ikshare.client.configuration.ClientConfiguration;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.ChatServerConversationListener;
import ikshare.domain.event.listener.ClientConfigurationListener;
import ikshare.protocol.command.chat.*;

import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.dialogs.ChatCreateRoomDialog;
import ikshare.client.gui.dialogs.ChatRoomPasswordDialog;

public class ChatPanel extends AbstractPanel implements ChatServerConversationListener, ClientConfigurationListener
{
	private LogonState loggedOn = LogonState.Offline;
	
    private CTabFolder chatWindowTabFolder;
    private HashMap<String, ChatRoom> chatRooms;
    
    private List publicRoomsList;
    
    private Composite cmpPublicRooms;
    private Composite cmpPrivateRooms;
    
    private Button btnLog;
    private Label lblLog;
    private Label lblOnline;
    
    final Shell parentShell;
    
    public ChatPanel(String text,String icon)
    {
        super(text,icon);
        
        chatRooms = new HashMap<String, ChatRoom>();
        
        EventController.getInstance().addChatServerConversationListener(this);
        EventController.getInstance().addClientConfigurationListener(this);
        
        GridLayout mainLayout = new GridLayout(2, false);
        this.setLayout(mainLayout);
        this.init();
        this.setConnectState(2);
        
        parentShell = this.getShell();
    }
    
    private void init()
    {
    	{
	    	//== Rooms Panel ==
	    	Composite cmpRooms = new Composite(this, SWT.NONE);
	    	GridLayout cmpRoomsLayout = new GridLayout(1, false);
	    	cmpRoomsLayout.marginBottom = cmpRoomsLayout.marginHeight = cmpRoomsLayout.marginLeft = 
    		cmpRoomsLayout.marginRight = cmpRoomsLayout.marginTop = cmpRoomsLayout.marginWidth = 0;
	    	cmpRooms.setLayout(cmpRoomsLayout);
	    	cmpRooms.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));
	    	
	    	{
	    		//== Log panel ==
	    		Composite cmpLog = new Composite(cmpRooms, SWT.LEFT);
	    		GridData gdLog = new GridData(SWT.NONE, SWT.NONE, false, false);
	    		gdLog.horizontalIndent = gdLog.verticalIndent = 0;
	    		gdLog.widthHint = 200;
	    		cmpLog.setLayoutData(gdLog);
	    		StackLayout layoutLog = new StackLayout();
	    		cmpLog.setLayout(layoutLog);
	    		
		    	Group grpLog = new Group(cmpLog, SWT.BACKGROUND);
		    	grpLog.setLayout(new GridLayout(2, false));
		    	GridData gdGroupLog = new GridData(SWT.FILL, SWT.FILL, false, false);
		    	gdGroupLog.widthHint = 200;
		    	grpLog.setLayoutData(gdGroupLog);
		    	
		    	btnLog = new Button(grpLog, SWT.BACKGROUND);
		    	btnLog.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
		    	btnLog.setText(ClientConfigurationController.getInstance().getChatString("logon"));
		    	btnLog.addListener(SWT.Selection, new Listener() {
                    @Override
                    public void handleEvent(Event event) {
                        if(loggedOn != LogonState.Offline)
                            logOff();
                        else
                            logOn();
                    }
		    	});
		    	
		    	lblLog = new Label(grpLog, SWT.LEFT);
		    	lblLog.setText(ClientConfigurationController.getInstance().getConfiguration().getNickname());
		    	lblLog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		    	
		    	lblOnline = new Label(grpLog, SWT.CENTER);
		    	GridData gdLblOnline = new GridData(SWT.FILL, SWT.FILL, true, false);
		    	gdLblOnline.horizontalSpan = 2;
		    	lblOnline.setLayoutData(gdLblOnline);
		    	
		    	layoutLog.topControl = grpLog;
	    	}
	    	{
		    	//== Public Rooms panel ==
		    	cmpPublicRooms = new Composite(cmpRooms, SWT.LEFT);
		        GridData gdPublicRooms = new GridData(SWT.NONE, SWT.FILL, false, true);
		        gdPublicRooms.horizontalIndent = gdPublicRooms.verticalIndent = 0;
		        gdPublicRooms.widthHint = 200;
		        cmpPublicRooms.setLayoutData(gdPublicRooms);
		        
		        StackLayout layoutPublicRooms = new StackLayout();
		        cmpPublicRooms.setLayout(layoutPublicRooms);
		        
		    	Group grpPublicRooms = new Group(cmpPublicRooms, SWT.BACKGROUND);
		    	grpPublicRooms.setText(ClientConfigurationController.getInstance().getChatString("publicchatrooms"));
		    	grpPublicRooms.setLayout(new GridLayout(1, false));
		    	GridData gdGrpRooms = new GridData(SWT.FILL, SWT.FILL, false, false);
		    	gdGrpRooms.widthHint = 200;
		    	grpPublicRooms.setLayoutData(gdGrpRooms);
		
		        publicRoomsList = new List(grpPublicRooms, SWT.V_SCROLL | SWT.BORDER);
		        GridData gdPublicRoomsList = new GridData(SWT.FILL,SWT.FILL,true,true);
		        publicRoomsList.setLayoutData(gdPublicRoomsList);
		        
		        publicRoomsList.addListener(SWT.DefaultSelection, new Listener() {
		        	public void handleEvent(Event e)
		        	{
		                String selectedItem = null;
		                int[] selection = publicRoomsList.getSelectionIndices();
		                if(selection.length==1)
		                	selectedItem = publicRoomsList.getItem(selection[0]);
		                if(selectedItem!=null)
		                	ClientController.getInstance().chatEnterRoom(selectedItem, "");
		        	}
		        });
		        
		        layoutPublicRooms.topControl = grpPublicRooms;
	    	}
	    	{
		        //== Private Rooms panel ==
		        cmpPrivateRooms = new Composite(cmpRooms, SWT.LEFT);
		        GridData gdPrivateRooms = new GridData(SWT.NONE, SWT.NONE, false, false);
		        gdPrivateRooms.horizontalIndent = gdPrivateRooms.verticalIndent = 0;
		        gdPrivateRooms.widthHint = 200;
		        cmpPrivateRooms.setLayoutData(gdPrivateRooms);
		        
		        StackLayout layoutPrivateRooms = new StackLayout();
		        cmpPrivateRooms.setLayout(layoutPrivateRooms);
		        
		    	Group grpPrivateRooms = new Group(cmpPrivateRooms, SWT.BACKGROUND);
		    	grpPrivateRooms.setText(ClientConfigurationController.getInstance().getChatString("privatechatrooms"));
		    	grpPrivateRooms.setLayout(new GridLayout(2, true));
		    	GridData gdGrpPrivateRooms = new GridData(SWT.FILL, SWT.FILL, false, false);
		    	gdGrpPrivateRooms.widthHint = 200;
		    	grpPrivateRooms.setLayoutData(gdGrpPrivateRooms);
		        
		    	Label lblPrivateRoomName = new Label(grpPrivateRooms, SWT.None);
		    	lblPrivateRoomName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		    	lblPrivateRoomName.setText(ClientConfigurationController.getInstance().getChatString("roomname"));
		    	
		    	final Text txtPrivateRoomName = new Text(grpPrivateRooms, SWT.BORDER);
		    	txtPrivateRoomName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		    	txtPrivateRoomName.addKeyListener(new KeyAdapter() {
		        	public void keyPressed(KeyEvent event)
		        	{
		        		if(loggedOn == LogonState.Online)
		        		{
			        		if(event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR)
				        	{
			        			ClientController.getInstance().chatEnterRoom(txtPrivateRoomName.getText(), "");
				        		txtPrivateRoomName.setText("");
				        	}
		        		}
		        	}
		        });
		    	
		        layoutPrivateRooms.topControl = grpPrivateRooms;
	    	}
    	}
    	
    	{
	        chatWindowTabFolder = new CTabFolder(this, SWT.NONE);
            chatWindowTabFolder.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
            chatWindowTabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() 
            {
                @Override
                public void close(CTabFolderEvent event) 
                {
                    if((event.item instanceof CTabItem) && (loggedOn==LogonState.Online))
                    {
                        leaveRoom(((CTabItem)event.item).getText());
                    }
                }
            });
            
            chatWindowTabFolder.addListener(SWT.Selection, new Listener(){
                public void handleEvent(Event event) {
                    chatRooms.get(chatWindowTabFolder.getSelection().getText()).getEnterTextField().setFocus();
                }
            });
    	}
    }
    
    private void BuildChatPanel(ArrayList<String> members, final String roomName)
    {
        ChatRoom room = new ChatRoom();
        room.setRoomName(roomName);
        
        //== Chat Window ==
        final CTabItem chatRoomTab = new CTabItem(chatWindowTabFolder, SWT.CLOSE);
        chatRoomTab.setText(roomName);
    
        Composite cmpChatWindow = new Composite(chatWindowTabFolder, SWT.RIGHT);
        GridData gdChatWindow = new GridData(SWT.FILL, SWT.FILL, true, true);
        cmpChatWindow.setLayoutData(gdChatWindow);
        GridLayout layoutChatWindow = new GridLayout(2, false);
        layoutChatWindow.marginBottom = layoutChatWindow.marginHeight = layoutChatWindow.marginLeft = 
                layoutChatWindow.marginRight = layoutChatWindow.marginTop = layoutChatWindow.marginWidth = 0;
        cmpChatWindow.setLayout(layoutChatWindow);
        
        Group grpChatWindow = new Group(cmpChatWindow, SWT.NONE);
        grpChatWindow.setLayout(new GridLayout(1, false));
        grpChatWindow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Text txtChatWindow = new Text(grpChatWindow, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
        txtChatWindow.setEditable(false);
        txtChatWindow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        room.setTextField(txtChatWindow);

        final Text txtChatEnterField = new Text(grpChatWindow, SWT.BORDER);
        txtChatEnterField.setEditable(true);
        txtChatEnterField.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        room.setEnterTextField(txtChatEnterField);
        txtChatEnterField.addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent event)
        	{
        		if(event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR)
	        	{
        			textEntered(txtChatEnterField.getText(), roomName);
        			txtChatEnterField.setText("");
	        	}
        	}
        });

        Group grpChatMembers = new Group(cmpChatWindow, SWT.BACKGROUND);
        grpChatMembers.setLayout(new GridLayout(1, false));
        grpChatMembers.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));

        List lstChatMembers = new List(grpChatMembers, SWT.V_SCROLL | SWT.BORDER);
        lstChatMembers.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true));
        for(String member : members)
        {
            lstChatMembers.add(member);
        }
        room.setMembersList(lstChatMembers);
        chatRooms.put(room.getRoomname(), room);
        
        chatRoomTab.setControl(cmpChatWindow);
        
        chatWindowTabFolder.setSelection(chatRoomTab);
        
        txtChatEnterField.setFocus();
    }

	private void textEntered(String text, String roomName) {
        if(loggedOn == LogonState.Online && !text.equals(""))
        {
            String nickName = lblLog.getText();
            String fullMessage = "<" + nickName + "> ";
            fullMessage += text;
            appendMessage(roomName, (fullMessage + "\n"));
            ClientController.getInstance().chatMessage(text, roomName, false, nickName);
        }
	}
	
	private void logOn(){
        if(ClientController.getInstance().chatLogon(lblLog.getText()))
        {
            btnLog.setText(ClientConfigurationController.getInstance().getChatString("logoff"));
            setConnectState(1);
        }
        else
        	setConnectState(3, ClientConfigurationController.getInstance().getChatString("servernotreached"));
	}
	
	private void logOff(){
        if(loggedOn!=LogonState.Offline)
        {
			publicRoomsList.removeAll();
			chatRooms.clear();
			ClientController.getInstance().chatLogoff(lblLog.getText());
			btnLog.setText(ClientConfigurationController.getInstance().getChatString("logon"));
			setConnectState(2);
            for(CTabItem item : chatWindowTabFolder.getItems())
            {
                item.dispose();
            }
            
            lblLog.setText(ClientConfigurationController.getInstance().getConfiguration().getNickname());
        }
	}
        
    private void leaveRoom(String roomName) {
        ClientController.getInstance().chatLeaveRoom(roomName);
        chatRooms.remove(roomName);
    }
	
    private void setConnectState(int state)
    {
    	setConnectState(state, "");
    }
    
	private void setConnectState(int state, String message)
	{
		if(lblOnline!=null)
		{
			switch(state)
			{
				case 0:
				{
					lblOnline.setText(ClientConfigurationController.getInstance().getChatString("online"));
					lblOnline.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
					loggedOn = LogonState.Online;
                                        cmpPrivateRooms.setVisible(true);
                                        cmpPublicRooms.setVisible(true);
					break;
				}
				case 1:
				{
					lblOnline.setText(ClientConfigurationController.getInstance().getChatString("connecting"));
					lblOnline.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					loggedOn = LogonState.Logging;
                                        cmpPrivateRooms.setVisible(false);
                                        cmpPublicRooms.setVisible(false);
					break;
				}
				case 2:
				{
					lblOnline.setText(ClientConfigurationController.getInstance().getChatString("offline"));
					lblOnline.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
					loggedOn = LogonState.Offline;
                                        cmpPrivateRooms.setVisible(false);
                                        cmpPublicRooms.setVisible(false);                                     
					break;
				}
				default:
				{
					lblOnline.setText(ClientConfigurationController.getInstance().getChatString(message));
					lblOnline.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
					loggedOn = LogonState.Offline;
                                        cmpPrivateRooms.setVisible(false);
                                        cmpPublicRooms.setVisible(false);
					break;
				}
			}
		}
	}
	
    private void appendMessage(String roomName, String message)
    {
        ChatRoom room = chatRooms.get(roomName);
        room.getTextField().append(message);
    }
    

    @Override
    public void receivedMessage(final ChatMessageCommando c) {
		this.getDisplay().asyncExec(
			new Runnable() 
			{
		        public void run() 
		        {	
			        if(!c.isPrivateMessage() && c.getText()!=null && !c.getText().equals(""))
			        {
			            String message = "<" + c.getSender() + "> " + c.getText() + "\n";
			            appendMessage(c.getRecipient(), message);
			        }
		        }
			}
		);
    }
    
    @Override
    public void userLeftRoom(final ChatHasLeftRoomCommando c) {
		this.getDisplay().asyncExec(
			new Runnable() 
			{
		        public void run() 
		        {
			        ChatRoom room = chatRooms.get(c.getRoomName());
			        if(room!=null)
			        {
			            room.getMembersList().remove(c.getNickName());
			        
			            String message = "* " + c.getNickName() +" "+ ClientConfigurationController.getInstance().getChatString("leftroom") + "\n";
			            appendMessage(c.getRoomName(), message);
			        }
		        }
			}
		);
    }

    @Override
    public void userEntersRoom(final ChatHasEnteredRoomCommando c) {
		this.getDisplay().asyncExec(
			new Runnable() 
			{
		        public void run() 
		        {
			        ChatRoom room = chatRooms.get(c.getRoomName());
			        if(room!=null)
			        {
			            room.getMembersList().add(c.getNickName());
			        
			            String message = "* " + c.getNickName() +" "+ ClientConfigurationController.getInstance().getChatString("enteredroom") + "\n";
			            appendMessage(c.getRoomName(), message);
			        }
		        }
			}
		);
    }

    @Override
    public void youEnterRoom(final ChatYouEnterRoomCommando c) {
		this.getDisplay().asyncExec(
			new Runnable() 
			{
		        public void run() 
		        {
			        BuildChatPanel(c.getRoomMembers(), c.getRoomName());
			        
			        String message =  ClientConfigurationController.getInstance().getChatString("youenterroom") + "\n";
			        appendMessage(c.getRoomName(), message);
		        }
			}
		);
    }
    
	@Override
	public void youLoggedOn(final ChatWelcomeCommando c) {
		this.getDisplay().asyncExec(
			new Runnable() 
			{
		        public void run() 
		        {
		        	for(String room : c.getRooms())
		        	{
		        		ChatRoom newRoom = new ChatRoom();
		        		newRoom.setRoomName(room);
		        		chatRooms.put(room, newRoom);
		        		publicRoomsList.add(room);
		        	}
		        	setConnectState(0);
		        }
			}
		);
	}
    
    @Override
	public void invalidRoomPassword(final ChatInvalidRoomPasswordCommando c) {
		this.getDisplay().asyncExec(
			new Runnable() 
			{
		        public void run() 
		        {	
					ChatRoomPasswordDialog CRPDialog = new ChatRoomPasswordDialog(parentShell, c.getRoomName());
					CRPDialog.open();
		        }
			}
		);
	}
    
	@Override
	public void chatServerInterupt(String message) {
		this.getDisplay().asyncExec(
			new Runnable() 
			{
		        public void run() 
		        {
					publicRoomsList.removeAll();
					chatRooms.clear();
					btnLog.setText(ClientConfigurationController.getInstance().getChatString("logon"));
					setConnectState(3, ClientConfigurationController.getInstance().getChatString("connectioninterupt"));
		            for(CTabItem item : chatWindowTabFolder.getItems())
		            {
		                item.dispose();
		            }
		        }
			}
		);
	}
	
	@Override
	public void chatRoomDoesNotExist(final ChatRoomDoesNotExistCommando c) 
	{
		this.getDisplay().asyncExec(
			new Runnable() 
			{
		        public void run() 
		        {	
					ChatCreateRoomDialog CCRDialog = new ChatCreateRoomDialog(parentShell, c.getRoomName());
					CCRDialog.open();
		        }
			}
		);
	} 
	
	@Override
	public void chatRoomsUpdate(final ChatUpdateRoomsListCommando c) {
		this.getDisplay().asyncExec(
			new Runnable() 
			{
		        public void run() 
		        {	
					if(c.isAdded())
						publicRoomsList.add(c.getRoomName());
					else
						publicRoomsList.remove(c.getRoomName());
		        }
			}
		);
		
	}

	public void logNiLukNi(final ChatLogNiLukNiCommando c) {
		this.getDisplay().asyncExec(
			new Runnable() 
			{
		        public void run() 
		        {
					setConnectState(3, ClientConfigurationController.getInstance().getChatString(c.getMessage()));
					btnLog.setText(ClientConfigurationController.getInstance().getChatString("logon"));
		        }
			}
		);
	}
   
	@Override
	public void update(ClientConfiguration config) {
		if(lblLog!= null && loggedOn==LogonState.Offline)
		{
			lblLog.setText(config.getNickname());
		}
	}
	
	private enum LogonState
	{
		Online,
		Logging,
		Offline
	}

	@Override
	public void initialiseFocus() {
		btnLog.setFocus();
	}
}
