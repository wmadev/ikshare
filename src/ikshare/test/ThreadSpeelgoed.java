/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.test;

import ikshare.domain.Transfer;
import ikshare.domain.TransferState;
import ikshare.domain.event.EventController;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author awosy
 */
public class ThreadSpeelgoed implements Runnable{

    public void run() {
        Transfer transfer = new Transfer();
        transfer.setFileName("Dummy thread file");
        transfer.setId(new Date().toString());
        transfer.setFileSize(786432000);
        transfer.setNumberOfBlocksFinished(0);
        transfer.setNumberOfBlocks((int)transfer.getFileSize()/1024);
        transfer.setState(TransferState.DOWNLOADING);
        transfer.setSpeed(307200);
        long doneFileSize = 0;
	long time = System.currentTimeMillis();
	long remainingSize = transfer.getFileSize();
	long totalDone = 0;
	while(transfer.getNumberOfBlocksFinished()<transfer.getNumberOfBlocks()){
            try {
                if(transfer.getNumberOfBlocksFinished()==0){
                    Thread.sleep(10000);
                    System.out.println("START");
                    EventController.getInstance().triggerDownloadStartedEvent(transfer);
                }
                Thread.sleep(1000);
                transfer.setNumberOfBlocksFinished(transfer.getNumberOfBlocksFinished() + 1);
                doneFileSize += 1024;
                totalDone += 1024;
                remainingSize -= 1024;
               
                    time = System.currentTimeMillis();
                    long rtime = ((transfer.getNumberOfBlocks()-transfer.getNumberOfBlocksFinished()) * 1024) / (transfer.getSpeed());
                    transfer.setRemainingTime(rtime);
                    EventController.getInstance().triggerDownloadStateChangedEvent(transfer);
                    doneFileSize = 0;
                  System.out.println(transfer.getFileName() + ":"+transfer.getNumberOfBlocksFinished()+"/"+transfer.getNumberOfBlocks());
               
                
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadSpeelgoed.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
