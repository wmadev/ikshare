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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;

public class MP3Player extends Composite implements SelectedMediaFileListener {
    private Composite parent;//, cmpPlayer;
    private Label lblCurrentMP3;
    //private Button btnPlay, btnStop;
    private static String ICON_PLAY = "resources/icons/player_start.png";
    private static String ICON_STOP = "resources/icons/player_stop.png";
    
    public MP3Player(Composite parent,int flags){
    	super(parent, flags);
        this.parent = parent;
        EventController.getInstance().addSelectedMediaFileListener(this);
        init();
    }
    
    private void init() {
        Composite cmpPlayer = new Composite(parent,SWT.RIGHT);
        cmpPlayer.setLayout(new GridLayout(3,false));
        Button btnPlay = new Button(cmpPlayer, SWT.NONE | SWT.TRANSPARENCY_MASK);
        btnPlay.setLayoutData(new GridData(SWT.NONE,SWT.NONE,false,false,1,1));
        if(new File(ICON_PLAY).exists()){
            btnPlay.setImage(new Image(Display.getCurrent(), ICON_PLAY));
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
        //btnPlay.setBounds(0, 0, 20, 20);
        Button btnStop = new Button(cmpPlayer, SWT.NONE);
	if(new File(ICON_STOP).exists()){
            btnStop.setImage(new Image(Display.getCurrent(), ICON_STOP));
        }
        btnStop.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
        btnStop.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                PlayerController.getInstance().stop();
            }
        });
        lblCurrentMP3 = new Label(cmpPlayer, SWT.NONE);
        lblCurrentMP3.setText("No mp3 file selected");
        lblCurrentMP3.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
	}

	public void selectedMP3FileChanged(File mp3file) {
		System.out.println(mp3file.getName());
		lblCurrentMP3.setText(mp3file.getName());
	}

	public void selectedMPEGFileChanged(File mpegFile) {
		// TODO Auto-generated method stub
		
	}
	
}
