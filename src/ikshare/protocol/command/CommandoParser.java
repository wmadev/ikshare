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
        else if (commandoString.startsWith(bundle.getString("pong"))) {
            return new PongCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("findbasic"))) {
            return new FindBasicCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("nevermind"))) {
            return new NeverMindCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("getpeer"))) {
            return new GetPeerCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("foundresult"))) {
            return new FoundResultCommando(commandoString);
        }
        else if( commandoString.startsWith(bundle.getString("noresultsfound"))){
            return new NoResultsFoundCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("founditall"))) {
            return new FoundItAllCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("givepeer"))) {
            return new GivePeerCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("filerequest"))) {
            return new FileRequestCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("fileconfirm"))) {
            return new FileConfirmCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("filenotfound"))) {
            return new FileNotFoundCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("yourturn"))) {
            return new YourTurnCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("myturn"))) {
            return new MyTurnCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("passturn"))) {
            return new PassTurnCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("goforit"))) {
        	return new GoForItCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("getconn"))) {
            return new GetConnCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("giveconn"))) {
            return new GiveConnCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("createaccount"))) {
            return new CreateAccountCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("logon"))) {
            return new LogOnCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("logoff"))) {
            return new LogOffCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("createdaccount"))) {
            return new CreatedAccountCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("invalidregister"))) {
            return new InvalidRegisterCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("welcome"))) {
            return new WelcomeCommando(commandoString);
        }       
        else if (commandoString.startsWith(bundle.getString("lognilukni"))) {
            return new LogNiLukNiCommando(commandoString);
        }     
        else if (commandoString.startsWith(bundle.getString("canceltransfer"))) {
            return new CancelTransferCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("servererror"))){
            return new ServerErrorCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("startsharesync"))){
            return new StartShareSynchronisationCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("addshare"))){
            return new AddShareCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("receivefolderid"))){
            return new ReceiveFolderIdCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("endsharesync"))){
            return new EndShareSynchronisationCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("pausetransfer"))){
            return new PauseTransferCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("resumetransfer"))){
            return new ResumeTransferCommando(commandoString);
        }
        else
        	throw new CommandNotFoundException(commandoString);
    }
}
