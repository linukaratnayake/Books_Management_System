package Login;

import Management.DBConnection;
import Management.PasswordHash;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.ResourceBundle;

public class LoginNewUserController implements Initializable {
    @FXML
    private TextField txtFullName;

    @FXML
    private TextField txtNewUsername;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtNewConfirmPassword;

    @FXML
    private Button btnCancel;

    @FXML
    private ImageView ivLogo;

    private String username;
    private boolean usernameOK = false;
    private boolean executed = false;
    private byte[] salt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ivLogo.setImage(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
    }

    public void btnCancelClicked(){
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    public void btnResetClicked(){
        txtFullName.setText("");
        txtNewUsername.setText("");
        txtNewPassword.setText("");
        txtNewConfirmPassword.setText("");
        txtNewUsername.setStyle("fx-faint-focus-color: -fx-control-faint-focus-color;");
    }

    public void checkUsername(){
        username = txtNewUsername.getText();

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM loginData WHERE username = '"+username+"'";
            ResultSet rs = stmt.executeQuery(query);
            this.usernameOK = !rs.next();

            if (this.usernameOK && !username.isBlank() && !username.contains(" ")) {
                txtNewUsername.setStyle("""
                        -fx-focus-color: -fx-control-inner-background;
                            -fx-faint-focus-color: -fx-control-inner-background;
                            -fx-border-width: 2px;
                            -fx-border-style: solid;
                            -fx-border-radius: 3px;
                            -fx-border-color: #009900;""");
            } else {
                txtNewUsername.setStyle("""
                        -fx-focus-color: -fx-control-inner-background;
                            -fx-faint-focus-color: -fx-control-inner-background;
                            -fx-border-width: 2px;
                            -fx-border-style: solid;
                            -fx-border-radius: 3px;
                            -fx-border-color: #CC0000;""");
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void btnSignUpClicked(){
        boolean passwordOK = txtNewPassword.getText().equals(txtNewConfirmPassword.getText()) && !txtNewPassword.getText().isBlank();
        boolean fullNameOK = !txtFullName.getText().isBlank();

        String fullName = txtFullName.getText();
        this.username = txtNewUsername.getText();
        String password = txtNewPassword.getText();
        String passwordHash = null;

        if (fullNameOK && this.usernameOK && passwordOK){

            try {
                this.salt = PasswordHash.generateSalt();
                passwordHash = PasswordHash.generateHash(password, this.salt);

                Connection con = DBConnection.getConnection();
                Statement stmt = con.createStatement();
                String query = "INSERT INTO loginData (fullName, username, salt, hash) VALUES ('"+fullName+"', '"+this.username+"', '"+ Arrays.toString(salt) +"', '"+passwordHash+"');";
                this.executed = !stmt.execute(query);
                con.close();
            } catch (SQLException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Error with the values entered.");
        }

        if (this.executed) {
            System.out.println("New Account Added Successfully!");
            LoginController newUserCredentials = new LoginController();
            newUserCredentials.newAccountCreated(fullName, this.username, this.salt, passwordHash);
            btnResetClicked();
            btnCancelClicked();
        } else {
            System.out.println("Unable to create the account.");
        }
    }

}
