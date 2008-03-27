package ikshare.domain;

import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;

public class SearchResult {
	private Peer peer;
	private File file;
	private HashMap<String, String> metadata;
	private String id;
	
	public SearchResult(String id, Peer peer, File file) {
		this.id = id;
		this.peer = peer;
		this.file = file;
		metadata = new HashMap<String, String>();
	}

	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public HashMap<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(HashMap<String, String> metadata) {
		this.metadata = metadata;
	}

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
	
	
}
