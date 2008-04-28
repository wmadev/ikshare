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
			private int startPosY=40,startPosX=40;

			public void paintControl(final PaintEvent event) {
				LinkedBlockingQueue<Integer> speeds = null;
				int max = StatisticsController.getInstance().getMaximum();
				int min = StatisticsController.getInstance().getMinimum();
				int diff = max-min;
				int part = Math.max(diff/(height-10),1);
				long total = 0;
				int avg=0;
				try {
					speeds = StatisticsController.getInstance().getLastSpeedDown();
				} catch (InterruptedException e) {
				}
				
				Rectangle rect= new Rectangle(startPosX,startPosY, width,height);
				event.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
				event.gc.fillRectangle(rect);
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
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
				event.gc.drawLine(startPosX, startPosY + height - 5, startPosX+width, startPosY + height - 5);
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
				event.gc.drawLine(startPosX, startPosY + 5, startPosX+width, startPosY + 5);
				event.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_CYAN));
				event.gc.drawLine(startPosX, startPosY + height - 6 + Math.max(-(avg/part), -199), startPosX+width, startPosY + height - 6 + Math.max(-(avg/part), -199));
					
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
