/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.Configuration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 *
 * @author awosy
 */
public class HomePanel extends AbstractPanel {
    public HomePanel(String text,String icon){
        super(text,icon);
        GridLayout gd=new GridLayout(2,false);
        this.setLayout(gd);
        init();
        
    }

    private void init() {
        Label lblnick=new Label(this, SWT.FILL);
        lblnick.setText("nickname:");
        GridData data=new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        data.widthHint=200;
        lblnick.setLayoutData(data);
        Text txtnick=new Text(this, SWT.BORDER);
        GridData data2=new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
        txtnick.setLayoutData(data2);
        Label lbllanguage=new Label(this, SWT.FILL);
        lbllanguage.setText("language:");
        data=new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        data.widthHint=200;
        lbllanguage.setLayoutData(data);
        final Combo cblanguages = new Combo(this,SWT.DROP_DOWN | SWT.READ_ONLY);
        data2=new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
        cblanguages.setLayoutData(data2);
        cblanguages.setItems(new String[] {"(en) English","(nl) Nederlands"});
        cblanguages.select(0);
        cblanguages.addListener(SWT.Selection,new Listener(){

			public void handleEvent(Event event) {
                            Configuration.getInstance().changeLanguage(cblanguages.getItem(cblanguages.getSelectionIndex()).substring(1, 3));
				
				
				
			}
			
		});
        Label lblbirthdate=new Label(this, SWT.FILL);
        lblbirthdate.setText("birthdate:");
        data=new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        data.widthHint=200;
        lblbirthdate.setLayoutData(data);
    }

}
