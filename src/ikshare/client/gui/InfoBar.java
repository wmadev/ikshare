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
        RowLayout rw = new RowLayout(SWT.VERTICAL);
        rw.fill=true;
        rw.pack=false;
        //GridData gd = new GridData(SWT.FILL, SWT.FILL, false, true, 1,1 );
        //gd.widthHint=200;
        //this.setLayoutData(gd);
        //GridLayout gl = new GridLayout(3,false);
        //gl.numColumns = 1;
        this.setLayout(rw);
        Label lblDownload = new Label(this,SWT.NONE);
        lblDownload.setBounds(0, 0, 200, 30);
        lblDownload.setText("Number of Downloads");
        //lblDownload.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1,1));
        lblDownload.setLayoutData( new RowData());
        Label lblNrDownload = new Label(this,SWT.NONE);
        lblNrDownload.setBounds(0, 0, 200, 30);
        lblNrDownload.setText("0");
        lblNrDownload.setLayoutData( new RowData());
        Label lblUpload = new Label(this,SWT.NONE);
        lblUpload.setBounds(0, 0, 200, 30);
        lblUpload.setText("Number of Uploads");
        lblUpload.setLayoutData(new RowData());
        Label lblNrUpload = new Label(this,SWT.NONE);
        lblNrUpload.setBounds(0, 0, 200, 30);
        lblNrUpload.setText("0");
        lblNrUpload.setLayoutData(new RowData());
        Label lblShared = new Label(this,SWT.NONE);
        lblShared.setBounds(0, 0, 200, 30);
        lblShared.setText("Number of Shared Files");
        lblShared.setLayoutData(new RowData());
        Label lblNrShared = new Label(this,SWT.NONE);
        lblNrShared.setBounds(0, 0, 200, 30);
        lblNrShared.setText("0");
        lblNrShared.setLayoutData(new RowData());
    }
}
