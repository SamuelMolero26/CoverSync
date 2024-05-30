package org.Login;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.Client.Client;
import org.json.JSONObject;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // Reference to the Client instance
    private Client client;

    // Setter method to set the Client instance
    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText(); // Retrieve username from TextField
        String password = passwordField.getText(); // Retrieve password from PasswordField

        // Check if the client instance is not null and both username and password are not empty
        if (client != null && !username.isEmpty() && !password.isEmpty()) {
            // Create a JSON object to hold the username and password
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);

            // Send the JSON object to the server
            client.sendToServer(json.toString());
        } else {
            System.out.println("Client is not initialized or username/password is empty");
        }
    }
}
