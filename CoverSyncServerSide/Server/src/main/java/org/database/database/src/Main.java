import java.util.*;
import java.sql.*;

public class Main {


    public static void main(String[] args) {
        try {
            Database db = new Database("jdbc:mysql://localhost:3306/clients_info", "samuel", "SM1234#Abc");
            testDatabase(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testDatabase(Database db) throws SQLException {
        // Create a new Customer object
        ArrayList<String> companyTags = new ArrayList<>();
        companyTags.add("Company1");
        companyTags.add("Company2");

        ArrayList<String> insuranceTypes = new ArrayList<>();
        insuranceTypes.add("Insurance1");
        insuranceTypes.add("Insurance2");

        Company company = new Company(1, "Ambetter", "Company1");
        InsuranceType insuranceType = new InsuranceType(1, "Insurance1");

        Customer customer = new Customer("59", "SAmuel", "123-44-6789", "595-999-999", "johndoe@example.com", "123 Main St", "2000-01-01", companyTags, insuranceTypes);

        // Add the customer to the database
        db.addCustomer(customer, company, insuranceType);

        // Retrieve the customer from the database
        Customer retrievedCustomer = db.getCustomerById(Integer.parseInt(customer.getId()));

        // Print the customer's name
        System.out.println(retrievedCustomer.getName());

        // Update the customer's name
        retrievedCustomer.setName("Samuel Updated");
        db.updateCustomer(retrievedCustomer, insuranceType, company);

        // Print the updated customer's name
        retrievedCustomer = db.getCustomerById(Integer.parseInt(customer.getId()));
        System.out.println(retrievedCustomer.getName());

        // Get customers by company
        List<Customer> customersByCompany = db.getCustomerByCompany("Company2");
        if (customersByCompany.isEmpty()) {
            System.out.println("No customers found for Company2");
        } else {
            for (Customer c : customersByCompany) {
                System.out.println(c.getName());
            }
        }

        // Get customers by insurance type
        List<Customer> customersByInsuranceType = db.getCustomerByInsuranceType("Insurance1");
        for (Customer c : customersByInsuranceType) {
            System.out.println(c.getName());
        }

        // Get companies for a customer
        List<Company> companiesForCustomer = db.getCompanyForCustomer(Integer.parseInt(customer.getId()));
        for (Company c : companiesForCustomer) {
            System.out.println(c.getName());
        }

        // Get insurance types for a customer
        List<InsuranceType> insuranceForCustomer = db.getInsuranceForCustomer(Integer.parseInt(customer.getId()));
        for (InsuranceType i : insuranceForCustomer) {
            System.out.println(i.getType());
        }

        // Print all customers
        printAll(db);

        // Delete the customer from the database
    }

    public static void printAll(Database db) throws SQLException {
        String query = "SELECT * FROM customers";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
        }
    }
}