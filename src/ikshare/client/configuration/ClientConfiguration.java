package ikshare.client.configuration;

import java.io.File;
import java.util.Calendar;

public class ClientConfiguration {
    private String language;
    private String nickname;
    private Calendar birthday;
    private File sharedFolder;
    private String ikshareServerAddress;
    private int ikshareServerPort, messagePort, fileTransferPort, maximumUploads;
    
    public ClientConfiguration(){
        
    }
    

    public int getMaximumUploads() {
		return maximumUploads;
	}


	public void setMaximumUploads(int maximumUploads) {
		this.maximumUploads = maximumUploads;
	}


	public int getFileTransferPort() {
        return fileTransferPort;
    }

    public void setFileTransferPort(int fileTransferPort) {
        this.fileTransferPort = fileTransferPort;
    }

    public String getIkshareServerAddress() {
        return ikshareServerAddress;
    }

    public void setIkshareServerAddress(String ikshareServerAddress) {
        this.ikshareServerAddress = ikshareServerAddress;
    }

    public int getIkshareServerPort() {
        return ikshareServerPort;
    }

    public void setIkshareServerPort(int ikshareServerPort) {
        this.ikshareServerPort = ikshareServerPort;
    }

    
    
    public int getMessagePort() {
		return messagePort;
	}

	public void setMessagePort(int messagePort) {
		this.messagePort = messagePort;
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
