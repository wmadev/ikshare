package ikshare.client.gui.panels;

import ikshare.client.ClientController;
import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.UtilityClass;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.client.gui.ExceptionWindow;
import ikshare.domain.Peer;
import ikshare.domain.PeerFacade;
import ikshare.domain.SearchResult;
import ikshare.domain.Transfer;
import ikshare.domain.TransferState;
import ikshare.domain.event.EventController;
import ikshare.client.gui.MainScreen;

import ikshare.domain.IKShareFile;
import ikshare.domain.event.listener.ServerConversationListener;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.DownloadInformationResponseCommand;
import ikshare.protocol.command.FoundResultCommando;
import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SearchPanel extends AbstractPanel implements ServerConversationListener {

    private static String ICON_SEARCH = "resources/icons/sp_found.png";
    private boolean advanced = false;
    private HashMap<String, CTabItem> searches;
    private Text txtKeyword;
    private CTabFolder folder;

    public SearchPanel(String text, String icon) {
        super(text, icon);
        EventController.getInstance().addServerConversationListener(this);
        searches = new HashMap<String, CTabItem>();
        this.setLayout(new GridLayout(2, false));
        this.init();
        this.load();
    }

    private Group drawAdvancedSearch(Composite parent) {
        //((GridData)(parent.getLayoutData())).heightHint=200;
        Group grpAdvanced = new Group(parent, SWT.BORDER);
        grpAdvanced.setLayout(new GridLayout(1, false));
        grpAdvanced.setText(ClientConfigurationController.getInstance().getString("advanced"));
        grpAdvanced.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        Label lblSearchName = new Label(grpAdvanced, SWT.NONE);
        lblSearchName.setText(ClientConfigurationController.getInstance().getString("name"));
        GridData gd = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        //gd.widthHint = 100;
        lblSearchName.setLayoutData(gd);
        txtKeyword = new Text(grpAdvanced, SWT.BORDER);
        txtKeyword.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        final Button btnAndOr = new Button(grpAdvanced, SWT.BORDER);
        btnAndOr.setText(ClientConfigurationController.getInstance().getString("and"));
        btnAndOr.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1));
        btnAndOr.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                if (btnAndOr.getText().equalsIgnoreCase(ClientConfigurationController.getInstance().getString("and"))) {
                    btnAndOr.setText(ClientConfigurationController.getInstance().getString("or"));
                } else {
                    btnAndOr.setText(ClientConfigurationController.getInstance().getString("and"));
                }
            }
        });
        Label lblSearchType = new Label(grpAdvanced, SWT.NONE);
        lblSearchType.setText(ClientConfigurationController.getInstance().getString("type"));
        lblSearchType.setLayoutData(gd);
        Combo cbTypes = new Combo(grpAdvanced, SWT.DROP_DOWN | SWT.READ_ONLY);
        cbTypes.setItems(new String[]{"----", ClientConfigurationController.getInstance().getString("audio"),
            ClientConfigurationController.getInstance().getString("video"),
            ClientConfigurationController.getInstance().getString("text"),
            ClientConfigurationController.getInstance().getString("other")
        });
        cbTypes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        cbTypes.select(0);

        Label lblSearchSize = new Label(grpAdvanced, SWT.NONE);
        lblSearchSize.setText(ClientConfigurationController.getInstance().getString("size"));
        GridData gdsize = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        lblSearchSize.setLayoutData(gdsize);

        Composite cmpSize = new Composite(grpAdvanced, SWT.NONE);
        cmpSize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        cmpSize.setLayout(new GridLayout(3, false));
        Label lblMin = new Label(cmpSize, SWT.NONE);
        lblMin.setText(ClientConfigurationController.getInstance().getString("between"));
        lblMin.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        Text txtMin = new Text(cmpSize, SWT.BORDER);
        txtMin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        Combo cbSize1 = new Combo(cmpSize, SWT.DROP_DOWN | SWT.READ_ONLY);
        cbSize1.setItems(new String[]{"-----", "byte", "Kbyte", "Mbyte", "Gbyte"});
        cbSize1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        cbSize1.select(0);

        Label lblMax = new Label(cmpSize, SWT.NONE);
        lblMax.setText(ClientConfigurationController.getInstance().getString("and"));
        lblMax.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        Text txtMax = new Text(cmpSize, SWT.BORDER);
        txtMax.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        Combo cbSize2 = new Combo(cmpSize, SWT.DROP_DOWN | SWT.READ_ONLY);
        cbSize2.setItems(new String[]{"-----", "byte", "Kbyte", "Mbyte", "Gbyte"});
        cbSize2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        cbSize2.select(0);

        Label lblSearchFolder = new Label(grpAdvanced, SWT.NONE);
        lblSearchFolder.setText(ClientConfigurationController.getInstance().getString("folder"));
        lblSearchFolder.setLayoutData(gd);
        Composite radioButtons = new Composite(grpAdvanced, SWT.NONE);
        radioButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        radioButtons.setLayout(new FillLayout());
        Button rbFile = new Button(radioButtons, SWT.RADIO);
        rbFile.setText(ClientConfigurationController.getInstance().getString("file"));
        rbFile.setSelection(true);
        Button rbFolder = new Button(radioButtons, SWT.RADIO);
        rbFolder.setText(ClientConfigurationController.getInstance().getString("folder"));
        return grpAdvanced;
    }

   
             
         private Group drawBasicSearch(Composite parent) {
        //((GridData)(parent.getLayoutData())).heightHint=75;
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
        txtKeyword = new Text(grpBasic, SWT.BORDER);
        txtKeyword.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        return grpBasic;
    }

    private void load() {
    /*TableItem ti=null;
    SearchResult searchResult=null;
    //searchResult = new SearchResult(new Date().getTime()+"",new Peer("Monet"), new File("C:\\/" + "testmiddelgroot.rar"));
    ti = new TableItem(tblResults, SWT.NONE);
    ti.setText(0, searchResult.getFile().getName());
    //ti.setText(1,UtilityClass.formatFileSize());
    ti.setText(2, searchResult.getPeer().getAccountName());
    ti.setData("results",searchResult);*/

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
                                } else {
                                    advanced = false;
                                    btnAdvanced.setText(ClientConfigurationController.getInstance().getString("advanced"));
                                    layout.topControl = drawBasicSearch(cmpSearch);
                                }
                                cmpSearch.layout();
                            }
                        });
            }
        });

        btnAdvanced.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
        Button btnSearch = new Button(cmpButtons, SWT.BORDER);
        btnSearch.setText(ClientConfigurationController.getInstance().getString("search"));
        btnSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
        btnSearch.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                if (!advanced) {
                    String searchId = String.valueOf(new Date().getTime());

                    ClientController.getInstance().findBasic(searchId, txtKeyword.getText());
                }

            }
        });
    }

    private void createTabItem(SearchResult sr) {
        final CTabItem result = new CTabItem(folder, SWT.CLOSE);
        
        searches.put(sr.getId(), result);
        if (new File(ICON_SEARCH).exists()) {
            result.setImage(new Image(Display.getCurrent(), ICON_SEARCH));
        }
        result.setText(sr.getId());
        result.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                result.dispose();
            }
            
        });
        folder.setSelection(result);
        
        Composite cmpResult1 = new Composite(folder, SWT.NONE);
        cmpResult1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        cmpResult1.setLayout(new GridLayout(1, false));
        
        final Table tblResults = new Table(cmpResult1, SWT.FULL_SELECTION | SWT.BORDER);
        result.setData("table", tblResults);
        tblResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        tblResults.setLinesVisible(true);
        tblResults.setHeaderVisible(true);
        tblResults.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent event) {
                final int selectedRow = tblResults.getSelectionIndex();
                if (selectedRow == -1) {
                    return;
                }

                SearchResult selected = (SearchResult) tblResults.getItem(selectedRow).getData("result");
                if (selected != null) {
                    ClientController.getInstance().getDownloadInformationForResult(selected);
                }
            }
        });

        result.setControl(cmpResult1);
        addTableColumn(tblResults, ClientConfigurationController.getInstance().getString("filename"), 300, SWT.LEFT);
        addTableColumn(tblResults, ClientConfigurationController.getInstance().getString("size"), 100, SWT.RIGHT);
        addTableColumn(tblResults, ClientConfigurationController.getInstance().getString("peer"), 100, SWT.RIGHT);
        addTableRow(sr, tblResults);
    }

    private void addTableRow(SearchResult sr, Table table) {
        TableItem item = new TableItem(table, SWT.NONE);
        item.setData("result", sr);
        item.setText(0, sr.getName());
        item.setText(1, UtilityClass.formatFileSize(sr.getSize()));
        item.setText(2, sr.getOwner());
    }

    private void addTableColumn(Table table, String text, int width, int align) {
        TableColumn column = new TableColumn(table, SWT.NONE);
        column.setText(text);
        column.pack();
        column.setWidth(width);
        column.setAlignment(align);
    }

    public void receivedCommando(final Commando c) {
        this.getDisplay().asyncExec(new Runnable() {

            public void run() {
                if (c instanceof FoundResultCommando) {
                    FoundResultCommando frc = (FoundResultCommando) c;
                    SearchResult sr  = new SearchResult(frc.getSearchID(), frc.getName(), frc.getSize(),frc.getAccountName(),frc.isFolder(),frc.getParentId());
                    if (searches.containsKey(sr.getId())) {
                        updateTabItem(searches.get(sr.getId()), sr);
                    } else {
                        createTabItem(sr);
                    }
                }
                else if(c instanceof DownloadInformationResponseCommand){
                    try {
                        int aantaldownloads = Integer.parseInt(MainScreen.getInstance().getInfoBar().getLblNrDownload().getText());
                        MainScreen.getInstance().getInfoBar().getLblNrDownload().setText("" + aantaldownloads + 1);
                        Transfer t = ClientController.getInstance().getTransferForDownload((DownloadInformationResponseCommand) c);
                        PeerFacade.getInstance().addToDownloads(t);
                        EventController.getInstance().triggerDownloadStartedEvent(t);
                    } catch (UnknownHostException ex) {
                        new ExceptionWindow(ex,MainScreen.getInstance(),false);
                    }
                }
            }
        });
    }

    private void updateTabItem(CTabItem tab, SearchResult sr) {
        Table tblResults = (Table) tab.getData("table");
        addTableRow(sr, tblResults);
    }
}
