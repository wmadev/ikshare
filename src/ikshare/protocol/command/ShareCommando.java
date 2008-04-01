/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.protocol.command;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author awosy
 */
public class ShareCommando extends Commando {

    private String accountName;
    private String shares;
    
    public ShareCommando() {
        super();
    }

    public ShareCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setShares(commandoLine.get(2));
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }
    
    
    

    
    
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

   
    
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("share")+del+getAccountName()+del+getShares();
    }
}