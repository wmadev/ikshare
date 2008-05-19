package ikshare.domain.event.listener;

import ikshare.client.configuration.ClientConfiguration;

public interface ClientConfigurationListener {
    public void update(ClientConfiguration config);
}
