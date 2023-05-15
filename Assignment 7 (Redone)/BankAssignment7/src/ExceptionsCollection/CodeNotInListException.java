package ExceptionsCollection;

public class CodeNotInListException extends Exception {
    public CodeNotInListException(String message){
        super(message);
    }
}
