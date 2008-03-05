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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 *
 * @author Jana
 */
public class SearchPanel extends AbstractPanel{
    private static String ICON_SEARCH="resources/icons/sp_found.png";
    public SearchPanel(String text,String icon){
        super(text,icon);
        GridLayout gl = new GridLayout(1,false);
        this.setLayout(gl);
        this.init();
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
        result1.setText("Resultaat 1");
    }

}
