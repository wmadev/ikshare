/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain.event;

import ikshare.client.configuration.ClientConfiguration;
import ikshare.domain.Transfer;
import ikshare.domain.event.listener.ChatServerConversationListener;
import ikshare.domain.event.listener.ClientConfigurationListener;
import ikshare.domain.event.listener.ClientControllerListener;
import ikshare.domain.event.listener.FileTransferListener;
import ikshare.domain.event.listener.SelectedMediaFileListener;
import ikshare.domain.event.listener.ServerConversationListener;
import ikshare.domain.event.listener.TransferQueueListener;
import ikshare.protocol.command.Commando;


import java.io.File;

import ikshare.protocol.command.chat.ChatHasEnteredRoomCommando;
import ikshare.protocol.command.chat.ChatHasLeftRoomCommando;
import ikshare.protocol.command.chat.ChatInvalidRoomPasswordCommando;
import ikshare.protocol.command.chat.ChatLogNiLukNiCommando;
import ikshare.protocol.command.chat.ChatMessageCommando;
import ikshare.protocol.command.chat.ChatRoomDoesNotExistCommando;
import ikshare.protocol.command.chat.ChatUpdateRoomsListCommando;
import ikshare.protocol.command.chat.ChatWelcomeCommando;
import ikshare.protocol.command.chat.ChatYouEnterRoomCommando;

import java.util.ArrayList;

/**
 *
 * @author awosy
 */
public class EventController {
	private static EventController instance;

	private ArrayList<FileTransferListener> fileTransferListeners;

	private ArrayList<ServerConversationListener> serverConversationListeners;

	private ArrayList<ClientConfigurationListener> clientConfigurationListeners;
	
	private ArrayList<TransferQueueListener> transferQueueListeners;

	private ArrayList<SelectedMediaFileListener> selectedMediaFileListener;

	private ArrayList<ChatServerConversationListener> chatServerConversationListeners;

        private ArrayList<ClientControllerListener> clientControllerListeners;
        
	private EventController() {
		fileTransferListeners = new ArrayList<FileTransferListener>();
		serverConversationListeners = new ArrayList<ServerConversationListener>();
		clientConfigurationListeners = new ArrayList<ClientConfigurationListener>();
		transferQueueListeners = new ArrayList<TransferQueueListener>();
		selectedMediaFileListener = new ArrayList<SelectedMediaFileListener>();
		chatServerConversationListeners = new ArrayList<ChatServerConversationListener>();
                clientControllerListeners = new ArrayList<ClientControllerListener>();
	}

	public static EventController getInstance() {
		if (instance == null)
			instance = new EventController();
		return instance;
	}
        public void addClientControllerListener(ClientControllerListener l){
            clientControllerListeners.add(l);
        }

	public void addFileTransferListener(FileTransferListener l) {
		fileTransferListeners.add(l);
	}

	public void addServerConversationListener(ServerConversationListener l) {
		serverConversationListeners.add(l);
	}

	public void addClientConfigurationListener(ClientConfigurationListener l) {
		clientConfigurationListeners.add(l);
	}
	
	public void addTransferQueueListener(TransferQueueListener l) {
		transferQueueListeners.add(l);
	}

	
	public void addSelectedMediaFileListener(SelectedMediaFileListener l) {
		selectedMediaFileListener.add(l);
	}

	
	public void addChatServerConversationListener(ChatServerConversationListener l) {
        chatServerConversationListeners.add(l);
	}
        
        public void triggerLoggedOnEvent(){
            for(ClientControllerListener l : clientControllerListeners){
                l.onLogOn();
            }
        }

        public void triggerLogOnFailedEvent(String message){
            for(ClientControllerListener l : clientControllerListeners){
                l.onLogOnFailed(message);
            }
        }
        
        
	public void triggerConfigurationUpdatedEvent(ClientConfiguration config) {
		for (ClientConfigurationListener listener : clientConfigurationListeners) {
			listener.update(config);
		}
	}

	public void triggerDownloadCanceledEvent(Transfer transfer) {
		//System.out.println("EVENT: Trigger download canceled");
		for (FileTransferListener listener : fileTransferListeners) {
			listener.transferCanceled(transfer);
		}
	}

	public void triggerDownloadStartedEvent(Transfer transfer) {
		//System.out.println("EVENT: Trigger download started");
		for (FileTransferListener listener : fileTransferListeners) {
			listener.transferStarted(transfer);
		}
	}

	public void triggerDownloadStateChangedEvent(Transfer transfer) {
		//System.out.println("EVENT: Trigger download state changed");
		for (FileTransferListener listener : fileTransferListeners) {
			listener.transferStateChanged(transfer);
		}
	}

	public void triggerDownloadFinishedEvent(Transfer transfer) {
		//System.out.println("EVENT: Trigger download finished");
		for (FileTransferListener listener : fileTransferListeners) {
			listener.transferFinished(transfer);
		}
	}

	public void triggerDownloadFailedEvent(Transfer transfer) {
		//System.out.println("EVENT: Trigger download finished");
		for (FileTransferListener listener : fileTransferListeners) {
			listener.transferFailed(transfer);
		}
	}

	public void triggerCommandoReceivedEvent(Commando c) {
		for (ServerConversationListener listener : serverConversationListeners) {
			listener.receivedCommando(c);
		}
	}

	public void triggerDownloadPausedEvent(Transfer transfer) {
		for (FileTransferListener listener : fileTransferListeners) {
			listener.transferPaused(transfer);
		}
	}

	public void triggerDownloadResumedEvent(Transfer transfer) {
		for (FileTransferListener listener : fileTransferListeners) {
			listener.transferResumed(transfer);
		}
	}
	
	public void triggerClearTransfers() {
		for (FileTransferListener listener : fileTransferListeners) {
			listener.transfersCleared();
		}
	}
	
	public void triggerActiveDownloadsChanged() {
		for (TransferQueueListener listener : transferQueueListeners) {
			listener.activeDownloadsChanged();
		}
	}
	
	public void triggerActiveUploadsChanged() {
		for (TransferQueueListener listener : transferQueueListeners) {
			listener.activeUploadsChanged();
		}
	}

	public void triggerSelectedMP3FileChanged(File mp3File) {
		for (SelectedMediaFileListener listener: selectedMediaFileListener) {
			listener.selectedMP3FileChanged(mp3File);
		}
	}
	
	public void triggerSelectedMPEGFileChanged(File mpegFile) {
		for (SelectedMediaFileListener listener: selectedMediaFileListener) {
			listener.selectedMPEGFileChanged(mpegFile);
		}
	}


	public void triggerYouLoggedOn(ChatWelcomeCommando c)
	{
		for (ChatServerConversationListener listener : chatServerConversationListeners)
		{
			listener.youLoggedOn(c);
		}
	}
	
    public void triggerUserLeavingRoom(ChatHasLeftRoomCommando c) {
        for (ChatServerConversationListener listener : chatServerConversationListeners)
        {
            listener.userLeftRoom(c);
        }
    }
    
    public void triggerUserEnteringRoom(ChatHasEnteredRoomCommando c) {
        for (ChatServerConversationListener listener : chatServerConversationListeners)
        {
            listener.userEntersRoom(c);
        }
    }


    public void triggerReceivingMessage(ChatMessageCommando c) {
        for (ChatServerConversationListener listener : chatServerConversationListeners)
        {
            listener.receivedMessage(c);
        }
    }
    
    public void triggerYouEnterRoom(ChatYouEnterRoomCommando c) {
        for (ChatServerConversationListener listener : chatServerConversationListeners)
        {
            listener.youEnterRoom(c);
        }
    }

	public void triggerInvalidRoomPassword(ChatInvalidRoomPasswordCommando c) {
        for (ChatServerConversationListener listener : chatServerConversationListeners)
        {
            listener.invalidRoomPassword(c);
        }
	}

	public void triggerRoomDoesNotExist(ChatRoomDoesNotExistCommando c) {
		for(ChatServerConversationListener listener : chatServerConversationListeners)
		{
			listener.chatRoomDoesNotExist(c);
		}
	}
	
	public void triggerChatServerInterupt(String message){
		for(ChatServerConversationListener listener : chatServerConversationListeners)
		{
			listener.chatServerInterupt(message);
		}
	}

	public void triggerRoomsUpdate(ChatUpdateRoomsListCommando c) {
		for(ChatServerConversationListener listener : chatServerConversationListeners)
		{
			listener.chatRoomsUpdate(c);
		}
	}

	public void triggerLogNiLukNi(ChatLogNiLukNiCommando c) {
		for(ChatServerConversationListener listener : chatServerConversationListeners)
		{
			listener.logNiLukNi(c);
		}
	}
}
