/*
 * This class is used to start the server application
 */

package server;


/**
 *
 * @author awosy
 */
public class StartUp {
    public static void main(String[] args){
        ServerController.getInstance().startServer();
    }
}
