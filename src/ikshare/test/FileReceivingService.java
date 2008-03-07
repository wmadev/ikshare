package ikshare.test;

import ikshare.domain.ResourceBundleManager;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author jonas
 */
public class FileReceivingService implements Runnable {

    private BufferedInputStream inStream;
    private File outputFile;
    private byte[] buffer;
    private Socket receiveSocket;

    public FileReceivingService(InetAddress address) {
        try {
            receiveSocket = new Socket(address, 6001);
            receiveSocket.setSoTimeout(5000);
            buffer = new byte[512];
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void run() {
        try {
            outputFile = new File("/film2.avi");
            FileOutputStream fileOutput = new FileOutputStream(outputFile);

            int n;

            // Zolang er input komt van de socket moet er worden weggeschreven naar het bestand.
            // Om de seconde wordt een event getriggerd met de gemiddelde snelheid en de resterende downloadtijd
            while ((n = inStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, n);
            }
            fileOutput.flush();
            fileOutput.close();
            inStream.close();
            receiveSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
