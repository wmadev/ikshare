package ikshare.chatserver;

import ikshare.chatserver.datatypes.ChatClient;
import ikshare.chatserver.datatypes.ChatRoom;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.chat.*;
import ikshare.protocol.exception.CommandNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author Boris martens
 */
public class HandleChatClientThread implements Runnable
{
    private Socket clientSocket;
    private boolean running = false;
    private PrintWriter outputWriter;
    private BufferedReader inputReader;
    private ChatClient client;
    
    public HandleChatClientThread(Socket socket) 
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
                	if(ChatServer.debug)
                		System.out.println("[INC] " + input);
                	
                	try
                	{
	                    Commando command = ChatCommandoParser.getInstance().parse(input);
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
	                    else if (command instanceof ChatCreateRoomCommando)
	                    {
	                    	handleCreateRoom((ChatCreateRoomCommando)command);
	                    }
	                    else
	                    {
	                        handleInvalidCommand();
	                    }
                	}
                	catch(CommandNotFoundException excep)
                	{
                		if(ChatServer.debug)
                			System.out.println("Invalid command: " + excep.getMessage());
                	}
                        catch(IndexOutOfBoundsException iobe)
                        {
                            ChatInvalidCommando invalid = new ChatInvalidCommando();
                            invalid.setMessage("invalidnumberofarguments");
                            SendMessage(invalid);
                        }
                        catch(Exception e)
                        {
                            ChatServerController.getInstance().ClientLogsOff(client);
                            running = false;
                        }
                }
                else
                    Thread.sleep(1);
            }
            
            Stop();
        }
        catch(SocketException se)
        {
        	if(client != null)
        		ChatServerController.getInstance().ClientLogsOff(client);
        	Stop();
        }
        catch(Exception e)
        {
            if(ChatServer.debug)
            e.printStackTrace();
            Stop();
        }
    }
    
    private void handleCreateRoom(ChatCreateRoomCommando command) 
    {
		ChatServerController.getInstance().CreateRoom(command, client);
	}

	private void handleIncomingMessage(ChatMessageCommando command)
    {
    	command.setSender(client.getNickName());
    	
    	ChatServerController.getInstance().ProcessMessage(command);
    }
    
    private void handleEnterRoom(ChatEnterRoomCommando command)
    {
    	ChatServerController.getInstance().ClientEntersRoom(command.getRoomName(), command.getPassword(), client);
    }
    
    private void handleLeaveRoom(ChatLeaveRoomCommando command)
    {
    	ChatServerController.getInstance().ClientLeavesRoom(command.getRoomName(), client);
    }
    
    private void handleLogOn(ChatLogOnCommando command)
    {
    	if(!checkNickName(command.getNickName()))
    	{
            ChatLogNiLukNiCommando LNLNCommand = new ChatLogNiLukNiCommando();
            LNLNCommand.setNickName(command.getNickName());
            LNLNCommand.setMessage("invalidnickname");
            SendMessage(LNLNCommand);
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
            ChatWelcomeCommando welcomeCommand = new ChatWelcomeCommando();
            for(ChatRoom publicRoom : ChatServerController.getInstance().getPublicRooms())
            {
            	welcomeCommand.addRoom(publicRoom.getRoomName());
            }
            welcomeCommand.setNickName(command.getNickName());
            SendMessage(welcomeCommand);
            client = new ChatClient(this);
            client.setNickName(command.getNickName());
            client.setIP(clientSocket.getInetAddress());
            ChatServerController.getInstance().ClientLogsIn(client);
        }
    }

    private void handleLogOff(ChatLogOffCommando command)
    {
        ChatClient toFind = ChatServerController.getInstance().GetClientByName(command.getNickName());
        
        if(toFind != null && toFind.getNickName().equals(client.getNickName()))
        {
            ChatServerController.getInstance().ClientLogsOff(toFind);
            Stop();
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
    	if(ChatServer.debug)
    		System.out.println("[OUT] " + command.toString());
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
    		if(ChatServer.debug)
    			e.printStackTrace();
    	}
    }
}
