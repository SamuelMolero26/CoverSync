package org.Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.Client.Client;

public class LoginGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Client client = new Client();
        client.setUpNetworking();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/loginPage.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setClient(client);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("My Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
