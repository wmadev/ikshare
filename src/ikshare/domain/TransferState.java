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
    public static final int STARTED = 1;
    public static final int STOPPED = 2;
    public static final int CANCELED = 3;
    public static final int FINISHED= 4;
    public static final int FAILED = 5;
    public static final int DOWNLOADING= 6;
    public static final int UPLOADING = 7;
}
