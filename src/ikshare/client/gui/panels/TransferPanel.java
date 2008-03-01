/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

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
        TabItem downloadTab = new TabItem(folder,SWT.NONE);
        downloadTab.setText("Downloads");
        if(new File(ICON_DOWN).exists()){
            Image icon = new Image(Display.getCurrent(), ICON_DOWN);
            downloadTab.setImage(icon);
        }
        TabItem uploadTab = new TabItem(folder,SWT.NONE);
        if(new File(ICON_UP).exists()){
            Image icon = new Image(Display.getCurrent(), ICON_UP);
            uploadTab.setImage(icon);
        }
        
        uploadTab.setText("Uploads");
    }
    
    
}
