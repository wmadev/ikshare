/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author awosy
 */
public class InfoBar extends Composite {
    public InfoBar(Shell shell,int flags){
        super(shell,flags);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, false, true, 1,1 );
        gd.widthHint=200;
        this.setLayoutData(gd);
        this.setLayout(new FillLayout());
        Label lblTest = new Label(this,SWT.BORDER);
        lblTest.setBounds(0, 0, 200, 30);
        lblTest.setText("Infobar");
    }
}
