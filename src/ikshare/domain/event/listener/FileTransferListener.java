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
    public void TransferStarted(Transfer transfer);
    public void TransferStopped(Transfer transfer);
    public void TransferCanceled(Transfer transfer);
    public void TransferStateChanged(Transfer transfer);
    public void TransferFailed(Transfer transfer);
}
