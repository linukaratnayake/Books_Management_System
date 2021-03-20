package Main;

import DBConnection.DBConnect;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    public void btnLoginClicked(){
        String uName = txtUsername.getText();
        String pWord = txtPassword.getText();
        String query = "SELECT * from loginData WHERE username = '"+uName+"' AND password = '"+pWord+"';";

        try {
            Connection con = DBConnect.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()){
                System.out.println("Login Successful!");
                txtUsername.setText("");
                txtPassword.setText("");
            }else{
                System.out.println("Login Failed!");
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnCancelClicked(){
        System.exit(0);
    }
}
