/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.init;

import ikshare.client.gui.MainScreen;

public class StartUp {

	public static void main(String[] args) {
		
		new StartUp();
		
	}
	private StartUp()
	{

                if(System.getProperty("os.name").startsWith("L"))
                    System.setProperty("file.seperator","/");
                else
                    System.setProperty("file.seperator","\\");

		MainScreen.getInstance();                
	}

}
