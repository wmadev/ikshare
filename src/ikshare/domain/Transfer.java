/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain;

/**
 *
 * @author awosy
 */
public class Transfer {
    private String fileName;
    private int fileSize;
    private TransferState state;
    private long speed;
    private Peer peer;
    
    public String getRemainingTime(){
        return "remaining";
    }
}
