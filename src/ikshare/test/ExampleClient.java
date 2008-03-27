package ikshare.test;


import ikshare.protocol.command.Commando;
import ikshare.protocol.command.CommandoParser;
import ikshare.protocol.command.CreateAccountCommando;
import ikshare.protocol.command.CreatedAccountCommando;
import ikshare.protocol.command.FileConfirmCommando;
import ikshare.protocol.command.FileNotFoundCommando;
import ikshare.protocol.command.FileRequestCommando;
import ikshare.protocol.command.FoundCommando;
import ikshare.protocol.command.FoundItAllCommando;
import ikshare.protocol.command.GetConnCommando;
import ikshare.protocol.command.GetPeerCommando;
import ikshare.protocol.command.GiveConnCommando;
import ikshare.protocol.command.InvalidRegisterCommando;
import ikshare.protocol.command.LogNiLukNiCommando;
import ikshare.protocol.command.LogOffCommando;
import ikshare.protocol.command.LogOnCommando;
import ikshare.protocol.command.MyTurnCommando;
import ikshare.protocol.command.NeverMindCommando;
import ikshare.protocol.command.PassTurnCommando;
import ikshare.protocol.command.PingCommando;
import ikshare.protocol.command.PongCommando;
import ikshare.protocol.command.WelcomeCommando;
import ikshare.protocol.command.YourTurnCommando;
import ikshare.protocol.exception.CommandNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Example client that uses TCP. Inspired by Comer, Computer Networks
 * and Internets, 4th edition, section 30.6.
 *
 * <p>Purpose: allocate a socket, connect to a server, and print all
 * output.</p>
 *
 * @author <a href="mailto:Bert.VanVreckem at hogent dot be">Bert Van
 * Vreckem</a>
 * @version Modified: <2005-09-06 14:18:59 bert>
 */
public class ExampleClient
{
    /** Default protocol port number. */
    private static final int PROTOPORT = 5193;

    /**
     * Main method.
     *
     * <p><pre>Usage: java ExampleClient [HOST [PORT]]
     *  HOST - name of a computer on which server is executing
     *  PORT - protocol port number server is using</pre></p>
     *
     * @param args a <code>String[]</code> value
     */
    public static void main(String[] args)
    {
        // Check command-line argument for protocol port and extract
        // port number if one is specified. Otherwise, use the default
        // port value given by constant PROTOPORT.
        
        int port = PROTOPORT;

        try 
        {
            if (args.length >= 2)
            {
                port = Integer.parseInt(args[1]);

                // don't accept negative port numbers
                if (port <= 0)
                {
                    throw new NumberFormatException();
                }
            }
        }
        catch (NumberFormatException nfe)
        {
            System.out.println(
                "Bad port number, positive integer expected: " + 
                args[1]);
            System.exit(2);
        }

        // Check host argument, assign host name and convert to IP
        // address.

        String hostName = "localhost";
        InetAddress host = null;
        
        if (args.length >= 1)
        {
            hostName = args[0];
        }

        try 
        {
            host = InetAddress.getByName(hostName);
        }
        catch (UnknownHostException uhe)
        {
            System.out.println("Unknown host: " + hostName);
            System.exit(1);
        }
        
        Socket link = null;
        BufferedReader in = null;
        
        try
        {
            // Step 1. Establish server connection
            link = new Socket(host, port);
            
            // Step 2. Set up input stream
            in = new BufferedReader(new
                InputStreamReader(link.getInputStream()));

            // Step 3. Receive data
            String input = in.readLine();
            
            while (input != null)
            {
	                Commando c = CommandoParser.getInstance().parse(input);
	                if (c instanceof CreateAccountCommando) {
	                	System.out.println(((CreateAccountCommando)c).getCommandoName());
	                	System.out.println(((CreateAccountCommando)c).getAccountName());
	                	System.out.println(((CreateAccountCommando)c).getEmail());
	                	System.out.println(((CreateAccountCommando)c).getPassword());
	                }
	                else if (c instanceof CreatedAccountCommando) {
	                	System.out.println(((CreatedAccountCommando)c).getCommandoName());
	                	System.out.println(((CreatedAccountCommando)c).getAccountName());
	                }
	                else if (c instanceof FileConfirmCommando) {
	                	System.out.println(((FileConfirmCommando)c).getCommandoName());
	                	System.out.println(((FileConfirmCommando)c).getAccountName());
	                	System.out.println(((FileConfirmCommando)c).getFileName());
	                	System.out.println(((FileConfirmCommando)c).getPath());
	                }
	                else if (c instanceof FileNotFoundCommando) {
	                	System.out.println(((FileNotFoundCommando)c).getCommandoName());
	                	System.out.println(((FileNotFoundCommando)c).getAccountName());
	                	System.out.println(((FileNotFoundCommando)c).getFileName());
	                	System.out.println(((FileNotFoundCommando)c).getPath());
	                }
	                else if (c instanceof FileRequestCommando) {
	                	System.out.println(((FileRequestCommando)c).getCommandoName());
	                	System.out.println(((FileRequestCommando)c).getAccountName());
	                	System.out.println(((FileRequestCommando)c).getFileName());
	                	System.out.println(((FileRequestCommando)c).getPath());
	                }
	                else if (c instanceof FoundCommando) {
	                	System.out.println(((FoundCommando)c).getCommandoName());
	                	System.out.println(((FoundCommando)c).getFileName());
	                	System.out.println(((FoundCommando)c).getPath());
	                	System.out.println(((FoundCommando)c).getSearchID());
	                	System.out.println(((FoundCommando)c).getMetaData());
	                }
	                else if (c instanceof FoundItAllCommando) {
	                	System.out.println(((FoundItAllCommando)c).getCommandoName());
	                	System.out.println(((FoundItAllCommando)c).getSearchID());
	                }
	                else if (c instanceof GetConnCommando) {
	                	System.out.println(((GetConnCommando)c).getCommandoName());
	                	System.out.println(((GetConnCommando)c).getPort());
	                }
	                else if (c instanceof GetPeerCommando) {
	                	System.out.println(((GetPeerCommando)c).getCommandoName());
	                	System.out.println(((GetPeerCommando)c).getAccountName());
	                }
	                else if (c instanceof GiveConnCommando) {
	                	System.out.println(((GiveConnCommando)c).getCommandoName());
	                	System.out.println(((GiveConnCommando)c).getPort());
	                }
	                else if (c instanceof InvalidRegisterCommando) {
	                	System.out.println(((InvalidRegisterCommando)c).getCommandoName());
	                	System.out.println(((InvalidRegisterCommando)c).getMessage());
	                }
	                else if (c instanceof LogNiLukNiCommando) {
	                	System.out.println(((LogNiLukNiCommando)c).getCommandoName());
	                	System.out.println(((LogNiLukNiCommando)c).getAccountName());
	                	System.out.println(((LogNiLukNiCommando)c).getMessage());
	                }
	                else if (c instanceof LogOffCommando) {
	                	System.out.println(((LogOffCommando)c).getCommandoName());
	                	System.out.println(((LogOffCommando)c).getAccountName());
	                	System.out.println(((LogOffCommando)c).getPassword());
	                }
	                else if (c instanceof LogOnCommando) {
	                	System.out.println(((LogOnCommando)c).getCommandoName());
	                	System.out.println(((LogOnCommando)c).getAccountName());
	                	System.out.println(((LogOnCommando)c).getPassword());
	                }
	                else if (c instanceof MyTurnCommando) {
	                	System.out.println(((MyTurnCommando)c).getCommandoName());
	                	System.out.println(((MyTurnCommando)c).getAccountName());
	                	System.out.println(((MyTurnCommando)c).getFileName());
	                	System.out.println(((MyTurnCommando)c).getPath());
	                }
	                else if (c instanceof NeverMindCommando) {
	                	System.out.println(((NeverMindCommando)c).getCommandoName());
	                	System.out.println(((NeverMindCommando)c).getSearchID());
	                }
	                else if (c instanceof PassTurnCommando) {
	                	System.out.println(((PassTurnCommando)c).getCommandoName());
	                	System.out.println(((PassTurnCommando)c).getAccountName());
	                	System.out.println(((PassTurnCommando)c).getFileName());
	                	System.out.println(((PassTurnCommando)c).getPath());
	                }
	                else if (c instanceof PingCommando) {
	                	System.out.println(((PingCommando)c).getCommandoName());
	                }
	                else if (c instanceof PongCommando) {
	                	System.out.println(((PongCommando)c).getCommandoName());
	                	System.out.println(((PongCommando)c).getAccountName());
	                }
	                else if (c instanceof WelcomeCommando) {
	                	System.out.println(((WelcomeCommando)c).getCommandoName());
	                	System.out.println(((WelcomeCommando)c).getAccountName());
	                }
	                else if (c instanceof YourTurnCommando) {
	                	System.out.println(((YourTurnCommando)c).getCommandoName());
	                	System.out.println(((YourTurnCommando)c).getAccountName());
	                	System.out.println(((YourTurnCommando)c).getFileName());
	                	System.out.println(((YourTurnCommando)c).getPath());
	                	System.out.println(((YourTurnCommando)c).getSize());
	                	System.out.println(((YourTurnCommando)c).getBlockSize());
	                }
                //System.out.println(input);
                input = in.readLine();
                System.out.println();
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            System.exit(1);
        } catch (CommandNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finally
        {
            try 
            {
                // Step 5: Close streams and connection
                in.close();
                link.close();
            }
            catch (IOException e)
            {
                System.out.println("Closing connections failed!");
                System.exit(1);
            }
        }
    }
}
