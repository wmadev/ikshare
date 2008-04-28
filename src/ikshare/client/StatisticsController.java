package ikshare.client;

import java.util.concurrent.LinkedBlockingQueue;

public class StatisticsController {
	private static StatisticsController instance;
	private LinkedBlockingQueue<Integer> lastSpeedUp, lastSpeedDown;
	private Integer maximum, minimum;
	
	public Integer getMaximum() {
		return maximum;
	}
	public Integer getMinimum() {
		return minimum;
	}

	protected StatisticsController(){
		lastSpeedDown = new LinkedBlockingQueue<Integer>(60);
		lastSpeedUp = new LinkedBlockingQueue<Integer>(60);
		for (int i=0; i<60; i++) {
			lastSpeedDown.offer(0);
			lastSpeedUp.offer(0);
		}
		maximum = Integer.MIN_VALUE;
		minimum = Integer.MAX_VALUE;
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
		int last = (int)(Math.random()*15000);
		minimum = (int)Math.min(minimum, last);
		maximum = (int)Math.max(maximum, last);
		while (!lastSpeedDown.offer(last)){
			lastSpeedDown.take();
		}
	}
	
	private void initLastSpeedUp() throws InterruptedException {
		int last = (int)(Math.random()*15000);
		minimum = (int)Math.min(minimum, last);
		maximum = (int)Math.max(maximum, last);
		while (!lastSpeedUp.offer(last)){
			lastSpeedUp.take();
		}
	}
}
