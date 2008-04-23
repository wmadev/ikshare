package ikshare.chatserver;

/**
 *
 * @author Boris martens
 */
public class Startup {
    
    /** Creates a new instance of Startup */
    public static void Main(String args[])
    {
        ChatServerController.getInstance().start();
    }
    
}
