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
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
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
        gd.widthHint=180;
        this.setLayoutData(gd);
        GridLayout gl = new GridLayout(2,false);
        this.setLayout(gl);
        this.init();
    }
        
    private void init() {
        Label lblDownload = new Label(this,SWT.FILL);
        lblDownload.setText("Number of Downloads:");
        GridData data=new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
        //data.widthHint=120;
        lblDownload.setLayoutData(data);
        Label lblNrDownload = new Label(this,SWT.NONE);
        lblNrDownload.setText("0");
        GridData data2=new GridData(SWT.END, SWT.CENTER, true, true, 1,1);
        data2.widthHint=80;
        lblNrDownload.setLayoutData(data2);
        Label lblUpload = new Label(this,SWT.NONE);
        lblUpload.setText("Number of Uploads:");
        data=new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
        //data.widthHint=120;
        lblUpload.setLayoutData(data);
        Label lblNrUpload = new Label(this,SWT.NONE);
        lblNrUpload.setText("1");
        data2=new GridData(SWT.END, SWT.CENTER, true, true, 1,1);
        data2.widthHint=80;
        lblNrUpload.setLayoutData(data2);
        Label lblShared = new Label(this,SWT.NONE);
        lblShared.setText("Number of Shared Files:");
        data=new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
        //data.widthHint=120;
        lblShared.setLayoutData(data);
        Label lblNrShared = new Label(this,SWT.NONE);
        lblNrShared.setText("2");
        data2=new GridData(SWT.END, SWT.CENTER, true, true, 1,1);
        data2.widthHint=80;
        lblNrShared.setLayoutData(data2);
    }
}
