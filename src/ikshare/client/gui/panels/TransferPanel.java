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
import org.eclipse.swt.custom.TreeEditor;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class TransferPanel extends AbstractPanel implements	FileTransferListener {


	private static String ICON_DOWN = "resources/icons/tp_down.png";

	private static String ICON_UP = "resources/icons/tp_up.png";
	
	private static int fileNamePos = 0, sizePos = 1, statusPos = 3, speedPos = 4, remainingPos = 5, userPos=6;

	private Tree treeUploadTransfer, treeDownloadTransfer;
	private Menu rightClickMenu;
	private HashMap<String, TreeItem> downloadHashMap, uploadHashMap;
	
	public TransferPanel(String text, String icon) {
		super(text, icon);

		EventController.getInstance().addFileTransferListener(this);
		FillLayout layout = new FillLayout();
		this.setLayout(layout);
		this.init();
		downloadHashMap = new HashMap<String, TreeItem>();
		uploadHashMap = new HashMap<String, TreeItem>();
		
	}


	private void init() {
		TabFolder folder=new TabFolder(this, SWT.NONE);

		// Download
		makeDownloadTab(folder);
		// Upload
		makeUploadTab(folder); 
	}


	private void makeUploadTab(TabFolder folder) {
		TabItem uploadTab = new TabItem(folder,SWT.BORDER);
		uploadTab.setText(ClientConfigurationController.getInstance().getString("uploads"));
		if(new File(ICON_UP).exists()){
			Image icon = new Image(Display.getCurrent(), ICON_UP);
			uploadTab.setImage(icon);
		}
		Composite cmpUpload=new Composite(folder, SWT.NONE);
		cmpUpload.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,3,1));
		cmpUpload.setLayout(new GridLayout(1,false));
		treeUploadTransfer = new Tree(cmpUpload,SWT.FULL_SELECTION | SWT.BORDER);
		treeUploadTransfer.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		treeUploadTransfer.setLinesVisible (true);
		treeUploadTransfer.setHeaderVisible (true);
		treeUploadTransfer.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent event) {
				if(event.button == 3){
					final int selectedRow = treeUploadTransfer.getSelectionCount();
					if (selectedRow == 0) 
						return;
					
					final Transfer selected = (Transfer)treeUploadTransfer.getSelection()[0].getData("transfer");
					if (selected != null) {
						rightClickMenu = new Menu (treeUploadTransfer.getShell(), SWT.POP_UP);
						clearMenu(selected, true);
					}
				}
			}
		});
		addTreeColumn(treeUploadTransfer,ClientConfigurationController.getInstance().getString("filename"),300,SWT.LEFT);
		addTreeColumn(treeUploadTransfer,ClientConfigurationController.getInstance().getString("size"),100,SWT.RIGHT);
		addTreeColumn(treeUploadTransfer,ClientConfigurationController.getInstance().getString("progress"), 100, SWT.RIGHT);
		addTreeColumn(treeUploadTransfer,ClientConfigurationController.getInstance().getString("state"), 150, SWT.RIGHT);
		addTreeColumn(treeUploadTransfer,ClientConfigurationController.getInstance().getString("speed"),100,SWT.RIGHT);
		addTreeColumn(treeUploadTransfer,ClientConfigurationController.getInstance().getString("remaining"), 100, SWT.RIGHT);
		addTreeColumn(treeUploadTransfer,ClientConfigurationController.getInstance().getString("peer"), 100, SWT.RIGHT);
		uploadTab.setControl(cmpUpload);
	}


	private void makeDownloadTab(TabFolder folder) {
		TabItem downloadTab = new TabItem(folder,SWT.BORDER);
		downloadTab.setText(ClientConfigurationController.getInstance().getString("downloads"));
		if(new File(ICON_DOWN).exists()){
			Image icon = new Image(Display.getCurrent(), ICON_DOWN);
			downloadTab.setImage(icon);
		}
		Composite cmpDownload=new Composite(folder, SWT.NONE);
		cmpDownload.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,3,1));
		cmpDownload.setLayout(new GridLayout(1,false));
		treeDownloadTransfer = new Tree(cmpDownload,SWT.FULL_SELECTION | SWT.BORDER);
		treeDownloadTransfer.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		treeDownloadTransfer.setLinesVisible (true);
		treeDownloadTransfer.setHeaderVisible (true);
		// Right click menu
		treeDownloadTransfer.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent event) {
				if(event.button == 3){
					final int selectedRow = treeDownloadTransfer.getSelectionCount();
					if (selectedRow == 0) 
						return;
					
					final Transfer selected = (Transfer)treeDownloadTransfer.getSelection()[0].getData("transfer");
					if (selected != null) {
						rightClickMenu = new Menu (treeDownloadTransfer.getShell(), SWT.POP_UP);
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

		addTreeColumn(treeDownloadTransfer,ClientConfigurationController.getInstance().getString("filename"),300,SWT.LEFT);
		addTreeColumn(treeDownloadTransfer,ClientConfigurationController.getInstance().getString("size"),100,SWT.RIGHT);
		addTreeColumn(treeDownloadTransfer,ClientConfigurationController.getInstance().getString("progress"), 100, SWT.RIGHT);
		addTreeColumn(treeDownloadTransfer,ClientConfigurationController.getInstance().getString("state"), 150, SWT.RIGHT);
		addTreeColumn(treeDownloadTransfer,ClientConfigurationController.getInstance().getString("speed"),100,SWT.RIGHT);
		addTreeColumn(treeDownloadTransfer,ClientConfigurationController.getInstance().getString("remaining"), 100, SWT.RIGHT);
		addTreeColumn(treeDownloadTransfer,ClientConfigurationController.getInstance().getString("peer"), 100, SWT.RIGHT);
		downloadTab.setControl(cmpDownload);
		/*
		Transfer test = new Transfer();
		test.setId("100");
		test.setBlockSize(2048);
		test.setNumberOfBytesFinished(20000);
		test.setPeer(new Peer("hallo"));
		test.setRemainingTime(0);
		test.setState(TransferState.DOWNLOADING);
		test.setFile(new IKShareFile("map", "bestand"));
		test.setFileSize(20000);
		test.setSpeed(100);
		EventController.getInstance().triggerDownloadStartedEvent(test);
		EventController.getInstance().triggerDownloadStateChangedEvent(test);
		 */

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
	

	private void addTreeColumn(Tree tree, String text, int width, int align) {
		TreeColumn column = new TreeColumn(tree, SWT.NONE);
		column.setText(text);
		column.pack();
		column.setWidth(width);
		column.setAlignment(align);
	}



	public void transferStarted(final Transfer transfer) {
		System.out.println(transfer.getState());
		this.getDisplay().syncExec(
				new Runnable() {
					public void run(){
						TreeItem item = null;
						ProgressBar bar = null;
						if(transfer.getState() == TransferState.DOWNLOADING){
							item = new TreeItem(treeDownloadTransfer,SWT.NONE);
							item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("queued"));
							bar = new ProgressBar(treeDownloadTransfer, SWT.SMOOTH);
							bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					        TreeEditor editor = new TreeEditor(treeDownloadTransfer);
					        editor.grabHorizontal = editor.grabVertical = true;
					        editor.setEditor(bar, item, 2);
					        downloadHashMap.put(transfer.getId(), item);
						}
						else if(transfer.getState() == TransferState.UPLOADING){
							item = new TreeItem(treeUploadTransfer,SWT.NONE);
							item.setText(TransferPanel.statusPos,ClientConfigurationController.getInstance().getString("queued"));
							bar = new ProgressBar(treeUploadTransfer, SWT.SMOOTH);
							bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					        TreeEditor editor = new TreeEditor(treeUploadTransfer);
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
						TreeItem item = null;
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
						
						TreeItem item = null;
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
						TreeItem item = null;
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
    				TreeItem item = null;
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
						TreeItem item = null;
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
						TreeItem item = null;
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

		for (int i=0; i<treeDownloadTransfer.getItemCount(); i++) {
			Transfer t = (Transfer) treeDownloadTransfer.getItem(i).getData("transfer");
			if (!PeerFacade.getInstance().getDownloadTransfers().contains(t)) {
				((ProgressBar)treeDownloadTransfer.getItem(i).getData("progressbar")).dispose();
				treeDownloadTransfer.getItem(i).dispose();
			}
		}

		for (int i=0; i<treeUploadTransfer.getItemCount(); i++) {
			Transfer t = (Transfer) treeUploadTransfer.getItem(i).getData("transfer");
			if (!PeerFacade.getInstance().getUploadTransfers().contains(t)) {
				((ProgressBar)treeUploadTransfer.getItem(i).getData("progressbar")).dispose();
				treeUploadTransfer.getItem(i).dispose();
			}
		}
		
		
		/*
		tblDownloadTransfer.removeAll();
		tblUploadTransfer.removeAll();
		*/
	}


	@Override
	public void initialiseFocus() {
		// TODO Auto-generated method stub
		
	}

}
