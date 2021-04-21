package MainWindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML
    private Label lblFullName;

    @FXML
    private Label lblUsername;

    @FXML
    private ImageView ivLogo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ivLogo.setImage(new Image(String.valueOf(getClass().getResource("/Logo/BMS Logo.png"))));
    }

    public void setUserData(String fullName, String username){
        lblFullName.setText(fullName);
        lblUsername.setText("@" + username);
    }
}
