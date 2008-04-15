/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain.event.listener;

import ikshare.domain.Transfer;

/**
 *
 * @author awosy
 */
public interface FileTransferListener {
    public void transferStarted(Transfer transfer);
    public void transferStopped(Transfer transfer);
    public void transferCanceled(Transfer transfer);
    public void transferStateChanged(Transfer transfer);
    public void transferFailed(Transfer transfer);
    public void transferFinished(Transfer transfer);
	public void transferPaused(Transfer transfer);
	public void transferResumed(Transfer transfer);
}
