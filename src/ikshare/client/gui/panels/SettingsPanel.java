/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.configuration.ClientConfiguration;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;
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
    private static String ICON_GENERAL="resources/icons/pref_gen.png";
    private static String ICON_NETWORK="resources/icons/pref_net.png";
    private ClientConfiguration config;
    
    
    private Text txtnick,txtSharedFolder,txtServerAddress,txtServerPort,txtFileTransferPort;
    private Combo cblanguages;
    private DateTime dt;
    
    public SettingsPanel(String text,String icon){
        super(text,icon);
        config = ClientConfigurationController.getInstance().getConfiguration();
        this.setLayout(new GridLayout(1,false));
        this.init();
    }

    private void init() {
        TabFolder folder=new TabFolder(this, SWT.NONE);
        folder.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
        makeGeneralFolder(folder);
        makeNetworkFolder(folder);
        Button btnSave = new Button(this, SWT.NONE);
        btnSave.setLayoutData(new GridData(SWT.LEFT,SWT.FILL,false,false,1,1));
        btnSave.setText(ClientConfigurationController.getInstance().getString("save"));
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
                config.setIkshareServerAddress(txtServerAddress.getText());
                try{
                    config.setIkshareServerPort(Integer.parseInt(txtServerPort.getText()));
                }catch(NumberFormatException e){
                    config.setIkshareServerPort(6000);
                }
                try{
                    config.setFileTransferPort(Integer.parseInt(txtFileTransferPort.getText()));
                }catch(NumberFormatException e){
                    config.setFileTransferPort(6666);
                }
                ClientConfigurationController.getInstance().saveConfiguration(config);
                EventController.getInstance().triggerConfigurationUpdatedEvent(config);
            }
        });
    }

    private void makeGeneralFolder(TabFolder folder) {
        TabItem generalTab = new TabItem(folder,SWT.BORDER);
        generalTab.setText(ClientConfigurationController.getInstance().getString("general"));
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
        lblnick.setText(ClientConfigurationController.getInstance().getString("nickname"));
        
        lblnick.setLayoutData(firstColum);
        txtnick=new Text(general, SWT.BORDER);
        txtnick.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
        txtnick.setText(config.getNickname());
        
        Label lbllanguage=new Label(general, SWT.FILL);
        lbllanguage.setText(ClientConfigurationController.getInstance().getString("language"));
        lbllanguage.setLayoutData(firstColum);
        
        cblanguages = new Combo(general,SWT.DROP_DOWN | SWT.READ_ONLY);
        cblanguages.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 2, 1));
        // Talen uitlezen uit xml?
        cblanguages.setItems(new String[] {"(en) English","(nl) Nederlands"});
        for(int i = 0 ; i < cblanguages.getItemCount();i++)
           if(cblanguages.getItem(i).substring(1, 3).equals(ClientConfigurationController.getInstance().getConfiguration().getLanguage())){
                        cblanguages.select(i);
           }

       
        
        Label lblbirthdate=new Label(general, SWT.FILL);
        lblbirthdate.setText(ClientConfigurationController.getInstance().getString("birthday"));
        
        lblbirthdate.setLayoutData(firstColum);
        dt=new DateTime(general, SWT.CALENDAR);
        dt.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 2, 1));
        dt.setDay(config.getBirthday().get(Calendar.DAY_OF_MONTH));
        dt.setYear(config.getBirthday().get(Calendar.YEAR));
        dt.setMonth(config.getBirthday().get(Calendar.MONTH));
       
        Label lblSharedFiles=new Label(general,SWT.FILL);
        lblSharedFiles.setText(ClientConfigurationController.getInstance().getString("sharedfolder"));
        
        lblSharedFiles.setLayoutData(firstColum);
        
        txtSharedFolder = new Text(general,SWT.MULTI);
        txtSharedFolder.setText(config.getSharedFolder().getPath());
        txtSharedFolder.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false,1,1));

        Button btnAddFile = new Button(general,SWT.NONE);
	btnAddFile.setLayoutData(new GridData(SWT.RIGHT,SWT.TOP,false,false,1,1));
	btnAddFile.setText(ClientConfigurationController.getInstance().getString("change"));
	btnAddFile.addListener(SWT.Selection,new Listener(){
            public void handleEvent(Event event) {
		DirectoryDialog dialog = new DirectoryDialog(general.getShell(),SWT.OPEN);
                String selectedDir = dialog.open();
                         
		if(selectedDir != null && !selectedDir.equalsIgnoreCase("") && new File(selectedDir).exists()&& new File(selectedDir).isDirectory())
                    {
                        txtSharedFolder.setText(selectedDir);
                    }
		
            }
	});
    }
    private void makeNetworkFolder(TabFolder folder) {
        TabItem networkTab = new TabItem(folder,SWT.BORDER);
        networkTab.setText(ClientConfigurationController.getInstance().getString("network"));
        if(new File(ICON_NETWORK).exists()){
            networkTab.setImage(new Image(Display.getCurrent(), ICON_NETWORK));
        }
        GridData firstColum = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        firstColum.widthHint = 100;
        firstColum.heightHint = 25;
        final Composite network=new Composite(folder,SWT.NONE);
        networkTab.setControl(network);
        network.setLayout(new GridLayout(1,false));

        Label lblServerAddress=new Label(network, SWT.FILL);
        lblServerAddress.setText(ClientConfigurationController.getInstance().getString("serveraddress"));
        lblServerAddress.setLayoutData(firstColum);
        txtServerAddress=new Text(network, SWT.BORDER);
        txtServerAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        txtServerAddress.setText(config.getIkshareServerAddress());

        Label lblServerPort=new Label(network, SWT.FILL);
        lblServerPort.setText(ClientConfigurationController.getInstance().getString("serverport"));
        lblServerPort.setLayoutData(firstColum);
        txtServerPort=new Text(network, SWT.BORDER);
        txtServerPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        txtServerPort.setText(String.valueOf(config.getIkshareServerPort()));

        Label lblTransferPort=new Label(network, SWT.FILL);
        lblTransferPort.setText(ClientConfigurationController.getInstance().getString("transferport"));
        lblTransferPort.setLayoutData(firstColum);
        txtFileTransferPort=new Text(network, SWT.BORDER);
        txtFileTransferPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        txtFileTransferPort.setText(String.valueOf(config.getFileTransferPort()));
        
     }
}
