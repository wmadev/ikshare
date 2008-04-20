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
import ikshare.protocol.command.DeleteShareCommando;
import ikshare.protocol.command.EndShareSynchronisationCommando;
import ikshare.protocol.command.ReceiveFolderIdCommando;
import ikshare.protocol.command.StartShareSynchronisationCommando;
import ikshare.protocol.exception.CommandNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
            boolean firstTimeSync = false;
            if(oldShareTree != null){
                if(((SharedFolder)newShareTree).getName().equals(((SharedFolder)oldShareTree).getName()) 
                        && ((SharedFolder)newShareTree).getPath().equals(((SharedFolder)oldShareTree).getPath())){
                    firstTimeSync = false;
                    oldShareTree.setState(SharedItemState.UNCHANGED);
                    newShareTree.setState(SharedItemState.UNCHANGED);
                    newShareTree.setParentId(oldShareTree.getParentId());
                    ((SharedFolder)newShareTree).setFolderID(((SharedFolder)oldShareTree).getFolderID());
                }
                else{
                    firstTimeSync = true;
                    newShareTree.setState(SharedItemState.ADDED);
                    oldShareTree.setState(SharedItemState.DELETED);
                }
            }else{
                firstTimeSync = true;
                newShareTree.setState(SharedItemState.ADDED);
            }
            
            if(firstTimeSync){
                sendCompleteTree(newShareTree);
                if(oldShareTree!=null){
                    sendCompleteTree(oldShareTree);
                }
                //printTree(newShareTree);
            }
            else{
                // If not, check for additions
                checkForAdditions(newShareTree,oldShareTree);
                // Check for deletions and add them to the newShareTree
                //checkForDeletions(newShareTree,oldShareTree);
                // Send the old tree to the server to for deletions
                //sendCompleteTree(oldShareTree);
                // Send the new tree to the server for additions
                //printTree(newShareTree);
                sendCompleteTree(newShareTree);
                //printTree(newShareTree);
                
                //printTree(oldShareTree);
            }
            endSynchronisation();
            serverConnection.close();
            
            saveNewTree(newShareTree);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void checkForAdditions(SharedItem newShareTree, SharedItem oldShareTree) {
        newShareTree.setParentId(oldShareTree.getParentId());
        newShareTree.setState(SharedItemState.UNCHANGED);
        // When the new item is a folder
        if(newShareTree.isFolder()){
            // Check if it's content is equal to the old
            // For all items in the new tree, check if the item is in the old tree
            // When not, set state in new tree as added
            for(Map.Entry item: ((SharedFolder)newShareTree).getContent().entrySet()){
                ((SharedItem)item.getValue()).setParentId(((SharedFolder)oldShareTree).getFolderID());
                // Check if the item is in the old tree
                if(((SharedFolder)oldShareTree).getContent().containsKey(item.getKey())){
                    ((SharedItem)item.getValue()).setState(SharedItemState.UNCHANGED);
                    // When the item is a folder, recall method to check additions in the folder
                    if(((SharedItem)item.getValue()).isFolder()){
                        //((SharedFolder)item.getValue()).setPath(((SharedFolder)oldShareTree).getPath()+((SharedFolder)item.getValue()).getPath());
                        ((SharedFolder)item.getValue()).setFolderID(((SharedFolder)((SharedFolder)oldShareTree).getContent().get(item.getKey())).getFolderID());
                        checkForAdditions((SharedFolder)item.getValue(), ((SharedFolder)oldShareTree).getContent().get(item.getKey()));
                    }
                }
                // The item was added, maybe it's a folder, maybe a file, does not mather
                else{
                    ((SharedItem)item.getValue()).setState(SharedItemState.ADDED);
                    
                }
            }
        }
        else{
            if(!((SharedFile)newShareTree).equals((SharedFile)oldShareTree)){
                newShareTree.setState(SharedItemState.ADDED);
            }
            else{
                newShareTree.setState(SharedItemState.UNCHANGED);
            }
        }
    }

    private void checkForDeletions(SharedItem newShareTree, SharedItem oldShareTree) {
       //WERKT, maar enkel bestanden en directories uit de root dir worden aangetekend als verwijderd.
       //Bestanden worden effectief verwijderd uit server, mappen niet. (fout in query ? niet duidelijk)
       if(oldShareTree.isFolder()){
           // Voor ieder element uit de oude structuur 
           for(Map.Entry item: ((SharedFolder)oldShareTree).getContent().entrySet()){
                // Kijk of de nieuwe structuur het bevat
                if(((SharedFolder)newShareTree).getContent().containsKey(item.getKey())){
                    // Zoja, state = unchanged
                    ((SharedItem)item.getValue()).setState(SharedItemState.UNCHANGED);
                    // Indien element een map is, kijk verder op verwijderingen in die map.
                    if(((SharedItem)item.getValue()).isFolder()){
                        checkForDeletions((SharedFolder)item.getValue(), ((SharedFolder)newShareTree).getContent().get(item.getKey()));
                    }
                }
                else{
                    // Zonee, state = deleted
                    ((SharedItem)item.getValue()).setState(SharedItemState.DELETED);
                    
                }
            }
        }
        else{
            if(!((SharedFile)oldShareTree).equals((SharedFile)newShareTree)){
               oldShareTree.setState(SharedItemState.DELETED);
            }
        }
        
        
        
        
        
        
       
       
    }

    private void endSynchronisation() {
        EndShareSynchronisationCommando essc = new EndShareSynchronisationCommando();
        sendCommand(essc);
    }

    private void printTree(SharedItem root) {
        if(root.isFolder()){
            System.out.println("Folder: "+root.getState()+" : "+((SharedFolder)root).getFolderID()+" : "+
                    ((SharedFolder)root).getParentId()+" : "+
                    ((SharedFolder)root).getName());
            Iterator it = ((SharedFolder)root).getContent().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry item = (Map.Entry)it.next();
                printTree((SharedItem)item.getValue());
            }
        }
        else {
            System.out.println("File: "+root.getState()+" : "+((SharedFile)root).getParentId()+" : "+
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
        }
        return root;
    }
    
    private SharedItem addSharedItemToTree(File file,SharedItemState state){
        SharedItem newItem = null;
        if(file.isDirectory()){
            String root = ClientConfigurationController.getInstance().getConfiguration().getSharedFolder().getPath();
            int index = file.getPath().indexOf(root)+root.length();
            newItem = new SharedFolder(true,file.getPath().substring(index));
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

    private void saveNewTree(SharedItem newShareTree) {
        
        File saveFile = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"ikshare"+System.getProperty("file.separator")+"_stsf.iks");
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(saveFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(newShareTree);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                oos.close();
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendCompleteTree(SharedItem newShareTree) throws CommandNotFoundException, IOException {
        if(newShareTree.isFolder()){
            if(newShareTree.getState()==SharedItemState.ADDED){
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
            else if(newShareTree.getState() == SharedItemState.DELETED){
                DeleteShareCommando dsc = new DeleteShareCommando();
                dsc.setDirectory(true);
                dsc.setFolderId(((SharedFolder)newShareTree).getFolderID());
                sendCommand(dsc);
            }
            else if(newShareTree.getState() == SharedItemState.UNCHANGED){
                Iterator it = ((SharedFolder)newShareTree).getContent().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry item = (Map.Entry)it.next();
                    ((SharedItem)item.getValue()).setParentId(((SharedFolder)newShareTree).getFolderID());
                    sendCompleteTree((SharedItem)item.getValue());
                }
            }
        }
        else {
            if(newShareTree.getState()==SharedItemState.ADDED){
                AddShareCommando asc = new AddShareCommando();
                asc.setDirectory(false);
                asc.setName(((SharedFile)newShareTree).getName());
                asc.setSize(((SharedFile)newShareTree).getSize());
                asc.setParentFolderID(((SharedFile)newShareTree).getParentId());
                sendCommand(asc);
            }
            else if(newShareTree.getState() == SharedItemState.DELETED){
                DeleteShareCommando dsc = new DeleteShareCommando();
                dsc.setDirectory(false);
                dsc.setParentFolderID(((SharedFile)newShareTree).getParentId());
                dsc.setName(((SharedFile)newShareTree).getName());
                dsc.setSize(((SharedFile)newShareTree).getSize());
                sendCommand(dsc);
            }
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
