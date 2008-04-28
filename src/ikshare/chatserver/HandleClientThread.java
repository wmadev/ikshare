package ikshare.chatserver;

import ikshare.chatserver.datatypes.ChatClient;
import ikshare.protocol.command.*;
import ikshare.protocol.command.chat.*;
import ikshare.protocol.exception.CommandNotFoundException;

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
    private ChatClient client;
    
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
                	try
                	{
	                    Commando command = CommandoParser.getInstance().parse(input);
	                    if (command instanceof ChatMessageCommando)
	                    {
	                    	handleIncomingMessage((ChatMessageCommando)command);
	                    }
	                    else if(command instanceof ChatEnterRoomCommando)
	                    {
	                    	handleEnterRoom((ChatEnterRoomCommando)command);
	                    }
	                    else if(command instanceof ChatLeaveRoomCommando)
	                    {
	                    	handleLeaveRoom((ChatLeaveRoomCommando)command);
	                    }
	                    else if(command instanceof ChatLogOnCommando)
	                    {
	                        handleLogOn((ChatLogOnCommando)command);
	                    }
	                    else if (command instanceof ChatLogOffCommando)
	                    {
	                        handleLogOff((ChatLogOffCommando)command);
	                    }
	                    else if (command instanceof ChatMessageCommando)
	                    {
	                    	handleIncomingMessage((ChatMessageCommando)command);
	                    }
	                    else
	                    {
	                        handleInvalidCommand();
	                    }
                	}
                	catch(CommandNotFoundException excep)
                	{
                		System.out.println("Invalid command: " + excep.getMessage());
                	}
                }
                else
                	wait(1); //wait 1 millisecond
            }
        }
        catch(Exception e)
        {
            
        }
    }
    
    private void handleIncomingMessage(ChatMessageCommando command)
    {
    	command.setSender(client.getNickName());
    	
    	ChatServerController.getInstance().ProcessMessage(command);
    }
    
    private void handleEnterRoom(ChatEnterRoomCommando command)
    {
    	ChatServerController.getInstance().ClientEntersRoom(command.getRoomName(), client);
    }
    
    private void handleLeaveRoom(ChatLeaveRoomCommando command)
    {
    	ChatServerController.getInstance().ClientLeavesRoom(command.getRoomName(), client);
    }
    
    private void handleLogOn(ChatLogOnCommando command)
    {
    	if(!checkNickName(command.getNickName()))
    	{
    		//TODO send message stating invalid nickname
    	}
    	else if(ChatServerController.getInstance().GetClientByName(command.getNickName())!=null)
        {
            ChatLogNiLukNiCommando LNLNCommand = new ChatLogNiLukNiCommando();
            LNLNCommand.setNickName(command.getNickName());
            LNLNCommand.setMessage("nicknametaken");
            SendMessage(LNLNCommand);
        }
        else
        {
            ChatWelcomeCommando WelcomeCommand = new ChatWelcomeCommando();
            WelcomeCommand.setNickName(command.getNickName());
            client = new ChatClient(this);
            client.setNickName(command.getNickName());
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
                Stop();
            }
        }
    }
   
    
    private void handleInvalidCommand()
    {
        ChatInvalidCommando invalid = new ChatInvalidCommando();
        invalid.setMessage("invalidcommando");
        SendMessage(invalid);
    }
    
    private boolean checkNickName(String nickname)
    { //TODO check for invalid characters
    	return (nickname!= null && !nickname.equals("") && nickname.length()>0);
    }
    
    public void SendMessage(Commando command)
    {
    	outputWriter.println(command.toString());
    }
    
    private void Stop()
    {
    	try
    	{
    		running = false;
    		inputReader.close();
    		outputWriter.close();
    		clientSocket.close();
    	}
    	catch(Exception e)
    	{
    	}
    
    }
}