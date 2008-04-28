package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;
import java.util.ArrayList;

/**
 *
 * @author Boris Martens
 */
public class ChatYouEnterRoomCommando extends Commando
{
    private String roomName;
    private ArrayList<String> roomMembers;
    
    public ChatYouEnterRoomCommando() 
    {
    	super();
    }
    
    public ChatYouEnterRoomCommando(String commandoString) 
    {
    	super(commandoString);
    	setRoomName(commandoLine.get(1));
    	for(int i =2; i < commandoLine.size(); i++)
    	{
    		addRoomMember(commandoLine.get(i));
    	}
    }
    
    public String getRoomName() 
    {
        return roomName;
    }

    public void setRoomName(String roomName) 
    {
        this.roomName = roomName;
    }
    
    public void addRoomMember(String member)
    {
        if(roomMembers==null)
            roomMembers = new ArrayList<String>();
        roomMembers.add(member);
    }
    
    public ArrayList<String> getRoomMembers()
    {
        return roomMembers;
    }
    
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        String totalString = commandoBundle.getString("chatyouenterroom") + del + getRoomName();
        
        for(String member : getRoomMembers())
        {
            totalString += (del + member);
        }
        
        return totalString;
    }
}
