/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ikshare.test;

import ikshare.domain.ResourceBundleManager;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

/**
 *
 * @author jonas
 */
public class FileSendingService implements Runnable {

    private Socket sendSocket;
    private byte[] buffer;
    private File sendFile;

    public FileSendingService(Socket receiveSocket) {
        sendSocket = receiveSocket;

        buffer = new byte[Integer.parseInt(ResourceBundleManager.getInstance().getCommandoBundle().getString("buffersize"))];

    }

    public void run() {
        try {
            // timeout zetten en streams aanmaken
            sendSocket.setSoTimeout(5000);
            BufferedOutputStream outStream = new BufferedOutputStream(sendSocket.getOutputStream());
            
            sendFile = new File("/s01e01 - The Pants Tent.avi");

            FileInputStream fileInput = new FileInputStream(sendFile.getAbsolutePath());

            int sentBytes = 0;

            while ((sentBytes = fileInput.read(buffer)) > 0) {
                outStream.write(buffer, 0, sentBytes);
            }
            outStream.close();
            fileInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
