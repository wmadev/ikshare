package ikshare.domain.event.listener;

import java.io.File;

public interface SelectedMediaFileListener {
	public void selectedMP3FileChanged(File mp3file);
	public void selectedMPEGFileChanged(File mpegFile);
}
