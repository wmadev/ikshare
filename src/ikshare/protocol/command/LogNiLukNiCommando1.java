package ikshare.protocol.command;

public class LogNiLukNiCommando1 extends Commando {

    private String accountName;

    public LogNiLukNiCommando1(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
