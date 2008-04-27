/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain.event;

import ikshare.client.configuration.ClientConfiguration;
import ikshare.domain.Transfer;
import ikshare.domain.event.listener.ChatServerConversationListener;
import ikshare.domain.event.listener.ClientConfigurationListener;
import ikshare.domain.event.listener.FileTransferListener;
import ikshare.domain.event.listener.ServerConversationListener;
import ikshare.domain.event.listener.TransferQueueListener;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.chat.ChatHasEnteredRoomCommando;
import ikshare.protocol.command.chat.ChatHasLeftRoomCommando;
import ikshare.protocol.command.chat.ChatMessageCommando;
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
	
	private ArrayList<ChatServerConversationListener> chatServerConversationListeners;

	private EventController() {
		fileTransferListeners = new ArrayList<FileTransferListener>();
		serverConversationListeners = new ArrayList<ServerConversationListener>();
		clientConfigurationListeners = new ArrayList<ClientConfigurationListener>();
		transferQueueListeners = new ArrayList<TransferQueueListener>();
		chatServerConversationListeners = new ArrayList<ChatServerConversationListener>();
	}

	public static EventController getInstance() {
		if (instance == null)
			instance = new EventController();
		return instance;
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
	
	public void addChatServerConversationListener(ChatServerConversationListener l) {
        chatServerConversationListeners.add(l);
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
}
