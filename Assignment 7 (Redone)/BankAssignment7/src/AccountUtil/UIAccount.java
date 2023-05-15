package AccountUtil;

public class UIAccount {
    private static int accountCounter = 100;

    public static int getAccountCounter(){
        return (accountCounter++);
    }
}
