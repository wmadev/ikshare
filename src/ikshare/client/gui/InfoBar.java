package ikshare.client.gui;

import java.awt.FlowLayout;
import java.io.File;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.client.gui.custom.MP3Player;
import ikshare.client.gui.custom.UIFileBrowser;
import ikshare.domain.PeerFacade;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.TransferQueueListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


public class InfoBar extends Composite implements TransferQueueListener{
    
    private Label lblNrUpload, lblNrDownload, lblNrSharedFolders, lblNrSharedFiles;
    private Composite cmpInformation;
    private MP3Player mp3Player;

	private static String ICON_DOWN = "resources/icons/tp_down.png";

	private static String ICON_UP = "resources/icons/tp_up.png";
	private static String ICON_SHAREDFOLDERS = "resources/icons/hp_sharedfolders.png";
	private static String ICON_SHAREDFILES = "resources/icons/hp_sharedfiles.png";
    /*
     * Sets the layout of the infobar in the shell right (1 vertical grid and 1 horizontal grid)
     * The labels gonna be committed by the method init.
     */
    public InfoBar(Shell shell,int flags){
        super(shell,flags);
        //GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1,1 );
        //gd.heightHint=50;
        //this.setLayoutData(gd);
        RowLayout rowLayout = new RowLayout();
        rowLayout.marginLeft = 5;
        rowLayout.marginTop = 2;
        rowLayout.marginRight = 5;
        rowLayout.marginBottom = 2;
        rowLayout.spacing = 0;
        this.setLayout(rowLayout);
        this.init();
        EventController.getInstance().addTransferQueueListener(this);
    }
    
    /*
     * Initializes the infobar with 3 labels and their specific numbers
     */    
    private void init() {
    	initInformation();
    	mp3Player = new MP3Player(this, SWT.FILL);
       // mp3Player.setLayoutData(new RowData(300,20));

    }

	private void initInformation() {
        RowLayout rowLayout = new RowLayout();
        rowLayout.wrap = false;
        rowLayout.pack = false;
        rowLayout.justify = false;
        rowLayout.marginLeft = 5;
        rowLayout.marginTop = 2;
        rowLayout.marginRight = 5;
        rowLayout.marginBottom = 2;
        rowLayout.spacing = 0;
		
		cmpInformation = new Composite(this, SWT.FILL);
		cmpInformation.setLayout(rowLayout);
		cmpInformation.setLayoutData(new RowData(550,20));
		
    	RowData rwIcons = new RowData(20,20);
    	RowData rwText = new RowData(30,20);
    	
        Label lblDownload = new Label(cmpInformation,SWT.FILL);
		if(new File(ICON_DOWN).exists()){
			Image icon = new Image(Display.getCurrent(), ICON_DOWN);
			lblDownload.setImage(icon);
		}
		lblDownload.setLayoutData(rwIcons);
        //lblDownload.setText(ClientConfigurationController.getInstance().getString("numberofdownloads")+":");
        //GridData data=new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
        //lblDownload.setLayoutData(data);
        
        
        lblNrDownload = new Label(cmpInformation,SWT.FILL);
        lblNrDownload.setText("0");
        lblNrDownload.setLayoutData(rwText);
        //GridData data2=new GridData(SWT.END, SWT.CENTER, true, true, 1,1);
        //data2.widthHint=80;
        //lblNrDownload.setLayoutData(data2);
        
        Label lblUpload = new Label(cmpInformation,SWT.FILL);
		if(new File(ICON_UP).exists()){
			Image icon = new Image(Display.getCurrent(), ICON_UP);
			lblUpload.setImage(icon);
		}
		lblUpload.setLayoutData(rwIcons);
        //lblUpload.setText(ClientConfigurationController.getInstance().getString("numberofuploads")+":");
        //data=new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
        //lblUpload.setLayoutData(data);
        
        lblNrUpload = new Label(cmpInformation,SWT.FILL);
        lblNrUpload.setText("0");
        lblNrUpload.setLayoutData(rwText);
        //lblNrUpload.setLayoutData(data2);
        
        Label lblSharedFolders = new Label(cmpInformation,SWT.FILL);
		if(new File(ICON_SHAREDFOLDERS).exists()){
			Image icon = new Image(Display.getCurrent(), ICON_SHAREDFOLDERS);
			lblSharedFolders.setImage(icon);
		}
		lblSharedFolders.setLayoutData(rwIcons);

        lblNrSharedFolders = new Label(cmpInformation,SWT.FILL);
        lblNrSharedFolders.setText(""+ClientConfigurationController.getInstance().getSharedInformation()[0]);;
        lblNrSharedFolders.setLayoutData(rwText);

        Label lblSharedFiles = new Label(cmpInformation,SWT.FILL);
		if(new File(ICON_SHAREDFILES).exists()){
			Image icon = new Image(Display.getCurrent(), ICON_SHAREDFILES);
			lblSharedFiles.setImage(icon);
		}
		lblSharedFiles.setLayoutData(rwIcons);

        lblNrSharedFiles = new Label(cmpInformation,SWT.FILL);
        lblNrSharedFiles.setText(""+ClientConfigurationController.getInstance().getSharedInformation()[1]);;
        lblNrSharedFiles.setLayoutData(rwText);
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
