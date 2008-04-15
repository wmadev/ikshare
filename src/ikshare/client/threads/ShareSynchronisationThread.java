/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.threads;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.SharedFile;
import ikshare.domain.SharedFolder;
import ikshare.domain.SharedItem;
import ikshare.domain.SharedItemState;
import ikshare.protocol.command.AddShareCommando;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.CommandoParser;
import ikshare.protocol.command.EndShareSynchronisationCommando;
import ikshare.protocol.command.ReceiveFolderIdCommando;
import ikshare.protocol.command.StartShareSynchronisationCommando;
import ikshare.protocol.exception.CommandNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author awosy
 */
public class ShareSynchronisationThread implements Runnable {

    private Socket serverConnection;
    private PrintWriter outputWriter;
    private BufferedReader incomingReader;
    private boolean running = false;
    private SharedItem newShareTree;
    private SharedItem oldShareTree;
    private String accountName;
    
    public ShareSynchronisationThread(String accountName, File root) throws IOException {
        newShareTree = readNewShareTree(root);
        oldShareTree = readOldShareTree();
        this.accountName = accountName;
        serverConnection = new Socket(
                InetAddress.getByName(ClientConfigurationController.getInstance().getConfiguration().getIkshareServerAddress()),
                ClientConfigurationController.getInstance().getConfiguration().getIkshareServerPort());
            outputWriter = new PrintWriter(serverConnection.getOutputStream(),true);
            incomingReader = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
            running = true;
    }
    
    public void run() {
        try {
            startSynchronisation();
            if(oldShareTree==null){
                sendCompleteTree(newShareTree);
                //printTree(newShareTree);
            }
            endSynchronisation();
            serverConnection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void endSynchronisation() {
        EndShareSynchronisationCommando essc = new EndShareSynchronisationCommando();
        sendCommand(essc);
    }

    private void printTree(SharedItem root) {
        if(root.isFolder()){
            System.out.println("Folder: "+((SharedFolder)root).getFolderID()+" : "+
                    ((SharedFolder)root).getParentID()+" : "+
                    ((SharedFolder)root).getName());
            Iterator it = ((SharedFolder)root).getContent().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry item = (Map.Entry)it.next();
                printTree((SharedItem)item.getValue());
            }
        }
        else {
            System.out.println("File: "+((SharedFile)root).getParentId()+" : "+
                    ((SharedFile)root).getName());
        }
    }
    
    private SharedItem readNewShareTree(File root){
        return addSharedItemToTree(root,SharedItemState.ADDED);
    }
    private SharedItem readOldShareTree(){
        File saveFile = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"ikshare"+System.getProperty("file.separator")+"_stsf.iks");
        if(!saveFile.exists()){
            return null;
        }
        SharedItem root = null;
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(saveFile);
            ois = new ObjectInputStream(fis);
            root = (SharedItem)ois.readObject();
        } catch (Exception ex) {
            root = null;
        } finally {
            try {
                ois.close();
                fis.close();
            } catch (IOException ex) {
                root = null;
            }
            root = null;
        }
        return root;
    }
    
    private SharedItem addSharedItemToTree(File file,SharedItemState state){
        SharedItem newItem = null;
        if(file.isDirectory()){
            newItem = new SharedFolder(true,file.getPath());
            File[] files = file.listFiles(); 
            for(int i = 0;i<files.length;i++){
                ((SharedFolder)newItem).getContent().put( files[i].getName()+files[i].length(), addSharedItemToTree(files[i],state) );
            }
        }else{
           newItem = new SharedFile(false, file.getName(), file.length());
        }
        newItem.setState(state);
        return newItem;
    }

    private void sendCompleteTree(SharedItem newShareTree) throws CommandNotFoundException, IOException {
        if(newShareTree.isFolder()){
            AddShareCommando asc = new AddShareCommando();
            asc.setDirectory(true);
            asc.setParentFolderID(((SharedFolder)newShareTree).getParentId());
            asc.setName(((SharedFolder)newShareTree).getName());
            asc.setPath(((SharedFolder)newShareTree).getPath());
            sendCommand(asc);
            ((SharedFolder)newShareTree).setFolderID(receiveFolderId());
            Iterator it = ((SharedFolder)newShareTree).getContent().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry item = (Map.Entry)it.next();
                ((SharedItem)item.getValue()).setParentId(((SharedFolder)newShareTree).getFolderID());
                sendCompleteTree((SharedItem)item.getValue());
            }
        }
        else {
            AddShareCommando asc = new AddShareCommando();
            asc.setDirectory(false);
            asc.setName(((SharedFile)newShareTree).getName());
            asc.setSize(((SharedFile)newShareTree).getSize());
            asc.setParentFolderID(((SharedFile)newShareTree).getParentId());
            sendCommand(asc);
        }
    }
    
    private int receiveFolderId() throws CommandNotFoundException, IOException{
        int id = 0;
        boolean wait =true;
        while (wait) {
            String inputLine = incomingReader.readLine();
                if (inputLine != null) {
                    Commando c = CommandoParser.getInstance().parse(inputLine);
                    if(c instanceof ReceiveFolderIdCommando){
                        ReceiveFolderIdCommando rfic = (ReceiveFolderIdCommando)c;
                        id = rfic.getFolderId();
                        wait = false;
                    }
                }
            }
        return id;
    }
    
    private void startSynchronisation(){
        StartShareSynchronisationCommando sssc = new StartShareSynchronisationCommando();
        sssc.setAccountName(accountName);
        sendCommand(sssc);
    }
    
    public void sendCommand(Commando command){
        if(running){
            outputWriter.println(command.toString());
        }
    }
    
    public void stop(){
        running = false;
    }

}
