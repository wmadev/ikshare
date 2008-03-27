/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.test;

import ikshare.domain.Peer;
import ikshare.server.data.DatabaseFactory;

/**
 *
 * @author awosy
 */
public class TestOracleConnection {
    public static void main(String[] args){
        Peer newUser = new Peer();
        newUser.setAccountName("awosy");
        newUser.setEmail("ward@awosy.be");
        newUser.setPassword("e=mc**2");
        DatabaseFactory factory = DatabaseFactory.getFactory();
        boolean success = factory.getAccountStorage().createAccount(newUser);
        if(success){
            System.out.println("Account aangemaakt");
        }
        else{
            System.err.println("Account NIET aangemaakt");
        }
    }
}
