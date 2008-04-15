package ikshare.client.gui;

import ikshare.client.configuration.ClientConfigurationController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


public class InfoBar extends Composite {
    /*
     * Sets the layout of the infobar in the shell right (1 vertical grid and 1 horizontal grid)
     * The labels gonna be committed by the method init.
     */
    public InfoBar(Shell shell,int flags){
        super(shell,flags);
        GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1,1 );
        //gd.heightHint=50;
        this.setLayoutData(gd);
        GridLayout gl = new GridLayout(6,false);
        this.setLayout(gl);
        this.init();
    }
    /*
     * Initializes the infobar with 3 labels and their specific numbers
     */    
    private void init() {
        Label lblDownload = new Label(this,SWT.FILL);
        lblDownload.setText(ClientConfigurationController.getInstance().getString("numberofdownloads")+":");
        GridData data=new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
        lblDownload.setLayoutData(data);
        
        Label lblNrDownload = new Label(this,SWT.NONE);
        lblNrDownload.setText("0");
        GridData data2=new GridData(SWT.END, SWT.CENTER, true, true, 1,1);
        data2.widthHint=80;
        lblNrDownload.setLayoutData(data2);
        
        Label lblUpload = new Label(this,SWT.NONE);
        lblUpload.setText(ClientConfigurationController.getInstance().getString("numberofuploads")+":");
        data=new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
        lblUpload.setLayoutData(data);
        
        Label lblNrUpload = new Label(this,SWT.NONE);
        lblNrUpload.setText("1");
        lblNrUpload.setLayoutData(data2);
        
        Label lblShared = new Label(this,SWT.NONE);
        lblShared.setText(ClientConfigurationController.getInstance().getString("numberofsharedfiles")+":");
        data=new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
        lblShared.setLayoutData(data);
        
        Label lblNrShared = new Label(this,SWT.NONE);
        lblNrShared.setText("2");
        lblNrShared.setLayoutData(data2);
    }
}
