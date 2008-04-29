/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.dialogs;

import ikshare.client.ClientController;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.client.gui.ExceptionWindow;
import ikshare.client.gui.MainScreen;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.ServerConversationListener;
import ikshare.domain.exception.NoServerConnectionException;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.CreatedAccountCommando;
import ikshare.protocol.command.InvalidRegisterCommando;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

/**
 *
 * @author awosy
 */
public class CreateAccountDialog extends Dialog implements ServerConversationListener {
    private Shell shell;
    private boolean valid;
    
    private Label lblAccountName;
    private Label lblAccountPassword;
    private Label lblAccountEmail;
    
    private Text txtAccountName;
    private Text txtAccountPassword;
    private Text txtAccountEmail;
    
    private Label lblError;
            
    private Button btnCreate;
    private Button btnCancel;
    
    private CreateAccountDialogData data;
    
    public CreateAccountDialog(Shell parent){
        super(parent);
        EventController.getInstance().addServerConversationListener(this);
    }
    
    public CreateAccountDialogData open (){
        shell = new Shell(getParent(),SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText(getText());
        shell.setSize(400,400);
        shell.setLayout(new GridLayout(3,false));
        GridData firstColum = new GridData(SWT.FILL,SWT.FILL,true,false,3,1);
        GridData secondColum = new GridData(SWT.FILL,SWT.FILL,true,false,3,1);
        firstColum.minimumHeight = secondColum.minimumHeight = 30;
        
        Label info = new Label(shell,SWT.WRAP);
        info.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,4,1));
        info.setText(ClientConfigurationController.getInstance().getString("createaccountinfo"));
        
        lblAccountName = new Label(shell, SWT.NONE);
        lblAccountName.setText(ClientConfigurationController.getInstance().getString("accountname")+" (*)");
        lblAccountName.setLayoutData(firstColum);
        txtAccountName = new Text(shell, SWT.BORDER);
        txtAccountName.setLayoutData(secondColum);
        
        lblAccountPassword = new Label(shell, SWT.NONE);
        lblAccountPassword.setText(ClientConfigurationController.getInstance().getString("password")+" (*)");
        lblAccountPassword.setLayoutData(firstColum);
        txtAccountPassword = new Text(shell, SWT.BORDER | SWT.PASSWORD);
        txtAccountPassword.setLayoutData(secondColum);
        
        lblAccountEmail = new Label(shell, SWT.NONE);
        lblAccountEmail.setText(ClientConfigurationController.getInstance().getString("email")+" (*)");
        lblAccountEmail.setLayoutData(firstColum);
        txtAccountEmail = new Text(shell, SWT.BORDER );
        txtAccountEmail.setLayoutData(secondColum);
        lblError = new Label(shell,SWT.NONE);
        lblError.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,4,1));
        lblError.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
        btnCreate = new Button (shell, SWT.PUSH);
        btnCreate.setText (ClientConfigurationController.getInstance().getString("create"));
        //btnCreate.setLayoutData(new GridData(SWT.RIGHT,SWT.FILL,true,false,1,1));
        btnCancel = new Button (shell, SWT.PUSH);
        btnCancel.setText (ClientConfigurationController.getInstance().getString("cancel"));
        //btnCancel.setLayoutData(new GridData(SWT.RIGHT,SWT.FILL,true,false,1,1));
        
        
        btnCreate.addListener(SWT.Selection, new Listener (){
            public void handleEvent (final Event event) {            
                    if(validate(txtAccountName.getText(),txtAccountPassword.getText(),txtAccountEmail.getText())){
                    try {
                        data = new CreateAccountDialogData();
                        data.setAccountName(txtAccountName.getText());
                        data.setAccountPassword(txtAccountPassword.getText());
                        data.setAccountEmail(txtAccountEmail.getText());
                        //ClientController.getInstance().startServerConversation();
                        ClientController.getInstance().createAccount(data.getAccountName(), data.getAccountPassword(), data.getAccountEmail());
                    } catch (NoServerConnectionException ex) {
                        new ExceptionWindow(ex,MainScreen.getInstance(),false);
                    }
                     }
            
            }
        });
        btnCancel.addListener(SWT.Selection, new Listener(){

            public void handleEvent(Event event) {
                data = null;
                shell.close();
                
            }
            
        });
        shell.open();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
                }
            
        
        return data;
    }
    
    public boolean validate(final String name, final String pass,final String email){
        valid = true;
        String errMessage = "";
        // validate account name
        Pattern p = Pattern.compile("[\\w-\\.\\@_]{5,32}+");
        
        Matcher m = p.matcher(name);
        if(!m.matches()){
            errMessage+=ClientConfigurationController.getInstance().getString("invalidaccountname")+"\n\n";
            valid = false;
        }
        // validate account password
        m = p.matcher(pass);
        if(!m.matches()){
            errMessage+=ClientConfigurationController.getInstance().getString("invalidaccountpassword")+"\n\n";
            valid = false;
        }
        
        // validate email
        p = Pattern.compile("[\\w]+[@][\\w]+\\.[\\w]{2,3}+");
        
        m = p.matcher(email);
        if(!m.matches()){
            errMessage+=ClientConfigurationController.getInstance().getString("invalidaccountemail")+"\n\n";
            valid = false;
        }
        lblError.setText(errMessage);
        return valid;
    }

    public void receivedCommando(final Commando c) {
        if(!shell.isDisposed()){
            shell.getDisplay().asyncExec(new Runnable(){
                public void run(){
                    if(c instanceof InvalidRegisterCommando){
                        lblError.setText(ClientConfigurationController.getInstance().getString("invalidregister")
                             +((InvalidRegisterCommando)c).getMessage()+"\n\n");
                    }
                    else if (c instanceof CreatedAccountCommando){
                        shell.close();
                    }
                }
            });
        }
    }
}
