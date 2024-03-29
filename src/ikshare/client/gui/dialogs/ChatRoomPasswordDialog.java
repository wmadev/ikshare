package ikshare.client.gui.dialogs;

import ikshare.client.ClientController;
import ikshare.client.configuration.ClientConfigurationController;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ChatRoomPasswordDialog extends Dialog{
	private Button btnOk;
	private Text txtPassword;
	
	private String chatRoom = "";
	
	private Shell shell;
	
	
	public ChatRoomPasswordDialog(Shell parent, String chatRoom) {
		super(parent);
		this.chatRoom=chatRoom;
	}

	public void open()
	{
		shell = new Shell(getParent(),SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText(ClientConfigurationController.getInstance().getChatString("passwordplease"));
        shell.setLayout(new GridLayout(2,false));
        shell.setSize(300,110);
        shell.setLocation(250, 350);
        
        Label lblExplained = new Label(shell, SWT.NONE);
        GridData gdExplained = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gdExplained.horizontalSpan = 2;
        lblExplained.setLayoutData(gdExplained);
        lblExplained.setText(ClientConfigurationController.getInstance().getChatString("passwordexplained") + " \"" + chatRoom + "\"");
        
        Label lblPassword = new Label(shell, SWT.NONE);
        lblPassword.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        lblPassword.setText(ClientConfigurationController.getInstance().getChatString("password"));
        
        txtPassword = new Text(shell, SWT.NONE);
        txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        txtPassword.setSize(1, 100);
        txtPassword.addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent event)
        	{
        		if(event.keyCode == SWT.CR  || event.keyCode == SWT.KEYPAD_CR)
	        	{                
        			if(!txtPassword.getText().equals(""))
	                {
	        			textEntered(txtPassword.getText());
		        	}
	        	}
        	}
        });

        btnOk = new Button(shell, SWT.NONE);
        GridData gdBtnOk = new GridData(SWT.NONE, SWT.NONE, false, false);
        gdBtnOk.minimumWidth = 75;
        gdBtnOk.horizontalSpan = 2;
        btnOk.setLayoutData(gdBtnOk);
        btnOk.setText(ClientConfigurationController.getInstance().getChatString("ok"));
        btnOk.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if(!txtPassword.getText().equals(""))
                {
                	textEntered(txtPassword.getText());
                }
            }
    	});
        
        shell.open();
	}
	
	private void textEntered(String text)
	{
		ClientController.getInstance().chatEnterRoom(chatRoom, text);
		shell.close();
	}
}
