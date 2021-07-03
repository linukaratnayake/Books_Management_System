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
    private Label lblMyBooks, lblBorrowedBooks, lblToBeReturned, lblFinishedReading;

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
    private TableView<Book> tblMyBooks;

    @FXML
    private TableColumn<Book, String> MyBooksBookID, MyBooksBookName, MyBooksBookAuthor, MyBooksBookDateBought, MyBooksBookCategory;

    @FXML
    private TableColumn<Book, Integer> MyBooksBookRead, MyBooksBookAvailable;

    private String username;

    ObservableList<Book> listMyBooks = FXCollections.observableArrayList();

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ivLogo.setImage(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
        lblMyBooks.setStyle("-fx-font-size: 26px");

        MyBooksBookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        MyBooksBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        MyBooksBookAuthor.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
        MyBooksBookDateBought.setCellValueFactory(new PropertyValueFactory<>("bookDateBought"));
        MyBooksBookCategory.setCellValueFactory(new PropertyValueFactory<>("bookCategory"));
        MyBooksBookRead.setCellValueFactory(new PropertyValueFactory<>("bookRead"));
        MyBooksBookAvailable.setCellValueFactory(new PropertyValueFactory<>("bookAvailable"));

        cmbCategory.setOnAction(actionEvent -> { // A Lambda expression, suggested by the IDE.
            String category = cmbCategory.getValue();
            tblMyBooks.getItems().clear();
            populateTableMyBooks(category);
        });

        tblMyBooks.getSelectionModel().selectedItemProperty().addListener((observableValue, book, t1) -> rowChanged()); // A Lambda expression, suggested by the IDE.
    }

    public void setUserData(String fullName, String username){
        lblFullName.setText(fullName);
        lblUsername.setText("@" + username);
    }

    public void populateTableMyBooks(String category){
        String query = "SELECT * FROM '"+this.username+"_books';";

        if ((category != null) && (!category.equals("All Categories"))){
            query = "SELECT * FROM '"+this.username+"_books' WHERE bookCategory = '"+category+"';";
        }

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                listMyBooks.add(new Book(rs.getString("bookID"), rs.getString("bookName"),rs.getString("bookAuthor"),
                        rs.getString("bookDateBought"), rs.getString("bookCategory"),
                        Integer.parseInt(rs.getString("bookRead")),Integer.parseInt(rs.getString("bookAvailable"))));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tblMyBooks.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // To select multiple rows at once.
        tblMyBooks.setItems(listMyBooks);
        cmbCategory.setItems(getAvailableCategories());
        cmbCategory.getSelectionModel().select(category);
    }

    public ObservableList<String> getAvailableCategories() {
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

            if (categories.size() == 0) {
                cmbCategory.setPromptText("(No Categories)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void rowChanged () {
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

        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);

        btnFinishedReading.setDisable(!atLeastOneNotRead);

        btnMarkAsBorrowed.setDisable(!allAvailable);
    }

    public void markAsRead() {
        ObservableList<Book> selectedBooks = tblMyBooks.getSelectionModel().getSelectedItems();

        for (Book book : selectedBooks) {
            book.setBookRead(true);
            // TODO add to 'Finished Reading'.
            String query = "UPDATE '"+this.username+"_books' SET bookRead = 1 WHERE bookID = '"+book.getBookID()+"';";
            try {
                Connection con = DBConnection.getConnection();
                Statement stmt = con.createStatement();
                stmt.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        tblMyBooks.refresh();
    }

    public void borrowBook() {
        ObservableList<Book> selectedBooks = tblMyBooks.getSelectionModel().getSelectedItems();

        for (Book book : selectedBooks) {
            book.setBookAvailable(false);
            // TODO add to 'Borrowed Books' and 'To be Returned'.
            String query = "UPDATE '"+this.username+"_books' SET bookAvailable = 0 WHERE bookID = '"+book.getBookID()+"';";
            try {
                Connection con = DBConnection.getConnection();
                Statement stmt = con.createStatement();
                stmt.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        tblMyBooks.refresh();
    }

    public void addNewBook() {
        Stage stageForNewBook = new Stage();
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddNewBook.fxml")));
            // Above code was suggested by the IDE.
            stageForNewBook.setTitle("Add New Book | Books Management System (BMS)");
            stageForNewBook.setScene(new Scene(root));
            stageForNewBook.setResizable(false);
            stageForNewBook.initOwner(Main.primaryStage);
            stageForNewBook.initModality(Modality.APPLICATION_MODAL);
            stageForNewBook.getIcons().add(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
            stageForNewBook.centerOnScreen();
            stageForNewBook.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
