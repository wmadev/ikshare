package ikshare.client.gui.dialogs;

import ikshare.client.chatComponents.ChatRoom;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ChatRoomPasswordDialog extends Dialog{
	private Button btnOk;
	private Button btnCancel;
	private Text txtPassword;
	private ChatRoom chatRoom;
	
	public ChatRoomPasswordDialog(Shell parent, ChatRoom chatRoom) {
		super(parent);
		this.chatRoom = chatRoom;
	}

}
