package org.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.database.Customer;
import org.database.Database;
import java.sql.SQLException;

public class DatabaseTest {

    @Test
    public void testCreateCustomer() {
        // Create a new instance of the Database class
        Database db = new Database();

        // Create a new Customer
        Customer customer = new Customer();
        customer.setName("Test Name");
        customer.setEmail("test@email.com");
        customer.setSSN("123-45-6789");
        customer.setNumber("1234567890");

        // Try to add the customer to the database
        try {
            db.createCustomer(customer);
        } catch (SQLException e) {
            // If an SQLException is thrown, fail the test
            fail("SQLException thrown: " + e.getMessage());
        }

        // Retrieve the customer from the database
        Customer retrievedCustomer = null;
        try {
            retrievedCustomer = db.readCustomer(customer.getID());
        } catch (SQLException e) {
            // If an SQLException is thrown, fail the test
            fail("SQLException thrown: " + e.getMessage());
        }

        // Assert that the retrieved customer is equal to the original customer
        assertEquals(customer, retrievedCustomer);
    }
}