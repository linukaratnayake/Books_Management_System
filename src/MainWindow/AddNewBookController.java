package MainWindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddNewBookController implements Initializable {

    @FXML
    private ImageView ivLogo;

    @FXML
    private TextField txtBookID, txtBookName, txtAuthor, txtCategory;

    @FXML
    private DatePicker dpDateBought;

    @FXML
    private CheckBox chkFinishedReading, chkBookAvailable;

    @FXML
    private Button btnAdd, btnReset, btnCancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ivLogo.setImage(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));

        // To change the displaying date format.
        // Code found from StackOverflow.
        dpDateBought.setConverter(new StringConverter<>() {
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
}
