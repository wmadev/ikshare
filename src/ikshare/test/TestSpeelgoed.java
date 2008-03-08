/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.test;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author awosy
 */
public class TestSpeelgoed {
     public static void main (String args[]) {
            ExecutorService service = Executors.newFixedThreadPool(1);
            service.execute(new ThreadSpeelgoed());
            
    }
}
