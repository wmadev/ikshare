/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.Configuration;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 *
 * @author Jana
 */
public class SettingsPanel extends AbstractPanel{
    public SettingsPanel(String text,String icon){
        super(text,icon);
        FillLayout fl=new FillLayout(SWT.NONE);
        this.setLayout(fl);
        this.init();
    }

    private void init() {
        TabFolder folder=new TabFolder(this, SWT.NONE);
        makeGeneralFolder(folder);
                     
           }

    private void makeGeneralFolder(TabFolder folder) {
          TabItem generalTab = new TabItem(folder,SWT.BORDER);
        generalTab.setText(Configuration.getInstance().getString("general"));
        Composite general=new Composite(folder,SWT.NONE);
        generalTab.setControl(general);
        GridLayout gd=new GridLayout(2,false);
        general.setLayout(gd);
        Label lblnick=new Label(general, SWT.FILL);
        lblnick.setText("nickname:");
        GridData data=new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        data.widthHint=200;
        lblnick.setLayoutData(data);
        Text txtnick=new Text(general, SWT.BORDER);
        GridData data2=new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
        txtnick.setLayoutData(data2);
        Label lbllanguage=new Label(general, SWT.FILL);
        lbllanguage.setText("language:");
        data=new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        data.widthHint=200;
        lbllanguage.setLayoutData(data);
        final Combo cblanguages = new Combo(general,SWT.DROP_DOWN | SWT.READ_ONLY);
        data2=new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
        cblanguages.setLayoutData(data2);
        cblanguages.setItems(new String[] {"(en) English","(nl) Nederlands"});
        cblanguages.select(0);
        cblanguages.addListener(SWT.Selection,new Listener(){

			public void handleEvent(Event event) {
                            Configuration.getInstance().changeLanguage(cblanguages.getItem(cblanguages.getSelectionIndex()).substring(1, 3));
				
				
				
			}
			
		});
        Label lblbirthdate=new Label(general, SWT.FILL);
        lblbirthdate.setText("birthdate:");
        data=new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        data.widthHint=200;
        lblbirthdate.setLayoutData(data);
        DateTime dt=new DateTime(general, SWT.CALENDAR);
        data2=new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
        dt.setLayoutData(data2);
        Label lblSharedFiles=new Label(general,SWT.FILL);
        lblSharedFiles.setText("Shared Files:");
        data=new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        data.widthHint=200;
        lblSharedFiles.setLayoutData(data);
        FileDialog fd=new FileDialog(general.getShell(), SWT.FILL);
        //fd.open();
        
       
    }

}
