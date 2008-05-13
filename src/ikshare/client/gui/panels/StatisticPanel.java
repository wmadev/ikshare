package ikshare.client.gui.panels;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import ikshare.client.StatisticsController;
import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.UtilityClass;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;

public class StatisticPanel extends AbstractPanel{
	private final StatisticPanel instance;
	
    public StatisticPanel(String text,String icon){
        super(text,icon);
        instance = this;
		addPaintListener(new PaintListener(){
			private int width=900;
			private int height=220;


			public void paintControl(final PaintEvent event) {
				drawSpeeds(event, true);
				drawSpeeds(event, false);	
			}

			private void drawSpeeds(final PaintEvent event, boolean downloads) {
				int startPosY=0;
				int startPosX=0;
				
				LinkedBlockingQueue<Integer> speeds = null;
				
				try {
					if (downloads) {
						speeds = StatisticsController.getInstance().getLastSpeedDown();
						startPosY=40;
						startPosX=40;
					}					
					else {
						speeds = StatisticsController.getInstance().getLastSpeedUp();
						startPosY=300;
						startPosX=40;
					}
				} catch (InterruptedException e) {
				}
				drawBox(startPosX, startPosY, event);
				drawAllSpeeds(startPosX, startPosY, event, speeds, downloads);
			}

			private void drawAllSpeeds(int startPosX, int startPosY, PaintEvent event, LinkedBlockingQueue<Integer> speeds, boolean downloads) {
				int max=0;
				int min=0;
				if (downloads) {
					max = StatisticsController.getInstance().getMaximumDownloadSpeed();
					min = StatisticsController.getInstance().getMinimumDownloadSpeed();
				} else {
					max = StatisticsController.getInstance().getMaximumUploadSpeed();
					min = StatisticsController.getInstance().getMinimumUploadSpeed();
				}
				
				int diff = max-min;
				int part = Math.max(diff/(height-10),1);
				long total = 0;
				int avg=0;
				
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));
				int sec=0;
				for (Integer current:speeds) {
					event.gc.setLineStyle(SWT.LINE_DOT);
					event.gc.drawLine(startPosX+sec*15, startPosY + height - 11, startPosX+sec*15, startPosY + 11);
					event.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
					Rectangle r = new Rectangle(startPosX+sec*15, startPosY + height - 11, 15, Math.max(-(current/part), -199));
					event.gc.setLineStyle(SWT.LINE_SOLID);
					event.gc.fillRectangle(r);
					event.gc.drawRectangle(r);
					total+=current;
					sec++;
				}
				avg = (int) (total/(long)60);
				drawMaxLine(startPosX, startPosY, event, max);
				drawMinLine(startPosX, startPosY, event, min);
				drawAvgLine(startPosX, startPosY, event, avg, part);
			}

			private void drawMaxLine(int startPosX, int startPosY, PaintEvent event, int max) {
				event.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
				event.gc.drawLine(startPosX, startPosY + 10, startPosX+width, startPosY + 10);
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
				event.gc.drawText("max: " + UtilityClass.formatFileSize((long)max), startPosX + 10, startPosY + 12, true);
			}

			private void drawMinLine(int startPosX, int startPosY, PaintEvent event, int min) {
				event.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
				event.gc.drawLine(startPosX, startPosY + height - 10, startPosX+width, startPosY + height - 10);
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
				event.gc.drawText("min: " + UtilityClass.formatFileSize((long)min), startPosX + 10, startPosY + height - 11, true);
			}

			private void drawAvgLine(int startPosX, int startPosY, PaintEvent event, int avg, int part) {
				event.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_CYAN));
				event.gc.drawLine(startPosX, startPosY + height - 11 + Math.max(-(avg/part), -199), startPosX+width, startPosY + height - 11 + Math.max(-(avg/part), -199));	
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
				event.gc.drawText("avg: " + UtilityClass.formatFileSize((long)avg), startPosX + 10, startPosY + height - 11 + Math.max(-(avg/part),-199), true);
				;
			}

			private void drawBox(int startPosX, int startPosY, final PaintEvent event) {
				Rectangle rect2= new Rectangle(startPosX+2,startPosY+2, width,height);
				event.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
				event.gc.fillRectangle(rect2);
				
				Rectangle rect3= new Rectangle(startPosX+1,startPosY+1, width,height);
				event.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
				event.gc.fillRectangle(rect3);
				
				Rectangle rect= new Rectangle(startPosX,startPosY, width,height);
				event.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
				event.gc.fillRectangle(rect);
			}
		});
        init();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable(){

			public void run() {
				boolean running=true;
				while (running) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        refresh();
				}
			}
        	
        });
    }


	private void refresh() {
		getDisplay().asyncExec(new Runnable(){
			public void run() {
					instance.redraw();
			}
			
		});
	}


	private void init() {
		this.setLayout(new RowLayout());
		
		this.redraw();
	}

    @Override
    public void initialiseFocus() {

    }

}
