/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.chatComponents.ChatRoom;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.ChatServerConversationListener;
import ikshare.protocol.command.chat.ChatHasEnteredRoomCommando;
import ikshare.protocol.command.chat.ChatHasLeftRoomCommando;
import ikshare.protocol.command.chat.ChatMessageCommando;
import ikshare.protocol.command.chat.ChatYouEnterRoomCommando;
import java.util.ArrayList;
import java.util.HashMap;
import javax.naming.event.EventContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import ikshare.client.gui.AbstractPanel;

/**
 *
 * @author Jana
 */
public class ChatPanel extends AbstractPanel implements ChatServerConversationListener
{
    private TabFolder chatWindowTabFolder;
    private HashMap<String, ChatRoom> chatRooms;
    
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
		    	//== Public Rooms panel ==
		    	Composite cmpPublicRooms = new Composite(cmpRooms, SWT.LEFT);
		        GridData gdPublicRooms = new GridData(SWT.NONE, SWT.FILL, false, true);
		        gdPublicRooms.horizontalIndent = gdPublicRooms.verticalIndent = 0;
		        gdPublicRooms.widthHint = 200;
		        cmpPublicRooms.setLayoutData(gdPublicRooms);
		        
		        StackLayout layoutPublicRooms = new StackLayout();
		        cmpPublicRooms.setLayout(layoutPublicRooms);
		        
		    	Group grpPublicRooms = new Group(cmpPublicRooms, SWT.BACKGROUND);
		    	grpPublicRooms.setText("Public Chatrooms");
		    	grpPublicRooms.setLayout(new GridLayout(1, false));
		    	GridData gdGrpRooms = new GridData(SWT.FILL, SWT.FILL, false, false);
		    	gdGrpRooms.widthHint = 200;
		    	grpPublicRooms.setLayoutData(gdGrpRooms);
		
		        final List publicRoomsList = new List(grpPublicRooms, SWT.V_SCROLL | SWT.BORDER);
		        GridData gdPublicRoomsList = new GridData(SWT.FILL,SWT.FILL,true,true);
		        publicRoomsList.setLayoutData(gdPublicRoomsList);
		        for(int i = 1; i < 25; i++)
		        	publicRoomsList.add("test room " + i);
		        
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
		    	grpPrivateRooms.setLayout(new GridLayout(1, false));
		    	GridData gdGrpPrivateRooms = new GridData(SWT.FILL, SWT.FILL, false, false);
		    	gdGrpPrivateRooms.widthHint = 200;
		    	grpPrivateRooms.setLayoutData(gdGrpPrivateRooms);
		        
		    	Label lblPrivateRoomName = new Label(grpPrivateRooms, SWT.None);
		    	lblPrivateRoomName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		    	lblPrivateRoomName.setText("Room name");
		    	
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
    
    private void BuildChatPanel(ArrayList<String> members, String roomName)
    {
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
        
        chatRoomTab.setControl(cmpChatWindow);

        Group grpChatWindow = new Group(cmpChatWindow, SWT.BACKGROUND);
        grpChatWindow.setLayout(new GridLayout(1, false));
        grpChatWindow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Text txtChatWindow = new Text(grpChatWindow, SWT.BORDER | SWT.V_SCROLL);
        txtChatWindow.setEditable(false);
        txtChatWindow.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        room.setTextField(txtChatWindow);

        Text txtChatEnterField = new Text(grpChatWindow, SWT.BORDER);
        txtChatEnterField.setEditable(true);
        txtChatEnterField.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));



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
    }

    public void receivedMessage(ChatMessageCommando c) {
        if(!c.isPrivateMessage() && c.getText()!=null && !c.getText().equals(""))
        {
            String message = "<" + c.getSender() + "> " + c.getText() + "\n";
            appendMessage(c.getRecipient(), message);
        }
    }

    public void userLeftRoom(ChatHasLeftRoomCommando c) {
        ChatRoom room = chatRooms.get(c.getRoomName());
        if(room!=null)
        {
            room.getMembersList().remove(c.getNickName());
        
            String message = "* " + c.getNickName() + " has left the room.";
            appendMessage(c.getRoomName(), message);
        }
    }

    public void userEntersRoom(ChatHasEnteredRoomCommando c) {
        ChatRoom room = chatRooms.get(c.getRoomName());
        if(room!=null)
        {
            room.getMembersList().add(c.getNickName());
        
            String message = "* " + c.getNickName() + " has entered the room.";
            appendMessage(c.getRoomName(), message);
        }
    }

    public void youEnterRoom(ChatYouEnterRoomCommando c) {
        ArrayList<String> members = c.getRoomMembers();

        BuildChatPanel(members, c.getRoomName());
        
        String message = "you have entered the room.";
        appendMessage(c.getRoomName(), message);
    }
    
    private void appendMessage(String roomName, String message)
    {
        ChatRoom room = chatRooms.get(roomName);
        room.getTextField().append(message);
    }
}
