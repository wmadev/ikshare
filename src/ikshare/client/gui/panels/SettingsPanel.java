/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.configuration.Configuration;
import ikshare.client.gui.configuration.ConfigurationController;
import java.util.Calendar;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 *
 * @author Jana
 */
public class SettingsPanel extends AbstractPanel{
    private Configuration config;
    
    public SettingsPanel(String text,String icon){
        super(text,icon);
        config = ConfigurationController.getInstance().getConfiguration();
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
        generalTab.setText(ConfigurationController.getInstance().getString("general"));
        
        Composite general=new Composite(folder,SWT.NONE);
        generalTab.setControl(general);
        GridLayout gd=new GridLayout(2,false);
        general.setLayout(gd);
        
        Label lblnick=new Label(general, SWT.FILL);
        lblnick.setText("nickname:");
        GridData data=new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        data.widthHint=200;
        lblnick.setLayoutData(data);
        final Text txtnick=new Text(general, SWT.BORDER);
        GridData data2=new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
        txtnick.setLayoutData(data2);
        txtnick.setText(config.getNickname());
        
        Label lbllanguage=new Label(general, SWT.FILL);
        lbllanguage.setText("language:");
        data=new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        data.widthHint=200;
        lbllanguage.setLayoutData(data);
        final Combo cblanguages = new Combo(general,SWT.DROP_DOWN | SWT.READ_ONLY);
        data2=new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
        cblanguages.setLayoutData(data2);
        cblanguages.setItems(new String[] {"(en) English","(nl) Nederlands"});
        for(int i = 0 ; i < cblanguages.getItemCount();i++)
           if(cblanguages.getItem(i).substring(1, 3).equals(ConfigurationController.getInstance().getConfiguration().getLanguage())){
                        cblanguages.select(i);
           }

       
        
        Label lblbirthdate=new Label(general, SWT.FILL);
        lblbirthdate.setText("birthdate:");
        data=new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        data.widthHint=200;
        lblbirthdate.setLayoutData(data);
        final DateTime dt=new DateTime(general, SWT.CALENDAR);
        data2=new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
        dt.setLayoutData(data2);
        dt.setDay(config.getBirthday().get(Calendar.DAY_OF_MONTH));
        dt.setYear(config.getBirthday().get(Calendar.YEAR));
        dt.setMonth(config.getBirthday().get(Calendar.MONTH));
       
        Label lblSharedFiles=new Label(general,SWT.FILL);
        lblSharedFiles.setText("Shared Files:");
        data=new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        data.widthHint=200;
        lblSharedFiles.setLayoutData(data);
        
        Button btnSave = new Button(general, SWT.NONE);
        btnSave.setText("Save");
        btnSave.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                config.setLanguage(cblanguages.getItem(cblanguages.getSelectionIndex()).substring(1, 3));
                Calendar date = Calendar.getInstance();
                date.set(Calendar.DAY_OF_MONTH, dt.getDay());
                date.set(Calendar.YEAR, dt.getYear());
                date.set(Calendar.MONTH, dt.getMonth());
                config.setBirthday(date);
                config.setNickname(txtnick.getText());
                ConfigurationController.getInstance().saveConfiguration(config);
            }
        });
        
       
    }
}
