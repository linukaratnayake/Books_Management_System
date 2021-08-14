package MainWindow;

import Management.DBConnection;
import Tables.Book;
import Tables.Borrower;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class LendBookController implements Initializable {

    @FXML
    private ImageView ivLogo;

    @FXML
    private Button btnAddNewBorrower, btnMarkAsBorrowed, btnCancel;

    @FXML
    private TableView<Book> tblSelectedBooks;

    @FXML
    private TableColumn<Book, String> SelectedBooksBookID, SelectedBooksBookName, SelectedBooksAuthor;

    @FXML
    private TableView<Borrower> tblBorrowers;

    @FXML
    private TableColumn<Borrower, String> BorrowersBorrowerID, BorrowersBorrowerName, BorrowersBorrowerDescription;

    @FXML
    private DatePicker dpDateBorrowed;

    private final String username;
    private final ObservableList<Book> selectedBooks;

    public LendBookController(String username, ObservableList<Book> selectedBooks) {
        this.username = username;
        this.selectedBooks = selectedBooks;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ivLogo.setImage(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));

        SelectedBooksBookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        SelectedBooksBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        SelectedBooksAuthor.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));

        BorrowersBorrowerID.setCellValueFactory(new PropertyValueFactory<>("borrowerID"));
        BorrowersBorrowerName.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        BorrowersBorrowerDescription.setCellValueFactory(new PropertyValueFactory<>("borrowerDescription"));


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

    public void populateTableSelectedBooks () {
        tblSelectedBooks.setItems(this.selectedBooks);
    }

    public void populateTableBorrowers () {
        ObservableList<Borrower> borrowers = FXCollections.observableArrayList();

        String query = "SELECT * FROM '"+this.username+"_borrowers';";

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                borrowers.add(new Borrower(rs.getString("borrowerID"), rs.getString("borrowerName"),
                        rs.getString("borrowerDescription")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblBorrowers.setItems(borrowers);
    }

    public void borrow () {
        Borrower borrower = tblBorrowers.getSelectionModel().getSelectedItem();
        String borrowerID = "0";

        if (borrower != null) {
            borrowerID = borrower.getBorrowerID();
        } else {
            System.out.println("Please Select a Borrower");
            // TODO - Add A Message Box.
            return;
        }

        String dateBorrowed = dpDateBorrowed.getValue() != null ? dpDateBorrowed.getValue().toString() : "";

        for (Book book : this.selectedBooks) {
            book.setBookAvailable(false);

            String queryToUpdateBookDetails = "UPDATE '" + this.username + "_books' SET bookAvailable = 0 WHERE bookID = '" + book.getBookID() + "';";
            String queryToAddBookToBorrowedBooks = "INSERT INTO "+this.username+"_borrowedBooks VALUES " +
                    "('"+borrowerID+"', '"+book.getBookID()+"', '"+dateBorrowed+"', '0000-00-00', 0);";

            try {
                Connection con = DBConnection.getConnection();
                Statement stmt = con.createStatement();
                stmt.execute(queryToUpdateBookDetails);
                stmt.execute(queryToAddBookToBorrowedBooks);
                stmt.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        cancel();
    }

    public void cancel() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }
}
