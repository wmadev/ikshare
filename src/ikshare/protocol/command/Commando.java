package ikshare.protocol.command;

import ikshare.domain.ResourceBundleManager;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class Commando {
	private String commandoString;
    protected ArrayList<String> commandoLine;
    protected static ResourceBundle commandoBundle;
    private String commandoName;

    public Commando(String commandoString) {
        this();
        this.commandoString = commandoString;
        parse();
    }
    
    public Commando() {
        commandoBundle = ResourceBundleManager.getInstance().getCommandoBundle();
        commandoLine = new ArrayList<String>();
        commandoString = "";
    }

    private void parse() {
        StringTokenizer commandTokenizer = new StringTokenizer(commandoString, commandoBundle.getString("commandoDelimiter"));
        
        while (commandTokenizer.hasMoreTokens()) {
            commandoLine.add(commandTokenizer.nextToken());
        }
        commandoName = commandoLine.get(0);
        
    }
    
    public String getCommandoName() {
        return commandoName;
    }
    
    public String getCommandoString() {
        return commandoString;
    }
    
    @Override
    public String toString() {
        return getCommandoString();
    }
}
