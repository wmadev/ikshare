package ikshare.client.gui;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.client.gui.panels.AboutPanel;
import ikshare.client.gui.panels.ChatPanel;
import ikshare.client.gui.panels.HelpPanel;
import ikshare.client.gui.panels.HomePanel;
import ikshare.client.gui.panels.SearchPanel;
import ikshare.client.gui.panels.SettingsPanel;
import ikshare.client.gui.panels.StatisticPanel;
import ikshare.client.gui.panels.TransferPanel;
import java.util.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class MainScreen {
    private Display display;
    private Shell shell;
    private java.util.List<AbstractPanel> panels;
    private ToolBar toolBar;
    private InfoBar infoBar;
    private Composite parent;
    private StackLayout layout;
    private static MainScreen instance;
    
    private boolean isOpen = false;
    
    /*
     * returns the only mainscreen, there can only be one instance of it
     * @return instance     The mainscreen
     */
    public static MainScreen getInstance()
    {
            if(instance==null)
                    instance = new MainScreen();
            return instance;
    }
    
    /*
     *Makes the mainscreen in the shell, sets the layout and adds the different panels to it.
     */
    public MainScreen() {
        ClientConfigurationController.getInstance();
        instance = this;
        display = new Display();
        shell = new Shell(display);
        panels = new ArrayList<AbstractPanel>();
        
        // Make layout of the shell
        doLayout();
        // Add toolbar to the shell
        doToolbar();
        // Add composite to shell
        doComposite();
        // Add infobar to the shell
        doInfobar();

        addPanel(new HomePanel(ClientConfigurationController.getInstance().getString("home"),"resources/icons/tb_home.png"));
        //addPanel(new MediaPlayerPanel(ClientConfigurationController.getInstance().getString("media"),"resources/icons/tb_media.png"));
        addPanel (new SearchPanel(ClientConfigurationController.getInstance().getString("search"),"resources/icons/tb_search.png"));
        addPanel (new TransferPanel(ClientConfigurationController.getInstance().getString("transfer"),"resources/icons/tb_down.png"));
        addPanel (new ChatPanel(ClientConfigurationController.getInstance().getString("chat"),"resources/icons/tb_chat.png"));
        addPanel (new SettingsPanel(ClientConfigurationController.getInstance().getString("settings"),"resources/icons/tb_configure.png"));
        addPanel (new StatisticPanel(ClientConfigurationController.getInstance().getString("statistics"),"resources/icons/tb_stat.png"));
        addPanel (new HelpPanel(ClientConfigurationController.getInstance().getString("help"),"resources/icons/tb_help.png"));
        addPanel (new AboutPanel(ClientConfigurationController.getInstance().getString("about"),"resources/icons/tb_about.png"));
        layout.topControl = panels.get(0);
        panels.get(0).getToolItem().setSelection(true);
        panels.get(0).initialiseFocus();
        
        shell.open();
        shell.forceActive();
        isOpen=true;
        while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {

                        display.sleep();
                }
        }
        ClientConfigurationController.getInstance().saveConfiguration();
               
        display.dispose();
        System.exit(0);
    }
        
    public InfoBar getInfoBar() {
        return infoBar;
    }

    public void setInfoBar(InfoBar infoBar) {
        this.infoBar = infoBar;
    }
    
    /*
     * returns the status of the mainscreen: open or not?
     */
    public boolean isOpen() {
            return isOpen;
    }
    
    /*
     * Makes a new toolbar and adds it to the mainscreen.
     * Sets the layout of the toolbar in the shell (1 vertical grid and 2 horizontal grids)
     * Sets the layout of the toolbar to a gridlayout with 8 koloms.
     */
    private void doToolbar() {
        toolBar = new ToolBar(shell, SWT.WRAP );
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 2,1);
        toolBar.setLayoutData(gd);
        
        GridLayout gl=new GridLayout(8,true);
        toolBar.setLayout(gl);
    }
    /*
     * Makes a new infobar and adds it to the shell
     */
    private void doInfobar(){
        infoBar = new InfoBar(shell,SWT.BORDER);
    }
    
    /*
     * Makes a new composite in the shell where the different panels fit in.
     * Sets the layout to a stacklayout
     */ 
    private void doComposite(){
        parent = new Composite(shell,SWT.BORDER);
        layout = new StackLayout();
        parent.setLayout(layout);
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true, 1,1));
    }

    
    /*
     * Sets the position of the mainscreen in the middle of the display.
     * Sets the layout of the mainscreen in a gridlayout with 2 koloms. 
     */
    private void doLayout() {
            shell.setMinimumSize(1000, 700);
            Monitor primary = display.getPrimaryMonitor();
            Rectangle bounds = primary.getBounds();
            Rectangle rect = shell.getBounds();
            int x = (bounds.x+(bounds.width-rect.width)/2);
            int y = (bounds.y+(bounds.height-rect.height)/2);
            GridLayout gl = new GridLayout(1,false);
            //shell.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,2));
            gl.marginHeight = gl.marginWidth = gl.verticalSpacing = 0;
            shell.setLocation(x, y);
            shell.setLayout(gl);
            shell.setText(ClientConfigurationController.getInstance().getString("ikshare"));

            /*String icon = "resources/logo.png";
            File file = new File(icon);
            if(file.exists())
                    shell.setImage(new Image(display, icon));*/
    }

    
    /*
     * Adds the different panels to the composite, sets the topcontrol on the first panel that is been added.
     */
    private void addPanel(final AbstractPanel p) {
           panels.add(p);
           p.getToolItem().addListener(SWT.Selection, new Listener() {
                    public void handleEvent(Event e) {
                            for (AbstractPanel pnl : panels)
                                    pnl.getToolItem().setSelection(false);

                            layout.topControl = p;
                            p.getToolItem().setSelection(true);
                            parent.layout();
                            p.initialiseFocus();
                    }
            });
    }

    public Shell getShell() {
            return shell;
    }

    public ToolBar getToolBar() {
            return toolBar;
    }
    
    public Composite getParent() {
            return parent;
    }
    
    public Display getDisplay() {
    	return display;
    }
}
