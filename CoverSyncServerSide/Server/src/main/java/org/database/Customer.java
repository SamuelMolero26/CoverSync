package org.database;


public class Customer {
    private int  id;
    private String name;
    private String email;
    private String SSN;
    private String number;



    //getters
    public int getID() { return id;}

    public String getName() {return name;}

    public String getEmail() {return email;}

    public String getSSN() { return SSN;}

    public String getNumber() { return number;}

    //setters

    public void setId(int id) { this.id = id;}

    public void setName(String name) {this.name = name; }

    public void setEmail(String email) {this.email = email;}

    public void setSSN(String SSN) {this.SSN = SSN; }

    public void setNumber(String number) {this.number = number;}




}