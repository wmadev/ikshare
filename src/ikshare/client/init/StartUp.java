package ikshare.client.init;

import ikshare.client.gui.MainScreen;

public class StartUp {

	public static void main(String[] args) {
		
		new StartUp();
		
	}
	private StartUp()
	{
           
              MainScreen.getInstance();                
	}

}
