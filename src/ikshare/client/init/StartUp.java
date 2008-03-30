/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.init;

import ikshare.client.ClientController;
import ikshare.client.gui.MainScreen;
import ikshare.client.threads.ServerConversationThread;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartUp {

	public static void main(String[] args) {
		
		new StartUp();
		
	}
	private StartUp()
	{
           
              MainScreen.getInstance();                
	}

}
