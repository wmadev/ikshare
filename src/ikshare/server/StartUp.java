/*
 * This class is used to start the server application
 */

package ikshare.server;


public class StartUp {
    public static void main(String[] args){
        ServerController.getInstance().startServer();
    }
}
