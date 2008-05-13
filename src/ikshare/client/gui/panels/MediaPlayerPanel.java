package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;

import java.awt.Frame;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.videolan.jvlc.JVLC;
import org.videolan.jvlc.MediaDescriptor;
import org.videolan.jvlc.MediaListPlayer;

/**
 *
 * @author awosy
 */
public class MediaPlayerPanel extends AbstractPanel {

    private MediaListPlayer mlp ;        
    private List lstPlayList;
    private java.awt.Canvas videoCanvas;
    private JVLC jvlc = new JVLC();
    private HashMap<String,String> videos;
    private HashMap<String,MediaDescriptor> mds;
    
    public MediaPlayerPanel(String text,String icon){
        super(text,icon);
        this.setLayout(new GridLayout(1,false));
        videos = new HashMap<String, String>();
        mds = new HashMap<String, MediaDescriptor>();
        mlp = new MediaListPlayer(jvlc);
        
        videos.put("Prison Break (1x22 Flight) XviD-mE.avi", "/home/awosy/ikshare/SharedFolder/Prison Break (Season 1 Complete) XviD-mE/Prison Break (1x22 Flight) XviD-mE.avi");
        videos.put("Prison Break (1x01 Pilot) XviD-mE.avi", "/home/awosy/ikshare/SharedFolder/Prison Break (Season 1 Complete) XviD-mE/Prison Break (1x01 Pilot) XviD-mE.avi");
        
        initVideoPlayList();
        
    }
    private void initVideoPlayList(){
        final Composite embedded = new Composite(this,SWT.EMBEDDED);
        GridData emb = new GridData(SWT.FILL,SWT.FILL,true,true,2,1);
        emb.minimumHeight = emb.minimumWidth = 150;
        embedded.setLayoutData(emb);
        Frame locationFrame = SWT_AWT.new_Frame(embedded);
        videoCanvas = new java.awt.Canvas();
        locationFrame.add(videoCanvas);
        jvlc.setVideoOutput(videoCanvas);
        Group grpVideo = new Group(this,SWT.NONE);
        grpVideo.setLayout(new GridLayout(2,false));
        grpVideo.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
        grpVideo.setText("Video");
        lstPlayList = new List(grpVideo,SWT.BORDER);
        for(String key:videos.keySet()){
            lstPlayList.add(key);
        }
        
        
        Button btnPlay = new Button(this,SWT.NONE);
	btnPlay.setSelection(true);
        btnPlay.setText("Play");
        btnPlay.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                
                MediaDescriptor md = new MediaDescriptor(jvlc, videos.get(lstPlayList.getSelection()[0]));
                jvlc.getMediaList().addMedia(md);
                mds.put(lstPlayList.getSelection()[0], md);
                mlp.setMediaList(jvlc.getMediaList());
                mlp.playItem(md);
                videoCanvas.setVisible(true); 
                //md.getMediaInstance().play();
            }
        });
        Button btnStop = new Button(this,SWT.NONE);
	btnStop.setText("Stop");
        btnStop.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
  	       MediaDescriptor md = mds.get(lstPlayList.getSelection()[0]);
               jvlc.getMediaList().removeMedia(md);
               mlp.stop();
               mlp.setMediaList(jvlc.getMediaList());
               videoCanvas.setVisible(false);
               
               
               System.out.println("GESTOPT ?");
            }
        });    
	       
	       
	   
	
	        
    }

    @Override
    public void initialiseFocus() {
        
    }
}
