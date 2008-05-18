package ikshare.client;

import ikshare.client.gui.panels.TransferPanel;
import ikshare.domain.TransferController;
import ikshare.domain.Transfer;
import ikshare.domain.TransferState;

import java.util.concurrent.LinkedBlockingQueue;

public class StatisticsController {
	private static StatisticsController instance;
	private LinkedBlockingQueue<Integer> lastSpeedUp, lastSpeedDown;
	private Integer maximumDownloadSpeed, minimumDownloadSpeed, maximumUploadSpeed, minimumUploadSpeed;
	
	public Integer getMaximumDownloadSpeed() {
		return maximumDownloadSpeed;
	}
	public Integer getMinimumDownloadSpeed() {
		return minimumDownloadSpeed;
	}
	public Integer getMaximumUploadSpeed() {
		return maximumUploadSpeed;
	}
	public Integer getMinimumUploadSpeed() {
		return minimumUploadSpeed;
	}

	protected StatisticsController(){
		lastSpeedDown = new LinkedBlockingQueue<Integer>(60);
		lastSpeedUp = new LinkedBlockingQueue<Integer>(60);
		for (int i=0; i<60; i++) {
			lastSpeedDown.offer(0);
			lastSpeedUp.offer(0);
		}
		maximumDownloadSpeed = Integer.MIN_VALUE;
		minimumDownloadSpeed = Integer.MAX_VALUE;
		maximumUploadSpeed = Integer.MIN_VALUE;
		minimumUploadSpeed = Integer.MAX_VALUE;
	}

	public static StatisticsController getInstance() {
		if (instance == null)
			instance = new StatisticsController();
		return instance;
	}
	
	public LinkedBlockingQueue<Integer> getLastSpeedUp() throws InterruptedException {
		initLastSpeedUp();
		return lastSpeedUp;
	}
	
	public LinkedBlockingQueue<Integer> getLastSpeedDown() throws InterruptedException {
		initLastSpeedDown();
		return lastSpeedDown;
	}

	private void initLastSpeedDown() throws InterruptedException {
		//int last = (int)(Math.random()*15000);
		int last=0;
		for (Transfer transfer : TransferController.getInstance().getDownloadTransfers()) {
			if (transfer.getState()==TransferState.DOWNLOADING)
				last += transfer.getSpeed();
		}
		minimumDownloadSpeed = (int)Math.min(minimumDownloadSpeed, last);
		maximumDownloadSpeed = (int)Math.max(maximumDownloadSpeed, last);
		while (!lastSpeedDown.offer(last)){
			lastSpeedDown.take();
		}
	}
	
	private void initLastSpeedUp() throws InterruptedException {
		int last = 0;
		for (Transfer transfer : TransferController.getInstance().getUploadTransfers()) {
			if (transfer.getState()==TransferState.UPLOADING)
				last += transfer.getSpeed();
		}
		minimumDownloadSpeed = (int)Math.min(minimumDownloadSpeed, last);
		maximumDownloadSpeed = (int)Math.max(maximumDownloadSpeed, last);
		while (!lastSpeedUp.offer(last)){
			lastSpeedUp.take();
		}
	}
}
