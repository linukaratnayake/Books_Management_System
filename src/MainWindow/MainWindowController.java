package MainWindow;

import Management.DBConnection;
import Tables.MyBooks;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private Label lblFullName;

    @FXML
    private Label lblUsername;

    @FXML
    private ImageView ivLogo;

    @FXML
    private Button btnAdd, btnUpdate, btnDelete;

    @FXML
    private TableView<MyBooks> tblMyBooks;

    @FXML
    private TableColumn<MyBooks, String> MyBooksBookID, MyBooksBookName, MyBooksBookAuthor, MyBooksBookDateBought, MyBooksBookCategory;

    @FXML
    private TableColumn<MyBooks, Integer> MyBooksBookRead, MyBooksBookAvailable;

    private String username;

    ObservableList<MyBooks> listMyBooks = FXCollections.observableArrayList();

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ivLogo.setImage(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));

        MyBooksBookID.setCellValueFactory(new PropertyValueFactory<>("BookID"));
        MyBooksBookName.setCellValueFactory(new PropertyValueFactory<>("BookName"));
        MyBooksBookAuthor.setCellValueFactory(new PropertyValueFactory<>("BookAuthor"));
        MyBooksBookDateBought.setCellValueFactory(new PropertyValueFactory<>("BookDateBought"));
        MyBooksBookCategory.setCellValueFactory(new PropertyValueFactory<>("BookCategory"));
        MyBooksBookRead.setCellValueFactory(new PropertyValueFactory<>("BookRead"));
        MyBooksBookAvailable.setCellValueFactory(new PropertyValueFactory<>("BookAvailable"));
    }

    public void setUserData(String fullName, String username){
        lblFullName.setText(fullName);
        lblUsername.setText("@" + username);
    }

    public void populateTableMyBooks(){

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM '"+this.username+"_books';");
            while (rs.next()) {
                listMyBooks.add(new MyBooks(rs.getString("bookID"), rs.getString("bookName"),rs.getString("bookAuthor"),
                        rs.getString("bookDateBought"), rs.getString("bookCategory"),
                        Integer.parseInt(rs.getString("bookRead")),Integer.parseInt(rs.getString("bookAvailable"))));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        tblMyBooks.setItems(listMyBooks);
    }
}
