import ExceptionsCollection.AccountClosedException;
import ExceptionsCollection.InsufficientBalanceException;

public class SavingsAccount extends Account{
    public SavingsAccount(Person customer){
        super("Savings", customer);

    }
    public SavingsAccount(Person customer, String ISOCode, double ISOValue){
        super("Savings", customer, ISOCode, ISOValue);
    }
    @Override
    public void withdraw(double amount) throws InsufficientBalanceException {
        if (getBalance() - amount < 0.0) {
            throw new InsufficientBalanceException("Not enough funds to cover withdrawal,\nCurrent Balance is: " +
                    getBalance());
        }
        super.withdraw(amount);
    }
}