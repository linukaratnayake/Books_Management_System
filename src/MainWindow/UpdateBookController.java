package MainWindow;

import Management.DBConnection;
import Tables.Book;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class UpdateBookController implements Initializable {

    @FXML
    private ImageView ivLogo;

    @FXML
    private TextField txtBookID, txtBookName, txtAuthor, txtCategory;

    @FXML
    private DatePicker dpDateBought;

    @FXML
    private CheckBox chkFinishedReading, chkBookAvailable;

    @FXML
    private Button btnUpdate, btnReset, btnCancel;

    private final String username;
    private final Book book;

    public UpdateBookController(String username, Book book) {
        this.username = username;
        this.book = book;
    }

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
        getData();
    }

    public void getData() {
        txtBookID.setText(this.book.getBookID());
        txtBookName.setText(this.book.getBookName());
        txtAuthor.setText(this.book.getBookAuthor());
        txtCategory.setText(this.book.getBookCategory());

        String[] date = this.book.getBookDateBought().split("-");
        boolean allValidDigits = true;
        for (String s : date) {
            if (!s.matches("[0-9]+")) {
                allValidDigits = false;
                break;
            }
        }
        if (allValidDigits) {
            dpDateBought.setValue(LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])));
        } else {
            dpDateBought.setValue(null);
        }

        chkFinishedReading.setSelected(this.book.getBookRead().equals("Yes"));
        chkBookAvailable.setSelected(this.book.getBookAvailable().equals("Yes"));
    }

    public void updateBook() {
        String bookID = txtBookID.getText();
        String bookName = txtBookName.getText();
        String author = txtAuthor.getText();
        String category = txtCategory.getText();
        String dateBought = dpDateBought.getValue() != null ? dpDateBought.getValue().toString() : "";
        int finishedReading = chkFinishedReading.isSelected() ? 1 : 0;
        int bookAvailable = chkBookAvailable.isSelected() ? 1 : 0;

        if (!bookName.isBlank()) {
            String queryToUpdate = "UPDATE '"+this.username+"_books' SET " +
                    "bookName = \""+bookName+"\", " +
                    "bookAuthor = '"+author+"', " +
                    "bookDateBought = '"+dateBought+"', " +
                    "bookCategory = '"+category+"', " +
                    "bookRead = '"+finishedReading+"', " +
                    "bookAvailable = '"+bookAvailable+"'" +
                    "WHERE bookID = '"+bookID+"';";

            try {
                Connection con = DBConnection.getConnection();
                Statement stmt = con.createStatement();
                stmt.execute(queryToUpdate);
                stmt.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            this.book.setBookName(bookName);
            this.book.setBookAuthor(author);
            this.book.setBookCategory(category);
            this.book.setBookDateBought(dateBought);
            this.book.setBookRead(finishedReading == 1);
            this.book.setBookAvailable(bookAvailable == 1);
            cancel();
        } else {
            System.out.println("Book name cannot be blank.");
        }
    }

    public void reset() {
        txtBookName.setText("");
        txtAuthor.setText("");
        txtCategory.setText("");
        dpDateBought.setValue(LocalDate.of(2000, 1, 1)); // If a String is inserted, to change it to a valid date.
        dpDateBought.setValue(null);
        chkFinishedReading.setSelected(false);
        chkBookAvailable.setSelected(false);
    }

    public void cancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }


}
