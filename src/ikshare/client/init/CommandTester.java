/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.init;

import ikshare.protocol.command.Commando;
import ikshare.protocol.command.CommandoParser;
import ikshare.protocol.command.FileNotFoundCommando;
import ikshare.protocol.command.FoundCommando;
import ikshare.protocol.exception.CommandNotFoundException;

/**
 *
 * @author jonas
 */
public class CommandTester {
    public static void main (String args[]) {
        try {
            FileNotFoundCommando o;

            o = new FileNotFoundCommando();
            o.setAccountName("jonas");
            o.setFileName("film.avi");
            o.setPath("/users/jonas/download");

            System.out.println(o);

            Commando fc = CommandoParser.getInstance().parse("{120} FOUND$20080302-0001$film.avi$/users/jonas/download/$scenario=Cé Nario$regisseur=Reginald Isseur");

            System.out.println(((FoundCommando)fc).getFileName());
            System.out.println(((FoundCommando)fc).getPath());
            System.out.println(((FoundCommando)fc).getSearchID());
            System.out.println(((FoundCommando)fc).getMetaData());
            
        } catch (CommandNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
