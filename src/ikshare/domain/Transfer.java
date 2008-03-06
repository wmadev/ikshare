/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain;

/**
 *
 * @author awosy
 */
public class Transfer {
    private String fileName;
    private long fileSize;
    private int state;
    private int numberOfBlocksFinished;
    private int numberOfBlocks;
    private long speed;
    private Peer peer;
    
    public Transfer(){
        
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
   
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
