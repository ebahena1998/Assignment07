import ExceptionsCollection.AccountClosedException;
import ExceptionsCollection.InsufficientBalanceException;

public class CheckingAccount extends Account{
    //Allows customer to withdraw more than the account balance
    //Negative balance is the overdraft limit;
    private double overdraftLimit;
    public CheckingAccount(Person customer, double overdraftLimit){
        //calls default super class constructor
      super("Checking", customer);
      //Overdraft will be a positive value when first set;
      this.overdraftLimit = overdraftLimit;
    }

    public CheckingAccount(Person customer, double overdraftLimit, String ISOCode, double ISOValue){
        super("Checking", customer, ISOCode, ISOValue);
        this.overdraftLimit = overdraftLimit;
    }

    //Debit Transaction represents withdrawal
    @Override
    public void withdraw(double amount) throws InsufficientBalanceException {
        //0, -200
        double currentBalance = getBalance();
        //500, 300
        double limit = this.overdraftLimit;

        //0 - 200, -200 - 250
        if (((currentBalance) - (amount)) <= 0.00) {
            //-200, -450
            currentBalance -= amount;
            //500 + -200 >= 0, 300 + -450 >=
            if(limit + currentBalance >= 0.00){
                //200
                super.withdraw(amount);

            } else {

                throw new InsufficientBalanceException("Not enough funds to cover withdrawal, overdraft is: "
                        + limit + "\nCurrent Balance is: " + (currentBalance + amount) + "\n" );
            }
        }
        else{
            super.withdraw(amount);
        }
    }


}