/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.UtilityClass;
import ikshare.client.gui.configuration.ConfigurationController;
import ikshare.domain.PeerFacade;
import ikshare.domain.Transfer;
import ikshare.domain.event.EventController;

import java.io.File;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
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
public class SearchPanel extends AbstractPanel{
    private static String ICON_SEARCH="resources/icons/sp_found.png";
	private Table tblResults;
	
    public SearchPanel(String text,String icon){
        super(text,icon);
        GridLayout gl = new GridLayout(1,false);
        this.setLayout(gl);
        this.init();
        this.load();
    }
        
    private void load() {
		TableItem ti=null;
		Transfer t=null;
		
		t = new Transfer();
		t.setFileName("/testklein.pdf");
		t.setId(new Date().toString());
		ti = new TableItem(tblResults, SWT.NONE);
		ti.setText(0, t.getFileName());
		ti.setText(1,UtilityClass.formatFileSize(t.getFileSize()));
		ti.setText(2, PeerFacade.getInstance().getPeer().getName());
		ti.setData("transfer",t);
		
		t = new Transfer();
		t.setFileName("/testmiddelgroot.rar");
		t.setId(new Date().toString());
		ti = new TableItem(tblResults, SWT.NONE);
		ti.setText(0, t.getFileName());
		ti.setText(1,UtilityClass.formatFileSize(t.getFileSize()));
		ti.setText(2, PeerFacade.getInstance().getPeer().getName());
		ti.setData("transfer",t);
		
		t = new Transfer();
		t.setFileName("/testgroot.avi");
		t.setId(new Date().toString());
		ti = new TableItem(tblResults, SWT.NONE);
		ti.setText(0, t.getFileName());
		ti.setText(1,UtilityClass.formatFileSize(t.getFileSize()));
		ti.setText(2, PeerFacade.getInstance().getPeer().getName());
		ti.setData("transfer",t);
		
	}

	private void init() {
        //SearchOptions
        Composite options = new Composite(this, SWT.BORDER);
        GridData gd=new GridData(SWT.FILL, SWT.FILL, true,false, 1,1);
        gd.heightHint = 100;
        options.setLayoutData(gd);
        //SearchResults
        Composite results = new Composite(this, SWT.BORDER);
        results.setLayout(new FillLayout());
        

        
        
        
        GridData gd2=new GridData(SWT.FILL, SWT.FILL, true,true, 1,1);
        results.setLayoutData(gd2);
        TabFolder folder=new TabFolder(results, SWT.BORDER);
        
        //Eerste resultaat
        
        TabItem result1 = new TabItem(folder,SWT.BORDER);
        if(new File(ICON_SEARCH).exists()){
            result1.setImage(new Image(Display.getCurrent(), ICON_SEARCH));
        }
        result1.setText(ConfigurationController.getInstance().getString("result")+" 1");
        
        Composite cmpResult1 = new Composite(folder, SWT.NONE);
        cmpResult1.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,3,1));
        cmpResult1.setLayout(new GridLayout(1,false));
        
        tblResults = new Table(cmpResult1,SWT.FULL_SELECTION | SWT.BORDER);
        tblResults.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
	    tblResults.setLinesVisible (true);
        tblResults.setHeaderVisible (true);
        tblResults.addMouseListener(new MouseAdapter() {
            public void mouseDoubleClick(MouseEvent event) {
                    final int selectedRow = tblResults.getSelectionIndex();
                    if (selectedRow == -1) 
                        return;
                    
                    Transfer selected = (Transfer)tblResults.getItem(selectedRow).getData("transfer");
                    PeerFacade.getInstance().startDownloadThread(selected);
                    EventController.getInstance().triggerDownloadStartedEvent(selected);
                        }
                    });

        result1.setControl(cmpResult1);
        addTableColumn(tblResults,ConfigurationController.getInstance().getString("filename"),300,SWT.LEFT);
	    addTableColumn(tblResults,ConfigurationController.getInstance().getString("size"),100,SWT.RIGHT);
        addTableColumn(tblResults,ConfigurationController.getInstance().getString("peer"), 100, SWT.RIGHT);
    }
    
    private void addTableColumn(Table table, String text, int width, int align) {
        TableColumn column = new TableColumn(table, SWT.NONE);
	column.setText(text);
	column.pack();
	column.setWidth(width);
	column.setAlignment(align);
	}

}
