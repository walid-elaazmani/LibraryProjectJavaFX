package javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.*;
import service.BookService;
import service.TransactionService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.DAYS;

public class UserMenuController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    static User user;
    BookService bookService = new BookService();

    @FXML
    private Label LoggedInAsLabel;
    @FXML
    private TableView<Book> table;
    @FXML
    private TableColumn<Book,String> title;
    @FXML
    private TableColumn<Book,String> author;
    @FXML
    private TableColumn<Book,String> isbn;
    @FXML
    private TableColumn<Book,Integer> year;
    @FXML
    private TableColumn<Book,Long> BookId;
    @FXML
    private TableColumn<Book, Status> status;

    @FXML
    private TextField searchField;

    ObservableList<Book> observableBooksList = FXCollections.observableArrayList();
    @FXML
    private Tab cartPanel;

    @FXML
    private ListView<Book> cartList;

    @FXML
    private Label accountBorrowedBooks;

    @FXML
    private Label accountNameLabel;

    @FXML
    private Label accountUserNameLabel;

    @FXML
    private ListView<BorrowedBook> borrowedBooks;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        title.setCellValueFactory(new PropertyValueFactory<Book,String>("title"));
        author.setCellValueFactory(new PropertyValueFactory<Book,String>("author"));
        isbn.setCellValueFactory(new PropertyValueFactory<Book,String>("isbn"));
        year.setCellValueFactory(new PropertyValueFactory<Book,Integer>("yearOfPublication"));
        BookId.setCellValueFactory(new PropertyValueFactory<Book,Long>("bookId"));
        status.setCellValueFactory(new PropertyValueFactory<Book,Status>("status"));

        observableBooksList.addAll(bookService.getListOfBooks());

        FilteredList<Book> filteredData = new FilteredList<>(observableBooksList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {

                if(newValue.isEmpty() || newValue.isBlank()){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(book.getAuthor().toLowerCase().contains(searchKeyword)){
                    return true;
                } else if (book.getTitle().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if ((String.valueOf(book.getYearOfPublication()).contains(searchKeyword))){
                    return true;
                } else if ((String.valueOf(book.getIsbn()).contains(searchKeyword))) {
                    return true;
                } else if ((String.valueOf(book.getBookId()).matches(searchKeyword))) {
                    return true;
                } else if ((String.valueOf(book.getStatus()).contains(searchKeyword.toUpperCase()))) {
                    return true;
                } else return false;
            });
        });

        SortedList<Book> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);


        // Initialise CartList
        ObservableList<Book> list = FXCollections.observableArrayList(user.getCart().getListOfBooksInCart());
        cartList.setItems(list);
        cartPanel.setText("Cart" + " (" + user.getCart().getListOfBooksInCart().size() + ")");

        // Initialise Account borrowed Books
        updateBorrowedBooks();
    }


    public void clearCart() {
        user.getCart().clearCart();
        updateCart();
    }

    public void removeBook(){
        user.getCart().removeBook(cartList.getSelectionModel().getSelectedItem());
        updateCart();
    }

    public void reserveBooks(){
        bookService.reserveBooks(user);
        updateCart();
        table.refresh();

        borrowedBooks.refresh();
        updateBorrowedBooks();
    }

    public void addBookToCart(){
        // Get selected Item from table
        Book book = table.getSelectionModel().getSelectedItem();

        // Add book to cart
        if(book == null){
            LoggedInAsLabel.setText("Select Book First !");
        }
        TransactionService transactionService = new TransactionService();
        LoggedInAsLabel.setText(transactionService.addBookToCart(user.getCart(), book));
        // Put items of User Cart into visible list
        updateCart();
    }

    private void updateCart(){
        ObservableList<Book> list = FXCollections.observableArrayList(user.getCart().getListOfBooksInCart());
        cartList.setItems(list);
        cartPanel.setText("Cart" + " (" + user.getCart().getListOfBooksInCart().size() + ")");
    }

    public void logOff(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Scene.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void updateBorrowedBooks(){
        ObservableList<BorrowedBook> list2 = FXCollections.observableArrayList();
        list2.addAll(user.getListOfActiveBorrowedBooks());
        borrowedBooks.setItems(list2);
    }
}