/**
 * This class represents a thread that handles a client that connects with the server.
 */
package ikshare.server.threads;

import ikshare.domain.*;
import ikshare.protocol.command.*;
import ikshare.server.*;
import ikshare.server.data.*;
import java.io.*;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ResourceBundle;
/**
 *
 * @author awosy
 */
public class HandleClientThread implements Runnable{
    private Socket clientSocket;
    private boolean running = false;
    private PrintWriter outputWriter;
    private BufferedReader incomingReader;
    private ResourceBundle bundle;
    
    public HandleClientThread(Socket socket){
        try {
            clientSocket = socket;
            outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            incomingReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bundle = ResourceBundle.getBundle("ikshare.server.server");
            running = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void run() {
        try {
            while(running){
                String inputLine = incomingReader.readLine();
                if (inputLine != null) {
                    System.out.println(inputLine);
                    Commando c = CommandoParser.getInstance().parse(inputLine);
	            if (c instanceof CreateAccountCommando) {
                        handleCreateAccountCommando(c);
	            }
                    else if( c instanceof LogOnCommando){
                        handleLogonCommando(c);
                    }
                    else if( c instanceof LogOffCommando){
                        handleLogoffCommando(c);
                    }
                    
                }
            }
            clientSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }

    private String getString(String key,String[] params){
        String text = bundle.getString(key);
        if(params!=null && text!=null) {
            MessageFormat format = new MessageFormat(text);
            text=format.format(params);
        }
        return text;
    }
    
    private void handleCreateAccountCommando(Commando c) {
        CreateAccountCommando cac = (CreateAccountCommando)c;
        Peer newUser = new Peer(cac.getAccountName(),cac.getPassword(),cac.getEmail());
        try {
            if(!ServerController.getInstance().checkAccount(newUser)){
                if (ServerController.getInstance().createAccount(newUser)) {
                    CreatedAccountCommando cdac = new CreatedAccountCommando();
                    cdac.setAccountName(newUser.getAccountName());
                    outputWriter.println(cdac.toString());
                }
            }
            else {
                InvalidRegisterCommando irc = new InvalidRegisterCommando();
                String[] param = {newUser.getAccountName()};
                irc.setMessage(getString("accountNameAlreadyExists",param));
                outputWriter.println(irc.toString());
            }
        }
        catch (DatabaseException ex) {
            ServerErrorCommando sec = new ServerErrorCommando();
            sec.setMessage(ex.getMessage());
            outputWriter.println(sec.toString());
        }
    }

    private void handleLogoffCommando(Commando c) {
        LogOffCommando lo = (LogOffCommando)c;
        Peer user = new Peer(lo.getAccountName(),lo.getPassword(),clientSocket.getInetAddress(),lo.getPort());
                     
        try {
            ServerController.getInstance().logoff(user);
        }
        catch (DatabaseException ex) {
            ServerErrorCommando sec = new ServerErrorCommando();
            sec.setMessage(ex.getMessage());
            outputWriter.println(sec.toString());
        }
        running = false;
    }

    private void handleLogonCommando(Commando c) {
        LogOnCommando lc = (LogOnCommando)c;
        Peer user = new Peer(lc.getAccountName(),lc.getPassword(),clientSocket.getInetAddress(),lc.getPort());
                     
        try {
            if(ServerController.getInstance().checkPassword(user)){
                if (ServerController.getInstance().logon(user)) {
                    WelcomeCommando wc = new WelcomeCommando();
                    wc.setAccountName(user.getAccountName());
                    outputWriter.println(wc.toString());
                }
            }
            else {
                LogNiLukNiCommando lnlnc = new LogNiLukNiCommando();
                String[] param = {user.getAccountName()};
                lnlnc.setAccountName(user.getAccountName());
                lnlnc.setMessage(getString("logonFailed",param));
                outputWriter.println(lnlnc.toString());
            }
        }
        catch (DatabaseException ex) {
            ServerErrorCommando sec = new ServerErrorCommando();
            sec.setMessage(ex.getMessage());
            outputWriter.println(sec.toString());
        }
    }
}
