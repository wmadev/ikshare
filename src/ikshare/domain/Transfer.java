/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain;

import java.io.File;

/**
 *
 * @author awosy
 */
public class Transfer {
    private String id;
    private File file;
    private long fileSize;
    private int state;
    private int numberOfBlocksFinished;
    private int numberOfBlocks;
    private long speed;
    private long remainingTime;
    private Peer peer;
    
    public Transfer(){
        
    }


	public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }

    public void setNumberOfBlocks(int numberOfBlocks) {
        this.numberOfBlocks = numberOfBlocks;
    }

    public int getNumberOfBlocksFinished() {
        return numberOfBlocksFinished;
    }

    public void setNumberOfBlocksFinished(int numberOfBlocksFinished) {
        this.numberOfBlocksFinished = numberOfBlocksFinished;
    }
   
    
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Peer getPeer() {
        return peer;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
