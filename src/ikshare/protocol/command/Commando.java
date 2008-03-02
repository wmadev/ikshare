package ikshare.protocol.command;

import ikshare.domain.ResourceBundleManager;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class Commando {

    private String commandoString;
    private ArrayList<String> commandoLine;
    private static ResourceBundle commandoBundle;

    public Commando(String commandoString) {
        this.commandoString = commandoString;
        commandoBundle = ResourceBundleManager.getCommandoBundle();
        commandoLine = new ArrayList<String>();
        parse();
    }

    private void parse() {
        StringTokenizer commandTokenizer = new StringTokenizer(commandoString, commandoBundle.getString("commandoDelimiter"));
        
        while (commandTokenizer.hasMoreTokens()) {
            commandoLine.add(commandTokenizer.nextToken());
        }
        

    }
}
