package MainWindow;

import Management.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AddNewBorrowerController implements Initializable {

    @FXML
    private TextField txtBorrowerName;

    @FXML
    private TextArea txtBorrowerDescription;

    @FXML
    private Button btnAdd, btnCancel;

    private final String username;

    public AddNewBorrowerController(String username) {
        this.username = username;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void addNewBorrower() {
        String borrowerName = txtBorrowerName.getText();

        if (borrowerName.isBlank()) {
            System.out.println("Enter a valid Borrower Name.");
            // TODO - Display an Alert.
            return;
        }

        String borrowerDescription = txtBorrowerDescription.getText().isEmpty() ? "" : txtBorrowerDescription.getText();

        String query = "INSERT INTO '"+this.username+"_borrowers' (borrowerName, borrowerDescription) VALUES " +
                "('"+borrowerName+"', '"+borrowerDescription+"');";

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cancel();
    }

    public void cancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }


}
