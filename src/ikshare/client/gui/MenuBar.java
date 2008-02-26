/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 *
 * @author awosy
 */
public class MenuBar{
    private Menu menuBar;
    
    public MenuBar(Shell shell,int flags){
        menuBar = new Menu(shell,flags);
        MenuItem file = new MenuItem(menuBar,SWT.CASCADE);
        file.setText("File");
    }
    public Menu getMenu(){
        return menuBar;
    }
        

}
