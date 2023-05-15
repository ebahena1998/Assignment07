package ExceptionsCollection;

public class AccountClosedException extends Exception {
    public AccountClosedException(String msg){
        super(msg);
    }
}
