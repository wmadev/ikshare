/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.protocol.command;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author awosy
 */
public class DownloadInformationResponseCommand extends Commando{
    private String ip;
    private int port;
    private String accountName;
    private String name;
    private String path;
    
    public DownloadInformationResponseCommand() {
        super();
    }

    public DownloadInformationResponseCommand(String commandoString) {
        super(commandoString);
        
            setAccountName(commandoLine.get(1));
            setName(commandoLine.get(2));
            setPath(commandoLine.get(3));
            setIp(commandoLine.get(4));
            setPort(Integer.parseInt(commandoLine.get(5)));
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAccountName() {
    	return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Override
    public String toString() {
        String del = commandoBundle.getString("commandoDelimiter");
	return commandoBundle.getString("downloadinformationresponse") + del + getAccountName()
                +del+getName()+del+getPath()+del+getIp()+del+getPort();
    }
}
