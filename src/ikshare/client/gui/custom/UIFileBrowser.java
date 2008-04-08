/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.custom;

import eu.medsea.util.MimeUtil;
import ikshare.client.configuration.ClientConfiguration;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.client.gui.UtilityClass;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.ClientConfigurationListener;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 *
 * @author awosy
 */
public class UIFileBrowser implements ClientConfigurationListener{
    private static String MIMETYPE_ICONS = "resources/icons/mimetypes/";
    private static String ICON_UP = "resources/icons/go_up.png";
    private Composite parent;
    private File root;
    private File current;
    private Table tblFileBrowser;
    private Label lblCurrent;
    
    public UIFileBrowser(Composite parent,File root){
        this.parent = parent;
        EventController.getInstance().addClientConfigurationListener(this);
        parent.setLayout(new GridLayout(3,false));
        this.root = current = root;
        init();
    }
    
    private void init(){
        Button btnUp = new Button(parent,SWT.NONE);
        btnUp.setLayoutData(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
        btnUp.setImage(new Image(Display.getCurrent(),ICON_UP));
        btnUp.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                if(current.equals(root)){
                    fillTable(current);
                }
                else{
                    current = current.getParentFile();
                    fillTable(current);
                }
            }

           
        });
        Label lblSelected = new Label(parent,SWT.NONE);
        lblSelected.setText(ClientConfigurationController.getInstance().getString("currentfolder"));
        lblSelected.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,false,false,1,1));
        lblCurrent = new Label(parent,SWT.NONE);
        lblCurrent.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false,1,1));
        lblCurrent.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
        tblFileBrowser = new Table(parent,SWT.BORDER|SWT.FULL_SELECTION);
        tblFileBrowser.setLinesVisible (true);
        tblFileBrowser.setHeaderVisible (true);
        
        tblFileBrowser.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,3,1));
        addTableColumn(tblFileBrowser,ClientConfigurationController.getInstance().getString("filename"),580,SWT.LEFT);
	addTableColumn(tblFileBrowser,ClientConfigurationController.getInstance().getString("filetype"),70,SWT.LEFT);
        addTableColumn(tblFileBrowser,ClientConfigurationController.getInstance().getString("size"), 90, SWT.LEFT);
        fillTable(current);
        tblFileBrowser.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event){
                if(event.button == 3){
                    final int selectedRow = tblFileBrowser.getSelectionIndex();
                    if (selectedRow == -1) {
                        return;
                    }
                    final File file = (File) tblFileBrowser.getItem(tblFileBrowser.getSelectionIndex()).getData("file");
                    Menu rightClickMenu = new Menu (tblFileBrowser.getShell(), SWT.POP_UP);
                    if(file.isDirectory()){
                        MenuItem selectFolderMenuItem = new MenuItem(rightClickMenu, SWT.PUSH);
                        selectFolderMenuItem.setText(ClientConfigurationController.getInstance().getString("opendirectory"));
                        selectFolderMenuItem.addListener (SWT.Selection, new Listener () {
                            public void handleEvent(Event event) {
                                fillTable(file);
                            }
                        });     
                    }
                    MenuItem openExternalMenuItem = new MenuItem(rightClickMenu, SWT.PUSH);
                    openExternalMenuItem.setText(ClientConfigurationController.getInstance().getString("openexternal"));
                    openExternalMenuItem.addListener (SWT.Selection, new Listener () {
                            public void handleEvent(Event event) {
                                try {
                                    Desktop.getDesktop().open(file);
                                } catch (IOException ex) {
                                    System.out.println(ex.getMessage());
                                }
                            }
                    });
                    MenuItem deleteMenuItem = new MenuItem(rightClickMenu, SWT.PUSH);
                    deleteMenuItem.setText(ClientConfigurationController.getInstance().getString("deletefile"));
                    deleteMenuItem.addListener (SWT.Selection, new Listener () {
                            public void handleEvent(Event event) {
                                File file = (File)tblFileBrowser.getItem(selectedRow).getData("file");
                                file.delete();
                                fillTable(current);
                            }
                    });
                    
                    rightClickMenu.setVisible (true);
                }
            }
            
            
            public void mouseDoubleClick(MouseEvent event) {
                 final int selectedRow = tblFileBrowser.getSelectionIndex();
                 if (selectedRow == -1) {
                    return;
                 }
                 File file = (File)tblFileBrowser.getItem(selectedRow).getData("file");
                 if(file.isDirectory()){
                    current = file;
                    fillTable(current);
                 }
                 else{
                     try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                 }
                 
             }
         });
                    
            
        
    }
    private void fillTable(File current) {
        lblCurrent.setText(current.getPath());
        tblFileBrowser.removeAll();
        for(File f:current.listFiles()){
            if(!f.getName().startsWith(".")){
                if(f.isDirectory())
                    addTableItem(f);
            }
        }
        for(File f:current.listFiles()){
            if(!f.getName().startsWith(".")){
                if(f.isFile())
                    addTableItem(f);
            }
        }
    }
    
    private void addTableItem(File file){
        TableItem item = new TableItem(tblFileBrowser, SWT.NONE);
        item.setText(0,file.getName());
        item.setText(1,MimeUtil.getFileExtension(file));
        if(file.isDirectory()){
            item.setForeground(0,Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
            item.setText(1,ClientConfigurationController.getInstance().getString("dir"));
        }
        Image icon = getMimeTypeIcon(file);
        if(icon!=null){
            item.setImage(0, icon);
        }
           
        
        item.setText(2,UtilityClass.formatFileSize(file.length()));
	item.setData("file",file);
    }
    private Image getMimeTypeIcon(File file){
        Image icon = null;
        String[] mimetypes = MimeUtil.getMimeType(file.getName()).split(",");
        
        if(file.isDirectory()){
            icon =  new Image(Display.getCurrent(),MIMETYPE_ICONS+"folder.png");
        }
        else{ 
            
            int i = 0;
            while(icon==null){
                
                if(i>mimetypes.length-1){
                    icon =new Image(Display.getCurrent(),MIMETYPE_ICONS+"unknown.png");    
                }
                else {
                    //System.out.println(mimetypes[i]);
                    if(new File(MIMETYPE_ICONS+mimetypes[i].replace("/", "_")+".png").exists()){
                        icon = new Image(Display.getCurrent(), MIMETYPE_ICONS+mimetypes[i].replace("/", "_")+".png");
                    }
                }
                i++;
                
            }
        }
        return icon;
    }
    
    private void addTableColumn(final Table table, String text, int width, int align) {
        final TableColumn column = new TableColumn(table, SWT.NONE);
	column.setText(text);
	column.pack();
	column.setWidth(width);
	column.setAlignment(align);
    }

    public void update(final ClientConfiguration config) {
        parent.getDisplay().asyncExec(
            new Runnable() {
                public void run(){
                    root = current = config.getSharedFolder();
                    fillTable(root);
            }
        });
    }
    
}
