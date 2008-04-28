/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import ikshare.client.StatisticsController;
import ikshare.client.gui.AbstractPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;

/**
 *
 * @author Jana
 */
public class StatisticPanel extends AbstractPanel{
	private final StatisticPanel instance;
	
    public StatisticPanel(String text,String icon){
        super(text,icon);
        instance = this;
		addPaintListener(new PaintListener(){
			private int width=900;
			private int height=210;


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
				drawAllSpeeds(startPosX, startPosY, event, speeds);
			}

			private void drawAllSpeeds(int startPosX, int startPosY, PaintEvent event, LinkedBlockingQueue<Integer> speeds) {
				int max = StatisticsController.getInstance().getMaximum();
				int min = StatisticsController.getInstance().getMinimum();
				int diff = max-min;
				int part = Math.max(diff/(height-10),1);
				long total = 0;
				int avg=0;
				
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));
				int sec=0;
				for (Integer current:speeds) {
					event.gc.drawLine(startPosX+sec*15, startPosY + height - 6, startPosX+sec*15, startPosY + 6);
					event.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
					Rectangle r = new Rectangle(startPosX+1+sec*15, startPosY + height - 6, 14, Math.max(-(current/part), -199));
					event.gc.fillRectangle(r);
					total+=current;
					sec++;
				}
				avg = (int) (total/(long)60);
				drawMaxLine(startPosX, startPosY, event);
				drawMinLine(startPosX, startPosY, event);
				drawAvgLine(startPosX, startPosY, event, avg, part);
			}

			private void drawMaxLine(int startPosX, int startPosY, PaintEvent event) {
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
				event.gc.drawLine(startPosX, startPosY + 5, startPosX+width, startPosY + 5);
			}

			private void drawMinLine(int startPosX, int startPosY, PaintEvent event) {
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
				event.gc.drawLine(startPosX, startPosY + height - 5, startPosX+width, startPosY + height - 5);
			}

			private void drawAvgLine(int startPosX, int startPosY, PaintEvent event, int avg, int part) {
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_CYAN));
				event.gc.drawLine(startPosX, startPosY + height - 6 + Math.max(-(avg/part), -199), startPosX+width, startPosY + height - 6 + Math.max(-(avg/part), -199));	
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

}
