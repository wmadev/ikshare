package ikshare.client;

import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.SelectedMediaFileListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import javazoom.jl.player.Player; //TODO

public class PlayerController implements SelectedMediaFileListener{
	private File selectedFile;
	private static PlayerController instance = null;
	private ExecutorService executorService;
	private Thread playerThread;
	private boolean playing=false;
	private Player player; 
	
	protected PlayerController() {
		EventController.getInstance().addSelectedMediaFileListener(this);
	}

	public static PlayerController getInstance() {
		if (instance == null)
			instance = new PlayerController();
		return instance;
	}
	
	public void play() throws Exception{
		startExecutorService();
		playing = true;
	
		if (selectedFile == null)
			throw new Exception("No MP3 selected");
		FileInputStream fis     = new FileInputStream(selectedFile.toString());
        BufferedInputStream bis = new BufferedInputStream(fis);
        player = new Player(bis);
		
		playerThread = new Thread() {
            public void run() {
                try { 
                	while (playing){
                		player.play();
                	}
                }
                catch (final Exception e) { 
                	//TODO exception handling
                	e.printStackTrace(); 
                }
            }
        };
        executorService.execute(playerThread);
		
	}
	
	public void stop() {
		if (player!=null)
			player.close();
		if (playerThread!=null)
			playing = false;
		if (executorService!=null)
			executorService.shutdown();
		executorService = null;
	}



	private void startExecutorService() {
		stop();
		executorService = Executors.newCachedThreadPool();
	}

	public void selectedMP3FileChanged(File mp3file) {
		selectedFile = mp3file;
	}

	public void selectedMPEGFileChanged(File mpegFile) {
		// TODO Auto-generated method stub
		
	}
}
