package ikshare.client.configuration;

import java.io.File;
import java.util.Calendar;

class DefaultClientConfiguration extends ClientConfiguration {

    public DefaultClientConfiguration()  {
        super.setAccountName("");
        super.setNickname("ikshare_user");
        super.setLanguage("en");
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, 17);
        date.set(Calendar.YEAR, 1986);
        date.set(Calendar.MONTH, 8);
        super.setBirthday(date);
        if(!new File(System.getProperty("user.home")+System.getProperty("file.separator")+"ikshare").exists()){
            new File(System.getProperty("user.home")+System.getProperty("file.separator")+"ikshare").mkdir();
        }
        if(!new File(System.getProperty("user.home")+System.getProperty("file.separator")+"ikshare"+System.getProperty("file.separator")+"SharedFolder").exists()){
            new File(System.getProperty("user.home")+System.getProperty("file.separator")+"ikshare"+System.getProperty("file.separator")+"SharedFolder").mkdir();
        }
        super.setSharedFolder(new File(System.getProperty("user.home")+System.getProperty("file.separator")+"ikshare"+System.getProperty("file.separator")+"SharedFolder"));
        
        // Default network settings
        super.setIkshareServerAddress("127.0.0.1");
        super.setIkshareServerPort(6000);
        super.setFileTransferPort(6666);
        
        super.setChatServerAddress("127.0.0.1");
        super.setChatServerPort(6005);
        super.setMaximumUploads(10);
    }

}
