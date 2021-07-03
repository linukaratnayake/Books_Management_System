package Login;

import Main.Main;
import MainWindow.MainWindowController;
import Management.DBConnection;
import Management.PasswordHash;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import java.security.NoSuchAlgorithmException;
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

    @FXML
    private Button btnCancel;

    private String fullName;
    private String uName;
    private String pWord;
    private byte[] salt = new byte[20];
    private String passwordHash;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ivLogo.setImage(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
        lblCreateNewAccount.setFocusTraversable(true);
    }

    public void newAccountCreated(String fullName, String uName, byte[] salt, String passwordHash){
        this.fullName = fullName;
        this.uName = uName;
        this.salt = salt;
        this.passwordHash = passwordHash;
        btnLoginClicked();
    }

    public void btnLoginClicked(){
        if (uName == null){
            this.uName = txtUsername.getText();
            this.pWord = txtPassword.getText();
        }
       if (passwordHash == null) {
           String queryToGetSalt = "SELECT salt FROM loginData WHERE username = '"+uName+"';";
           try {
               Connection con = DBConnection.getConnection();
               Statement stmt = con.createStatement();
               ResultSet rs = stmt.executeQuery(queryToGetSalt);

               if (rs.next()) {
                   String hashAsString = rs.getString("salt").replace("[", "").replace("]", "");
                   String[] hashSplit = hashAsString.split("\\s*,\\s*"); //To get numbers separated with commas without spaces.
                   for (int i = 0; i < hashSplit.length; i++) {
                       salt[i] = Byte.parseByte(hashSplit[i]);
                   }
                   try {
                       passwordHash = PasswordHash.generateHash(pWord, salt);
                   } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                       noSuchAlgorithmException.printStackTrace();
                   }
               }
           }
           catch (SQLException e) {
               e.printStackTrace();
           }
       }

       String queryToConfirmLogin = "SELECT fullName, username from loginData WHERE username = '"+uName+"' AND hash = '"+passwordHash+"';";

       try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(queryToConfirmLogin);
            if (rs.next()){
                System.out.println("Login Successful!");
                this.fullName = rs.getString("fullName");

                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../MainWindow/MainWindow.fxml"));
                    Parent root = loader.load();

                    MainWindowController user = loader.getController();
                    user.setUsername(this.uName);
                    user.setUserData(this.fullName, this.uName);
                    user.populateTableMyBooks(null);

                    Main.primaryStage.setTitle("Books Management System (BMS)");
                    Main.primaryStage.setScene(new Scene(root));
                    Main.primaryStage.centerOnScreen();
                    Main.primaryStage.setMaximized(true);
                    Main.primaryStage.setResizable(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Login Failed!");
            }
            con.close();
            passwordHash = null;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        uName = null;
        pWord = null;
    }

    public void btnCancelClicked(){
        ((Stage)btnCancel.getScene().getWindow()).close();
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
            stageForNewUser.getIcons().add(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
            stageForNewUser.centerOnScreen();
            stageForNewUser.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enterPressedOnCreateNewUser(KeyEvent event){
        if (event.getCode().equals(KeyCode.ENTER)){
            btnCreateAccountClicked();
        }
    }

}
