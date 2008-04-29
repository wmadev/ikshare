
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.MainScreen;
import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
  import org.eclipse.swt.browser.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 *
 * @author Jana
 */
public class HelpPanel extends AbstractPanel{
    

    public HelpPanel(String text,String icon){
        super(text,icon);
        String folder = "resources/help/en";
        final String[] urls;
        final String[] titles;
        final int index;

        File file = new File(folder);
        File[] files = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
		return name.endsWith(".html") || name.endsWith(".htm");
            }
        });
    if (files.length == 0) return;
        urls = new String[files.length];
        titles = new String[files.length];
        index = 0;
        for (int i = 0; i < files.length; i++) {
                try {
                        String url = files[i].toURL().toString();
                        urls[i] = url;
                        titles[i] = files[i].getName();
                } catch (MalformedURLException ex) {}
        }
        
        
        this.setLayout(new GridLayout(2,false));
        
        
        
        final List list = new List(this, SWT.SINGLE);
        GridData gd = new GridData(SWT.LEFT,SWT.FILL,false,true,1,1);
        gd.widthHint = 170;
        list.setLayoutData(gd);
        final Browser browser = new Browser(this, SWT.NONE);
        browser.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
        list.removeAll();
        for (int i = 0; i < titles.length; i++){
            list.add(titles[i].replaceAll(".html", "").substring(3));
        }
        list.select(0);
        browser.setUrl(urls[0]);
       	
	
	list.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event e) {
			int index = list.getSelectionIndex();
			browser.setUrl(urls[index]);
		}
	});
	final LocationListener locationListener = new LocationListener() {
		public void changed(LocationEvent event) {
			Browser browser = (Browser)event.widget;
		}
		public void changing(LocationEvent event) {
		}
	};
        final TitleListener tocTitleListener = new TitleListener() {
		public void changed(TitleEvent event) {
			titles[index] = event.title;
		}
	};
	browser.addTitleListener(tocTitleListener);
        if (urls.length > 0) browser.setUrl(urls[0]);
    
	
        /*
        final String[] urls = {"configuration.html", "downloaden.html"};
        final Browser browser = new Browser(this, SWT.NONE);
           
        final List list = new List(this, SWT.SIMPLE);
        
        list.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                   int index = list.getSelectionIndex();
         browser.setUrl(urls[index]);
            }
      });*/
    }

    @Override
    public void initialiseFocus() {
        
    }

}
