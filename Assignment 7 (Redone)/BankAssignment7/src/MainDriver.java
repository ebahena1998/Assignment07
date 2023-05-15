//Title: Banking Application
//Author: Edgar Bahena
//Email: ebahena5@toromail.csudh.edu
//Date: 05/02/2023


import ExceptionsCollection.*;

import java.net.*;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainDriver{
    public static final String PROMPT_ID = ("\nThank you, the account number is: %d%n");
    public static Scanner scnr = new Scanner(System.in);
    private static final String urlName = "http://www.usman.cloud/banking/exchange-rate.csv";
    public static void main(String[] args){
        request();
        int userInput = printMenu();
        checkChoices(userInput);
    }

    public static int printMenu(){
        System.out.println("1 - Open a Checking Account");
        System.out.println("2 - Open a Savings Account");
        System.out.println("3 - List Accounts");
        System.out.println("4 - View Statements");
        System.out.println("5 - Account information");
        System.out.println("6 - Deposit Funds");
        System.out.println("7 - Withdraw Funds");
        System.out.println("8 - Close an Account");
        System.out.println("9 - Foreign Exchange");
        System.out.println("10 - Exiting\n");
        System.out.print("Please Enter your choice: ");
        int userInput = scnr.nextInt();
        scnr.nextLine();

        //Error checking
        while( !((userInput >= 1) && (userInput <= 11)) ){
            userInput = printMenu();
        }
        //return valid option
        return userInput;
    }

    public static void checkChoices(int userSelection){
            switch (userSelection) {
                case 1:
                    openCheckingAccount();
                    checkChoices(printMenu());
                    break;
                case 2:
                    openSavingsAccount();
                    checkChoices(printMenu());
                    break;
                case 3:
                    listAccounts();
                    checkChoices(printMenu());
                    break;
                case 4:
                    accountStatement();
                    checkChoices(printMenu());
                    break;
                case 5:
                    showAccountInformation();
                    checkChoices(printMenu());
                case 6:
                    depositFunds();
                    checkChoices(printMenu());
                    break;
                case 7:
                    withdrawFunds();
                    checkChoices(printMenu());
                    break;
                case 8:
                    closeAnAccount();
                    checkChoices(printMenu());
                    break;
                case 9:
                    currencyConversion();
                    //saveTransactions();
                    checkChoices(printMenu());
                    break;
                case 10:
                    System.out.println("Exiting");
                    break;
                default:
                    checkChoices(printMenu());
            }
    }

    //CASE 1
    public static void openCheckingAccount(){
        Person customer = createCustomer();
        System.out.print("Enter overdraft limit: ");
        double overdraft = scnr.nextDouble();
        scnr.nextLine();
        if(Bank.status){
            String userCurrencyCode = askForAccountCurrency();
            printAccountId(PROMPT_ID, Bank.createCheckingAccount(customer, overdraft, userCurrencyCode.toUpperCase()));
        } else{
            printAccountId(PROMPT_ID, Bank.createCheckingAccount(customer, overdraft));
        }
    }

    //CASE 2
    public static void openSavingsAccount(){
        Person customer = createCustomer();
        if(Bank.status){
            String userCurrencyCode = askForAccountCurrency();
            printAccountId(PROMPT_ID, Bank.createSavingsAccount(customer, userCurrencyCode.toUpperCase()));
        } else{
            printAccountId(PROMPT_ID, Bank.createSavingsAccount(customer));
        }
    }

    //CASE 3
    public static void listAccounts(){
        try{
            OutputStream out = System.out;
            Bank.heldAccounts(out);

        } catch(IOException | NoSuchAccountException ioe){
            System.out.println(ioe.getMessage());
        }
    }
    //CASE 4
    public static void accountStatement() {
        try{
            int userInput = askAccountNumber();
            OutputStream out = System.out;
            Bank.printAccountTransactions(userInput, out);
        } catch (IOException | NoSuchAccountException ioe){
            System.out.println(ioe.getMessage());
        }
    }

    //CASE 5
    public static void showAccountInformation(){
        try{
            int userInput = askAccountNumber();
            OutputStream out = System.out;
            Bank.printAccountInformation(userInput, out);
        } catch(IOException | NoSuchAccountException ioe){
            System.out.println(ioe.getMessage());
        }
    }

    //CASE 6
    public static void depositFunds() {
        try{
            int userInput = askAccountNumber();
            System.out.print("Enter amount to deposit: ");
            double amount = scnr.nextDouble();
            System.out.print(Bank.depositIntoAccount(userInput, amount));
        } catch (AccountClosedException | NoSuchAccountException nsae){
            System.out.println(nsae.getMessage());
        }

    }

    //CASE 7
    public static void withdrawFunds(){
        try{
            int userInput = askAccountNumber();
            System.out.print("Enter amount to withdraw: ");
            double amount = scnr.nextDouble();
            System.out.print(Bank.withdrawFromAccount(userInput, amount));
        } catch (NoSuchAccountException | InsufficientBalanceException | AccountClosedException e){
            System.out.println(e.getMessage());
        }
    }

    public static void request() {
        try {
            HttpURLConnection hr = Bank.requestHttp(urlName);
            InputStream in = hr.getInputStream();
            Bank.storeKeyValues(in);

        } catch(InterruptedException | IOException ioe){
            System.out.println(ioe.getMessage());
        }

    }

    public static void currencyConversion(){
        try {
            System.out.print("The currency you are selling: ");
            String sellInput = scnr.nextLine();
            System.out.print("The amount you are selling: ");
            double amount = scnr.nextDouble();
            scnr.nextLine();
            System.out.print("The currency you are buying: ");
            String buyingInput = scnr.nextLine();
            System.out.print(Bank.getConversion(sellInput.toUpperCase(), amount, buyingInput.toUpperCase()));

        } catch (CodeNotInListException | InputMismatchException e){
            System.out.println(e.getMessage());
        }
    }

    //CASE 8
    public static void saveTransactions() {
        //(OLD CASE 8)
        try{
            int userInput = askAccountNumber();
            File file = new File("transactions.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(file);
            Bank.printAccountTransactions(userInput , out);
            out.close();
        } catch (IOException | NoSuchAccountException ioe){
            ioe.printStackTrace();
        }
    }

    //CASE 9
    public static void closeAnAccount(){
        try{
            System.out.println(Bank.closeAccount(askAccountNumber()));
        } catch(NoSuchAccountException nsae){
            System.out.println(nsae.getMessage());
        }
    }


    //END OF CASES

    //RANDOM METHODS()
    public static void printAccountId(String statement, Account temp){
        statement = String.format(statement, temp.getAccountId());
        System.out.println(statement);
    }
    public static Person createCustomer(){
        //Returns a Reference to created person obj
        System.out.print("Enter first name: ");
        String firstName = scnr.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scnr.nextLine();
        System.out.print("Enter social security number: ");
        String SSN = scnr.nextLine();
        Person customer = new Person(firstName, lastName, SSN);
        return (customer);
    }
    public static int askAccountNumber(){
        System.out.print("Enter account number: ");
        int accountNumber = scnr.nextInt();
        scnr.nextLine();
        return accountNumber;
    }
    public static void checkBankFile(){
        /**
         * try{
         InputStream in = new FileInputStream(Bank.readFile("exchange-rate.csv"));
         Bank.storeKeyValues(in);
         in.close();

         } catch(IOException ioe){
         System.out.println(ioe.getMessage());
         }
         *
         */

    }

    public static String askForAccountCurrency(){
        String currencyISO;
        boolean condition;
        do{
            System.out.print("Enter Account Currency: ");
            currencyISO = scnr.nextLine();
            condition = Bank.checkExchangeList(currencyISO);
            if(!condition){
                System.out.println("Currency is not available, please try again");
                continue;
            }
        } while(!condition);
        return currencyISO;
    }

}