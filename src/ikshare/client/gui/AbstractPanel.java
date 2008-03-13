package ikshare.client.gui;

import java.io.File;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;


public class AbstractPanel extends Composite {
        private String title;
	private Image icon;
	private ToolItem toolItem;
	private File file;
        
	/*
         * Makes an abstract panel, all the other panels must inherit from this panel
         * @param title     The title of the panel
         * @param icon      The icon that presents the meaning of the panel
        */
	public AbstractPanel( String title, String icon) {
		super(MainScreen.getInstance().getParent(), SWT.PUSH);
		this.title = title;
		file = new File(icon);
		if(file.exists()){
                    this.icon = new Image(Display.getCurrent(), icon);
                }
		this.toolItem = new ToolItem (MainScreen.getInstance().getToolBar(), SWT.RADIO);
                toolItem.setWidth(60);
                toolItem.setText(getTitle());
		toolItem.setImage(getIcon());	
        }
        
        /*
         * Returns the icon that presents the meaning of the panel
         * @return icon     The icon
         */
	public Image getIcon() {
                return icon;
	}
	
        /*
         * Returns the item in the toolbar that belongs to the panel
         * @return toolItem     The toolItem
         */
	public ToolItem getToolItem() {
		return toolItem;
	}

        /*
         * Returns the title that is being used in de toolbar
         * @return title     The title
         */
	public String getTitle() {
		return title;
	}
}
