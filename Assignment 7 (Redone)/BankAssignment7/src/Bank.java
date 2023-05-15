import ExceptionsCollection.AccountClosedException;
import ExceptionsCollection.CodeNotInListException;
import ExceptionsCollection.InsufficientBalanceException;
import ExceptionsCollection.NoSuchAccountException;
import BankUI.BankUtility;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class Bank{
    private static Map<Integer,Account> accountList =new TreeMap<Integer,Account>();
    private static Map<String, Double> exchangeRateList = new TreeMap<String, Double>();
    private static HttpURLConnection response;
    //private static File exchangeRateFile;
    public static boolean status = false;

    public static HttpURLConnection requestHttp(String name) throws IOException, InterruptedException {
        URL url = new URL(name);
        HttpURLConnection hr = (HttpURLConnection) url.openConnection();
        if(hr.getResponseCode() != 200){
            throw new IOException("Error, Response Code is not 200");
        }
        status = true;
        return hr;
    }
    public static void storeKeyValues(InputStream in) throws IOException{
        String line = "";
        int i;
        while((i = in.read()) != -1){
            if(i < 127){
                line += (char) i;
            }
            if(i == 10){
                String[] text = line.split(",");
                exchangeRateList.put(text[0], Double.parseDouble(text[2]));
                line = "";
            }
        }
    }
    public static boolean checkExchangeList(String input){
        if(exchangeRateList.containsKey(input.toUpperCase())){
            return true;
        }
        return false;
    }
    //TESTING MAP OF KEY-VALUES
    public static void printMAPLIST(){
        for(String key : exchangeRateList.keySet()){
            System.out.println("KEY: " + key + " VALUE: " + exchangeRateList.get(key));
        }
    }

    public static Account createCheckingAccount(Person customer, double overdraft){
        Account bankCheckingAccount = new CheckingAccount(customer, overdraft);
        accountList.put(bankCheckingAccount.getAccountId(), bankCheckingAccount);
        return bankCheckingAccount;
    }

    public static Account createCheckingAccount(Person customer, double overdraft, String ISOCode) {
        Account bankCheckingAccount = new CheckingAccount(customer, overdraft, ISOCode ,exchangeRateList.get(ISOCode));
        accountList.put(bankCheckingAccount.getAccountId(), bankCheckingAccount);
        return (bankCheckingAccount);
    }

    public static Account createSavingsAccount(Person customer){
        Account bankSavingsAccount = new SavingsAccount(customer);
        accountList.put(bankSavingsAccount.getAccountId(), bankSavingsAccount);
        return bankSavingsAccount;
    }
    public static Account createSavingsAccount(Person customer, String ISOCode){
        Account bankSavingsAccount = new SavingsAccount(customer, ISOCode, exchangeRateList.get(ISOCode));
        accountList.put(bankSavingsAccount.getAccountId(), bankSavingsAccount);
        return bankSavingsAccount;
    }
    public static Account findAccount(int userInput) throws NoSuchAccountException{
        if(!accountList.containsKey(userInput))
            throw new NoSuchAccountException(BankUtility.ACCOUNT_NOT_FOUND);
        else
            return (accountList.get(userInput));
    }
    //If case 3

    public static void heldAccounts(OutputStream out) throws IOException, NoSuchAccountException{
        //ascii code 10 is a new line
        out.write((byte)10);
        Collection<Account> col = accountList.values();
        for (Account a : col) {
            out.write(a.toString().getBytes());
            out.write((byte)10);
        }
        out.write((byte)10);
        out.flush();
        return;
    }

    //If case 4
    public static void printAccountTransactions(int userInput, OutputStream out) throws IOException,
            NoSuchAccountException{
        findAccount(userInput).getTransactions(out);
    }
    public static void printAccountInformation(int userInput, OutputStream out) throws IOException,
            NoSuchAccountException{

        findAccount(userInput).printInfo(out);
    }

    //If case 6
    public static String depositIntoAccount(int userInput, double amount) throws AccountClosedException,
                                                                                        NoSuchAccountException{
        //Deposit into Account
        findAccount(userInput).deposit(amount);
        //return Account balance
        return (String.format(BankUtility.PROMPT_DEPOSIT_SUCCESS, findAccount(userInput).getBalance()));
    }
    //If case 7

    public static String withdrawFromAccount(int userInput, double amount) throws NoSuchAccountException,
                                                                InsufficientBalanceException, AccountClosedException{
        findAccount(userInput).withdraw(amount);
       return (String.format(BankUtility.PROMPT_WITHDRAW_SUCCESSFUL, findAccount(userInput).getBalance()));

    }

    public static String getConversion(String sellInput, double amount, String buyingInput) throws
                                                                                    CodeNotInListException{
        if(!(sellInput.equalsIgnoreCase("USD") || buyingInput.equalsIgnoreCase("USD"))){
            throw new CodeNotInListException("Error, the currency must have a USD option");
        }
        if(!exchangeRateList.containsKey(sellInput) || !exchangeRateList.containsKey(buyingInput)){
            throw new CodeNotInListException("Error, the currency is not available");
        }
        String result = "The exchange rate is %f and you will get %s %,.2f%n%n";
        double convertAmount = 0.00;

        if(! (sellInput.equalsIgnoreCase("USD"))){
            convertAmount = exchangeRateList.get(sellInput);
            convertAmount *= amount;
            return (String.format(result, exchangeRateList.get(sellInput), buyingInput, convertAmount));
        }
        convertAmount = amount;
        convertAmount /= exchangeRateList.get(buyingInput);
        return (String.format(result, exchangeRateList.get(buyingInput), buyingInput, convertAmount));

    }

    public static String closeAccount(int userInput) throws NoSuchAccountException{
       findAccount(userInput).close();
       return ("Account is now closed\n");
    }


}


