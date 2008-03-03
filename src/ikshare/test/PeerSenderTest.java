package ikshare.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jonas
 */
public class PeerSenderTest {
    public static void main (String args[]) {
        try {
            ExecutorService service = Executors.newFixedThreadPool(1);
            service.execute(new FileReceivingService(InetAddress.getLocalHost()));
        } catch (UnknownHostException ex) {
           ex.printStackTrace();
        }
    }

}
