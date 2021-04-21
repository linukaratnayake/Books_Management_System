package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../Login/Login.fxml")));
        Main.primaryStage.setTitle("Login | Books Management System (BMS)");
        Main.primaryStage.setScene(new Scene(root));
        Main.primaryStage.setResizable(false);
        Main.primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
        Main.primaryStage.centerOnScreen();
        Main.primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
