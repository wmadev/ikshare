/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain;

/**
 *
 * @author awosy
 */
public class TransferState {
    public static final int STOPPED = 1;
    public static final int CANCELEDDOWNLOAD = 2;
    public static final int FINISHED= 3;
    public static final int FAILED = 4;
    public static final int DOWNLOADING= 5;
    public static final int UPLOADING = 6;
    public static final int CANCELEDUPLOAD = 7;
    public static final int PAUSED = 8;
}
