import ExceptionsCollection.AccountClosedException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import AccountUtil.UIAccount;
import ExceptionsCollection.InsufficientBalanceException;

public class Account{
    private String accountName;
    private Person customer;
    private ArrayList<Transactions> accountTransactions;
    private int accountId;
    private boolean status;

    private String ISOCode;
    private double ISOValue;
    public Account(String accountName, Person customer){
        this.accountName = accountName;
        this.customer = customer;
        this.accountTransactions = new ArrayList<Transactions>();
        //set to 100
        this.accountId = UIAccount.getAccountCounter();
        this.status = true;

        //Added
        this.ISOCode = "USD";
        this.ISOValue = 1.00;
    }

    public Account(String accountName, Person customer, String ISOCode, double ISOValue){
        this.accountName = accountName;
        this.customer = customer;
        this.accountTransactions = new ArrayList<Transactions>();
        this.accountId = UIAccount.getAccountCounter();
        this.status = true;

        this.ISOCode = ISOCode;
        this.ISOValue = ISOValue;
    }
    //Setters

    //Getters
    public String getAccountName(){
        return (this.accountName);
    }
    public Person getCustomerInfo(){
        return (this.customer);
    }
    public boolean getStatus(){
        return (this.status);
    }
    public void close(){
        this.status = false;
    }
    public String getISOCode(){
        return (this.ISOCode);
    }
    public double getISOValue(){
        return (this.ISOValue);
    }
    public double getBalance(){
        double totalBalance = 0.0;
        for(Transactions temp: this.accountTransactions){
            if(temp.getType().equalsIgnoreCase("Credit")){
                totalBalance += temp.getTransactionAmount();
            }
            else{
                totalBalance -= temp.getTransactionAmount();
            }
        }
        return (totalBalance);
    }

    public double getCurrencyBalance(){
        double currencyBalance = 0.00;
        currencyBalance = getBalance();
        currencyBalance /= getISOValue();
        return currencyBalance;
    }
    public int getAccountId(){
        return (this.accountId);
    }

    //Credit Transaction represents deposit

    public void deposit(double amount) throws AccountClosedException {
        double balance = getBalance();
        //if the account is closed and the balance is greater than or equal to 0.00;
        if (!(getStatus()) && (balance >= 0.00)){
            //Then you cannot deposit into the account
            throw new AccountClosedException("Account is closed, deposits not allowed!\n");
        }
        accountTransactions.add(new Transactions("Credit", amount));
    }

    //Debit Transaction represents withdrawal
    public void withdraw(double amount) throws InsufficientBalanceException {
        double balance = getBalance();
        //if the account is closed and the balance is greater than or equal to 0.00;
        if(!(getStatus()) && (balance <= 0.00)){
            //Then you cannot withdraw from the account since you do not have enough funds
            throw new InsufficientBalanceException("Account is closed, withdraws not allowed!\n");
        }
        accountTransactions.add(new Transactions("Debit", amount));
    }

    //METHODS

    public void getTransactions(OutputStream out) throws IOException{
        //ASCII code for newline
        out.write("TRANSACTIONS".getBytes());
        out.write((byte)10);
        for(Transactions temp : this.accountTransactions){
            out.write(temp.toString().getBytes());
            out.write((byte)10);
        }
        out.write(("Balance: " + getBalance()).getBytes());
        out.write((byte)10);
        out.flush();
        return;
    }
    public void printInfo(OutputStream out) throws IOException{
        out.write((byte)10);
        out.write("Account Number: ".getBytes());
        String accountNum = "" + this.accountId;
        out.write(accountNum.getBytes());
        out.write((byte)10);

        out.write("Name: ".getBytes());
        out.write(customer.getFirstName().getBytes());
        out.write(" ".getBytes());
        out.write(customer.getLastName().getBytes());
        out.write((byte)10);
        out.write("SSN: ".getBytes());
        out.write(customer.getSSN().getBytes());
        out.write((byte)10);
        out.write("Currency: ".getBytes());
        out.write(ISOCode.getBytes());
        out.write((byte)10);
        out.write("Currency Balance: ".getBytes());
        out.write(ISOCode.getBytes());
        String cBalance = String.format(" %,.2f", getCurrencyBalance());
        out.write(cBalance.getBytes());
        out.write((byte)10);
        out.write("USD Balance: ".getBytes());
        String newBalance = String.format(" %,.2f", getBalance());
        out.write("USD".getBytes());
        out.write(newBalance.getBytes());
        out.write((byte)10);
        out.write((byte)10);
        out.flush();
        return;

    }


    public String toString() {
        String accountInfo = "";
        accountInfo += String.format("%d(%s) : %s : ", getAccountId(), getAccountName(), getCustomerInfo().toString());
        accountInfo += String.format("%s : %,.2f : ", getISOCode(), getCurrencyBalance());
        accountInfo += String.format("%,.2f : %s", getBalance(), (getStatus() ? "Account Open" : "Account Closed"));
        return accountInfo;
    }


}