package ikshare.client.chatComponents;

import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 *
 * @author Boris Martens
 */
public class ChatRoom 
{
    private List members;
    private Text textField;
    private String roomName;
    
    public ChatRoom() 
    {
    }
    
    public void setMembersList (List list)
    {
        this.members = list;
    }
    
    public List getMembersList()
    {
        return members;
    }
    
    public void setTextField(Text text)
    {
        this.textField = text;
    }
    
    public Text getTextField()
    {
        return textField;
    }
    
    public void setRoomName(String name)
    {
        this.roomName = name;
    }
    
    public String getRoomname()
    {
        return roomName;
    }
}
