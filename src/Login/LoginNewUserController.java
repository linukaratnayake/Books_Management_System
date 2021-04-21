package Login;

import DBConnection.DBConnect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ivLogo.setImage(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
    }

    public void btnCancelClicked(){
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    public void btnResetClicked(){
        txtNewUsername.setText("");
        txtNewPassword.setText("");
        txtNewConfirmPassword.setText("");
    }

    public void checkUsername(){
        username = txtNewUsername.getText();

        try {
            Connection con = DBConnect.getConnection();
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM loginData WHERE username = '"+username+"'";
            ResultSet rs = stmt.executeQuery(query);
            this.usernameOK = !rs.next();

            if (this.usernameOK && !username.isBlank()) {
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
        // TODO - Add Code to detect empty username and password.
        boolean passwordOK = txtNewPassword.getText().equals(txtNewConfirmPassword.getText());
        String fullName = txtFullName.getText();
        this.username = txtNewUsername.getText();
        String password = txtNewPassword.getText();
        if (this.usernameOK && passwordOK){

            try {
                Connection con = DBConnect.getConnection();
                Statement stmt = con.createStatement();
                String query = "INSERT INTO loginData (fullName, username, password) VALUES ('"+fullName+"', '"+username+"', '"+ password +"');";
                this.executed = !stmt.execute(query);
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Username or Password Error!");
        }

        if (this.executed) {
            System.out.println("New Account Added Successfully!");
            btnResetClicked();
            LoginController newUserCredentials = new LoginController();
            newUserCredentials.newAccountCreated(fullName, this.username, password);
            btnCancelClicked();
        } else {
            System.out.println("Unable to create the account.");
        }
    }

}
