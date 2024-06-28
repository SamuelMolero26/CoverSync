package  org.database;

import java.util.ArrayList;

public class Customer {
    private int id;
    private String first_name;
    private String last_name;
    private String ssn;
    private String phone;
    private String email;
    private String address;
    private String dateOfBirth;
    private ArrayList<String> company_tag;
    private ArrayList<String> insurance_type;








    //user can have more than 1 company tag
    public Customer(int id, String first_name, String last_name, String ssn, String phone, String email, String address, String dateOfBirth, ArrayList<String> company_tag, ArrayList<String> insurance_type) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.ssn = ssn;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.company_tag = new ArrayList<>();
        this.insurance_type = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public String getfirstName() {
        return first_name;
    }

    public String getlastName() {
        return last_name;
    }

    public String getSsn() {
        return ssn;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public ArrayList<String> getCompanyTag() {
        return company_tag;
    }

    public ArrayList<String> getInsuranceType() {
        return insurance_type;
    }

    public void addCompanyTag(String tag) {
        this.company_tag.add(tag);

    }

    public void addInsuranceType(String type) {
        this.insurance_type.add(type);

    }

    public void addId(String id) {
        this.id = Integer.parseInt(id);

    }

    public void setName(String name1, String name2) {
        this.first_name = name1;
        this.last_name = name2;

    }

    public void setSsn(String ssn) {
        this.ssn = ssn;


    }

    public void setPhone(String phone) {
        this.phone = phone;

    }

    public void setEmail(String email) {
        this.email = email;

    }

    public void setAddress(String address) {
        this.address = address;

    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;

    }



}
