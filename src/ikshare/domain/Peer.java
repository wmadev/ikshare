/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain;


import java.net.InetAddress;

/**
 * 
 * @author awosy
 */
public class Peer {

	private String accountName;
	private InetAddress internetAddress;
	private String email;
        private String password;
        private int port;
        
	public Peer() {
		
	}
	
	public Peer(String name, InetAddress internetAddress) {
		this.accountName = name;
		this.internetAddress = internetAddress;
	}

    public Peer(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;
    }

    public Peer(String accountName, InetAddress ip, int port) {
        setAccountName(accountName);
        setInternetAddress(ip);
        setPort(port);
    }

    public Peer(String accountName, String password, String email) {
        this.accountName = accountName;
        this.password = password;
        this.email = email;
    }

    public Peer(String accountName, String password, InetAddress inetAddress, int port) {
        this.accountName = accountName;
        this.password = password;
        this.internetAddress = inetAddress;
        this.port = port;
    }

    public Peer(String accountName) {
        this.accountName = accountName;
    }
    
    public int getPort(){
        return port;
    }
    public void setPort(int port){
        this.port= port;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
