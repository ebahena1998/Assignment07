import java.io.Serializable;
import java.util.ArrayList;

import UITransaction.UtilTransaction;

public class Transactions {
    private String type;
    private int id;
    double transactionAmount;
    public Transactions(String type, double transactionAmount){
        this.type = type;
        this.transactionAmount = transactionAmount;
        this.id = UtilTransaction.getCounterId();
    }
    //Getters
    public String getType(){
        return (this.type);
    }
    public double getTransactionAmount(){
        return (this.transactionAmount);
    }
    //Methods
    public String toString(){
        String tranInfo = "";
        if(this.type.equals("Credit")){
            tranInfo += String.format("%d: %s : %.2f", this.id, "Deposit(Credit)", this.transactionAmount);
        }
        else{
            tranInfo += String.format("%d: %s : %.2f", this.id, "Withdrawal(Debit)", this.transactionAmount);
        }

        return tranInfo;
    }

}