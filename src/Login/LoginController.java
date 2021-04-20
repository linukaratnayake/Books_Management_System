package Login;

import DBConnection.DBConnect;
import Main.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblCreateNewAccount;

    @FXML
    private ImageView ivLogo;

    private String uName;
    private String pWord;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ivLogo.setImage(new Image(String.valueOf(getClass().getResource("/Logo/Logo.png"))));
    }

    public void newAccountCreated(String uName, String pWord){
        this.uName = uName;
        this.pWord = pWord;
        btnLoginClicked();
    }

    public void btnLoginClicked(){
        if (uName == null && pWord == null){
            this.uName = txtUsername.getText();
            this.pWord = txtPassword.getText();
        }
        String query = "SELECT * from loginData WHERE username = '"+uName+"' AND password = '"+pWord+"';";

        try {
            Connection con = DBConnect.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()){
                System.out.println("Login Successful!");
                // TODO - Add code to direct to the next Stage.
            }else{
                System.out.println("Login Failed!");
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        uName = null;
        pWord = null;
    }

    public void btnCancelClicked(){
        System.exit(0);
    }

    public void enterPressed(KeyEvent event){
        if (event.getCode().equals(KeyCode.ENTER)){
            btnLoginClicked();
        }
    }

    public void btnCreateAccountClicked(){
        Stage stageForNewUser = new Stage();
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginNewUser.fxml")));
            // Above code was suggested by the IDE.
            stageForNewUser.setTitle("Create Account | Books Management System (BMS)");
            stageForNewUser.setScene(new Scene(root));
            stageForNewUser.setResizable(false);
            stageForNewUser.initOwner(Main.primaryStage);
            stageForNewUser.initModality(Modality.APPLICATION_MODAL);
            stageForNewUser.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnCreateAccountHovered(){
        lblCreateNewAccount.setStyle("-fx-text-fill: #05077F; -fx-font-size: 17px;");
    }

    public void btnCreateAccountNotHovered(){
        lblCreateNewAccount.setStyle("-fx-text-fill: #2300D5; -fx-font-size: 16px;");
    }

}
