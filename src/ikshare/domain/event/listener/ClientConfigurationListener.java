/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain.event.listener;

import ikshare.client.configuration.ClientConfiguration;

/**
 *
 * @author awosy
 */
public interface ClientConfigurationListener {
    public void update(ClientConfiguration config);
}
