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
    private String id;
    private IKShareFile file;
    private int state;
    private long numberOfBlocksFinished, numberOfBlocks, speed, remainingTime, fileSize, blockSize;
    private Peer peer;
    
    public Transfer(){
        
    }
    
	public long getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(long blockSize) {
		this.blockSize = blockSize;
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
    
    public long getNumberOfBlocks() {
        return numberOfBlocks;
    }

    public void setNumberOfBlocks(long numberOfBlocks) {
        this.numberOfBlocks = numberOfBlocks;
    }

    public long getNumberOfBlocksFinished() {
        return numberOfBlocksFinished;
    }

    public void setNumberOfBlocksFinished(long numberOfBlocksFinished) {
        this.numberOfBlocksFinished = numberOfBlocksFinished;
    }
   
    
    public IKShareFile getFile() {
        return file;
    }

    public void setFile(IKShareFile file) {
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
    
    public boolean equals (Object transfer) {
		return ((Transfer)transfer).getId().equals(this.getId());	
    }    
}
