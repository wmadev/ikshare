/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.configuration.Configuration;
import ikshare.client.gui.configuration.ConfigurationController;
import java.io.File;
import java.util.Calendar;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 *
 * @author Jana
 */
public class SettingsPanel extends AbstractPanel{
    private static String ICON_GENERAL="resources/icons/pref_user.png";
    private Configuration config;
    
    public SettingsPanel(String text,String icon){
        super(text,icon);
        config = ConfigurationController.getInstance().getConfiguration();
        this.setLayout(new FillLayout(SWT.NONE));
        this.init();
    }

    private void init() {
        TabFolder folder=new TabFolder(this, SWT.NONE);
        makeGeneralFolder(folder);
    }

    private void makeGeneralFolder(TabFolder folder) {
        TabItem generalTab = new TabItem(folder,SWT.BORDER);
        generalTab.setText(ConfigurationController.getInstance().getString("general"));
        if(new File(ICON_GENERAL).exists()){
            generalTab.setImage(new Image(Display.getCurrent(), ICON_GENERAL));
        }
        GridData firstColum = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
        firstColum.widthHint = 100;
        firstColum.heightHint = 25;
        final Composite general=new Composite(folder,SWT.NONE);
        generalTab.setControl(general);
        general.setLayout(new GridLayout(3,false));
        
        Label lblnick=new Label(general, SWT.FILL);
        lblnick.setText(ConfigurationController.getInstance().getString("nickname"));
        
        lblnick.setLayoutData(firstColum);
        final Text txtnick=new Text(general, SWT.BORDER);
        txtnick.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true, 2, 1));
        txtnick.setText(config.getNickname());
        
        Label lbllanguage=new Label(general, SWT.FILL);
        lbllanguage.setText(ConfigurationController.getInstance().getString("language"));
        lbllanguage.setLayoutData(firstColum);
        
        final Combo cblanguages = new Combo(general,SWT.DROP_DOWN | SWT.READ_ONLY);
        cblanguages.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 2, 1));
        // Talen uitlezen uit xml?
        cblanguages.setItems(new String[] {"(en) English","(nl) Nederlands"});
        for(int i = 0 ; i < cblanguages.getItemCount();i++)
           if(cblanguages.getItem(i).substring(1, 3).equals(ConfigurationController.getInstance().getConfiguration().getLanguage())){
                        cblanguages.select(i);
           }

       
        
        Label lblbirthdate=new Label(general, SWT.FILL);
        lblbirthdate.setText(ConfigurationController.getInstance().getString("birthday"));
        
        lblbirthdate.setLayoutData(firstColum);
        final DateTime dt=new DateTime(general, SWT.CALENDAR);
        dt.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 2, 1));
        dt.setDay(config.getBirthday().get(Calendar.DAY_OF_MONTH));
        dt.setYear(config.getBirthday().get(Calendar.YEAR));
        dt.setMonth(config.getBirthday().get(Calendar.MONTH));
       
        Label lblSharedFiles=new Label(general,SWT.FILL);
        lblSharedFiles.setText(ConfigurationController.getInstance().getString("sharedfolder"));
        
        lblSharedFiles.setLayoutData(firstColum);
        
        final Text txtSharedFolder = new Text(general,SWT.NONE);
        txtSharedFolder.setText(config.getSharedFolder().getPath());
        txtSharedFolder.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));
        
        Button btnAddFile = new Button(general,SWT.NONE);
	btnAddFile.setLayoutData(new GridData(SWT.RIGHT,SWT.TOP,false,false,1,1));
	btnAddFile.setText(ConfigurationController.getInstance().getString("change"));
	btnAddFile.addListener(SWT.Selection,new Listener(){
            public void handleEvent(Event event) {
		DirectoryDialog dialog = new DirectoryDialog(general.getShell(),SWT.OPEN);
                String selectedDir = dialog.open();
                         
		if(!selectedDir.equalsIgnoreCase("") && new File(selectedDir).exists()&& new File(selectedDir).isDirectory())
                    {
                        txtSharedFolder.setText(selectedDir);
                    }
		
            }
	});
        
        Button btnSave = new Button(general, SWT.NONE);
        btnSave.setText(ConfigurationController.getInstance().getString("save"));
        btnSave.setLayoutData(firstColum);
        btnSave.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                config.setLanguage(cblanguages.getItem(cblanguages.getSelectionIndex()).substring(1, 3));
                Calendar date = Calendar.getInstance();
                date.set(Calendar.DAY_OF_MONTH, dt.getDay());
                date.set(Calendar.YEAR, dt.getYear());
                date.set(Calendar.MONTH, dt.getMonth());
                config.setBirthday(date);
                config.setNickname(txtnick.getText());
                config.setSharedFolder(new File(txtSharedFolder.getText()));
                ConfigurationController.getInstance().saveConfiguration(config);
            }
        });
        
       
    }
}
