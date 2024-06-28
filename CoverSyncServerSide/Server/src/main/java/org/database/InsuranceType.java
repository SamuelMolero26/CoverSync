package org.database;

public class InsuranceType {

    private int id;
    private String type;

    public InsuranceType(int id, String type) {
        this.id = id;
        this.type = type;
    }


    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }
}
