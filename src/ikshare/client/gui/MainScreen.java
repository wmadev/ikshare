package ikshare.client.gui;

import ikshare.client.gui.panels.HomePanel;
import java.util.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 *
 * @author WardMaenhout
 */
public class MainScreen {
    private Display display;
    private Shell shell;
    private java.util.List<AbstractPanel> panels;
    private Menu menuBar;
    private ToolBar toolBar;
    private InfoBar infoBar;
    private Composite parent;
    private StackLayout layout;
    private static MainScreen instance;

    private boolean isOpen = false;

    public static MainScreen getInstance()
    {
            if(instance==null)
                    instance = new MainScreen();
            return instance;
    }

    public MainScreen() {
        instance = this;
        display = new Display();
        shell = new Shell(display);
        panels = new ArrayList<AbstractPanel>();
        // Add menubar to the shell
        doMenu();
        // Make layout of the shell
        doLayout();
        // Add toolbar to the shell
        doToolbar();
        // Add infobar to the shell
        doInfobar();
        // Add composite to shell
        doComposite();

        addPanel(new HomePanel("Home",""));
       

            shell.open();
            shell.forceActive();
            isOpen=true;
            while (!shell.isDisposed()) {
                    if (!display.readAndDispatch()) {

                            display.sleep();
                    }
            }
            display.dispose();
            System.exit(0);
    }

    public boolean isOpen() {
            return isOpen;
    }
    private void doMenu() {
        MenuBar b = new MenuBar(shell,SWT.BAR);
        shell.setMenuBar(b.getMenu());
    }
    
    private void doToolbar() {
        toolBar = new ToolBar(shell, SWT.WRAP );
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 2,1);
        toolBar.setLayoutData(gd);
    }

    private void doInfobar(){
        infoBar = new InfoBar(shell,SWT.BORDER);
    }

    private void doComposite(){
        parent = new Composite(shell,SWT.BORDER);
        layout = new StackLayout();
        parent.setLayout(layout);
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true, 1,1));
    }

    
    
    private void doLayout() {
            shell.setMinimumSize(800, 600);
            Monitor primary = display.getPrimaryMonitor();
            Rectangle bounds = primary.getBounds();
            Rectangle rect = shell.getBounds();
            int x = bounds.x+(bounds.width-rect.width)/2;
            int y = (bounds.y+(bounds.height-rect.height)/2)-25;
            GridLayout gl = new GridLayout(2,false);
            gl.numColumns = 2;
            //shell.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,2));
            gl.marginHeight = gl.marginWidth = gl.verticalSpacing = 0;
            shell.setLocation(x, y);
            shell.setLayout(gl);
            shell.setText("iKshare");

            /*String icon = "resources/logo.png";
            File file = new File(icon);
            if(file.exists())
                    shell.setImage(new Image(display, icon));*/
    }

    private void addPanel(final AbstractPanel p) {
            panels.add(p);
            if(panels.size()==1) {
                    layout.topControl = p;
                    p.getToolItem().setSelection(true);
            }
            p.getToolItem().addListener(SWT.Selection, new Listener() {
                    public void handleEvent(Event event) {
                            for (AbstractPanel pnl : panels)
                                    pnl.getToolItem().setSelection(false);

                            layout.topControl = p;
                            p.getToolItem().setSelection(true);
                            parent.layout();
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
}
