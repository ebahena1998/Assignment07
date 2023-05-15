package UITransaction;

public class UtilTransaction {
    private static int T_COUNTER_ID = 1;
    public static int getCounterId(){
        return (T_COUNTER_ID++);
    }
}
