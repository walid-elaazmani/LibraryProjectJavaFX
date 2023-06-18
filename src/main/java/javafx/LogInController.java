package javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Librarian;
import model.Person;
import model.User;
import service.UserService;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static javafx.scene.paint.Color.color;

public class LogInController {
    UserService userService = new UserService();

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField logInUserName;
    @FXML
    private PasswordField logInPassword;
    @FXML
    private Label loginerror;

    @FXML
    private ImageView crying;

    @FXML
    private ImageView rage;
    int count = 0;


    public void LogIn(ActionEvent event) throws IOException {

        Optional<Person> person = userService.login(logInUserName.getText(), logInPassword.getText());

        if(person.isPresent() && person.get() instanceof User){
            UserMenuController.user = (User)person.get();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserMenu.fxml")));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

        if(person.isPresent() && person.get() instanceof Librarian){
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LibrarianMenu.fxml")));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }


        if(count >= 2){
            crying.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("crying.png"))));
            rage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("rage.png"))));
        }

        loginerror.setText("wrong credentials !");
        loginerror.setTextFill(color(1,0,0));

        count ++;
    }

    public void logOff(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Scene.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
