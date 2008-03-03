/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.Configuration;
import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 *
 * @author Jana
 */
public class TransferPanel extends AbstractPanel{
    private static String ICON_DOWN="resources/icons/tp_down.png";
    private static String ICON_UP="resources/icons/tp_up.png";
    
    public TransferPanel(String text,String icon){
        super(text,icon);
        FillLayout layout=new FillLayout();
        this.setLayout(layout);
        this.init();
    }

    private void init() {
        TabFolder folder=new TabFolder(this, SWT.NONE);
        
        // Download
        TabItem downloadTab = new TabItem(folder,SWT.NONE);
        downloadTab.setText(Configuration.getInstance().getString("downloads"));
        if(new File(ICON_DOWN).exists()){
            Image icon = new Image(Display.getCurrent(), ICON_DOWN);
            downloadTab.setImage(icon);
        }
        Composite cmpDownload=new Composite(folder, SWT.NONE);
        cmpDownload.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,3,1));
	cmpDownload.setLayout(new GridLayout(1,false));
	Table tblDownloadTransfer = new Table(cmpDownload,SWT.FULL_SELECTION | SWT.BORDER);
        tblDownloadTransfer.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
	tblDownloadTransfer.setLinesVisible (true);
        tblDownloadTransfer.setHeaderVisible (true);
        addTableColumn(tblDownloadTransfer,Configuration.getInstance().getString("filename"),300,SWT.LEFT);
	addTableColumn(tblDownloadTransfer,Configuration.getInstance().getString("size"),100,SWT.RIGHT);
	addTableColumn(tblDownloadTransfer,Configuration.getInstance().getString("state"), 150, SWT.RIGHT);
	addTableColumn(tblDownloadTransfer,Configuration.getInstance().getString("speed"),100,SWT.RIGHT);
	addTableColumn(tblDownloadTransfer,Configuration.getInstance().getString("remaining"), 100, SWT.RIGHT);
        downloadTab.setControl(cmpDownload);
        
        // Upload
        TabItem uploadTab = new TabItem(folder,SWT.NONE);
        if(new File(ICON_UP).exists()){
            Image icon = new Image(Display.getCurrent(), ICON_UP);
            uploadTab.setImage(icon);
        }
        uploadTab.setText(Configuration.getInstance().getString("uploads"));
        Composite cmpUpload=new Composite(folder, SWT.NONE);
        cmpUpload.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,3,1));
	cmpUpload.setLayout(new GridLayout(1,false));
	Table tblUploadTransfer = new Table(cmpUpload,SWT.FULL_SELECTION | SWT.BORDER);
        tblUploadTransfer.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
	tblUploadTransfer.setLinesVisible (true);
        tblUploadTransfer.setHeaderVisible (true);
        addTableColumn(tblUploadTransfer,Configuration.getInstance().getString("filename"),300,SWT.LEFT);
	addTableColumn(tblUploadTransfer,Configuration.getInstance().getString("size"),100,SWT.RIGHT);
	addTableColumn(tblUploadTransfer,Configuration.getInstance().getString("state"), 150, SWT.RIGHT);
	addTableColumn(tblUploadTransfer,Configuration.getInstance().getString("speed"),100,SWT.RIGHT);
	addTableColumn(tblUploadTransfer,Configuration.getInstance().getString("remaining"), 100, SWT.RIGHT);
        uploadTab.setControl(cmpUpload);   
    }
    
    private void addTableColumn(Table table, String text, int width, int align) {
        TableColumn column = new TableColumn(table, SWT.NONE);
	column.setText(text);
	column.pack();
	column.setWidth(width);
	column.setAlignment(align);
	}
    
    
}
