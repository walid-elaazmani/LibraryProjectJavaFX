package javafx;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Node;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.Label;
        import javafx.scene.control.TextField;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.stage.Stage;
        import model.Person;
        import model.User;
        import service.UserService;

        import java.io.IOException;
        import java.util.Objects;
        import java.util.Optional;

public class RegisterSceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    UserService userService = new UserService();

    @FXML
    private ImageView wrongImage;

    @FXML
    private Label message;

    @FXML
    private TextField name;

    @FXML
    private TextField password;

    @FXML
    private TextField userName;


    @FXML
    void goBack(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Scene.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void register(ActionEvent event) throws IOException {
        Optional<Person> user = userService.register(name.getText(), userName.getText(), password.getText());

        if(user.isEmpty()){
            message.setText("wrong credentials !");
            wrongImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("wrong.png"))));
            return;
        }

        message.setText("Registered !");
        wrongImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("thumbUp.png"))));
    }
}

