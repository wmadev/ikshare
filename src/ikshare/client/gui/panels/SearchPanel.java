package ikshare.client.gui.panels;

import ikshare.client.ClientController;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.ExceptionWindow;
import ikshare.client.gui.MainScreen;
import ikshare.client.gui.UtilityClass;
import ikshare.domain.PeerFacade;
import ikshare.domain.SearchResult;
import ikshare.domain.Transfer;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.ClientControllerListener;
import ikshare.domain.event.listener.ServerConversationListener;
import ikshare.domain.exception.NoServerConnectionException;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.DownloadInformationResponseCommand;
import ikshare.protocol.command.FoundResultCommando;
import ikshare.protocol.command.NoResultsFoundCommando;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class SearchPanel extends AbstractPanel implements ClientControllerListener, KeyListener {

    private static String ICON_SEARCH = "resources/icons/sp_found.png";
    private boolean advanced = false, keywordAnd = true;
    private HashMap<String, CTabItem> searches;
    private HashMap<Integer,TreeItem> treeItems;
    private Text txtKeywordBasic, txtKeywordAdvanced, txtMax, txtMin;
    private CTabFolder folder;
    private Button btnSearch, rbFile, rbFolder, btnAndOr;
    private Combo cbSizeMax, cbSizeMin, cbTypes;
    private String keyword="", minSize="", maxSize="";
    private int typeID=0, minID=0, maxID=0;
    private boolean file=true;
    private Label lblValidation;
    
    public SearchPanel(String text, String icon) {
        super(text, icon);
        EventController.getInstance().addClientControllerListener(this);
        searches = new HashMap<String, CTabItem>();
        treeItems = new HashMap<Integer,TreeItem>();
        this.setLayout(new GridLayout(2, false));
        this.init();
        
    }


    private void init() {
        //Search options
        final Composite cmpSearch = new Composite(this, SWT.NONE);
        final GridData gd = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
        gd.horizontalIndent = gd.verticalIndent = 0;
        gd.widthHint = 250;
        cmpSearch.setLayoutData(gd);
        final StackLayout layout = new StackLayout();
        cmpSearch.setLayout(layout);
        layout.topControl = drawBasicSearch(cmpSearch);

        //SearchResults
        Composite results = new Composite(this, SWT.BORDER);
        results.setLayout(new FillLayout());
        GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
        results.setLayoutData(gd2);
        folder = new CTabFolder(results, SWT.BORDER);
        
        //Search Buttons
        Composite cmpButtons = new Composite(this, SWT.NONE);
        cmpButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.END, false, false, 1, 1));
        cmpButtons.setLayout(new GridLayout(2, false));
        final Button btnAdvanced = new Button(cmpButtons, SWT.BORDER);
        btnAdvanced.setText(ClientConfigurationController.getInstance().getString("advanced"));
        btnAdvanced.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                Display.getCurrent().asyncExec(
                        new Runnable() {

                            public void run() {

                                if (!advanced) {
                                    advanced = true;
                                    btnAdvanced.setText(ClientConfigurationController.getInstance().getString("basic"));
                                    layout.topControl = drawAdvancedSearch(cmpSearch);
                                    txtKeywordAdvanced.setFocus();
                                    if(keyword.startsWith(txtKeywordBasic.getText())){
                                        txtKeywordAdvanced.setText(keyword);
                                    }
                                    else{
                                        txtKeywordAdvanced.setText(txtKeywordBasic.getText());
                                    }
                                    cbTypes.select(typeID);
                                    cbSizeMax.select(maxID);
                                    cbSizeMin.select(minID);
                                    txtMax.setText(maxSize);
                                    txtMin.setText(minSize);
                                    if(file){
                                        rbFile.setSelection(true);
                                    }
                                    else{
                                        rbFolder.setSelection(true);
                                        cbTypes.setEnabled(false);
                                    }
                                    if(keywordAnd){
                                        btnAndOr.setText(ClientConfigurationController.getInstance().getString("and"));
                                    }
                                    else{
                                        btnAndOr.setText(ClientConfigurationController.getInstance().getString("or"));
                                    }
                                }
                                else {
                                    advanced = false;
                                    btnAdvanced.setText(ClientConfigurationController.getInstance().getString("advanced"));
                                    layout.topControl = drawBasicSearch(cmpSearch);
                                    typeID= cbTypes.getSelectionIndex();
                                    maxID= cbSizeMax.getSelectionIndex();
                                    minID=cbSizeMin.getSelectionIndex();
                                    maxSize=txtMax.getText();
                                    minSize=txtMin.getText();
                                    keyword=txtKeywordAdvanced.getText();
                                    txtKeywordBasic.setFocus();
                                    txtKeywordBasic.setText(keyword.substring(0, keyword.indexOf(" ")==-1?keyword.length():keyword.indexOf(" ")));
                                    if(rbFile.getSelection()){
                                        file=true;
                                    }
                                    else{
                                        file=false;
                                    }
                                
                                    }
                                cmpSearch.layout();
                            }
                        });
            }
        });

        btnAdvanced.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
        btnSearch = new Button(cmpButtons, SWT.BORDER);
        btnSearch.setText(ClientConfigurationController.getInstance().getString("search"));
        btnSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
        btnSearch.setEnabled(false);
        btnSearch.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                search();
                }
                });
    }
    
     private Group drawBasicSearch(Composite parent) {
        Group grpBasic = new Group(parent, SWT.BORDER);
        grpBasic.setLayout(new GridLayout(1, false));
        grpBasic.setText(ClientConfigurationController.getInstance().getString("basic"));
        GridData gdbasic = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
        gdbasic.widthHint = 250;
        grpBasic.setLayoutData(gdbasic);
        Label lblSearchBasic = new Label(grpBasic, SWT.NONE);
        lblSearchBasic.setText("Keyword");
        GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd.widthHint = 100;
        lblSearchBasic.setLayoutData(gd);
        txtKeywordBasic = new Text(grpBasic, SWT.BORDER);
        txtKeywordBasic.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        txtKeywordBasic.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                if(!advanced){
                    
                    if(txtKeywordBasic.getText().equalsIgnoreCase("")){
                    btnSearch.setEnabled(false);
                    }
                    else{
                        btnSearch.setEnabled(true);
                    }
                }
            }
            
        });
        txtKeywordBasic.addKeyListener(this);
        lblValidation = new Label(grpBasic,SWT.WRAP);
        lblValidation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        lblValidation.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
        
        return grpBasic;
    }
     
     private Group drawAdvancedSearch(Composite parent) {
        Group grpAdvanced = new Group(parent, SWT.BORDER);
        grpAdvanced.setLayout(new GridLayout(1, false));
        grpAdvanced.setText(ClientConfigurationController.getInstance().getString("advanced"));
        grpAdvanced.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        Label lblSearchName = new Label(grpAdvanced, SWT.NONE);
        lblSearchName.setText(ClientConfigurationController.getInstance().getString("name"));
        GridData gd = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        lblSearchName.setLayoutData(gd);
        txtKeywordAdvanced = new Text(grpAdvanced, SWT.BORDER);
        txtKeywordAdvanced.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        txtKeywordAdvanced.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                if(txtKeywordAdvanced.getText().equalsIgnoreCase("")){
                    btnSearch.setEnabled(false);
                }
                else{
                    btnSearch.setEnabled(true);
                }
            }
            
        });
        txtKeywordAdvanced.addKeyListener(this);
        btnAndOr = new Button(grpAdvanced, SWT.BORDER);
        btnAndOr.setText(ClientConfigurationController.getInstance().getString("and"));
        btnAndOr.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1));
        btnAndOr.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                if (btnAndOr.getText().equalsIgnoreCase(ClientConfigurationController.getInstance().getString("and"))) {
                    btnAndOr.setText(ClientConfigurationController.getInstance().getString("or"));
                    keywordAnd=false;
                } else {
                    btnAndOr.setText(ClientConfigurationController.getInstance().getString("and"));
                    keywordAnd=true;
                }
            }
        });
        Label lblSearchType = new Label(grpAdvanced, SWT.NONE);
        lblSearchType.setText(ClientConfigurationController.getInstance().getString("type"));
        lblSearchType.setLayoutData(gd);
        cbTypes = new Combo(grpAdvanced, SWT.DROP_DOWN | SWT.READ_ONLY);
        cbTypes.setItems(new String[]{"----", ClientConfigurationController.getInstance().getString("audio"),
            ClientConfigurationController.getInstance().getString("video"),
            ClientConfigurationController.getInstance().getString("text"),
            ClientConfigurationController.getInstance().getString("other")
        });
        cbTypes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        
        Label lblSearchSize = new Label(grpAdvanced, SWT.NONE);
        lblSearchSize.setText(ClientConfigurationController.getInstance().getString("size"));
        GridData gdsize = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        lblSearchSize.setLayoutData(gdsize);

        Composite cmpSize = new Composite(grpAdvanced, SWT.NONE);
        cmpSize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        cmpSize.setLayout(new GridLayout(3, false));
        Label lblMin = new Label(cmpSize, SWT.NONE);
        lblMin.setText(ClientConfigurationController.getInstance().getString("min"));
        lblMin.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
             
        
        txtMin = new Text(cmpSize, SWT.BORDER);
        txtMin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        cbSizeMin = new Combo(cmpSize, SWT.DROP_DOWN | SWT.READ_ONLY);
        cbSizeMin.setItems(new String[]{"byte", "Kbyte", "Mbyte", "Gbyte"});
        cbSizeMin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        
        Label lblMax = new Label(cmpSize, SWT.NONE);
        lblMax.setText(ClientConfigurationController.getInstance().getString("max"));
        lblMax.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        txtMax = new Text(cmpSize, SWT.BORDER);
        txtMax.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        cbSizeMax = new Combo(cmpSize, SWT.DROP_DOWN | SWT.READ_ONLY);
        cbSizeMax.setItems(new String[]{"byte", "Kbyte", "Mbyte", "Gbyte"});
        cbSizeMax.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
     
        Label lblSearchFolder = new Label(grpAdvanced, SWT.NONE);
        lblSearchFolder.setText(ClientConfigurationController.getInstance().getString("folder"));
        GridData gdfolder = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        lblSearchFolder.setLayoutData(gdfolder);
        Composite radioButtons = new Composite(grpAdvanced, SWT.NONE);
        radioButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        radioButtons.setLayout(new FillLayout());
        rbFile = new Button(radioButtons, SWT.RADIO);
        rbFile.setText(ClientConfigurationController.getInstance().getString("file"));
        rbFile.addKeyListener(this);
        rbFolder = new Button(radioButtons, SWT.RADIO);
        rbFolder.setText(ClientConfigurationController.getInstance().getString("folder"));
        rbFolder.addListener(SWT.Selection, new Listener(){

            public void handleEvent(Event event) {
                if(rbFolder.getSelection()){
                    cbTypes.setEnabled(false);
                }
                else {
                    cbTypes.setEnabled(true);
                }
            }
            
        });
        rbFolder.addKeyListener(this);
        lblValidation = new Label(grpAdvanced, SWT.WRAP);
        lblValidation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        lblValidation.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
        return grpAdvanced;
    }

    private void createTabItem(SearchResult sr, String keyword) {
        final CTabItem result = new CTabItem(folder, SWT.CLOSE);
        
        searches.put(sr.getId(), result);

        if (new File(ICON_SEARCH).exists()) {
            result.setImage(new Image(Display.getCurrent(), ICON_SEARCH));
        }
        result.setText(keyword);
        result.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                result.dispose();
            }
            
        });
        folder.setSelection(result);
        
        Composite cmpResult1 = new Composite(folder, SWT.NONE);
        cmpResult1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        cmpResult1.setLayout(new GridLayout(1, false));
        
        final Tree treeResults = new Tree(cmpResult1, SWT.FULL_SELECTION | SWT.BORDER);
        result.setData("tree",treeResults);
        treeResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        treeResults.setLinesVisible(true);
        treeResults.setHeaderVisible(true);
        treeResults.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent event) {
                if(treeResults.getSelection()[0]== null){
                    return;
                }
                SearchResult selected = (SearchResult) treeResults.getSelection()[0].getData("result");

                	
                    try {
                    	getDownloadInformation(selected);
                    } catch (NoServerConnectionException ex) {
                        new ExceptionWindow(ex,MainScreen.getInstance(),false);
                    }
            }
            
            public void getDownloadInformation(SearchResult start) throws NoServerConnectionException {
                if (start != null) {
                	if (!start.isFolder()) {
	            		ClientController.getInstance().getDownloadInformationForResult(start);
	            	}
	            	else {
	            		
                		PeerFacade.getInstance().makeFolder(start);
		            	TreeItem item = treeItems.get(start.getFolderId());
		            	for (int i=0; i<item.getItemCount(); i++)
		            		getDownloadInformation((SearchResult) item.getItem(i).getData("result"));
	            	}
                }
            }
        });
        result.setControl(cmpResult1);
        addTreeColumn(treeResults, ClientConfigurationController.getInstance().getString("filename"), 300, SWT.LEFT);
        addTreeColumn(treeResults, ClientConfigurationController.getInstance().getString("size"), 100, SWT.RIGHT);
        addTreeColumn(treeResults, ClientConfigurationController.getInstance().getString("peer"), 100, SWT.RIGHT);
        addTreeRow(sr, treeResults);
    }
    
    private void addTreeRow(SearchResult sr, Tree tree) {
        TreeItem item = null;
        if(sr.isFolder()){
            if(sr.getParentId()!=0){
                TreeItem parent = treeItems.get(sr.getParentId());
                if(parent!=null){
                    item = new TreeItem(parent,SWT.NONE,0);
                }
                else{
                    item = new TreeItem(tree,SWT.NONE);
                }
            }else{
                item = new TreeItem(tree,SWT.NONE);
            }
            treeItems.put(sr.getFolderId(),item);
        }
        else{
            TreeItem parent = treeItems.get(sr.getFolderId());
            if(parent!=null){
                item = new TreeItem(parent, SWT.NONE);
            }else{
                item = new TreeItem(tree, SWT.NONE);       
            }

        }
        item.setData("result", sr);
        item.setText(0, sr.getName());
        item.setText(1, UtilityClass.formatFileSize(sr.getSize()));
        item.setText(2, sr.getOwner());

    }
    
    private void addTreeColumn(Tree tree, String text, int width, int align) {
        TreeColumn column = new TreeColumn(tree, SWT.NONE);
        column.setText(text);
        column.pack();
        column.setWidth(width);
        column.setAlignment(align);
    }
    
    private void createTabItem(String notfoundkeyword) {
        final CTabItem result = new CTabItem(folder, SWT.CLOSE);
        
        if (new File(ICON_SEARCH).exists()) {
            result.setImage(new Image(Display.getCurrent(), ICON_SEARCH));
        }
        result.setText(notfoundkeyword);
        
        result.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                result.dispose();
            }
            
        });
        folder.setSelection(result);
        
        Composite cmpResult1 = new Composite(folder, SWT.NONE);
        cmpResult1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        cmpResult1.setLayout(new GridLayout(1, false));
        new Label(cmpResult1,SWT.NONE).setText(ClientConfigurationController.getInstance().getString("noresultsfoundfor")+" "+notfoundkeyword);
        result.setControl(cmpResult1);
       
    }

    public void onResultFound(final SearchResult found,final String keyword){
        this.getDisplay().asyncExec(new Runnable() {
            public void run() {
                if (searches.containsKey(found.getId())) {
                        updateTabItem(searches.get(found.getId()), found);
                    } else {
                        createTabItem(found, keyword);
                    }
            }
        });
    }
    public void onNoResultFound(final String keyword){
        this.getDisplay().asyncExec(new Runnable() {
            public void run() {
                createTabItem(keyword);
            }
        });
    }
    
   private void updateTabItem(CTabItem tab, SearchResult sr) {
        Tree treeResults = (Tree) tab.getData("tree");
        addTreeRow(sr, treeResults);
    }
    private boolean validate(){
        boolean valid = true;
        Pattern p = Pattern.compile("[\\w- \\.\\@_]{3,}+");
        if(!advanced){
            Matcher m = p.matcher(txtKeywordBasic.getText());
            if(!m.matches()){
                valid=false;
            }
        }
        else{
            Matcher m = p.matcher(txtKeywordAdvanced.getText());
            if(!m.matches()){
                valid=false;
            }
            p = Pattern.compile("[\\d]{0,}");
            m = p.matcher(txtMax.getText());
            if(!m.matches()){
                valid=false;
            }
            m = p.matcher(txtMin.getText());
            if(!m.matches()){
                valid=false;
            }
        }
        return valid;
    }
    
    private void search() {
        lblValidation.setText("");
        if(validate()){
            String searchId = String.valueOf(new Date().getTime());
            if (!advanced) {
            try {
                ClientController.getInstance().findBasic(searchId, txtKeywordBasic.getText());
            } catch (NoServerConnectionException ex) {
                new ExceptionWindow(ex,MainScreen.getInstance(),false);
            }
                }
                else {
                    long minBytes=0;
                    long maxBytes=0;
                    if(!txtMin.getText().equalsIgnoreCase("")){
                        minBytes=Long.parseLong(txtMin.getText());
                        for (int i =1; i<=cbSizeMin.getSelectionIndex();i++){
                            minBytes*=1024;
                        }
                    }
                    if(!txtMax.getText().equalsIgnoreCase("")){
                        maxBytes=Long.parseLong(txtMax.getText());
                        for (int i =1; i<=cbSizeMax.getSelectionIndex();i++){
                            maxBytes*=1024;
                        }
                    }   
                    if(rbFile.getSelection()){          //search for a file
                try {
                    
                    ClientController.getInstance().findAdvancedFile(searchId, txtKeywordAdvanced.getText(), keywordAnd, cbTypes.getSelectionIndex(), minBytes, maxBytes);
                } catch (NoServerConnectionException ex) {
                    new ExceptionWindow(ex,MainScreen.getInstance(),false);
                }
                    }
                    else{                               //search for a folder
                try {
                    
                    ClientController.getInstance().findAdvancedFolder(searchId, txtKeywordAdvanced.getText(), keywordAnd, minBytes, maxBytes);
                } catch (NoServerConnectionException ex) {
                    new ExceptionWindow(ex,MainScreen.getInstance(),false);
                }
                        
                    }
                }
        }
        else{
            lblValidation.setText(ClientConfigurationController.getInstance().getString("invalidinput"));
        }
                
    }

    public void keyPressed(KeyEvent e) {
                        
    }

    public void keyReleased(KeyEvent e) {
        lblValidation.setText("");
        if(btnSearch.isEnabled()){
                if(e.character == ' ' && !advanced){
                    lblValidation.setText(ClientConfigurationController.getInstance().getString("useadvancedsearch"));
                    //new ExceptionWindow(new Exception("GEen spaties"), MainScreen.getInstance(), false);
                    txtKeywordBasic.setText(txtKeywordBasic.getText().substring(0,txtKeywordBasic.getText().length()-1));
                }
                else if(e.keyCode == SWT.CR){
                    search();
                    btnSearch.setFocus();
                }
        }
    }

    @Override
    public void initialiseFocus() {
        if(advanced){
            txtKeywordAdvanced.setFocus();
        }else{
            txtKeywordBasic.setFocus();
        }
    }

    public void connectionInterrupted() {
        // Not required by SearchPanel
    }

    public void onLogOn() {
        // Not required by SearchPanel
    }

    public void onLogOnFailed(String message) {
        // Not required by SearchPanel
    }
}
