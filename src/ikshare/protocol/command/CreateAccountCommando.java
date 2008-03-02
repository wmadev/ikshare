package ikshare.protocol.command;

public class CreateAccountCommando extends Commando {

    private String accountName,  email,  password;

    public CreateAccountCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setEmail(commandoLine.get(2));
        setPassword(commandoLine.get(3));
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        return commandoBundle.getString("createaccount")+commandoBundle.getString("commandoDelimiter")+getAccountName()+commandoBundle.getString("commandoDelimiter")+getEmail()+commandoBundle.getString("commandoDelimiter")+getPassword();
    }
    
}
