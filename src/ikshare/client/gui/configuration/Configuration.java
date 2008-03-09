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
public class Configuration {
    private String language;
    private String nickname;
    private Calendar birthday;
    private File sharedFolder;
    
    public Configuration(){
        
    }

    public File getSharedFolder(){
        return sharedFolder;
    }
    public void setSharedFolder(File file){
        sharedFolder = file;
    }
    
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    
}
