package ikshare.client.gui;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.PeerFacade;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.TransferQueueListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


public class InfoBar extends Composite implements TransferQueueListener{
    
    Label lblNrUpload;
    Label lblNrDownload;
    Label lblNrShared;
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
        EventController.getInstance().addTransferQueueListener(this);
    }
    
    public Label getLblNrDownload() {
        return lblNrDownload;
    }

    public void setLblNrDownload(Label lblNrDownload) {
        this.lblNrDownload = lblNrDownload;
    }

    public Label getLblNrShared() {
        return lblNrShared;
    }

    public void setLblNrShared(Label lblNrShared) {
        this.lblNrShared = lblNrShared;
    }

    public Label getLblNrUpload() {
        return lblNrUpload;
    }

    public void setLblNrUpload(Label lblNrUpload) {
        this.lblNrUpload = lblNrUpload;
    }
    
    /*
     * Initializes the infobar with 3 labels and their specific numbers
     */    
    private void init() {
        Label lblDownload = new Label(this,SWT.FILL);
        lblDownload.setText(ClientConfigurationController.getInstance().getString("numberofdownloads")+":");
        GridData data=new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
        lblDownload.setLayoutData(data);
        
        lblNrDownload = new Label(this,SWT.NONE);
        lblNrDownload.setText("0");
        GridData data2=new GridData(SWT.END, SWT.CENTER, true, true, 1,1);
        data2.widthHint=80;
        lblNrDownload.setLayoutData(data2);
        
        Label lblUpload = new Label(this,SWT.NONE);
        lblUpload.setText(ClientConfigurationController.getInstance().getString("numberofuploads")+":");
        data=new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
        lblUpload.setLayoutData(data);
        
        lblNrUpload = new Label(this,SWT.NONE);
        lblNrUpload.setText("0");
        lblNrUpload.setLayoutData(data2);
        
        Label lblShared = new Label(this,SWT.NONE);
        lblShared.setText(ClientConfigurationController.getInstance().getString("numberofsharedfiles")+":");
        data=new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
        lblShared.setLayoutData(data);
        
        lblNrShared = new Label(this,SWT.NONE);
        lblNrShared.setText("0");
        lblNrShared.setLayoutData(data2);
    }

	public void activeDownloadsChanged() {
		this.getDisplay().asyncExec(
				new Runnable() {
					public void run() {
						lblNrDownload.setText(String.valueOf(PeerFacade.getInstance().getActiveDownloads()));
					}
				});
	}

	public void activeUploadsChanged() {
		this.getDisplay().asyncExec(
				new Runnable() {
					public void run() {
						lblNrUpload.setText(String.valueOf(PeerFacade.getInstance().getActiveUploads()));
					}
				});
	}
}
