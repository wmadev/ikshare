/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data.oracle;

import ikshare.server.configuration.ServerConfiguration;
import ikshare.server.data.AccountStorage;
import ikshare.server.data.DatabaseFactory;

/**
 *
 * @author awosy
 */
public class OracleDatabaseFactory extends DatabaseFactory {

    public static DatabaseFactory getInstance(ServerConfiguration configuration) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public AccountStorage getAccountStorage() {
        return OracleAccountStorage.getInstance();
    }

}
