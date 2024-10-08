package MainWindow;

import Main.Main;
import Management.DBConnection;
import Tables.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private AnchorPane paneMyBooks, paneBorrowedFromMe, paneBorrowedByMe;

    @FXML
    private Label lblMyBooks, lblBorrowedFromMe, lblToBeReturned, lblFinishedReading;

    @FXML
    private Label lblFullName;

    @FXML
    private Label lblUsername;

    @FXML
    private ImageView ivLogo;

    @FXML
    private ComboBox<String> cmbCategory;

    @FXML
    private Button btnAdd, btnUpdate, btnDelete, btnMarkAsBorrowed, btnFinishedReading;

    @FXML
    private Button btnMarkAsReturned;

    @FXML
    private Button btnAddNewByMe, btnUpdateByMe, btnDeleteByMe, btnMarkAsReturnedByMe;

    @FXML
    protected TableView<Book> tblMyBooks, tblBorrowedFromMe, tblBorrowedByMe;

    @FXML
    private TableColumn<Book, String> MyBooksBookID, MyBooksBookName, MyBooksBookAuthor, MyBooksBookDateBought, MyBooksBookCategory;

    @FXML
    private TableColumn<Book, Integer> MyBooksBookRead, MyBooksBookAvailable;

    @FXML
    private TableColumn<Book, String> BorrowedFromMeBorrower, BorrowedFromMeBookID, BorrowedFromMeBookName, BorrowedFromMeBookAuthor,
            BorrowedFromMeDateBorrowed, BorrowedFromMeDateReturned;

    @FXML
    private TableColumn<Book, Integer> BorrowedFromMeReturned;

    @FXML
    private TableColumn<Book, String> BorrowedByMeBorrowedFrom, BorrowedByMeBookID, BorrowedByMeBookName, BorrowedByMeBookAuthor,
            BorrowedByMeDateBorrowed, BorrowedByMeDateReturned;

    @FXML
    private TableColumn<Book, Integer> BorrowedByMeReturned;

    protected final String username;
    private final String fullName;
    protected String currentCategory = "All Categories";
    private boolean comboBoxActionListenerOn = true;

    Stage stageToLendBook = new Stage();

    public MainWindowController(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paneBorrowedFromMe.setVisible(false);
        // TODO - Set other pane visibilities too.

        setUserData();
        ivLogo.setImage(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
       // lblMyBooks.setStyle("-fx-font-size: 26px");

        MyBooksBookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        MyBooksBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        MyBooksBookAuthor.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
        MyBooksBookDateBought.setCellValueFactory(new PropertyValueFactory<>("bookDateBought"));
        MyBooksBookCategory.setCellValueFactory(new PropertyValueFactory<>("bookCategory"));
        MyBooksBookRead.setCellValueFactory(new PropertyValueFactory<>("bookRead"));
        MyBooksBookAvailable.setCellValueFactory(new PropertyValueFactory<>("bookAvailable"));

        BorrowedFromMeBorrower.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        BorrowedFromMeBookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        BorrowedFromMeBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        BorrowedFromMeBookAuthor.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
        BorrowedFromMeDateBorrowed.setCellValueFactory(new PropertyValueFactory<>("bookDateBorrowed"));
        BorrowedFromMeDateReturned.setCellValueFactory(new PropertyValueFactory<>("bookDateReturned"));
        BorrowedFromMeReturned.setCellValueFactory(new PropertyValueFactory<>("bookReturned"));

        cmbCategory.valueProperty().addListener((observableValue, s, t1) -> {
            if (comboBoxActionListenerOn) {
                tblMyBooks.getItems().clear();
                populateTableMyBooks(cmbCategory.getValue());
            }
        });

        tblMyBooks.getSelectionModel().selectedItemProperty().addListener((observableValue, book, t1) -> rowChangedTblMyBooks());
        populateTableBorrowedFromMe();
        tblBorrowedFromMe.getSelectionModel().selectedItemProperty().addListener((observableValue, book, t1) -> rowChangedTblBorrowedFromMe());
    }

    private void setUserData(){
        lblFullName.setText(fullName);
        lblUsername.setText("@" + username);
    }

    public void populateTableMyBooks(String category){
        if (category != null) {
            this.currentCategory = category;
        }

        String query = "SELECT * FROM '"+this.username+"_books';";

        if ((category != null) && (!category.equals("All Categories"))){
            query = "SELECT * FROM '"+this.username+"_books' WHERE bookCategory = '"+category+"';";
        }

        ObservableList<Book> listMyBooks = FXCollections.observableArrayList();

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                listMyBooks.add(new Book(rs.getString("bookID"), rs.getString("bookName"),rs.getString("bookAuthor"),
                        rs.getString("bookDateBought"), rs.getString("bookCategory"),
                        Integer.parseInt(rs.getString("bookRead")),Integer.parseInt(rs.getString("bookAvailable"))));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tblMyBooks.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // To select multiple rows at once.
        tblMyBooks.setItems(listMyBooks);
        cmbCategory.setItems(getAvailableCategories());
        cmbCategory.getSelectionModel().select(category);
    }

    private ObservableList<String> getAvailableCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList();

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT bookCategory FROM '"+this.username+"_books';");

            while (rs.next()) {
                if (rs.isFirst()) {
                    categories.add("All Categories");
                }
                String category = rs.getString("bookCategory");
                if (!categories.contains(category)) {
                    categories.add(category);
                }
            }
            rs.close();
            stmt.close();
            con.close();

            if (categories.size() == 0) {
                cmbCategory.setPromptText("(No Categories)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void populateTableBorrowedFromMe () {
        ObservableList<Book> listBorrowedBooks = FXCollections.observableArrayList();

        String query = "SELECT "+this.username+"_borrowers.borrowerName, " +
                ""+this.username+"_books.bookID, " +
                ""+this.username+"_books.bookName, " +
                ""+this.username+"_books.bookAuthor, " +
                ""+this.username+"_borrowedBooks.dateBorrowed, " +
                ""+this.username+"_borrowedBooks.dateReturned, " +
                ""+this.username+"_borrowedBooks.returned " +
                "FROM "+this.username+"_borrowedBooks " +
                "LEFT JOIN "+this.username+"_borrowers ON "+this.username+"_borrowers.borrowerID = "+this.username+"_borrowedBooks.borrowerID " +
                "LEFT JOIN "+this.username+"_books ON "+this.username+"_books.bookID = "+this.username+"_borrowedBooks.bookID;";

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                listBorrowedBooks.add(new Book(rs.getString("borrowerName"), rs.getString("bookID"),
                        rs.getString("bookName"), rs.getString("bookAuthor"),
                        rs.getString("dateBorrowed"), rs.getString("dateReturned"),
                        Integer.parseInt(rs.getString("returned"))));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblBorrowedFromMe.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblBorrowedFromMe.setItems(listBorrowedBooks);
    }

    private void rowChangedTblMyBooks() {
        ObservableList<Book> selectedBooks = tblMyBooks.getSelectionModel().getSelectedItems();

        boolean atLeastOneNotRead = false;
        boolean allAvailable = true;
        for (Book book : selectedBooks) {
            if (book.getBookRead().equals("No")) {
                atLeastOneNotRead = true;
            }
            if (book.getBookAvailable().equals("No")) {
                allAvailable = false;
            }
        }

        btnUpdate.setDisable(selectedBooks.size() != 1);
        btnDelete.setDisable(false);

        btnFinishedReading.setDisable(!atLeastOneNotRead);

        btnMarkAsBorrowed.setDisable(!allAvailable);
    }

    public void rowChangedTblBorrowedFromMe() {
        ObservableList<Book> selectedBooks = tblBorrowedFromMe.getSelectionModel().getSelectedItems();

        boolean notReturnedYet = true;

        for (Book book : selectedBooks) {
            if (book.getBookReturned().equals("Yes")) {
                notReturnedYet = false;
                break;
            }
        }

        btnMarkAsReturned.setDisable(!notReturnedYet);
    }

    public void refreshTableMyBooks() {
        rowChangedTblMyBooks();
        tblMyBooks.refresh();
    }

    public void refreshTableBorrowedFromMe() {
        rowChangedTblBorrowedFromMe();
        tblBorrowedFromMe.refresh();
    }

    public void markAsReturned() {
        ObservableList<Book> selectedBooks = tblBorrowedFromMe.getSelectionModel().getSelectedItems();

        Stage stageToGetReturnedDate = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookReturnDetails.fxml"));
            BookReturnDetailsController bookReturnDetailsController = new BookReturnDetailsController();
            loader.setController(bookReturnDetailsController);
            Parent root = loader.load();
            stageToGetReturnedDate.setTitle("Book Returning - Date Returned | Books Management System (BMS)");
            stageToGetReturnedDate.setScene(new Scene(root));
            stageToGetReturnedDate.setResizable(false);
            stageToGetReturnedDate.initOwner(Main.primaryStage);
            stageToGetReturnedDate.initModality(Modality.APPLICATION_MODAL);
            stageToGetReturnedDate.getIcons().add(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
            stageToGetReturnedDate.centerOnScreen();
            stageToGetReturnedDate.show();
            stageToGetReturnedDate.setOnHiding(windowEvent -> {
                String returnedDate = bookReturnDetailsController.getReturnedDate();
                boolean isReturnedDateCollectedSuccessfully = bookReturnDetailsController.getReturnedDate() != null;

                if (!isReturnedDateCollectedSuccessfully) {
                    return;
                }

                for (Book book : selectedBooks) {
                    book.setBookReturned(true);
                    book.setBookDateReturned(returnedDate);

                    String queryToUpdateTableBorrowedBooks = "UPDATE '"+this.username+"_borrowedBooks' SET dateReturned = '"+returnedDate+"', returned = 1 WHERE bookID = '"+book.getBookID()+"';";
                    String queryToUpdateTableMyBooks = "UPDATE '"+this.username+"_books' SET bookAvailable = 1 WHERE bookID = '"+book.getBookID()+"';";
                    try {
                        Connection con = DBConnection.getConnection();
                        Statement stmt = con.createStatement();
                        stmt.execute(queryToUpdateTableBorrowedBooks);
                        stmt.execute(queryToUpdateTableMyBooks);
                        stmt.close();
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                refreshTableBorrowedFromMe();

                comboBoxActionListenerOn = false;
                populateTableMyBooks(currentCategory);
                comboBoxActionListenerOn = true;
                // Temporal disabling of the ActionListener is mandatory because it can cause an infinite loop of populating the table.
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void markAsRead() {
        ObservableList<Book> selectedBooks = tblMyBooks.getSelectionModel().getSelectedItems();

        for (Book book : selectedBooks) {
            book.setBookRead(true);

            String query = "UPDATE '"+this.username+"_books' SET bookRead = 1 WHERE bookID = '"+book.getBookID()+"';";
            try {
                Connection con = DBConnection.getConnection();
                Statement stmt = con.createStatement();
                stmt.execute(query);
                stmt.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        refreshTableMyBooks();
    }

    public void borrowBook() {
        ObservableList<Book> selectedBooks = tblMyBooks.getSelectionModel().getSelectedItems();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LendBook.fxml"));
            LendBookController lendBookController = new LendBookController(this.username, selectedBooks, stageToLendBook);
            loader.setController(lendBookController);
            Parent root = loader.load();
            lendBookController.populateTableSelectedBooks();
            lendBookController.populateTableBorrowers();
            stageToLendBook.setTitle("Lend Book(s) | Books Management System (BMS)");
            stageToLendBook.setScene(new Scene(root));
            stageToLendBook.setResizable(false);
            stageToLendBook.initOwner(Main.primaryStage);
            stageToLendBook.initModality(Modality.APPLICATION_MODAL);
            stageToLendBook.getIcons().add(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
            stageToLendBook.centerOnScreen();
            stageToLendBook.show();
            stageToLendBook.setOnHiding(windowEvent -> {
                comboBoxActionListenerOn = false;
                populateTableMyBooks(currentCategory);
                comboBoxActionListenerOn = true;
                populateTableBorrowedFromMe();
                // Temporal disabling of the ActionListener is mandatory because it can cause an infinite loop of populating the table.
            }); // A lambda expression, suggested by the IDE.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewBook() {
        Stage stageForNewBook = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddNewBook.fxml"));
            AddNewBookController addNewBookController = new AddNewBookController(this.username);
            loader.setController(addNewBookController);
            Parent root = loader.load();
            stageForNewBook.setTitle("Add New Book | Books Management System (BMS)");
            stageForNewBook.setScene(new Scene(root));
            stageForNewBook.setResizable(false);
            stageForNewBook.initOwner(Main.primaryStage);
            stageForNewBook.initModality(Modality.APPLICATION_MODAL);
            stageForNewBook.getIcons().add(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
            stageForNewBook.centerOnScreen();
            stageForNewBook.show();
            stageForNewBook.setOnHiding(windowEvent -> {
                comboBoxActionListenerOn = false;
                populateTableMyBooks(currentCategory);
                comboBoxActionListenerOn = true;
                // Temporal disabling of the ActionListener is mandatory because it can cause an infinite loop of populating the table.
            }); // A lambda expression, suggested by the IDE.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateBook() {
        Book book = tblMyBooks.getSelectionModel().getSelectedItem();

        Stage stageToUpdateBook = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateBook.fxml"));
            UpdateBookController updateBookController = new UpdateBookController(this.username, book);
            loader.setController(updateBookController);
            Parent root = loader.load();
            stageToUpdateBook.setTitle("Update Book Details | Books Management System (BMS)");
            stageToUpdateBook.setScene(new Scene(root));
            stageToUpdateBook.setResizable(false);
            stageToUpdateBook.initOwner(Main.primaryStage);
            stageToUpdateBook.initModality(Modality.APPLICATION_MODAL);
            stageToUpdateBook.getIcons().add(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
            stageToUpdateBook.centerOnScreen();
            stageToUpdateBook.show();
            stageToUpdateBook.setOnHiding(windowEvent -> {
                refreshTableMyBooks();
                populateTableBorrowedFromMe();
            }); // A lambda expression, suggested by the IDE.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteBooks() {
        ObservableList<Book> selectedBooks = tblMyBooks.getSelectionModel().getSelectedItems();

        // TODO Confirmation dialog ,must appear here.

        for (Book book : selectedBooks) {
            String bookID = book.getBookID();
            String queryToDelete = "DELETE FROM '"+this.username+"_books' WHERE bookID = '"+bookID+"';";
            try {
                Connection con = DBConnection.getConnection();
                Statement stmt = con.createStatement();
                stmt.execute(queryToDelete);
                stmt.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        comboBoxActionListenerOn = false;
        populateTableMyBooks(this.currentCategory);
        comboBoxActionListenerOn = true;
        // Temporal disabling of the ActionListener is mandatory because it can cause an infinite loop of populating the table.
    }

    public void addNewBorrowedBook() {
        Stage stageForNewBorrowedBook = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddNewBorrowedBook.fxml"));
            AddNewBorrowedBookController addNewBorrowedBookController = new AddNewBorrowedBookController(this.username);
            loader.setController(addNewBorrowedBookController);
            Parent root = loader.load();
            stageForNewBorrowedBook.setTitle("Add New Book | Books Management System (BMS)");
            stageForNewBorrowedBook.setScene(new Scene(root));
            stageForNewBorrowedBook.setResizable(false);
            stageForNewBorrowedBook.initOwner(Main.primaryStage);
            stageForNewBorrowedBook.initModality(Modality.APPLICATION_MODAL);
            stageForNewBorrowedBook.getIcons().add(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
            stageForNewBorrowedBook.centerOnScreen();
            stageForNewBorrowedBook.show();
            stageForNewBorrowedBook.setOnHiding(windowEvent -> {
//                comboBoxActionListenerOn = false;
                // TODO - populateTableBorrowedByMeBooks
                // populateTableMyBooks(currentCategory);
//                comboBoxActionListenerOn = true;
//                // Temporal disabling of the ActionListener is mandatory because it can cause an infinite loop of populating the table.
            }); // A lambda expression, suggested by the IDE.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void logout() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../Login/Login.fxml")));
            Main.primaryStage.setTitle("Login | Books Management System (BMS)");
            Main.primaryStage.setMaximized(false);
            Main.primaryStage.setScene(new Scene(root));
            Main.primaryStage.centerOnScreen();
            Main.primaryStage.setResizable(false);
            Main.primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
            Main.primaryStage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadPaneMyBooks () {
        paneMyBooks.setVisible(true);
        paneBorrowedFromMe.setVisible(false);
        paneBorrowedByMe.setVisible(false);
    }

    public void loadPaneBorrowedFromMe () {
        paneMyBooks.setVisible(false);
        paneBorrowedFromMe.setVisible(true);
        paneBorrowedByMe.setVisible(false);
    }

    public void loadPaneBorrowedByMe () {
        paneMyBooks.setVisible(false);
        paneBorrowedFromMe.setVisible(false);
        paneBorrowedByMe.setVisible(true);
    }
}
