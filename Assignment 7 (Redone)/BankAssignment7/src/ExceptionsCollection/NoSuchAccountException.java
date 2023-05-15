package ExceptionsCollection;

public class NoSuchAccountException extends Exception{
    public NoSuchAccountException(String msg){
        super(msg);
    }
}
