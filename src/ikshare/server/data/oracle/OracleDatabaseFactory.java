/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data.oracle;

import ikshare.server.data.AccountStorage;
import ikshare.server.data.DatabaseFactory;

/**
 *
 * @author awosy
 */
public class OracleDatabaseFactory extends DatabaseFactory {

    @Override
    public AccountStorage getAccountStorage() {
        return OracleAccountStorage.getInstance();
    }

}
