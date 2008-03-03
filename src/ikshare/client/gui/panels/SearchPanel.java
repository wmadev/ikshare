/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 *
 * @author Jana
 */
public class SearchPanel extends AbstractPanel{
    public SearchPanel(String text,String icon){
        super(text,icon);
        GridLayout gl = new GridLayout(2,false);
        gl.numColumns = 1;
        this.setLayout(gl);
        Group options = new Group(this, SWT.BORDER);
        options.setData(new GridData(SWT.FILL, SWT.FILL, true,true, 1,1));
        Composite results = new Composite(this, SWT.BORDER);
        results.setData(new GridData(SWT.FILL, SWT.FILL, true,true, 1,1));
        TabFolder folder=new TabFolder(results, SWT.NONE);
        
        //Eerste resultaat
        TabItem result1 = new TabItem(folder,SWT.NONE);
    }

}
