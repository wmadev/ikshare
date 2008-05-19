package ikshare.client.gui.dialogs;

public class CreateAccountDialogData {
    private String accountName;
    private String accountPassword;
    private String accountEmail;
    
    private boolean buttonCreate;
    
    public CreateAccountDialogData(){
        accountName = accountPassword = accountEmail = "";
        buttonCreate = false;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public boolean isButtonCreate() {
        return buttonCreate;
    }

    public void setButtonCreate(boolean buttonCreate) {
        this.buttonCreate = buttonCreate;
    }
}