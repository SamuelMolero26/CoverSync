
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {

            // Initialize the database connection
            Database db = new Database("jdbc:mysql://192.168.1.94:3306/clients_info", "samuel", "SM1234#Abc");

            // Create sample data
            ArrayList<String> companyTags = new ArrayList<>(List.of("Tech", "Finance"));
            ArrayList<String> insuranceTypes = new ArrayList<>(List.of("Health", "Auto"));
            Customer customer1 = new Customer(69, "JoHn", "Doe", "123-45-6789", "555-1234", "john.doe@example.com", "123 Elm Street", "1980-01-01", insuranceTypes, companyTags);
            Company company = new Company(1, "Tech", "Technology");
            InsuranceType insuranceType = new InsuranceType(1, "Health");

            // Add the customer
            db.addCustomer(customer1, company, insuranceType);
            System.out.println("Customer added successfully.");

            // Update the customer
            customer1.setPhone("555-5678");
            db.updateCustomer(customer1, insuranceType, company);
            System.out.println("Customer updated successfully.");

            // Retrieve customer by ID
            Customer retrievedCustomer = db.getCustomerById(1);
            System.out.println("Retrieved Customer by ID: " + retrievedCustomer.getfirstName() + " " + retrievedCustomer.getlastName());

            // Retrieve customers by company tag
            List<Customer> customersByCompany = db.getCustomerByCompany("Ambetter");
            System.out.println("Customers by Company:");
            for (Customer c : customersByCompany) {
                System.out.println(c.getfirstName() + " " + c.getlastName());
            }

            // Retrieve customers by insurance type
            List<Customer> customersByInsuranceType = db.getCustomerByInsuranceType("Health");
            System.out.println("Customers by Insurance Type:");
            for (Customer c : customersByInsuranceType) {
                System.out.println(c.getfirstName() + " " + c.getlastName());
            }

            // Retrieve insurance types for a customer
            List<InsuranceType> insuranceForCustomer = db.getInsuranceForCustomer(1);
            System.out.println("Insurance Types for Customer:");
            for (InsuranceType insurance : insuranceForCustomer) {
                System.out.println(insurance.getType());
            }

            // Delete the customer
            db.deleteCustomer(1);
            System.out.println("Customer deleted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
