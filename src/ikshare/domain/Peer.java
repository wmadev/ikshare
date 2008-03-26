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

	private String accountName;
	private InetAddress internetAddress;
	
	public Peer() {
		
	}
	
	public Peer(String name, InetAddress internetAddress) {
		this.accountName = name;
		this.internetAddress = internetAddress;
	}
	
	public InetAddress getInternetAddress() {
		return internetAddress;
	}
	public void setInternetAddress(InetAddress internetAddress) {
		this.internetAddress = internetAddress;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String name) {
		this.accountName = name;
	}
	
	
}
