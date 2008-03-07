package ikshare.test;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jonas
 */
public class PeerSenderTest {
    public static void main (String args[]) {
        try {
            while (true) {
                ServerSocket ss = new ServerSocket(6001);
                System.out.println("waiting");
                ExecutorService service = Executors.newFixedThreadPool(1);
                service.execute(new FileSendingService(ss.accept()));
            }
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }

}
