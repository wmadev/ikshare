/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.UtilityClass;
import ikshare.domain.PeerFacade;
import ikshare.domain.Transfer;
import ikshare.domain.TransferState;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.FileTransferListener;

import java.io.File;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
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
	
	private static int fileNamePos = 0, sizePos = 1, statusPos = 3, speedPos = 4, remainingPos = 5, userPos=6;

	private Table tblUploadTransfer, tblDownloadTransfer;
	private Menu rightClickMenu;
	private HashMap<String, TableItem> downloadHashMap, uploadHashMap;
	
	public TransferPanel(String text, String icon) {
		super(text, icon);
		FillLayout layout = new FillLayout();
		this.setLayout(layout);
		this.init();
		downloadHashMap = new HashMap<String, TableItem>();
		uploadHashMap = new HashMap<String, TableItem>();
		
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
					
					final Transfer selected = (Transfer)tblDownloadTransfer.getItem(selectedRow).getData("transfer");
					if (selected != null) {
						rightClickMenu = new Menu (tblDownloadTransfer.getShell(), SWT.POP_UP);
						if (selected.getState() == TransferState.DOWNLOADING) {
							cancelMenu(selected, true);
							pauseMenu(selected, true);
							resumeMenu(selected, false);
							clearMenu(selected, true);
						} else if (selected.getState() == TransferState.PAUSEDDOWNLOAD) {
							cancelMenu(selected, true);
							pauseMenu(selected, false);
							resumeMenu(selected, true);
							clearMenu(selected, true);
						} else {
							cancelMenu(selected, false);
							pauseMenu(selected, false);
							resumeMenu(selected, false);
							clearMenu(selected, true);
						}
						rightClickMenu.setVisible (true);
					}
				}
			}
		});

		addTableColumn(tblDownloadTransfer,ClientConfigurationController.getInstance().getString("filename"),300,SWT.LEFT);
		addTableColumn(tblDownloadTransfer,ClientConfigurationController.getInstance().getString("size"),100,SWT.RIGHT);
		addTableColumn(tblDownloadTransfer,ClientConfigurationController.getInstance().getString("progress"), 100, SWT.RIGHT);
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
		addTableColumn(tblUploadTransfer,ClientConfigurationController.getInstance().getString("progress"), 100, SWT.RIGHT);
		addTableColumn(tblUploadTransfer,ClientConfigurationController.getInstance().getString("state"), 150, SWT.RIGHT);
		addTableColumn(tblUploadTransfer,ClientConfigurationController.getInstance().getString("speed"),100,SWT.RIGHT);
		addTableColumn(tblUploadTransfer,ClientConfigurationController.getInstance().getString("remaining"), 100, SWT.RIGHT);
		addTableColumn(tblUploadTransfer,ClientConfigurationController.getInstance().getString("peer"), 100, SWT.RIGHT);
		uploadTab.setControl(cmpUpload); 
	}
	
	private void resumeMenu(final Transfer selected, boolean enabled) {
		MenuItem resumeMenuItem = new MenuItem(rightClickMenu, SWT.PUSH);
		resumeMenuItem.setText(ClientConfigurationController.getInstance().getString("resumedownload"));
		resumeMenuItem.setEnabled(enabled);
		resumeMenuItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent(Event event) {
					PeerFacade.getInstance().resumeDownloadThread(selected);
			}
		});
	}
	
	private void pauseMenu(final Transfer selected, boolean enabled) {
		MenuItem pauseMenuItem = new MenuItem(rightClickMenu, SWT.PUSH);
		pauseMenuItem.setText(ClientConfigurationController.getInstance().getString("pausedownload"));
		pauseMenuItem.setEnabled(enabled);
		pauseMenuItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent(Event event) {
					PeerFacade.getInstance().pauseDownloadThread(selected);
			}
		});
	}
	
	private void cancelMenu(final Transfer selected, boolean enabled) {
		MenuItem cancelMenuItem = new MenuItem(rightClickMenu, SWT.PUSH);
		cancelMenuItem.setText(ClientConfigurationController.getInstance().getString("canceldownload"));
		cancelMenuItem.setEnabled(enabled);
		cancelMenuItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent(Event event) {
					PeerFacade.getInstance().cancelDownloadThread(selected);
			}
		});
	}
	
	private void clearMenu(final Transfer selected, boolean enabled) {
		MenuItem clearMenuItem = new MenuItem(rightClickMenu, SWT.PUSH);
		clearMenuItem.setText(ClientConfigurationController.getInstance().getString("cleartransfers"));
		clearMenuItem.setEnabled(enabled);
		clearMenuItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent(Event event) {
					PeerFacade.getInstance().clearTransfers();
			}
		});
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
						ProgressBar bar = null;
						if(transfer.getState() == TransferState.DOWNLOADING){
							item = new TableItem(tblDownloadTransfer,SWT.NONE);
							item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("queued"));
							bar = new ProgressBar(tblDownloadTransfer, SWT.SMOOTH);
							bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					        TableEditor editor = new TableEditor(tblDownloadTransfer);
					        editor.grabHorizontal = editor.grabVertical = true;
					        editor.setEditor(bar, item, 2);
					        downloadHashMap.put(transfer.getId(), item);
						}
						else if(transfer.getState() == TransferState.UPLOADING){
							item = new TableItem(tblUploadTransfer,SWT.NONE);
							item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("queued"));
							bar = new ProgressBar(tblUploadTransfer, SWT.SMOOTH);
							bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					        TableEditor editor = new TableEditor(tblUploadTransfer);
					        editor.grabHorizontal = editor.grabVertical = true;
					        editor.setEditor(bar, item, 2);
					        uploadHashMap.put(transfer.getId(), item);
						}

						if (item != null) {
							item.setText(TransferPanel.fileNamePos,transfer.getFile().getName());
							item.setText(TransferPanel.sizePos,UtilityClass.formatFileSize(transfer.getFileSize()));
							item.setText(TransferPanel.speedPos,"0");
							item.setText(TransferPanel.remainingPos,UtilityClass.formatTime(transfer.getRemainingTime()));
							item.setText(TransferPanel.userPos, transfer.getPeer().getAccountName());
							item.setData("transfer",transfer);
							item.setData("progressbar", bar);
						}

					}
				});
	}

	public void transferCanceled(final Transfer transfer) {
		this.getDisplay().asyncExec(
				new Runnable() {
					public void run(){
						TableItem item = null;
						if(transfer.getState() == TransferState.CANCELLEDDOWNLOAD){
							item = downloadHashMap.get(transfer.getId());
							if (item != null) {
								item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("canceled"));
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
							}
						}
						else if(transfer.getState() == TransferState.CANCELLEDUPLOAD){
							item = uploadHashMap.get(transfer.getId());
							if (item != null) {
								item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("canceled"));
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
							}
						}
					}
				});
	}




	public void transferStateChanged(final Transfer transfer) {
		this.getDisplay().asyncExec(

				new Runnable() {
					public void run(){
						System.out.println(transfer.getState());
						
						TableItem item = null;
						if(transfer.getState() == TransferState.DOWNLOADING){
							item = downloadHashMap.get(transfer.getId());
							if (item != null) {
								item.setText(TransferPanel.sizePos, UtilityClass.formatFileSize(transfer.getFileSize()));
								item.setText(TransferPanel.speedPos,UtilityClass.formatFileSize(transfer.getSpeed()));
								item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("downloading"));
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));  								item.setText(TransferPanel.remainingPos,UtilityClass.formatTime(transfer.getRemainingTime()));
								((ProgressBar)item.getData("progressbar")).setSelection(transfer.getProgress());
							}
						}
						else if(transfer.getState() == TransferState.UPLOADING){
							item = uploadHashMap.get(transfer.getId());
							if (item != null) {
								System.out.println(transfer.getSpeed());
								item.setText(TransferPanel.sizePos, UtilityClass.formatFileSize(transfer.getFileSize()));
								item.setText(TransferPanel.speedPos, UtilityClass.formatFileSize(transfer.getSpeed()));
								item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("uploading"));
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));  
								item.setText(TransferPanel.remainingPos,UtilityClass.formatTime(transfer.getRemainingTime()));			
								((ProgressBar)item.getData("progressbar")).setSelection(transfer.getProgress());
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
						TableItem item = null;
						if(transfer.getState() == TransferState.FAILED){
							item = downloadHashMap.get(transfer.getId());
							if (item != null) {
								item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("failed"));
								item.setText(TransferPanel.speedPos,UtilityClass.formatFileSize(transfer.getSpeed()));
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));  
								item.setText(TransferPanel.remainingPos,UtilityClass.formatTime((long)0));
							}
							
							item = uploadHashMap.get(transfer.getId());
							if (item != null) {
								item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("failed"));
								item.setText(TransferPanel.speedPos,UtilityClass.formatFileSize(transfer.getSpeed()));
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));  
								item.setText(TransferPanel.remainingPos,UtilityClass.formatTime((long)0));
							}
						}
					}
				});
	}

    public void transferFinished(final Transfer transfer) {
    	this.getDisplay().asyncExec(
    		new Runnable() {
    			public void run(){
    				TableItem item = null;
    				if(transfer.getState() == TransferState.FINISHED){
    					item = downloadHashMap.get(transfer.getId());
    					if(item != null){
    						item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("finished"));
    						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
    						((ProgressBar)item.getData("progressbar")).setSelection(100);
    						item.setText(TransferPanel.remainingPos, UtilityClass.formatTime((long)0));
    						item.setText(TransferPanel.speedPos, UtilityClass.formatFileSize((long)0));
    					}
    					item = uploadHashMap.get(transfer.getId());
    					if(item != null) {
    						item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("finished"));
    						item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
    						((ProgressBar)item.getData("progressbar")).setSelection(100);
    						item.setText(TransferPanel.remainingPos, UtilityClass.formatTime((long)0));
    						item.setText(TransferPanel.speedPos, UtilityClass.formatFileSize((long)0));
                        }
                    }
                }
        });
    }


	public void transferPaused(final Transfer transfer) {
		this.getDisplay().asyncExec(
				new Runnable() {
					public void run(){
						TableItem item = null;
						if(transfer.getState() == TransferState.PAUSEDDOWNLOAD){
							item = downloadHashMap.get(transfer.getId());
							if(item != null) {
								item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("paused"));
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
								item.setText(TransferPanel.remainingPos, "..:..:..");
								item.setText(TransferPanel.speedPos, UtilityClass.formatFileSize((long)0));
							}
						}
						else if(transfer.getState() == TransferState.PAUSEDUPLOAD){
							item = uploadHashMap.get(transfer.getId());
							if(item != null) {
								item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("paused"));
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
								item.setText(TransferPanel.remainingPos, "..:..:..");
								item.setText(TransferPanel.speedPos, UtilityClass.formatFileSize((long)0));
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
							item = downloadHashMap.get(transfer.getId());
							if (item != null) {
								item.setText(TransferPanel.sizePos, UtilityClass.formatFileSize(transfer.getFileSize()));
								item.setText(TransferPanel.speedPos,UtilityClass.formatFileSize(transfer.getSpeed()));
								item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("downloading"));
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));  
								item.setText(TransferPanel.remainingPos,UtilityClass.formatTime(transfer.getRemainingTime()));									
								((ProgressBar)item.getData("progressbar")).setSelection(transfer.getProgress());
							}
						}
						else if(transfer.getState() == TransferState.RESUMEDUPLOAD){
							item = uploadHashMap.get(transfer.getId());
							if(item != null) {
								item.setText(TransferPanel.sizePos, UtilityClass.formatFileSize(transfer.getFileSize()));
								item.setText(TransferPanel.speedPos, UtilityClass.formatFileSize(transfer.getSpeed()));
								item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("uploading"));
								item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));  
								item.setText(TransferPanel.remainingPos,UtilityClass.formatTime(transfer.getRemainingTime()));
								((ProgressBar)item.getData("progressbar")).setSelection(transfer.getProgress());
							}
						}
					}
				});
	}

	public void transfersCleared() {
		// TODO Auto-generated method stub
		for (int i=0; i<tblDownloadTransfer.getItemCount(); i++) {
			if (tblDownloadTransfer.getItem(i).getData("transfer")==null)
				tblDownloadTransfer.remove(i);
		}
		tblDownloadTransfer.redraw();
		for (int i=0; i<tblUploadTransfer.getItemCount(); i++) {
			if (tblUploadTransfer.getItem(i).getData("transfer")==null)
				tblUploadTransfer.remove(i);
		}
		tblUploadTransfer.redraw();
	}

}
