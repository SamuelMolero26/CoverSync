import java.util.ArrayList;
import java.util.HashMap;

public class Company {
    private int id;
    private String name;
    private String tag;
    private HashMap<Integer, Customer> Customers;

    public Company(int id, String name, String tag) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.Customers = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addCustomer(Customer customer) {
        String key = this.id + "-" + customer.getId(); //combines the company id and customer id to create a unique key
        Customers.put(Integer.valueOf(key), customer);
    }
}


