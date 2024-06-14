import java.util.ArrayList;

public class Customer {
    private int id;
    private String name;
    private String ssn;
    private String phone;
    private String email;
    private String address;
    private String dateOfBirth;
    private ArrayList<String> company_tag;
    private ArrayList<String> insurance_type;


    //for checks
    private boolean isCompanyTagChanged = false;
    private boolean isInsuranceTypeChanged = false;
    private boolean isIdChanged = false;
    private boolean isNameChanged = false;
    private boolean isSsnChanged = false;
    private boolean isPhoneChanged = false;
    private boolean isEmailChanged = false;
    private boolean isAddressChanged = false;
    private boolean isDateOfBirthChanged = false;




    //user can have more than 1 company tag
    public Customer(String id, String name, String ssn, String phone, String email, String address, String dateOfBirth, ArrayList<String> company_tag, ArrayList<String> insurance_type) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.ssn = ssn;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.company_tag = new ArrayList<>();
        this.insurance_type = new ArrayList<>();
    }


    public String getId() {
        return String.valueOf(id);
    }

    public String getName() {
        return name;
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
        this.isCompanyTagChanged = true;
    }

    public void addInsuranceType(String type) {
        this.insurance_type.add(type);
        this.isInsuranceTypeChanged = true;
    }

    public void addId(String id) {
        this.id = Integer.parseInt(id);
        this.isIdChanged = true;
    }

    public void setName(String name) {
        this.name = name;
        this.isNameChanged = true;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
        this.isSsnChanged = true;

    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.isPhoneChanged = true;
    }

    public void setEmail(String email) {
        this.email = email;
        this.isEmailChanged = true;
    }

    public void setAddress(String address) {
        this.address = address;
        this.isAddressChanged = true;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.isDateOfBirthChanged = true;
    }


    public void clearCompanyChanges() {
        this.isCompanyTagChanged = false;
    }

    public void clearInsuranceChanges() {
        this.isInsuranceTypeChanged = false;
    }

    public void clearIdChanges() {
        this.isIdChanged = false;
    }

    public void clearNameChanges() {
        this.isNameChanged = false;
    }

    public void clearSsnChanges() {
        this.isSsnChanged = false;
    }

    public void clearPhoneChanges() {
        this.isPhoneChanged = false;
    }

    public void clearEmailChanges() {
        this.isEmailChanged = false;
    }

    public void clearAddressChanges() {
        this.isAddressChanged = false;
    }

    public void clearDateOfBirthChanges() {
        this.isDateOfBirthChanged = false;
    }

    //functs for checks

    public boolean isNameChanged() {
        return this.isNameChanged;
    }

    public boolean isSsnChanged() {
        return this.isSsnChanged;
    }

    public boolean isPhoneChanged() {
        return this.isPhoneChanged;
    }

    public boolean isEmailChanged() {
        return this.isEmailChanged;
    }

    public boolean isAddressChanged() {
        return this.isAddressChanged;
    }

    public boolean isDateOfBirthChanged() {
        return this.isDateOfBirthChanged;
    }

    public boolean isCompanyTagChanged() {
        return this.isCompanyTagChanged;
    }

    public boolean isInsuranceTypeChanged() {
        return this.isInsuranceTypeChanged;
    }

    public boolean isIdChanged() {
        return this.isIdChanged;
    }




}
