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
	private String email;
        private String password;
        
	public Peer() {
		
	}
	
	public Peer(String accountName, InetAddress internetAddress) {
		this(accountName);
		this.internetAddress = internetAddress;
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
	
	public Peer(String accountName) {
		this.accountName = accountName;
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
