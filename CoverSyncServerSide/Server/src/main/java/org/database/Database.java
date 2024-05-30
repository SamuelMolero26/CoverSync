package org.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.database.Customer;


public class Database {

    private static final String URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String USER = "samuelmolero";
    private static final String PASSWORD = "SM30355vzm*";


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASSWORD);

    }

    public void createCustomer(Customer custom) throws SQLException {
        String sql = "INSERT INTO customers (name, email, SSN, number) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, custom.getName());
            statement.setString(2, custom.getEmail());
            statement.setString(3, custom.getSSN());
            statement.setString(4, custom.getNumber());

            statement.executeUpdate();
        }
    }

    public ResultSet readCustomer(int id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE id = ?";

        try(Connection connect = getConnection();
            PreparedStatement statement = connect.prepareStatement(sql)) {
            statement.setInt(1,id);
            return statement.executeQuery();
        }

    }



}