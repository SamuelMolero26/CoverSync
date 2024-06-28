package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatabaseTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private Database database;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testAddCustomer_NewCustomer() throws SQLException {
        // Arrange
        Customer customer = new Customer(1, "John", "Doe", "123-45-6789", "555-5555", "john.doe@example.com", "123 Main St", "1990-01-01", new ArrayList<>(), new ArrayList<>());
        Company company = new Company(1, "Company Inc", "Tag1");
        InsuranceType insuranceType = new InsuranceType(1, "Health");

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // Act
        database.addCustomer(customer, company, insuranceType);

        // Assert
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testAddCustomer_ExistingCustomer() throws SQLException {
        // Arrange
        Customer customer = new Customer(1, "John", "Doe", "123-45-6789", "555-5555", "john.doe@example.com", "123 Main St", "1990-01-01", new ArrayList<>(), new ArrayList<>());
        Company company = new Company(1, "Company Inc", "Tag1");
        InsuranceType insuranceType = new InsuranceType(1, "Health");

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        // Act
        database.addCustomer(customer, company, insuranceType);

        // Assert
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testGetCustomerById_CachedCustomer() throws SQLException {
        // Arrange
        Customer customer = new Customer(1, "John", "Doe", "123-45-6789", "555-5555", "john.doe@example.com", "123 Main St", "1990-01-01", new ArrayList<>(), new ArrayList<>());
        database.customerCache.put(1, customer);

        // Act
        Customer result = database.getCustomerById(1);

        // Assert
        assertEquals(customer, result);
        verify(mockPreparedStatement, times(0)).executeQuery();  // Ensure the DB is not queried
    }

    @Test
    public void testGetCustomerById_NotCachedCustomer() throws SQLException {
        // Arrange
        Customer customer = new Customer(1, "John", "Doe", "123-45-6789", "555-5555", "john.doe@example.com", "123 Main St", "1990-01-01", new ArrayList<>(), new ArrayList<>());

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("first_name")).thenReturn("John");
        when(mockResultSet.getString("last_name")).thenReturn("Doe");
        when(mockResultSet.getString("ssn")).thenReturn("123-45-6789");
        when(mockResultSet.getString("phone")).thenReturn("555-5555");
        when(mockResultSet.getString("email")).thenReturn("john.doe@example.com");
        when(mockResultSet.getString("address")).thenReturn("123 Main St");
        when(mockResultSet.getString("dob")).thenReturn("1990-01-01");

        // Act
        Customer result = database.getCustomerById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testDeleteCustomer() throws SQLException {
        // Arrange
        int customerId = 1;

        // Act
        database.deleteCustomer(customerId);

        // Assert
        verify(mockPreparedStatement, times(1)).executeUpdate();
        assertFalse(database.customerCache.containsKey(customerId));
    }

    @Test
    public void testGetCustomerByInsuranceType() throws SQLException {
        // Arrange
        Customer customer = new Customer(1, "John", "Doe", "123-45-6789", "555-5555", "john.doe@example.com", "123 Main St", "1990-01-01", new ArrayList<>(), new ArrayList<>());

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("first_name")).thenReturn("John");
        when(mockResultSet.getString("last_name")).thenReturn("Doe");
        when(mockResultSet.getString("ssn")).thenReturn("123-45-6789");
        when(mockResultSet.getString("phone")).thenReturn("555-5555");
        when(mockResultSet.getString("email")).thenReturn("john.doe@example.com");
        when(mockResultSet.getString("address")).thenReturn("123 Main St");
        when(mockResultSet.getString("dob")).thenReturn("1990-01-01");

        // Act
        List<Customer> customers = database.getCustomerByInsuranceType("Health");

        // Assert
        assertFalse(customers.isEmpty());
        assertEquals(1, customers.size());
        assertEquals("John", customers.get(0).getFirstName());
    }

    @Test
    public void testGetCustomerByCompany() throws SQLException {
        // Arrange
        Customer customer = new Customer(1, "John", "Doe", "123-45-6789", "555-5555", "john.doe@example.com", "123 Main St", "1990-01-01", new ArrayList<>(), new ArrayList<>());

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("first_name")).thenReturn("John");
        when(mockResultSet.getString("last_name")).thenReturn("Doe");
        when(mockResultSet.getString("ssn")).thenReturn("123-45-6789");
        when(mockResultSet.getString("phone")).thenReturn("555-5555");
        when(mockResultSet.getString("email")).thenReturn("john.doe@example.com");
        when(mockResultSet.getString("address")).thenReturn("123 Main St");
        when(mockResultSet.getString("dob")).thenReturn("1990-01-01");

        // Act
        List<Customer> customers = database.getCustomerByCompany("Tag1");

        // Assert
        assertFalse(customers.isEmpty());
        assertEquals(1, customers.size());
        assertEquals("John", customers.get(0).getFirstName());
    }
}
