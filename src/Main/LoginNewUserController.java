package Main;

import DBConnection.DBConnect;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginNewUserController {
    @FXML
    private TextField txtNewUsername;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtNewConfirmPassword;

    @FXML
    private Button btnCancel;

    private String username;
    private boolean usernameOK = false;
    private boolean executed = false;

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
                txtNewUsername.setStyle("-fx-focus-color: -fx-control-inner-background;\n" +
                        "    -fx-faint-focus-color: -fx-control-inner-background;\n" +
                        "    -fx-border-width: 2px;\n" +
                        "    -fx-border-style: solid;\n" +
                        "    -fx-border-radius: 3px;\n" +
                        "    -fx-border-color: #009900;");
            } else {
                txtNewUsername.setStyle("-fx-focus-color: -fx-control-inner-background;\n" +
                        "    -fx-faint-focus-color: -fx-control-inner-background;\n" +
                        "    -fx-border-width: 2px;\n" +
                        "    -fx-border-style: solid;\n" +
                        "    -fx-border-radius: 3px;\n" +
                        "    -fx-border-color: #CC0000;");
            }

            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnSignUpClicked(){
        boolean passwordOK = txtNewPassword.getText().equals(txtNewConfirmPassword.getText());
        this.username = txtNewUsername.getText();
        String password = txtNewPassword.getText();
        if (this.usernameOK && passwordOK){

            try {
                Connection con = DBConnect.getConnection();
                Statement stmt = con.createStatement();
                String query = "INSERT INTO loginData (username, password) VALUES ('"+username+"', '"+ password +"');";
                this.executed = !stmt.execute(query);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } else {
            System.out.println("Username or Password Error!");
        }

        if (this.executed) {
            System.out.println("New Account Added Successfully!");
            btnResetClicked();
            LoginController newUserCredentials = new LoginController();
            newUserCredentials.newAccountCreated(this.username, password);
            btnCancelClicked();
        } else {
            System.out.println("Unable to create the account.");
        }
    }

}
