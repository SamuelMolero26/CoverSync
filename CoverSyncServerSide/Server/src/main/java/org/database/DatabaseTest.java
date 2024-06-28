package org.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatabaseTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private Database database;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        database = new Database(mockConnection);
    }

    @Test
    public void testGetCustomerById() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("first_name")).thenReturn("John");
        // Add more when statements for other fields

        Customer customer = database.getCustomerById(1);

        assertNotNull(customer);
        assertEquals(1, customer.getId());
        assertEquals("John", customer.getfirstName());
        // Add more assertions for other fields

        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    // Add more test methods for other methods in the Database class
}