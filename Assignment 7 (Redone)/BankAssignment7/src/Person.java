//A customer can request the bank to open a new account
public class Person{
    private String firstName;
    private String lastName;
    private String SSN;
    public Person(String firstName, String lastName, String SSN){
        this.firstName = firstName;
        this.lastName = lastName;
        this.SSN = SSN;
    }
    //Getters
    public String getFirstName(){
        return (this.firstName);
    }
    public String getLastName(){
        return (this.lastName);
    }
    public String getSSN(){
        return (this.SSN);
    }
    //Methods: toString()
    public String toString(){
        String personInfo = "";
        personInfo += String.format("%s : %s : %s", getFirstName(), getLastName(), getSSN());
        return (personInfo);
    }
}