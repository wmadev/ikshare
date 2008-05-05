package ikshare.client.gui.dialogs;

import ikshare.client.ClientController;
import ikshare.client.configuration.ClientConfigurationController;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ChatCreateRoomDialog extends Dialog{
	private Button btnOk;
	
	private Text txtRoomName;
	private Text txtPassword;
	private Button chkPublicRoom;

	private Shell shell;

	private String chatRoom;
	
	public ChatCreateRoomDialog(Shell parent, String chatRoom) {
		super(parent);
		this.chatRoom=chatRoom;
	}

	public void open()
	{
		shell = new Shell(getParent(),SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText(ClientConfigurationController.getInstance().getChatString("newroom"));
        shell.setLayout(new GridLayout(2,false));
        shell.setSize(300,140);
        shell.setLocation(300, 350);
        
        Label lblExplained = new Label(shell, SWT.NONE);
        GridData gdExplained = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gdExplained.horizontalSpan = 2;
        lblExplained.setLayoutData(gdExplained);
        lblExplained.setText(ClientConfigurationController.getInstance().getChatString("newroomexplained"));
        
        Label lblRoomName = new Label(shell, SWT.NONE);
        lblRoomName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        lblRoomName.setText(ClientConfigurationController.getInstance().getChatString("roomname"));
        
        txtRoomName = new Text(shell, SWT.NONE);
        txtRoomName.setText(chatRoom);
        txtRoomName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        txtRoomName.setSize(1, 100);
        
        Label lblPassword = new Label(shell, SWT.NONE);
        lblPassword.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        lblPassword.setText(ClientConfigurationController.getInstance().getChatString("password"));
        
        txtPassword = new Text(shell, SWT.NONE);
        txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        txtPassword.setSize(1, 100);
        
        Label lblPrivateRoom = new Label(shell, SWT.NONE);
        lblPrivateRoom.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        lblPrivateRoom.setText(ClientConfigurationController.getInstance().getChatString("ispublic"));
        
        chkPublicRoom = new Button(shell, SWT.CHECK);
        chkPublicRoom.setSelection(true);
        chkPublicRoom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        btnOk = new Button(shell, SWT.NONE);
        GridData gdBtnOk = new GridData(SWT.NONE, SWT.NONE, false, false);
        gdBtnOk.minimumWidth = 75;
        gdBtnOk.horizontalSpan = 2;
        btnOk.setLayoutData(gdBtnOk);
        btnOk.setText(ClientConfigurationController.getInstance().getChatString("ok"));
        btnOk.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if(!txtRoomName.getText().equals(""))
                {
                	textEntered(txtPassword.getText());
                }
            }
    	});
        
        shell.open();
        
        btnOk.setFocus();
	}
	
	private void textEntered(String text)
	{
		ClientController.getInstance().chatCreateRoom(txtRoomName.getText(), txtPassword.getText(), chkPublicRoom.getSelection());
		shell.close();
	}
}
