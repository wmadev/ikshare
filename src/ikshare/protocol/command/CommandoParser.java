package ikshare.protocol.command;

import ikshare.domain.ResourceBundleManager;
import ikshare.protocol.exception.*;
import java.util.ResourceBundle;

public class CommandoParser {

    private static ResourceBundle bundle;
    private static CommandoParser instance;

    protected CommandoParser() {
        bundle = ResourceBundleManager.getInstance().getCommandoBundle();
    }
    
    public static CommandoParser getInstance() {
        if (instance==null)
            instance = new CommandoParser();
        return instance;
    }

    public Commando parse(String commandoString) throws CommandNotFoundException {
        if (commandoString.startsWith(bundle.getString("ping"))) {
            return new PingCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("pong"))) {
            return new PongCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("find"))) {
            return new FindCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("nevermind"))) {
            return new NeverMindCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("getpeer"))) {
            return new GetPeerCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("found"))) {
            return new FoundCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("founditall"))) {
            return new FoundItAllCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("givepeer"))) {
            return new GivePeerCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("filerequest"))) {
            return new FileRequestCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("fileconfirm"))) {
            return new FileConfirmCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("filenotfound"))) {
            return new FileNotFoundCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("yourturn"))) {
            return new YourTurnCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("myturn"))) {
            return new MyTurnCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("passturn"))) {
            return new PassTurnCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("getconn"))) {
            return new GetConnCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("giveconn"))) {
            return new GiveConnCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("createaccount"))) {
            return new CreateAccountCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("checkaccount"))) {
            return new CheckAccountCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("logon"))) {
            return new LogOnCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("logoff"))) {
            return new LogOffCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("createdaccount"))) {
            return new CreatedAccountCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("invalidregister"))) {
            return new InvalidRegisterCommando(commandoString);
        }
        if (commandoString.startsWith(bundle.getString("checkedaccount"))) {
            return new CheckedAccountCommando(commandoString);
        }        
        if (commandoString.startsWith(bundle.getString("welcome"))) {
            return new WelcomeCommando(commandoString);
        }       
        if (commandoString.startsWith(bundle.getString("lognilukni"))) {
            return new LogNiLukNiCommando(commandoString);
        }        
        throw new CommandNotFoundException(commandoString);
    }
}
