/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import ikshare.client.gui.AbstractPanel;

/**
 *
 * @author Jana
 */
public class ChatPanel extends AbstractPanel
{
    public ChatPanel(String text,String icon)
    {
        super(text,icon);
        
        GridLayout mainLayout = new GridLayout();
        mainLayout.numColumns = 2;
        this.setLayout(mainLayout);
        this.init();
    }
    
    private void init()
    {
    	//Public Rooms panel
    	Composite cmpPublicRooms = new Composite(this, SWT.NONE);
        GridData gdPublicRooms = new GridData(SWT.FILL, SWT.FILL, false, true, 100, 1);
        gdPublicRooms.horizontalIndent = gdPublicRooms.verticalIndent = 0;
        gdPublicRooms.widthHint = 200;
        cmpPublicRooms.setLayoutData(gdPublicRooms);
        
        StackLayout layoutPublicRooms = new StackLayout();
        cmpPublicRooms.setLayout(layoutPublicRooms);
        
    	Group grpPublicRooms = new Group(cmpPublicRooms, SWT.BACKGROUND);
    	grpPublicRooms.setText("Public Chatrooms");
    	grpPublicRooms.setLayout(new GridLayout(1, false));
    	GridData gdGrpRooms = new GridData(SWT.FILL, SWT.FILL, false, false);
    	gdGrpRooms.widthHint = 200;
    	grpPublicRooms.setLayoutData(gdGrpRooms);

        final List publicRoomsList = new List(grpPublicRooms, SWT.V_SCROLL | SWT.BORDER);
        GridData gdPublicRoomsList = new GridData(SWT.FILL,SWT.FILL,true,true);
        publicRoomsList.setLayoutData(gdPublicRoomsList);
        for(int i = 1; i < 25; i++)
        	publicRoomsList.add("test room " + i);
        
        layoutPublicRooms.topControl = grpPublicRooms;
        
        //Private Rooms panel
        Composite cmpPrivateRooms = new Composite(this, SWT.NONE);
        GridData gdPrivateRooms = new GridData(SWT.FILL, SWT.FILL, false, true, 100, 1);
        gdPrivateRooms.horizontalIndent = gdPrivateRooms.verticalIndent = 0;
        gdPrivateRooms.widthHint = 200;
        cmpPrivateRooms.setLayoutData(gdPrivateRooms);
        
        StackLayout layoutPrivateRooms = new StackLayout();
        cmpPrivateRooms.setLayout(layoutPrivateRooms);
        
    	Group grpPrivateRooms = new Group(cmpPrivateRooms, SWT.BACKGROUND);
    	grpPrivateRooms.setText("Private Chatrooms");
    	grpPrivateRooms.setLayout(new GridLayout(1, false));
    	GridData gdGrpPrivateRooms = new GridData(SWT.FILL, SWT.FILL, false, false);
    	gdGrpPrivateRooms.widthHint = 200;
    	grpPrivateRooms.setLayoutData(gdGrpPrivateRooms);
        
    	Label lblPrivateRoomName = new Label(grpPrivateRooms, SWT.None);
    	lblPrivateRoomName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    	lblPrivateRoomName.setText("Room name");
    	
    	Text txtPrivateRoomName = new Text(grpPrivateRooms, SWT.BORDER);
    	txtPrivateRoomName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
    	
        layoutPrivateRooms.topControl = grpPrivateRooms;
        
        //Chat screen
        
    }
}
