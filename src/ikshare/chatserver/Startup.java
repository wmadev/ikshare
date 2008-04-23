package ikshare.chatserver;

/**
 *
 * @author Boris martens
 */
public class Startup {
    
    public static void main(String args[])
    {
        ChatServerController.getInstance().start();
    }
    
}
