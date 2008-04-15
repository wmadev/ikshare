/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.UtilityClass;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.Peer;
import ikshare.domain.PeerFacade;
import ikshare.domain.SearchResult;
import ikshare.domain.Transfer;
import ikshare.domain.TransferState;
import ikshare.domain.event.EventController;

import java.io.File;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SearchPanel extends AbstractPanel{
    private static String ICON_SEARCH="resources/icons/sp_found.png";
    private Table tblResults;
    private boolean advanced = false;
    	
    public SearchPanel(String text,String icon){
        super(text,icon);
        this.setLayout(new GridLayout(2,false));
        this.init();
        this.load();
    }
    private Group drawAdvancedSearch(Composite parent) {
        //((GridData)(parent.getLayoutData())).heightHint=200;
        Group grpAdvanced = new Group(parent, SWT.BORDER);
        grpAdvanced.setLayout(new GridLayout(1,false));
        grpAdvanced.setText("Advanced Search");
        grpAdvanced.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true, 1,1));
        Label lblSearchName = new Label(grpAdvanced,SWT.NONE);
        lblSearchName.setText("Name");
        GridData gd=new GridData(SWT.LEFT, SWT.CENTER, false,false, 1,1);
        gd.widthHint = 100;
        lblSearchName.setLayoutData(gd);
        Text txtName = new Text(grpAdvanced,SWT.BORDER);
        txtName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,false, 1,1));
        final Button btnAndOr= new  Button(grpAdvanced, SWT.BORDER);
        btnAndOr.setText("And");
        btnAndOr.setLayoutData(new GridData(SWT.LEFT,SWT.FILL, false, false, 2, 1));
        btnAndOr.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                if(btnAndOr.getText().equalsIgnoreCase("And")){
                    btnAndOr.setText("Or");
                }
                else{
                    btnAndOr.setText("And");
                }
             }
        });
        Label lblSearchType = new Label(grpAdvanced,SWT.NONE);
        lblSearchType.setText("Type");
        lblSearchType.setLayoutData(gd);
        Text txtType = new Text(grpAdvanced,SWT.BORDER);
        txtType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,false, 1,1));
        Label lblSearchSize = new Label(grpAdvanced,SWT.NONE);
        lblSearchSize.setText("Size");
        lblSearchSize.setLayoutData(gd);
        Text txtSize = new Text(grpAdvanced,SWT.BORDER);
        txtSize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,false, 1,1));
        Label lblSearchFolder = new Label(grpAdvanced,SWT.NONE);
        lblSearchFolder.setText("Folder");
        lblSearchFolder.setLayoutData(gd);
        Composite radioButtons= new Composite(grpAdvanced, SWT.NONE);
        radioButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,false, 1,1));
        radioButtons.setLayout(new FillLayout());
        Button rbFile=new Button(radioButtons, SWT.RADIO);
        rbFile.setText("File");
        rbFile.setSelection(true);
        Button rbFolder=new Button(radioButtons, SWT.RADIO);
        rbFolder.setText("Folder");
        return grpAdvanced;
    };
    
    private Group drawBasicSearch(Composite parent) {
        //((GridData)(parent.getLayoutData())).heightHint=75;
        Group grpBasic = new Group(parent, SWT.BORDER);
        grpBasic.setLayout(new GridLayout(1,false));
        grpBasic.setText("Basic Search");
        grpBasic.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,false, 1,1));
        Label lblSearchBasic = new Label(grpBasic,SWT.NONE);
        lblSearchBasic.setText("Keyword");
        GridData gd=new GridData(SWT.LEFT, SWT.CENTER, false,false, 1,1);
        gd.widthHint = 100;
        lblSearchBasic.setLayoutData(gd);
        Text txtKeyword = new Text(grpBasic,SWT.BORDER);
        txtKeyword.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,false, 1,1));
        return grpBasic;
    }
        
    private void load() {
		TableItem ti=null;
		SearchResult searchResult=null;
		
		
		searchResult = new SearchResult(new Date().getTime()+"",new Peer("Monet"), new File("C:\\/" + "testmiddelgroot.rar"));
		ti = new TableItem(tblResults, SWT.NONE);
		ti.setText(0, searchResult.getFile().getName());
		//ti.setText(1,UtilityClass.formatFileSize());
		ti.setText(2, searchResult.getPeer().getAccountName());
		ti.setData("results",searchResult);
		
	}

	private void init() {
        //Search options
        final Composite cmpSearch = new Composite(this,SWT.NONE);
        final GridData gd=new GridData(SWT.FILL,SWT.FILL,true,true,1,1);
        gd.widthHint=200;
        gd.horizontalIndent=gd.verticalIndent=0;
        cmpSearch.setLayoutData(gd);
        final StackLayout layout = new StackLayout();
        cmpSearch.setLayout(layout);
        layout.topControl = drawBasicSearch(cmpSearch);
              
        //SearchResults
        Composite results = new Composite(this, SWT.BORDER);
        results.setLayout(new FillLayout());
        GridData gd2=new GridData(SWT.FILL, SWT.FILL, true,true, 1,2);
        results.setLayoutData(gd2);
        TabFolder folder=new TabFolder(results, SWT.BORDER);
        
        //Eerste resultaat
        TabItem result1 = new TabItem(folder,SWT.BORDER);
        if(new File(ICON_SEARCH).exists()){
            result1.setImage(new Image(Display.getCurrent(), ICON_SEARCH));
        }
        result1.setText(ClientConfigurationController.getInstance().getString("result")+" 1");
        
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
		                    
		                    SearchResult selected = (SearchResult)tblResults.getItem(selectedRow).getData("results");
		                    if (selected!=null) {
		                    	Transfer newTransfer = new Transfer();
		                    	newTransfer.setId(new Date().getTime()+"");
		                    	newTransfer.setFile(selected.getFile());
		                    	newTransfer.setPeer(selected.getPeer());
		                    	newTransfer.setState(TransferState.DOWNLOADING);
		                    	PeerFacade.getInstance().addToDownloads(newTransfer);
		                    	EventController.getInstance().triggerDownloadStartedEvent(newTransfer);
		                    }
                        }
                    });

        result1.setControl(cmpResult1);
        addTableColumn(tblResults,ClientConfigurationController.getInstance().getString("filename"),300,SWT.LEFT);
	addTableColumn(tblResults,ClientConfigurationController.getInstance().getString("size"),100,SWT.RIGHT);
        addTableColumn(tblResults,ClientConfigurationController.getInstance().getString("peer"), 100, SWT.RIGHT);
        
         //Search Buttons
        Composite cmpButtons = new Composite(this,SWT.NONE);
        cmpButtons.setLayoutData(new GridData(SWT.RIGHT,SWT.END,false,false,1,1));
        cmpButtons.setLayout(new GridLayout(2,false));
        final Button btnAdvanced = new Button(cmpButtons,SWT.BORDER);
        btnAdvanced.setText("Advanced search");
        btnAdvanced.addListener(SWT.Selection, new Listener(){

                   public void handleEvent(Event e) {
                       Display.getCurrent().asyncExec(
            new Runnable() {
                public void run(){
                       
                       if(!advanced){
                            advanced = true;
                            btnAdvanced.setText("Basic search");
                            gd.heightHint=200;
                            cmpSearch.setLayoutData(gd);
                            layout.topControl = drawAdvancedSearch(cmpSearch);
                        }
                        else{
                            advanced = false;
                            btnAdvanced.setText("Advanced search");
                            gd.heightHint=75;
                            cmpSearch.setLayoutData(gd);
                            layout.topControl = drawBasicSearch(cmpSearch);
                        }
                        cmpSearch.layout();
                 }
        });                     
                }
        
                });
                


        btnAdvanced.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false,false, 1,1));
        Button btnSearch = new Button(cmpButtons,SWT.BORDER);
        btnSearch.setText("Search");
        btnSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false,false, 1,1));
            
    }
    
    private void addTableColumn(Table table, String text, int width, int align) {
        TableColumn column = new TableColumn(table, SWT.NONE);
	column.setText(text);
	column.pack();
	column.setWidth(width);
	column.setAlignment(align);
	}

}
