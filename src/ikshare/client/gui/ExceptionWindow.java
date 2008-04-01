/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui;

import ikshare.client.configuration.ClientConfigurationController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author awosy
 */
public class ExceptionWindow {
	public ExceptionWindow(Exception e,MainScreen mw, boolean fatal) {
		checkMainWindow(mw,e.getMessage());
		open(e,mw.getShell(),null,fatal);
	}
	
        private void open(Exception e, Shell shell, String title, boolean fatal) {
		open(e.getLocalizedMessage(),shell,title,fatal);
		//e.printStackTrace();
	}
	private void open(String s, Shell shell, String title, boolean fatal) {
		MessageBox mb = new MessageBox(shell,(fatal)?SWT.ICON_ERROR:SWT.ICON_WARNING);
		if (title==null)
			mb.setText((fatal)?ClientConfigurationController.getInstance().getString("fatalerror"):ClientConfigurationController.getInstance().getString("applicationerror"));
		else
			mb.setText(title);
		if (s!=null)
			mb.setMessage((fatal)?ClientConfigurationController.getInstance().getString("fatalerrorinfo")+s:ClientConfigurationController.getInstance().getString("applicationerrorinfo")+s);
		mb.open();
		if(fatal)
			shell.getDisplay().dispose();
	}
	
	private void checkMainWindow(MainScreen mw, String s) {
		/* als de gui nog niet zichtbaar is */
		if(!mw.isOpen())
                    throw new RuntimeException(s);
	}
}

