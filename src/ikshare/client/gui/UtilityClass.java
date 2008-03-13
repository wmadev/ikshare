
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
	String timeInSeconds;
	if(seconds.toString().length()>2)
            timeInSeconds= seconds.toString().substring(0, 2);
        else
            timeInSeconds= seconds.toString();
        
        long timeInMinutes = 0;
        long timeInHours = 0;

        if(seconds>59)
            timeInMinutes = seconds/60;
        if(timeInMinutes>59)
            timeInHours = seconds/360;
        return timeInHours+":"+timeInMinutes+":"+timeInSeconds;
    }
}