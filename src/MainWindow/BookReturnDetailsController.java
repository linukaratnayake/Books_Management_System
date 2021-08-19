package MainWindow;

import Management.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class BookReturnDetailsController implements Initializable {

    @FXML
    private Button btnOK, btnCancel;

    @FXML
    private DatePicker dpDateReturned;

    private String returnedDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // To change the displaying date format.
        // Code found from StackOverflow.
        dpDateReturned.setConverter(new StringConverter<>() {
            private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate != null) {
                    return dateTimeFormatter.format(localDate);
                }
                return "";
            }

            @Override
            public LocalDate fromString(String dateString) {
                if((dateString == null) || (dateString.trim().isEmpty())){
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });

    }

    public void collectReturnedDate() {
        returnedDate = dpDateReturned.getValue() != null ? dpDateReturned.getValue().toString() : "";
        cancel();
    }

    public void cancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    public String getReturnedDate() {
        return returnedDate;
    }
}
