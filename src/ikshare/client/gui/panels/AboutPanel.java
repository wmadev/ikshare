/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.panels;

import ikshare.client.gui.AbstractPanel;
import ikshare.client.gui.configuration.ConfigurationController;
import java.io.File;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class AboutPanel extends AbstractPanel {
    private Composite parent;
    
    public AboutPanel(String title,String icon){	
    super(title, icon);
		GridLayout layout = new GridLayout(1,false);
		this.setLayout(layout);
		GridData gridData = new GridData(SWT.FILL,SWT.FILL,true,true);
		parent = new Composite(this,SWT.NONE);
		parent.setLayoutData(gridData);
		parent.setLayout(new GridLayout(1,false));
		this.initPanel();
	}
	
	private void initPanel()
	{
		GridData gd0 = new GridData(SWT.FILL,SWT.FILL,true,false);
		gd0.heightHint = 10;
		Composite spacer = new Composite(parent,SWT.None);
		spacer.setLayoutData(gd0);
		GridData gd1 = new GridData(SWT.FILL,SWT.FILL,true,false);
		gd1.heightHint = 50;
		String logo = "resources/logo.png";
		File file = new File(logo);
		/*if(file.exists()) {
			Image image = new Image(Display.getCurrent(), logo);
			Label label = new Label(parent, SWT.CENTER);
			label.setImage(image);
			label.setLayoutData(gd1);
		}*/
		GridData gd2 = new GridData(SWT.FILL,SWT.FILL,true,false);
		gd2.heightHint = 30;
		Label atopeer = new Label(parent, SWT.CENTER);
		atopeer.setLayoutData(gd2);
		atopeer.setText(ConfigurationController.getInstance().getString("ikshare"));
		FontData data = Display.getCurrent().getSystemFont().getFontData()[0];
		Font newFont = new Font(Display.getCurrent(), data.getName(), 20, SWT.BOLD);
		atopeer.setFont(newFont);
		GridData gd3 = new GridData(SWT.FILL,SWT.FILL,true,false);
		gd3.heightHint = 400;
		Label versie = new Label(parent, SWT.CENTER);
		versie.setLayoutData(gd3);
		String output ="\nJava v "+System.getProperty("java.version")+
				"	\nSWT v "+SWT.getVersion()+" \n "+System.getProperty("os.name")+" "
					+System.getProperty("os.version")+"("+System.getProperty("os.arch")+")"+
					"\n\n Jana De Four \nWard Maenhout\n Jonas Wille  \nBoris Martens\n\n";
		output+="\n Please report all bugs/suggestions/comments to following emailaddresses:\n\n" +
				"\njanadefour@telenet.be\nward@awosy.be \n jonaswille@gmail.com\nboris.martens@gmail.com";
		
		versie.setText(output);
	}
}
