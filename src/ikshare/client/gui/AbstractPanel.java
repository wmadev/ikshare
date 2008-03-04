package ikshare.client.gui;

import java.io.File;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 *
 * @author awosy
 */
public class AbstractPanel extends Composite {
        private String title;
	private Image icon;
	private ToolItem toolItem;
	private File file;
	
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

	public Image getIcon() {
                return icon;
	}
	
	public ToolItem getToolItem() {
		return toolItem;
	}

	public String getTitle() {
		return title;
	}
}
