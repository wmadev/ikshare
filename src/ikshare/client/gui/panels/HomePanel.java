/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.configuration.ClientConfiguration;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.PeerFacade;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


public class HomePanel extends AbstractPanel {
    public HomePanel(String text,String icon){
        super(text,icon);
        GridLayout gd=new GridLayout(2,false);
        this.setLayout(gd);
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
        Text txtAccountName = new Text(grpConnect,SWT.BORDER);
        txtAccountName.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
        
        Label lblAccountPassword = new Label(grpConnect,SWT.NONE);
        lblAccountPassword.setText(ClientConfigurationController.getInstance().getString("password"));
        lblAccountPassword.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,false,false,1,1));
        Text txtAccountPassword = new Text(grpConnect,SWT.BORDER| SWT.PASSWORD);
        txtAccountPassword.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
        
        Button btnConnect=new Button(grpConnect, SWT.NONE);
        btnConnect.setText(ClientConfigurationController.getInstance().getString("logon"));
        btnConnect.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
        btnConnect.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				// Fileserver mss beter starten bij opstarten van applicatie???
                                // Hier beter enkel de logon op het netwerk
                                PeerFacade.getInstance().getPeerFileServer().startServer();
			}
        	
        });
        Button btnCreateNew=new Button(grpConnect, SWT.NONE);
        btnCreateNew.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
        btnCreateNew.setText(ClientConfigurationController.getInstance().getString("create"));
        btnCreateNew.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				 // Popup waarin nieuwe account kan gemaakt worden
			}
        	
        });
        
    }

}
