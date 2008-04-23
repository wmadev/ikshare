package ikshare.chatserver;

import ikshare.chatserver.datatypes.ChatClient;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.CommandoParser;
import ikshare.protocol.command.chat.ChatLogNiLukNiCommando;
import ikshare.protocol.command.chat.ChatLogOffCommando;
import ikshare.protocol.command.chat.ChatLogOnCommando;
import ikshare.protocol.command.chat.ChatWelcomeCommando;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Boris martens
 */
public class HandleClientThread implements Runnable
{
    private Socket clientSocket;
    private boolean running = false;
    private PrintWriter outputWriter;
    private BufferedReader inputReader;
    
    public HandleClientThread(Socket socket) 
    {
        try
        {
            clientSocket = socket;
            outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            running = true;
        }
        catch(IOException excep)
        {
            running = false;
        }
    }

    public void run() 
    {
        try
        {
            while(running)
            {
                String input = inputReader.readLine();
                if(input!=null)
                {
                    Commando command = CommandoParser.getInstance().parse(input);
                    if(command instanceof ChatLogOnCommando)
                    {
                        handleLogOn((ChatLogOnCommando)command);
                    }
                    else if (command instanceof ChatLogOffCommando)
                    {
                        handleLogOff((ChatLogOffCommando)command);
                    }
                    else 
                    {
                        handleInvalidCommand();
                    }
                }
            }
        }
        catch(Exception e)
        {
            
        }
    }
    
    private void handleLogOn(ChatLogOnCommando command)
    {
        if(ChatServerController.getInstance().GetClientByName(command.getNickName())!=null)
        {
            ChatLogNiLukNiCommando LNLNCommand = new ChatLogNiLukNiCommando();
            LNLNCommand.setNickName(command.getNickName());
            LNLNCommand.setMessage("nicknametaken");
            outputWriter.println(LNLNCommand.toString());
        }
        else
        {
            ChatWelcomeCommando WelcomeCommand = new ChatWelcomeCommando();
            WelcomeCommand.setNickName(command.getNickName());
            ChatClient client = new ChatClient();
            client.setNickName(command.getNickName());
            client.setPort(command.getPort());
            client.setIP(clientSocket.getInetAddress());
            ChatServerController.getInstance().ClientLogsIn(client);
        }
    }
    
    private void handleLogOff(ChatLogOffCommando command)
    {
        ChatClient toFind = ChatServerController.getInstance().GetClientByName(command.getNickName());
        
        if(toFind != null)
        {
            if(toFind.getIP()==clientSocket.getInetAddress())
            {
                ChatServerController.getInstance().ClientLogsOff(toFind);
            }
        }
    }
    
    private void handleInvalidCommand()
    {
        
    }
}
