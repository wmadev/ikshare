/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
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
        /*GridLayout gl = new GridLayout(1,false);
        this.setLayout(gl);*/
        RowLayout rw=new RowLayout(SWT.VERTICAL);
        rw.pack=false;
        rw.wrap=false;
        this.setLayout(rw);
        //SearchOptions
        Group options = new Group(this, SWT.BORDER);
        /*GridData gd=new GridData(SWT.FILL, SWT.FILL, true,true, 1,1);
        gd.heightHint=200;*/
        RowData rd=new RowData();
        options.setData(rd);
        //SearchResults
        Composite results = new Composite(this, SWT.BORDER);
        /*GridData gd2=new GridData(SWT.FILL, SWT.FILL, true,true, 1,1);
        results.setData(gd2);*/
        rd=new RowData();
        results.setData(rd);
        TabFolder folder=new TabFolder(results, SWT.BORDER);
        
        //Eerste resultaat
        TabItem result1 = new TabItem(folder,SWT.BORDER);
        result1.setText("eerste resultaat");
    }

}
