/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 *
 * @author Jana
 */
public class TransferPanel extends AbstractPanel{
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
        TabItem uploadTab = new TabItem(folder,SWT.NONE);
        uploadTab.setText("Uploads");
    }
    
    
}
