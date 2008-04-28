/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.ClientController;
import ikshare.client.chatComponents.ChatRoom;
import ikshare.client.configuration.ClientConfiguration;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.ChatServerConversationListener;
import ikshare.domain.event.listener.ClientConfigurationListener;
import ikshare.protocol.command.chat.ChatHasEnteredRoomCommando;
import ikshare.protocol.command.chat.ChatHasLeftRoomCommando;
import ikshare.protocol.command.chat.ChatInvalidRoomPasswordCommando;
import ikshare.protocol.command.chat.ChatMessageCommando;
import ikshare.protocol.command.chat.ChatWelcomeCommando;
import ikshare.protocol.command.chat.ChatYouEnterRoomCommando;

import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import ikshare.client.gui.AbstractPanel;

/**
 *
 * @author Jana
 */
public class ChatPanel extends AbstractPanel implements ChatServerConversationListener, ClientConfigurationListener
{
	private LogonState loggedOn = LogonState.Offline;
	
    private TabFolder chatWindowTabFolder;
    private HashMap<String, ChatRoom> chatRooms;
    
    private List publicRoomsList;
    
    private Button btnLog;
    private Label lblLog;
    private Label lblOnline;
    
    public ChatPanel(String text,String icon)
    {
        super(text,icon);
        
        chatRooms = new HashMap<String, ChatRoom>();
        
        EventController.getInstance().addChatServerConversationListener(this);
        
        GridLayout mainLayout = new GridLayout(2, false);
        this.setLayout(mainLayout);
        this.init();
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
		    	grpLog.setLayout(new GridLayout(2, true));
		    	GridData gdGroupLog = new GridData(SWT.FILL, SWT.FILL, false, false);
		    	gdGroupLog.widthHint = 200;
		    	grpLog.setLayoutData(gdGroupLog);
		    	
		    	btnLog = new Button(grpLog, SWT.BACKGROUND);
		    	btnLog.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		    	btnLog.setText("Log on"); // TODO: bundle
		    	btnLog.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(Event event) {
						if(loggedOn != LogonState.Offline)
							logOff();
						else
							logOn();
					}
		    	});
		    	
		    	lblLog = new Label(grpLog, SWT.BACKGROUND);
		    	lblLog.setText(ClientConfigurationController.getInstance().getConfiguration().getNickname());
		    	
		    	lblOnline = new Label(grpLog, SWT.CENTER);
		    	GridData gdLblOnline = new GridData(SWT.FILL, SWT.FILL, false, false);
		    	gdLblOnline.horizontalSpan = 2;
		    	lblOnline.setLayoutData(gdLblOnline);
		    	setStateOnline(2);
		    	
		    	layoutLog.topControl = grpLog;
	    	}
	    	{
		    	//== Public Rooms panel ==
		    	Composite cmpPublicRooms = new Composite(cmpRooms, SWT.LEFT);
		        GridData gdPublicRooms = new GridData(SWT.NONE, SWT.FILL, false, true);
		        gdPublicRooms.horizontalIndent = gdPublicRooms.verticalIndent = 0;
		        gdPublicRooms.widthHint = 200;
		        cmpPublicRooms.setLayoutData(gdPublicRooms);
		        
		        StackLayout layoutPublicRooms = new StackLayout();
		        cmpPublicRooms.setLayout(layoutPublicRooms);
		        
		    	Group grpPublicRooms = new Group(cmpPublicRooms, SWT.BACKGROUND);
		    	grpPublicRooms.setText("Public Chatrooms");// TODO: bundle
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
		                String selectedItem = "";
		                int[] selection = publicRoomsList.getSelectionIndices();
		                if(selection.length==1)
		                	selectedItem = publicRoomsList.getItem(selection[0]);
		                ClientController.getInstance().chatEnterRoom(selectedItem, "", false);
		        	}
		        });
		        
		        layoutPublicRooms.topControl = grpPublicRooms;
	    	}
	    	{
		        //== Private Rooms panel ==
		        Composite cmpPrivateRooms = new Composite(cmpRooms, SWT.LEFT);
		        GridData gdPrivateRooms = new GridData(SWT.NONE, SWT.NONE, false, false);
		        gdPrivateRooms.horizontalIndent = gdPrivateRooms.verticalIndent = 0;
		        gdPrivateRooms.widthHint = 200;
		        cmpPrivateRooms.setLayoutData(gdPrivateRooms);
		        
		        StackLayout layoutPrivateRooms = new StackLayout();
		        cmpPrivateRooms.setLayout(layoutPrivateRooms);
		        
		    	Group grpPrivateRooms = new Group(cmpPrivateRooms, SWT.BACKGROUND);
		    	grpPrivateRooms.setText("Private Chatrooms");
		    	grpPrivateRooms.setLayout(new GridLayout(2, true));
		    	GridData gdGrpPrivateRooms = new GridData(SWT.FILL, SWT.FILL, false, false);
		    	gdGrpPrivateRooms.widthHint = 200;
		    	grpPrivateRooms.setLayoutData(gdGrpPrivateRooms);
		        
		    	Label lblPrivateRoomName = new Label(grpPrivateRooms, SWT.None);
		    	lblPrivateRoomName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		    	lblPrivateRoomName.setText("Room name");// TODO: bundle
		    	
		    	Text txtPrivateRoomName = new Text(grpPrivateRooms, SWT.BORDER);
		    	txtPrivateRoomName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		    	
		        layoutPrivateRooms.topControl = grpPrivateRooms;
	    	}
    	}
    	
    	{
	        chatWindowTabFolder = new TabFolder(this, SWT.NONE);
                chatWindowTabFolder.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
    	}
    }
    
    private void BuildChatPanel(ArrayList<String> members, final String roomName)
    {
    	// TODO: room panels kunnen sluiten.
    	
        ChatRoom room = new ChatRoom();
        room.setRoomName(roomName);
        
        //== Chat Window ==
        TabItem chatRoomTab = new TabItem(chatWindowTabFolder, SWT.BORDER);
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
        txtChatEnterField.addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent event)
        	{
        		if(event.keyCode == SWT.CR)
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
    }

	private void textEntered(String text, String roomName) {
		if(loggedOn == LogonState.Online && !text.equals(""))
		{
			String fullMessage = "<" + ClientConfigurationController.getInstance().getConfiguration().getNickname() + "> ";
			fullMessage += text;
			appendMessage(roomName, fullMessage);
		}
	}
	
	private void logOn(){
		if(ClientController.getInstance().chatLogon(ClientConfigurationController.getInstance().getConfiguration().getNickname()))
		{
			btnLog.setText("Log off");
			setStateOnline(1);
		}
	}
	
	private void logOff(){
		publicRoomsList.removeAll();
		chatRooms.clear();
		ClientController.getInstance().chatLogoff(ClientConfigurationController.getInstance().getConfiguration().getNickname());
		btnLog.setText("Log on");
		setStateOnline(2);
	}
	
	private void setStateOnline(int state)
	{
		if(lblOnline!=null)
		{
			switch(state)
			{
				case 0:
				{
					lblOnline.setText("Online"); //TODO: bundle
					lblOnline.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
					loggedOn = LogonState.Online;
					break;
				}
				case 1:
				{
					lblOnline.setText("Connecting..."); //TODO: bundle
					lblOnline.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					loggedOn = LogonState.Logging;
					break;
				}
				case 2:
				{
					lblOnline.setText("Offline"); //TODO: bundle
					lblOnline.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
					loggedOn = LogonState.Offline;
					break;
				}
				default:
				{
					lblOnline.setText("Offline"); //TODO: bundle
					lblOnline.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
					loggedOn = LogonState.Offline;
					break;
				}
			}
		}
	}
	
    private void appendMessage(String roomName, String message)
    {
        ChatRoom room = chatRooms.get(roomName);
        room.getTextField().append(message + "\n");
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
				        
				            String message = "* " + c.getNickName() + " has left the room.";// TODO: bundle
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
				        
				            String message = "* " + c.getNickName() + " has entered the room.";// TODO: bundle
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
				        
				        String message = "you have entered the room.";// TODO: bundle
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
		        	setStateOnline(0);
		        }
			}
		);
	}
    
    @Override
	public void invalidRoomPassword(ChatInvalidRoomPasswordCommando c) {
		// TODO Auto-generated method stub
		
	}
   
	@Override
	public void update(ClientConfiguration config) {
		if(lblLog!= null)
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
}
