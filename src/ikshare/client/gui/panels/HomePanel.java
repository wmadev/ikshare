/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.configuration.Configuration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


public class HomePanel extends AbstractPanel {
    public HomePanel(String text,String icon){
        super(text,icon);
        GridLayout gd=new GridLayout(2,false);
        this.setLayout(gd);
        init();
        
    }

    private void init() {
        Button btnConnect=new Button(this, SWT.NONE);
        btnConnect.setText("Connect");
        
    }

}
