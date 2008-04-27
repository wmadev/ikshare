/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.configuration;

import java.io.File;
import java.util.Calendar;


/**
 *
 * @author awosy
 */
class DefaultClientConfiguration extends ClientConfiguration {

    public DefaultClientConfiguration()  {
        super.setNickname("nick ;)");
        super.setLanguage("en");
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, 17);
        date.set(Calendar.YEAR, 1986);
        date.set(Calendar.MONTH, 8);
        super.setBirthday(date);
        super.setSharedFolder(new File(System.getProperty("user.home")));
        
        // Default network settings
        super.setIkshareServerAddress("127.0.0.1");
        super.setIkshareServerPort(6000);
        super.setFileTransferPort(6666);
        
        super.setChatServerAddress("127.0.0.1");
        super.setChatServerPort(6005);
    }

}
