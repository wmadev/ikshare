package ikshare.test;

import ikshare.protocol.command.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

/**
 * Example server that uses TCP. Inspired by Comer, Computer Networks
 * and Internets, 4th edition, section 30.7.
 *
 * <p>Purpose: allocate a socket and then repeatedly execute the
 * following:</p>
 *
 * <ol>
 * <li>wait for the next connection from a client</li>
 * <li>send a short message to the client</li>
 * <li>close the connection</li>
 * <li>go back to step 1</li>
 * </ol>
 *
 * @author <a href="mailto:Bert.VanVreckem at hogent dot be">Bert Van
 * Vreckem</a>
 * @version Modified: <2005-09-06 14:19:09 bert>
 */
public class ExampleServer
{
    /** Default protocol port number. */
    private static final int PROTOPORT = 5193;

    /**
     * Main method.
     * <p><pre>Usage: java ExampleServer [PORT]
     *  PORT - (optional) protocol port number to use.</pre></p>
     */
    public static void main(String[] args)
    {
        // Check command-line argument for protocol port and extract
        // port number if one is specified. Otherwise, use the default
        // port value given by constant PROTOPORT.

        int port = PROTOPORT;

        try 
        {
            if (args.length >= 1)
            {
                port = Integer.parseInt(args[0]);

                // don't accept negative port numbers
                if (port <= 0)
                {
                    throw new NumberFormatException();
                }
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println(
                "Bad port number, positive integer expected: " + 
                args[0]);
            System.exit(2);
        }

        int visits = 0; // number of contacts by clients
        ServerSocket serverSocket = null; 
        Socket link = null;
        PrintWriter out = null;

        try 
        {
            // Step 1: Create a ServerSocket
            serverSocket = new ServerSocket(port);
        }
        catch (IOException ioe)
        {
            System.out.println("Failed to create socket");
            System.exit(1);
        }
        
        try 
        {
            while (true)
            {
                // Step 2: Wait for client to contact server
                link = serverSocket.accept();

                // Step 3: Set up output stream
                out = new PrintWriter
                    (link.getOutputStream(), true);

                // Step 4: Send a short message to the client
                
                CheckAccountCommando checkAccountCommando = new CheckAccountCommando();
                checkAccountCommando.setAccountName("Jonas");
                out.println(checkAccountCommando);
                
                CheckedAccountCommando checkedAccountCommando = new CheckedAccountCommando();
                checkedAccountCommando.setAccountName("Jonas");
                checkedAccountCommando.setStatus("FREE");
                out.println(checkedAccountCommando);
                
                CreateAccountCommando createAccountCommando = new CreateAccountCommando();
                createAccountCommando.setAccountName("Jonas");
                createAccountCommando.setEmail("errorruleone@hotmail.com");
                createAccountCommando.setPassword("j0n4s");
                out.println(createAccountCommando);
                
                CreatedAccountCommando createdAccountCommando = new CreatedAccountCommando();
                createdAccountCommando.setAccountName("Jonas");
                out.println(createdAccountCommando);
                
                FileConfirmCommando fileConfirmCommando = new FileConfirmCommando();
                fileConfirmCommando.setAccountName("Jonas");
                fileConfirmCommando.setFileName("testmiddelgroot.pdf");
                fileConfirmCommando.setPath("/");
                out.println(fileConfirmCommando);
                
                FileNotFoundCommando fileNotFoundCommando = new FileNotFoundCommando();
                fileNotFoundCommando.setAccountName("Jonas");
                fileNotFoundCommando.setFileName("testgroot.avi");
                fileNotFoundCommando.setPath("/");
                out.println(fileNotFoundCommando);
                
                FileRequestCommando fileRequestCommando = new FileRequestCommando();
                fileRequestCommando.setAccountName("Jonas");
                fileRequestCommando.setFileName("testmiddelgroot.pdf");
                fileRequestCommando.setPath("/");
                out.println(fileRequestCommando);
                
                FindCommando findCommando = new FindCommando();
                String id=new Date().getTime()+"Jonas";
                findCommando.setSearchID(id);
                findCommando.setSearchQuery("Eddy Wally");
                out.println(findCommando);
                
                FoundCommando foundCommando = new FoundCommando();
                foundCommando.setSearchID(id);
                foundCommando.setFileName("eddywally.mp3");
                foundCommando.setPath("/");
                foundCommando.setMetaData(new HashMap<String, String>());
                foundCommando.getMetaData().put("title","ik spring uit een vliegmasjien");
                foundCommando.getMetaData().put("album", "china");
                foundCommando.getMetaData().put("year", "1997");
                out.println(foundCommando);
                
                FoundItAllCommando foundItAllCommando = new FoundItAllCommando();
                foundItAllCommando.setSearchID(id);
                out.println(foundItAllCommando);
                
                GetConnCommando getConnCommando = new GetConnCommando();
                getConnCommando.setPort(5139);
                out.println(getConnCommando);
                
                GetPeerCommando getPeerCommando = new GetPeerCommando();
                getPeerCommando.setAccountName("Jonas");
                out.println(getPeerCommando);
                
                GiveConnCommando giveConnCommando = new GiveConnCommando();
                giveConnCommando.setPort(5139);
                out.println(giveConnCommando);
                
                GivePeerCommando givePeerCommando = new GivePeerCommando();
                givePeerCommando.setAccountName("Jonas");
                givePeerCommando.setIpAddress("192.168.16.237");
                givePeerCommando.setPort(5139);
                out.println(givePeerCommando);
                
                InvalidRegisterCommando invalidRegisterCommando = new InvalidRegisterCommando();
                invalidRegisterCommando.setMessage("Tinigeluktjejongene");
                out.println(invalidRegisterCommando);
                
                LogNiLukNiCommando logNiLukNiCommando = new LogNiLukNiCommando();
                logNiLukNiCommando.setAccountName("Jonas");
                logNiLukNiCommando.setMessage("Tinigeluktjejongene");
                out.println(logNiLukNiCommando);
                
                LogOffCommando logOffCommando = new LogOffCommando();
                logOffCommando.setAccountName("Jonas");
                logOffCommando.setPassword("j0n4s");
                out.println(logOffCommando);
                
				LogOnCommando logOnCommando = new LogOnCommando();
				logOnCommando.setAccountName("Jonas");
				logOnCommando.setPassword("j0n4s");
				out.println(logOnCommando);
				
				MyTurnCommando myTurnCommando = new MyTurnCommando();
				myTurnCommando.setAccountName("Jonas");
				myTurnCommando.setFileName("testmiddelgroot.rar");
				myTurnCommando.setPath("/");
				out.println(myTurnCommando);
				
				NeverMindCommando neverMindCommando = new NeverMindCommando();
				neverMindCommando.setSearchID(id);
				out.println(neverMindCommando);
				
				PassTurnCommando passTurnCommando = new PassTurnCommando();
				passTurnCommando.setAccountName("Jonas");
				passTurnCommando.setFileName("testgroot.avi");
				passTurnCommando.setPath("/");
				out.println(passTurnCommando);
				
                PingCommando pingCommando = new PingCommando();
				out.println(pingCommando);
				
				PongCommando pongCommando = new PongCommando();
				pongCommando.setAccountName("Jonas");
				out.println(pongCommando);
				
				WelcomeCommando welcomeCommando = new WelcomeCommando();
				welcomeCommando.setAccountName("Jonas");
				out.println(welcomeCommando);
				
				YourTurnCommando yourTurnCommando = new YourTurnCommando();
				yourTurnCommando.setAccountName("Jonas");
				yourTurnCommando.setSize(300);
				yourTurnCommando.setBlockSize(2048);
				yourTurnCommando.setFileName("testmiddelgroot.rar");
				yourTurnCommando.setPath("/");
				out.println(yourTurnCommando);
				
				

                // Step 5: Close streams and connection
                out.close();
                link.close();
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            System.exit(1);
        }
        finally
        {
            try 
            {
                out.close();
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
