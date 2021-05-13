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
    private TableColumn<MyBooks, String> MBBkID, MBBkName, MBBkAuthor, MBBkDateBought, MBBkCategory;

    @FXML
    private TableColumn<MyBooks, Integer> MBBkRead, MBBkAvailable;

    ObservableList<MyBooks> listMyBooks = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ivLogo.setImage(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));

        MBBkID.setCellValueFactory(new PropertyValueFactory<>("BkID"));
        MBBkName.setCellValueFactory(new PropertyValueFactory<>("BkName"));
        MBBkAuthor.setCellValueFactory(new PropertyValueFactory<>("BkAuthor"));
        MBBkDateBought.setCellValueFactory(new PropertyValueFactory<>("BkDateBought"));
        MBBkCategory.setCellValueFactory(new PropertyValueFactory<>("BkCategory"));
        MBBkRead.setCellValueFactory(new PropertyValueFactory<>("BkRead"));
        MBBkAvailable.setCellValueFactory(new PropertyValueFactory<>("BkAvailable"));
        populateTableMyBooks();
    }

    public void setUserData(String fullName, String username){
        lblFullName.setText(fullName);
        lblUsername.setText("@" + username);
    }

    public void populateTableMyBooks(){
        // TODO - Tables should populate correspond to the user.
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books;");
            while (rs.next()) {
                listMyBooks.add(new MyBooks(rs.getString("bkID"), rs.getString("bkName"),rs.getString("bkAuthor"),
                        rs.getString("bkDateBought"), rs.getString("bkCategory"),
                        Integer.parseInt(rs.getString("bkRead")),Integer.parseInt(rs.getString("bkAvailable"))));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        tblMyBooks.setItems(listMyBooks);
    }
}
