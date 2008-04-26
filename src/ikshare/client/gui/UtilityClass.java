
package ikshare.client.gui;


public class UtilityClass {
    /**
     * This method formats a long that represents a filesize in bytes in a more readable form
     * @param bytes
     * @return the formatted representation
     */
    public static String formatFileSize(Long size) {
	int counter = 0;
	double bytes = (double) size;
	while (bytes > 1024) {
            bytes = bytes / 1024;
            ++counter;
	}
	Object[] arg = { new Double(bytes) };
	String output = String.format("%.2f", arg);
	switch (counter) {
            case 0:
            	return output + " byte";
            case 1:
                return output + " kb";
            case 2:
                return output + " Mb";
            case 3:
                return output + " Gb";
            case 4:
                return output + bytes + " Tb";
            default:
                return "> Tb";
            }
	}
    /**
     * This method formats a long that represents a time in seconds into a more readable form
     * @param seconds
     * @return the formatted representation
     */
    public static String formatTime(Long seconds) {
    	String returnVal="";
    	long timeInDays, timeInSeconds = 0, timeInMinutes = 0, timeInHours = 0, restingSeconds=0;
        restingSeconds = seconds;
        
    	timeInDays = restingSeconds / (long)(86400);
    	restingSeconds = restingSeconds%(long)(86400);
        timeInHours = restingSeconds / (long)(3600);
        restingSeconds = restingSeconds%(long)(3600);
        timeInMinutes = restingSeconds / (long)(60);
        timeInSeconds = restingSeconds%(long)(60);
        
        if(timeInDays != 0)
        	returnVal += timeInDays + ":";
        if (timeInHours < 10)
        	returnVal += "0";
        returnVal += timeInHours + ":";
        if (timeInMinutes < 10)
        	returnVal += "0";
        returnVal += timeInMinutes + ":";
        if (timeInSeconds < 10)
        	returnVal += "0";
        returnVal += timeInSeconds;
        return returnVal;
    }
}