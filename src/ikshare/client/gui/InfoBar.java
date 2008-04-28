package ikshare.client.gui;

import ikshare.client.ClientController;
import java.awt.FlowLayout;
import java.io.File;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.client.gui.custom.MP3Player;
import ikshare.client.gui.custom.UIFileBrowser;
import ikshare.domain.PeerFacade;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.TransferQueueListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class InfoBar extends Composite implements TransferQueueListener {

    private Label lblNrUpload,  lblNrDownload,  lblNrSharedFolders,  lblNrSharedFiles;
    private static String ICON_DOWN = "resources/icons/tp_down.png";
    private static String ICON_UP = "resources/icons/tp_up.png";
    private static String ICON_SHAREDFOLDERS = "resources/icons/info_folders.png";
    private static String ICON_SHAREDFILES = "resources/icons/info_files.png";

    /*
     * Sets the layout of the infobar in the shell right (1 vertical grid and 1 horizontal grid)
     * The labels gonna be committed by the method init.
     */
    public InfoBar(Shell shell, int flags) {
        super(shell, flags);
        GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        this.setLayoutData(gd);
        this.setLayout(new RowLayout());
        init();
        EventController.getInstance().addTransferQueueListener(this);
    }

    private void init() {
        // Transfer part
        new Label(this, SWT.NONE).setText("Transfers: ");

        if (new File(ICON_DOWN).exists()) {
            new Label(this, SWT.NONE).setImage(new Image(Display.getCurrent(), ICON_DOWN));
        }
        lblNrDownload = new Label(this, SWT.NONE);
        lblNrDownload.setText("0");

        if (new File(ICON_UP).exists()) {
            new Label(this, SWT.NONE).setImage(new Image(Display.getCurrent(), ICON_UP));
        }
        lblNrUpload = new Label(this, SWT.NONE);
        lblNrUpload.setText("0");

        // Shared folder part
        new Label(this, SWT.NONE).setText("|");
        new Label(this, SWT.NONE).setText("Shared Files: ");
        int[] info = ClientConfigurationController.getInstance().getSharedInformation();
        if (new File(ICON_SHAREDFOLDERS).exists()) {
            new Label(this, SWT.NONE).setImage(new Image(Display.getCurrent(), ICON_SHAREDFOLDERS));
        }
        lblNrSharedFolders = new Label(this, SWT.NONE);
        lblNrSharedFolders.setText(String.valueOf(info[0]));

        if (new File(ICON_SHAREDFILES).exists()) {
            new Label(this, SWT.NONE).setImage(new Image(Display.getCurrent(), ICON_SHAREDFILES));
        }
        lblNrSharedFiles = new Label(this, SWT.NONE);
        lblNrSharedFiles.setText(String.valueOf(info[1]));

        // player
        new MP3Player(this, SWT.NONE);


    }

    public void activeDownloadsChanged() {
        this.getDisplay().asyncExec(new Runnable() {

            public void run() {
                lblNrDownload.setText(String.valueOf(PeerFacade.getInstance().getActiveDownloads()));
            }
        });
    }

    public void activeUploadsChanged() {
        this.getDisplay().asyncExec(new Runnable() {

            public void run() {
                lblNrUpload.setText(String.valueOf(PeerFacade.getInstance().getActiveUploads()));
            }
        });
    }
}
