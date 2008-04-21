package ikshare.domain;

import ikshare.client.configuration.ClientConfigurationController;

import java.io.File;

public class IKShareFile extends File {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4315713515270010597L;
	private String folder, fileName;
	
	public IKShareFile(String folder, String fileName) {
		super(ClientConfigurationController.getInstance().getConfiguration().getSharedFolder()+ folder + fileName);
		setFolder(folder);
		setFileName(fileName);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}
	

}
