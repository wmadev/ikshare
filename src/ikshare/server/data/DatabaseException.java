package ikshare.server.data;

public class DatabaseException extends Exception {
    public DatabaseException(String message){
        super(message);
    }
    
    public String toString(){
        return this.getMessage();
    }
}
