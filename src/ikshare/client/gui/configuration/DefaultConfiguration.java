/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.configuration;

import java.io.File;
import java.util.Calendar;


/**
 *
 * @author awosy
 */
class DefaultConfiguration extends Configuration {

    public DefaultConfiguration()  {
        super.setNickname("J@n@ ;)");
        super.setLanguage("en");
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, 17);
        date.set(Calendar.YEAR, 1986);
        date.set(Calendar.MONTH, 8);
        super.setBirthday(date);
        super.setSharedFolder(new File(System.getProperty("user.home")));
    }

}
