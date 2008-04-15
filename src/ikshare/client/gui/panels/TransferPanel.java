/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.domain.PeerFacade;
import ikshare.domain.Transfer;
import ikshare.domain.TransferState;
import ikshare.client.gui.AbstractPanel;
import ikshare.client.configuration.ClientConfiguration;
import ikshare.client.gui.UtilityClass;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.Transfer;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.FileTransferListener;
import java.io.File;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 *
 * @author Jana
 */
public class TransferPanel extends AbstractPanel implements	FileTransferListener {


	private static String ICON_DOWN = "resources/icons/tp_down.png";

	private static String ICON_UP = "resources/icons/tp_up.png";

	private Table tblUploadTransfer, tblDownloadTransfer;

	private Transfer f;

	public TransferPanel(String text, String icon) {
		super(text, icon);
		FillLayout layout = new FillLayout();
		this.setLayout(layout);
		this.init();
		EventController.getInstance().addFileTransferListener(this);
	}


	private void init() {
		TabFolder folder=new TabFolder(this, SWT.NONE);

		// Download
		TabItem downloadTab = new TabItem(folder,SWT.NONE);
		downloadTab.setText(ClientConfigurationController.getInstance().getString("downloads"));
		if(new File(ICON_DOWN).exists()){
			Image icon = new Image(Display.getCurrent(), ICON_DOWN);
			downloadTab.setImage(icon);
		}
		Composite cmpDownload=new Composite(folder, SWT.NONE);
		cmpDownload.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,3,1));
		cmpDownload.setLayout(new GridLayout(1,false));
		tblDownloadTransfer = new Table(cmpDownload,SWT.FULL_SELECTION | SWT.BORDER);
		tblDownloadTransfer.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		tblDownloadTransfer.setLinesVisible (true);
		tblDownloadTransfer.setHeaderVisible (true);
		// Right click menu
		tblDownloadTransfer.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent event) {
				if(event.button == 3){
					final int selectedRow = tblDownloadTransfer.getSelectionIndex();
					if (selectedRow == -1) 
						return;
					Menu rightClickMenu = new Menu (tblDownloadTransfer.getShell(), SWT.POP_UP);
					MenuItem cancelMenuItem = new MenuItem(rightClickMenu, SWT.PUSH);
					cancelMenuItem.setText(ClientConfigurationController.getInstance().getString("canceldownload"));
					cancelMenuItem.addListener (SWT.Selection, new Listener () {
						public void handleEvent(Event event) {
							Transfer selected = (Transfer)tblDownloadTransfer.getItem(selectedRow).getData("transfer");
							if (selected != null) {
								PeerFacade.getInstance().cancelDownloadThread(selected);
							}
						}
					});
					MenuItem pauseMenuItem = new MenuItem(rightClickMenu, SWT.PUSH);
					pauseMenuItem.setText(ClientConfigurationController.getInstance().getString("pausedownload"));
					pauseMenuItem.addListener (SWT.Selection, new Listener () {
						public void handleEvent(Event event) {
							Transfer selected = (Transfer)tblDownloadTransfer.getItem(selectedRow).getData("transfer");
							if (selected != null) {
								PeerFacade.getInstance().pauseDownloadThread(selected);
							}
						}
					});
					MenuItem resumeMenuItem = new MenuItem(rightClickMenu, SWT.PUSH);
					resumeMenuItem.setText(ClientConfigurationController.getInstance().getString("resumedownload"));
					resumeMenuItem.addListener (SWT.Selection, new Listener () {
						public void handleEvent(Event event) {
							Transfer selected = (Transfer)tblDownloadTransfer.getItem(selectedRow).getData("transfer");
							if (selected != null) {
								PeerFacade.getInstance().resumeDownloadThread(selected);
							}
						}
					});
					rightClickMenu.setVisible (true);
				}
			}
		});

		addTableColumn(tblDownloadTransfer,ClientConfigurationController.getInstance().getString("filename"),300,SWT.LEFT);
		addTableColumn(tblDownloadTransfer,ClientConfigurationController.getInstance().getString("size"),100,SWT.RIGHT);
		addTableColumn(tblDownloadTransfer,ClientConfigurationController.getInstance().getString("state"), 150, SWT.RIGHT);
		addTableColumn(tblDownloadTransfer,ClientConfigurationController.getInstance().getString("speed"),100,SWT.RIGHT);
		addTableColumn(tblDownloadTransfer,ClientConfigurationController.getInstance().getString("remaining"), 100, SWT.RIGHT);
		addTableColumn(tblDownloadTransfer,ClientConfigurationController.getInstance().getString("peer"), 100, SWT.RIGHT);
		downloadTab.setControl(cmpDownload);



		// Upload
		TabItem uploadTab = new TabItem(folder,SWT.NONE);
		if(new File(ICON_UP).exists()){
			Image icon = new Image(Display.getCurrent(), ICON_UP);
			uploadTab.setImage(icon);
		}
		uploadTab.setText(ClientConfigurationController.getInstance().getString("uploads"));
		Composite cmpUpload=new Composite(folder, SWT.NONE);
		cmpUpload.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,3,1));
		cmpUpload.setLayout(new GridLayout(1,false));
		tblUploadTransfer = new Table(cmpUpload,SWT.FULL_SELECTION | SWT.BORDER);
		tblUploadTransfer.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		tblUploadTransfer.setLinesVisible (true);
		tblUploadTransfer.setHeaderVisible (true);
		addTableColumn(tblUploadTransfer,ClientConfigurationController.getInstance().getString("filename"),300,SWT.LEFT);
		addTableColumn(tblUploadTransfer,ClientConfigurationController.getInstance().getString("size"),100,SWT.RIGHT);
		addTableColumn(tblUploadTransfer,ClientConfigurationController.getInstance().getString("state"), 150, SWT.RIGHT);
		addTableColumn(tblUploadTransfer,ClientConfigurationController.getInstance().getString("speed"),100,SWT.RIGHT);
		addTableColumn(tblUploadTransfer,ClientConfigurationController.getInstance().getString("remaining"), 100, SWT.RIGHT);
		addTableColumn(tblUploadTransfer,ClientConfigurationController.getInstance().getString("peer"), 100, SWT.RIGHT);
		uploadTab.setControl(cmpUpload); 

		/*
        Button btnDownload = new Button(cmpDownload, SWT.PUSH);
        btnDownload.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event arg0) {

				// TODO Auto-generated method stub

				Transfer transfer = new Transfer();
				transfer.setFileName("/kopie");
				transfer.setState(TransferState.DOWNLOADING);
				transfer.setId(new Date().toString());
				transfer.setFileSize(2000);
				transfer.setNumberOfBlocks(4000);
				transfer.setNumberOfBlocksFinished(0);

				EventController.getInstance().triggerDownloadStartedEvent(transfer);

				PeerFacade.getInstance().startDownloadThread(transfer);
                                }
			});
		 */

	}

	private void addTableColumn(Table table, String text, int width, int align) {
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText(text);
		column.pack();
		column.setWidth(width);
		column.setAlignment(align);
	}



	public void transferStarted(final Transfer transfer) {
		this.getDisplay().asyncExec(
				new Runnable() {
					public void run(){
						TableItem item = null;
						if(transfer.getState() == TransferState.DOWNLOADING){
							item = new TableItem(tblDownloadTransfer,SWT.NONE);
							item.setText(2,ClientConfigurationController.getInstance().getString("downloading"));
						}
						else if(transfer.getState() == TransferState.UPLOADING){
							item = new TableItem(tblUploadTransfer,SWT.NONE);
							item.setText(2,ClientConfigurationController.getInstance().getString("uploading"));
						}

						if (item != null) {
							//System.out.println(transfer.getFile().getName()==null);
							item.setText(0,transfer.getFile().getName());
							item.setText(1,UtilityClass.formatFileSize(transfer.getFileSize()));

							item.setText(3,"0");
							item.setText(4,UtilityClass.formatTime(transfer.getRemainingTime()));
							item.setText(5, transfer.getPeer().getAccountName());
							item.setData("transfer",transfer);
						}

					}
				});
	}

	public void transferCanceled(final Transfer transfer) {
		this.getDisplay().asyncExec(
				new Runnable() {
					public void run(){
						if(transfer.getState() == TransferState.CANCELEDDOWNLOAD){
							for(TableItem item : tblDownloadTransfer.getItems())
							{
								Transfer t = (Transfer) item.getData("transfer");
								if(t.getId().equals(transfer.getId()))
								{
									item.setText(2,ClientConfigurationController.getInstance().getString("canceled"));
									item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
								}
							}
						}
						else if(transfer.getState() == TransferState.CANCELEDUPLOAD){
							for(TableItem item : tblUploadTransfer.getItems())
							{
								Transfer t = (Transfer) item.getData("transfer");
								if(t.getId().equals(transfer.getId()))
								{
									item.setText(2,ClientConfigurationController.getInstance().getString("canceled"));
									item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
								}
							}
						}

					}
				});
	}




	public void transferStateChanged(final Transfer transfer) {
		this.getDisplay().asyncExec(
				new Runnable() {
					public void run(){
						if(transfer.getState() == TransferState.DOWNLOADING){
							for(TableItem item : tblDownloadTransfer.getItems())
							{
								Transfer t = (Transfer) item.getData("transfer");
								if(t.getId().equals(transfer.getId()))
								{
									item.setText(1, UtilityClass.formatFileSize(t.getFileSize()));
									item.setText(3,UtilityClass.formatFileSize(transfer.getSpeed()));
									item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));  
									item.setText(4,UtilityClass.formatTime(transfer.getRemainingTime()));
								}
							}
						}
						else if(transfer.getState() == TransferState.UPLOADING){
							for(TableItem item : tblUploadTransfer.getItems())
							{
								Transfer t = (Transfer) item.getData("transfer");
								if(t.getId().equals(transfer.getId()))
								{
									item.setText(1, UtilityClass.formatFileSize(t.getFileSize()));
									item.setText(3,UtilityClass.formatFileSize(transfer.getSpeed()));
									item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));  
									item.setText(4,UtilityClass.formatTime(transfer.getRemainingTime()));
								}
							}
						}
					}
				});
	}



	public void transferStopped(Transfer transfer) {
		throw new UnsupportedOperationException("Not supported yet.");
	}


	public void transferFailed(final Transfer transfer) {
		this.getDisplay().asyncExec(
				new Runnable() {
					public void run(){
						if(transfer.getState() == TransferState.FAILED){
							for(TableItem item : tblUploadTransfer.getItems())
							{
								Transfer t = (Transfer) item.getData("transfer");
								if(t.getId().equals(transfer.getId()))
								{
									item.setText(2,ClientConfigurationController.getInstance().getString("failed"));
									item.setText(3,UtilityClass.formatFileSize(transfer.getSpeed()));
									item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));  
									item.setText(4,UtilityClass.formatTime(transfer.getRemainingTime()));
								}
							}
							for(TableItem item : tblDownloadTransfer.getItems())
							{
								Transfer t = (Transfer) item.getData("transfer");
								if(t.getId().equals(transfer.getId()))
								{
									item.setText(2,ClientConfigurationController.getInstance().getString("failed"));
									item.setText(3,UtilityClass.formatFileSize(transfer.getSpeed()));
									item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));  
									item.setText(4,UtilityClass.formatTime(transfer.getRemainingTime()));
								}
							}
						}
					}
				});
	}

    public void transferFinished(final Transfer transfer) {
        this.getDisplay().asyncExec(
            new Runnable() {
                public void run(){
                    if(transfer.getState() == TransferState.FINISHED){
                        for(TableItem item : tblDownloadTransfer.getItems())
                        {
                            Transfer t = (Transfer) item.getData("transfer");
                            if(t.getId().equals(transfer.getId()))
                            {
                                item.setText(2,ClientConfigurationController.getInstance().getString("finished"));
                                item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
                            }
                        }
                        for(TableItem item : tblUploadTransfer.getItems())
                        {
                            Transfer t = (Transfer) item.getData("transfer");
                            if(t.getId().equals(transfer.getId()))
                            {
                                item.setText(2,ClientConfigurationController.getInstance().getString("finished"));
                                item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
                            }
                        }
                    }
                    
                }
        });
    }


	public void transferPaused(final Transfer transfer) {
		this.getDisplay().asyncExec(
				new Runnable() {
					public void run(){
						if(transfer.getState() == TransferState.PAUSEDDOWNLOAD){
							for(TableItem item : tblDownloadTransfer.getItems())
							{
								Transfer t = (Transfer) item.getData("transfer");
								if(t.getId().equals(transfer.getId()))
								{
									item.setText(2,ClientConfigurationController.getInstance().getString("paused"));
									item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
								}
							}
						}
						else if(transfer.getState() == TransferState.PAUSEDUPLOAD){
							for(TableItem item : tblUploadTransfer.getItems())
							{
								Transfer t = (Transfer) item.getData("transfer");
								if(t.getId().equals(transfer.getId()))
								{
									item.setText(2,ClientConfigurationController.getInstance().getString("paused"));
									item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
								}
							}
						}

					}
				});
		
	}


	public void transferResumed(final Transfer transfer) {
		this.getDisplay().asyncExec(
				new Runnable() {
					public void run(){
						TableItem item = null;
						if(transfer.getState() == TransferState.RESUMEDDOWNLOAD){
							item = new TableItem(tblDownloadTransfer,SWT.NONE);
							item.setText(2,ClientConfigurationController.getInstance().getString("downloading"));
						}
						else if(transfer.getState() == TransferState.RESUMEDUPLOAD){
							item = new TableItem(tblUploadTransfer,SWT.NONE);
							item.setText(2,ClientConfigurationController.getInstance().getString("uploading"));
						}

						if (item != null) {
							//System.out.println(transfer.getFile().getName()==null);
							item.setText(0,transfer.getFile().getName());
							item.setText(1,UtilityClass.formatFileSize(transfer.getFileSize()));

							item.setText(3,"0");
							item.setText(4,UtilityClass.formatTime(transfer.getRemainingTime()));
							item.setText(5, transfer.getPeer().getAccountName());
							item.setData("transfer",transfer);
						}

					}
				});
	}
}
