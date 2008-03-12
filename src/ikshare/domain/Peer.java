/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain;


import java.io.File;
import java.net.InetAddress;

/**
 * 
 * @author awosy
 */
public class Peer {

	private String name;
	private InetAddress internetAddress;
	private File downloadableFile; //enkel voor testdoeleinden
	
	public File getDownloadableFile() {
		return downloadableFile;
	}
	public void setDownloadableFile(File downloadableFile) {
		this.downloadableFile = downloadableFile;
	}
	public InetAddress getInternetAddress() {
		return internetAddress;
	}
	public void setInternetAddress(InetAddress internetAddress) {
		this.internetAddress = internetAddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
