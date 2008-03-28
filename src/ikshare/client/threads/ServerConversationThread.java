/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.threads;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.CommandoParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author awosy
 */
public class ServerConversationThread implements Runnable{

    private Socket serverConnection;
    private PrintWriter outputWriter;
    private BufferedReader incomingReader;
    private boolean running = false;
    
    public ServerConversationThread(){
        try{
            serverConnection = new Socket(
                InetAddress.getByName(ClientConfigurationController.getInstance().getConfiguration().getIkshareServerAddress()),
                ClientConfigurationController.getInstance().getConfiguration().getIkshareServerPort());
            outputWriter = new PrintWriter(serverConnection.getOutputStream());
            incomingReader = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
            running = true;
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void run() {
        try {
            while (running) {
                String inputLine = incomingReader.readLine();
                if (inputLine != null) {
                    System.out.println(inputLine);
                    Commando c = CommandoParser.getInstance().parse(inputLine);
	            /*if (c instanceof CreateAccountCommando) {
                        handleCreateAccountCommando(c);
	            }
                    else if( c instanceof LogOnCommando){
                        handleLogonCommando(c);
                    }
                    else if( c instanceof LogOffCommando){
                        handleLogoffCommando(c);
                    }*/
                    
                }
            }
            serverConnection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void sendCommand(Commando command){
        outputWriter.println(command.toString());
        System.out.println("SEND: "+command.toString());
    }
    
    public void stop(){
        running = false;
    }

    
}
