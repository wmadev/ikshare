package ikshare.client.gui.panels;

import ikshare.client.ClientController;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.ExceptionWindow;
import ikshare.client.gui.MainScreen;
import ikshare.client.gui.custom.UIFileBrowser;
import ikshare.client.gui.dialogs.CreateAccountDialog;
import ikshare.client.gui.dialogs.CreateAccountDialogData;
import ikshare.domain.TransferController;
import ikshare.domain.SearchResult;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.ClientControllerListener;
import ikshare.domain.exception.NoServerConnectionException;
import ikshare.exceptions.ConfigurationException;
import java.io.File;
import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;


public class HomePanel extends AbstractPanel implements ClientControllerListener{

    private static String ICON_LOGON = "resources/icons/home_logon.png";
    private static String ICON_LOGOFF = "resources/icons/home_logoff.png";
    private static String ICON_CREATE = "resources/icons/home_create.png";
    
    private Label lblStatus,lblAccountNameValidation,lblAccountPasswordValidation;
    private Button btnConnect,btnCreateNew;
    private Text txtAccountName,txtAccountPassword;
            
    public HomePanel(String text,String icon){
        super(text,icon);
        GridLayout gd=new GridLayout(1,false);
        this.setLayout(gd);
        EventController.getInstance().addClientControllerListener(this);
        init();
        
    }

    private void init() {
        Group grpConnect = new Group(this,SWT.NONE);
        grpConnect.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
        grpConnect.setText(ClientConfigurationController.getInstance().getString("connection"));
        grpConnect.setLayout(new GridLayout(4,false));
        
        GridData lblData = new GridData(SWT.NONE,SWT.FILL,false,false,1,1);
        lblData.widthHint = 100;
        
        GridData txtData = new GridData(SWT.LEFT,SWT.FILL,false,false,2,1);
        txtData.widthHint = 150;
        txtData.heightHint = 15;
        
        GridData btnData = new GridData(SWT.LEFT,SWT.FILL,false,false,1,1);
        btnData.widthHint = 100;
        
        Label lblAccountName = new Label(grpConnect, SWT.NONE);
        lblAccountName.setText(ClientConfigurationController.getInstance().getString("accountname")+" (*)");
        lblAccountName.setLayoutData(lblData);
        txtAccountName = new Text(grpConnect, SWT.BORDER);
        txtAccountName.setLayoutData(txtData);
        txtAccountName.setText(ClientConfigurationController.getInstance().getConfiguration().getAccountName());
        
        lblAccountNameValidation = new Label(grpConnect,SWT.NONE);
        lblAccountNameValidation.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false,1,1));
        lblAccountNameValidation.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
        
        Label lblAccountPassword = new Label(grpConnect, SWT.NONE);
        lblAccountPassword.setText(ClientConfigurationController.getInstance().getString("password")+" (*)");
        lblAccountPassword.setLayoutData(lblData);
        txtAccountPassword = new Text(grpConnect, SWT.BORDER | SWT.PASSWORD);
        txtAccountPassword.setLayoutData(txtData);
        txtAccountPassword.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                
            }

            public void keyReleased(KeyEvent e) {
                 if(e.keyCode == SWT.CR){  
                    if(validate()){
                        if(btnConnect.getText().equals(ClientConfigurationController.getInstance().getString("logon"))){
                            handleLogOn();
                        }
                        else if(btnConnect.getText().equals(ClientConfigurationController.getInstance().getString("logoff"))){
                            handleLogOff();
                        }
                    }
                }
            }
            
        });
        
        lblAccountPasswordValidation = new Label(grpConnect,SWT.NONE);
        lblAccountPasswordValidation.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false,1,1));
        lblAccountPasswordValidation.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
        
        
        
        btnConnect=new Button(grpConnect, SWT.NONE);
        btnConnect.setText(ClientConfigurationController.getInstance().getString("logon"));
        if(new File(ICON_LOGON).exists()){
            btnConnect.setImage(new Image(Display.getCurrent(), ICON_LOGON));
        }
        btnConnect.setLayoutData(btnData);
        
        btnCreateNew=new Button(grpConnect, SWT.NONE);
        btnCreateNew.setText(ClientConfigurationController.getInstance().getString("create"));
        if(new File(ICON_CREATE).exists()){
            btnCreateNew.setImage(new Image(Display.getCurrent(), ICON_CREATE));
        }
        btnCreateNew.setLayoutData(btnData);
        
        lblStatus = new Label(grpConnect,SWT.NONE);
        lblStatus.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false,2,1));
        lblStatus.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        lblStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
        lblStatus.setText(ClientConfigurationController.getInstance().getString("disconnected"));
        
        btnConnect.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {
                
                
                if(validate()){
                    if(btnConnect.getText().equals(ClientConfigurationController.getInstance().getString("logon"))){
                    
                	handleLogOn();
                   }
                    else if(btnConnect.getText().equals(ClientConfigurationController.getInstance().getString("logoff"))){
                        handleLogOff();
                    }
                }
            }
        });
        
        btnCreateNew.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0){
               handleCreateNew();
            }
        });
        
        Group grpShared = new Group(this,SWT.NONE);
        grpShared.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
        grpShared.setText(ClientConfigurationController.getInstance().getString("sharedfiles"));
        UIFileBrowser browser = new UIFileBrowser(grpShared, ClientConfigurationController.getInstance().getConfiguration().getSharedFolder());
        
    }
    
    private void handleCreateNew(){
        CreateAccountDialog dialog = new CreateAccountDialog(getShell());
        CreateAccountDialogData data = dialog.open();
        if(data!=null){
            txtAccountName.setText(data.getAccountName());
            txtAccountPassword.setText(data.getAccountPassword());
            handleLogOn();
        }
    }

    private void handleLogOn(){
        try {
            ClientController.getInstance().startServerConversation();
            ClientController.getInstance().logon(txtAccountName.getText(), txtAccountPassword.getText(), ClientConfigurationController.getInstance().getConfiguration().getMessagePort());
            TransferController.getInstance();
        }
        catch(IOException ex){
            lblStatus.setText(ClientConfigurationController.getInstance().getString("noconnectionwithserver"));
        }
        catch (NoServerConnectionException ex) {
            lblStatus.setText(ClientConfigurationController.getInstance().getString("noconnectionwithserver"));
        }
        catch(ConfigurationException e){
            lblStatus.setText(ClientConfigurationController.getInstance().getString("encryptpassworderror"));
        }
    }
    private void handleLogOff(){
        try {
            ClientController.getInstance().logoff(txtAccountName.getText(), txtAccountPassword.getText(), ClientConfigurationController.getInstance().getConfiguration().getFileTransferPort());
            ClientController.getInstance().stopServerConversation();
            lblStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
            lblStatus.setText(ClientConfigurationController.getInstance().getString("disconnected"));
            btnConnect.setText(ClientConfigurationController.getInstance().getString("logon"));
            btnCreateNew.setEnabled(true);
            if (new File(ICON_LOGON).exists()) {
                btnConnect.setImage(new Image(Display.getCurrent(), ICON_LOGON));
            }
        } catch (NoServerConnectionException ex) {
            lblStatus.setText(ClientConfigurationController.getInstance().getString("noconnectionwithserver"));
        }
        catch(ConfigurationException e){
            lblStatus.setText(ClientConfigurationController.getInstance().getString("encryptpassworderror"));
        }
    }
    
    private boolean validate(){
        boolean valid = true;
        Pattern p = Pattern.compile("[\\w-\\.\\@_]{5,32}+");
        Matcher m = p.matcher(txtAccountName.getText());

        if(!m.matches()){
            lblAccountNameValidation.setText(ClientConfigurationController.getInstance().getString("invalidaccountname"));
            valid = false;
        }
        else{
            lblAccountNameValidation.setText("");
        }
        m = p.matcher(txtAccountPassword.getText());
        if(!m.matches()){
            lblAccountPasswordValidation.setText(ClientConfigurationController.getInstance().getString("invalidaccountpassword"));
            valid =false;
        }
        else{
            lblAccountPasswordValidation.setText("");
        }
        return valid;
    }
    

    @Override
    public void initialiseFocus() {
        if(txtAccountName.getText().length()>0)
            txtAccountPassword.setFocus();
        else{
            txtAccountName.setFocus();
        }
    }
    
    public void onLogOn() {
        this.getDisplay().asyncExec(
            new Runnable() {
                public void run(){
                try {
                    ClientConfigurationController.getInstance().getConfiguration().setAccountName(txtAccountName.getText());
                    ClientController.getInstance().share(txtAccountName.getText(), ClientConfigurationController.getInstance().getConfiguration().getSharedFolder());
                    lblStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
                    lblStatus.setText(ClientConfigurationController.getInstance().getString("connected"));
                    btnConnect.setText(ClientConfigurationController.getInstance().getString("logoff"));
                    btnCreateNew.setEnabled(false);
                    if (new File(ICON_LOGOFF).exists()) {
                        btnConnect.setImage(new Image(Display.getCurrent(), ICON_LOGOFF));
                    }
                } catch (IOException ex) {
                    new ExceptionWindow(ex, MainScreen.getInstance(), false);
                }
                }
        });
    }

    public void onLogOnFailed(final String message) {
        getDisplay().asyncExec(new Runnable() {
            public void run() {
                lblStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
                lblStatus.setText(message);
            }
        });
           
    }

    public void connectionInterrupted() {
        
        getDisplay().asyncExec(new Runnable() {
            public void run() {
                    if(ClientController.getInstance().isLoggedOn()){
                        ClientController.getInstance().stopServerConversation();        
                        btnConnect.setText(ClientConfigurationController.getInstance().getString("logon"));
                        btnCreateNew.setEnabled(true);
                        if (new File(ICON_LOGON).exists()) {
                            btnConnect.setImage(new Image(Display.getCurrent(), ICON_LOGON));
                        }

                        lblStatus.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
                        lblStatus.setText(ClientConfigurationController.getInstance().getString("connectionwithserverlost"));
                    }
            }
        });
        
    }

    public void onResultFound(SearchResult found, String keyword) {
        // Not required for HomePanel
    }

    public void onNoResultFound(String keyword) {
        // Not required for HomePanel
    }

}
