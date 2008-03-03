/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
        GridLayout gl = new GridLayout(3,false);
        gl.numColumns = 1;
        this.setLayout(gl);
        Label lblDownload = new Label(this,SWT.BORDER);
        lblDownload.setBounds(0, 0, 200, 30);
        lblDownload.setText("Number of Downloads");
        lblDownload.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true, 1,1));
        Label lblUpload = new Label(this,SWT.BORDER);
        lblUpload.setBounds(0, 0, 200, 30);
        lblUpload.setText("Number of Uploads");
        lblDownload.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,1));
        Label lblShared = new Label(this,SWT.BORDER);
        lblShared.setBounds(0, 0, 200, 30);
        lblShared.setText("Number of Uploads");
        lblShared.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,1));
    }
}
