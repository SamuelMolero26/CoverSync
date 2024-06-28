
package  org.database;

import java.util.ArrayList;
import java.util.HashMap;

public class Company {
    private int id;
    private String name;
    private ArrayList<String> tag;
    private HashMap<Integer, Customer> Customers;

    public Company(int id, String name, String tag) {
        this.id = id;
        this.name = name;
        this.tag = new ArrayList<>();
        this.Customers = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tags) {
        if(tags != null) {
            this.tag.addAll(tags);
        }
    }


}


