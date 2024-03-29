package ikshare.domain;

import java.util.Date;

public class Transfer {
    private String id;
    private Date startTransfer;
    private IKShareFile file;
    private TransferState state;
    private long numberOfBytesFinished, speed, remainingTime, fileSize, blockSize;
    private Peer peer;
    private String downloadLocation = "";
    
    
    public int getProgress() {
    	return (int)Math.round((double)numberOfBytesFinished/(double)fileSize * 100);
    }
    
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

    public long getNumberOfBytesFinished() {
        return numberOfBytesFinished;
    }

    public void setNumberOfBytesFinished(long numberOfBlocksFinished) {
        this.numberOfBytesFinished = numberOfBlocksFinished;
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

    public TransferState getState() {
        return state;
    }

    public void setState(TransferState state) {
        this.state = state;
    }
    
    public boolean equals (Object transfer) {
		return ((Transfer)transfer).getId().equals(this.getId());	
    }

	public Date getStartTransfer() {
		return startTransfer;
	}

	public void setStartTransfer(Date startTransfer) {
		this.startTransfer = startTransfer;
	}

	public String getDownloadLocation() {
		return downloadLocation;
	}

	public void setDownloadLocation(String downloadLocation) {
		this.downloadLocation = downloadLocation;
	}
    
    
}
