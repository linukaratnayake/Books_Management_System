package Main;

import DBConnection.DBConnect;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Main.primaryStage.setTitle("Login | Books Management System (BMS)");
        Main.primaryStage.setScene(new Scene(root));
        Main.primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
