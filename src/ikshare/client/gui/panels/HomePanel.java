package ikshare.client.gui.panels;

import ikshare.client.ClientController;
import ikshare.client.gui.AbstractPanel;
import ikshare.client.configuration.*;
import ikshare.client.gui.custom.UIFileBrowser;
import ikshare.client.gui.dialogs.CreateAccountDialog;
import ikshare.client.gui.dialogs.CreateAccountDialogData;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.ServerConversationListener;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.LogNiLukNiCommando;
import ikshare.protocol.command.WelcomeCommando;
import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


public class HomePanel extends AbstractPanel implements ServerConversationListener{

    private static String ICON_LOGON = "resources/icons/home_logon.png";
    private static String ICON_LOGOFF = "resources/icons/home_logoff.png";
    
    private Label lblStatus;
    private Button btnConnect;
            
    public HomePanel(String text,String icon){
        super(text,icon);
        GridLayout gd=new GridLayout(2,false);
        this.setLayout(gd);
        EventController.getInstance().addServerConversationListener(this);
        init();
        
    }

    private void init() {
        Group grpConnect = new Group(this,SWT.NONE);
        grpConnect.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));
        grpConnect.setText(ClientConfigurationController.getInstance().getString("connection"));
        grpConnect.setLayout(new GridLayout(6,true));
        Label lblAccountName = new Label(grpConnect,SWT.NONE);
        lblAccountName.setText(ClientConfigurationController.getInstance().getString("accountname"));
        lblAccountName.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,false,false,1,1));
        final Text txtAccountName = new Text(grpConnect,SWT.BORDER);
        txtAccountName.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
        
        Label lblAccountPassword = new Label(grpConnect,SWT.NONE);
        lblAccountPassword.setText(ClientConfigurationController.getInstance().getString("password"));
        lblAccountPassword.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,false,false,1,1));
        final Text txtAccountPassword = new Text(grpConnect,SWT.BORDER| SWT.PASSWORD);
        txtAccountPassword.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
        
        btnConnect=new Button(grpConnect, SWT.NONE);
        btnConnect.setText(ClientConfigurationController.getInstance().getString("logon"));
        if(new File(ICON_LOGON).exists()){
            btnConnect.setImage(new Image(Display.getCurrent(), ICON_LOGON));
        }
        
        btnConnect.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
        
        Button btnCreateNew=new Button(grpConnect, SWT.NONE);
        btnCreateNew.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
        btnCreateNew.setText(ClientConfigurationController.getInstance().getString("create"));
        
        Label lblCurrentState = new Label(grpConnect,SWT.NONE);
        lblCurrentState.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
        lblCurrentState.setText(ClientConfigurationController.getInstance().getString("networkstate"));
        lblStatus = new Label(grpConnect,SWT.NONE);
        lblStatus.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,5,1));
        lblStatus.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        lblStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
        lblStatus.setText(ClientConfigurationController.getInstance().getString("disconnected"));
        

        
        btnConnect.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                // Fileserver mss beter starten bij opstarten van applicatie???
                // Hier beter enkel de logon op het netwerk
                if(btnConnect.getText().equals(ClientConfigurationController.getInstance().getString("logon"))){
                    ClientController.getInstance().startServerConversation();
                    ClientController.getInstance().logon(
                        txtAccountName.getText(),
                        txtAccountPassword.getText(),
                        ClientConfigurationController.getInstance().getConfiguration().getFileTransferPort());
                                    //PeerFacade.getInstance().getPeerFileServer().startServer();
                }
                else if(btnConnect.getText().equals(ClientConfigurationController.getInstance().getString("logoff"))){
                    ClientController.getInstance().logoff(
                        txtAccountName.getText(),
                        txtAccountPassword.getText(),
                        ClientConfigurationController.getInstance().getConfiguration().getFileTransferPort());
                        ClientController.getInstance().stopServerConversation();
                        lblStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
                        lblStatus.setText(ClientConfigurationController.getInstance().getString("disconnected"));
                        btnConnect.setText(ClientConfigurationController.getInstance().getString("logon"));
                        if(new File(ICON_LOGON).exists()){
                            btnConnect.setImage(new Image(Display.getCurrent(), ICON_LOGON));
                        }
                }
            }
        });
        btnCreateNew.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0){
                // Popup waarin nieuwe account kan gemaakt worden
                CreateAccountDialog dialog = new CreateAccountDialog(getShell());
                CreateAccountDialogData data = dialog.open();
                if( data!=null){
                    txtAccountName.setText(data.getAccountName());
                    txtAccountPassword.setText(data.getAccountPassword());
                    ClientController.getInstance().logon(data.getAccountName(), data.getAccountPassword(), ClientConfigurationController.getInstance().getConfiguration().getFileTransferPort());
                }
            }
        });
        
        Group grpShared = new Group(this,SWT.NONE);
        grpShared.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
        grpShared.setText(ClientConfigurationController.getInstance().getString("sharedfiles"));
        UIFileBrowser browser = new UIFileBrowser(grpShared, ClientConfigurationController.getInstance().getConfiguration().getSharedFolder());
        
        
    }

    public void receivedCommando(final Commando c) {
        this.getDisplay().asyncExec(
            new Runnable() {
                public void run(){
                    if(c instanceof WelcomeCommando){
                       lblStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
                       lblStatus.setText(ClientConfigurationController.getInstance().getString("connected"));
                       btnConnect.setText(ClientConfigurationController.getInstance().getString("logoff"));
                       if(new File(ICON_LOGOFF).exists()){
                               btnConnect.setImage(new Image(Display.getCurrent(), ICON_LOGOFF));
                       }
                    }
                    else if (c instanceof LogNiLukNiCommando){
                       lblStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
                       lblStatus.setText(((LogNiLukNiCommando)c).getMessage());
                    }
                    
                }
        });
    }

}
