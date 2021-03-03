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
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.primaryStage.setTitle("Books Management System (BMS)");
        Main.primaryStage.setScene(new Scene(root));
        Main.primaryStage.show();
    }


    public static void main(String[] args) {
        // TODO, remove this line later.
        DBConnect.getConnection();

        launch(args);
    }
}
