package MainWindow;

import Management.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddNewBorrowedBookController implements Initializable {

    @FXML
    private ImageView ivLogo;

    @FXML
    private TextField txtBorrowedBookID, txtBorrowedBookName, txtBorrowedAuthor, txtBookLender;

    @FXML
    private DatePicker dpDateBorrowed;

    @FXML
    private CheckBox chkFinishedReading;

    @FXML
    private Button btnAdd, btnReset, btnCancel;

    private final String username;

    public AddNewBorrowedBookController(String username) {
        this.username = username;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ivLogo.setImage(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));

        // To change the displaying date format.
        // Code found from StackOverflow.
        dpDateBorrowed.setConverter(new StringConverter<>() {
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

//    public void addNewBook() {
//
//        boolean bookExists = false;
//        String newBookID = txtBookID.getText();
//        String bookName = txtBookName.getText();
//        String author = txtAuthor.getText();
//        String category = txtCategory.getText();
//        String dateBought = dpDateBought.getValue() != null ? dpDateBought.getValue().toString() : "";
//        int finishedReading = chkFinishedReading.isSelected() ? 1 : 0;
//        int bookAvailable = chkBookAvailable.isSelected() ? 1 : 0;
//
//        String queryToCheckBookExists = "SELECT * FROM '"+this.username+"_books' WHERE bookID = '"+newBookID+"';";
//
//        try {
//            Connection con = DBConnection.getConnection();
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(queryToCheckBookExists);
//
//            if (rs.next()) {
//                bookExists = true;
//            }
//            rs.close();
//            stmt.close();
//            con.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        if ((!bookExists) && (!newBookID.isBlank()) && (!bookName.isBlank())){
//
//            String queryToAdd = "INSERT INTO '"+this.username+"_books' VALUES (" +
//                    "'"+newBookID+"', " +
//                    "\""+bookName+"\", " +
//                    "'"+author+"', " +
//                    "'"+dateBought+"', " +
//                    "'"+category+"', " +
//                    "'"+finishedReading+"', " +
//                    "'"+bookAvailable+"'" +
//                    ");";
//
//            try {
//                Connection con = DBConnection.getConnection();
//                Statement stmt = con.createStatement();
//                stmt.execute(queryToAdd);
//                stmt.close();
//                con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            cancel();
//        } else {
//            System.out.println("Book already exists or invalid BookID or BookName.");
//            // TODO display a dialog.
//        }
//    }

    public void reset() {
        txtBorrowedBookID.setText("");
        txtBorrowedBookName.setText("");
        txtBorrowedAuthor.setText("");
        txtBookLender.setText("");
        dpDateBorrowed.setValue(LocalDate.of(2000, 1, 1)); // If a String is inserted, to change it to a valid date.
        dpDateBorrowed.setValue(null);
        chkFinishedReading.setSelected(false);
    }

    public void cancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }


}
