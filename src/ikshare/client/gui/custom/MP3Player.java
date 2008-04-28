package ikshare.client.gui.custom;

import ikshare.client.PlayerController;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.SelectedMediaFileListener;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;

public class MP3Player extends Composite implements SelectedMediaFileListener {
    private Composite parent, cmpPlayer;
    private Label lblCurrentMP3;
    private Button btnPlay, btnStop;
    private static String ICON_PLAY = "resources/icons/hp_play.png";
    private static String ICON_STOP = "resources/icons/hp_stop.png";
    
    public MP3Player(Composite parent,int flags){
    	super(parent, flags);
        this.parent = parent;
        EventController.getInstance().addSelectedMediaFileListener(this);
        init();
    }

	private void init() {
		// TODO Auto-generated method stub
		RowData rdMP3Name = new RowData(150, 20);
		RowData rdButtons = new RowData(50, 25);
		
        cmpPlayer = parent;
        
        btnPlay = new Button(cmpPlayer, SWT.PUSH);
		if(new File(ICON_PLAY).exists()){
			Image icon = new Image(Display.getCurrent(), ICON_PLAY);
			btnPlay.setImage(icon);
		}
        btnPlay.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				try {
					PlayerController.getInstance().play();
				} catch (Exception e) {
					lblCurrentMP3.setText(ClientConfigurationController.getInstance().getString("noselectedmp3"));
				}
			}
        	
        });
        btnPlay.setLayoutData(rdButtons);
        
        lblCurrentMP3 = new Label(cmpPlayer, SWT.FILL);
        lblCurrentMP3.setLayoutData(rdMP3Name);
        
        btnStop = new Button(cmpPlayer, SWT.NONE);
		if(new File(ICON_STOP).exists()){
			Image icon = new Image(Display.getCurrent(), ICON_STOP);
			btnStop.setImage(icon);
		}
        btnStop.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				PlayerController.getInstance().stop();
			}
        	
        });
        btnStop.setLayoutData(rdButtons);

	}

	public void selectedMP3FileChanged(File mp3file) {
		System.out.println(mp3file.getName());
		lblCurrentMP3.setText(mp3file.getName());
	}

	public void selectedMPEGFileChanged(File mpegFile) {
		// TODO Auto-generated method stub
		
	}
	
}
