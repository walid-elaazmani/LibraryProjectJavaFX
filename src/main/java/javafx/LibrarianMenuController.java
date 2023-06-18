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
import javafx.stage.Stage;
import model.*;
import repository.BookRepository;
import service.BookService;
import service.TransactionService;
import service.UserService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class LibrarianMenuController implements Initializable {
    UserService userService = new UserService();
    TransactionService transactionService = new TransactionService();
    BookService bookService = new BookService();

    //<-----------------------------------------------USER TAB--------------------------------------------------------->
    // UserTable
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> userName;
    @FXML
    private TableColumn<User, String> name;
    @FXML
    private TableColumn<User, Double> fine;
    // User search field
    @FXML
    private TextField searchUserText;
    // Book table
    @FXML
    private TableView<BorrowedBook> bookTable;
    @FXML
    private TableColumn<BorrowedBook, LocalDate> dateIssued;
    @FXML
    private TableColumn<BorrowedBook, LocalDate> dateOfReturn;
    @FXML
    private TableColumn<BorrowedBook, Book> book;
    @FXML
    private Label message;

    // <------------------------------------------BOOKS TAB------------------------------------------------------------>
    @FXML
    private TableView<Book> bookTable2;
    @FXML
    private TableColumn<Book, String> bookAuthor;
    @FXML
    private TableColumn<Book, String> bookISBN;
    @FXML
    private TableColumn<Book, Long> bookId;
    @FXML
    private TableColumn<Book, Status> bookStatus;
    @FXML
    private TableColumn<Book, String> bookTitle;
    @FXML
    private TableColumn<Book, Integer> bookYear;

    ObservableList<Book> observableBooksList = FXCollections.observableArrayList();
    @FXML
    private TextField bookSearchField;
    @FXML
    private TableView<User> userTable2;
    @FXML
    private TableColumn<User,String> userName2;
    @FXML
    private TableColumn<User,String> name2;
    @FXML
    private TableColumn<User, Double> fine2;
    @FXML
    private TextField userSearchField;
    @FXML
    private DatePicker datePicker;

    Alert alert;

    @FXML
    private TextField addBookAuthor;

    @FXML
    private TextField addBookTitle;

    @FXML
    private TextField addBookTitleIsbn;

    @FXML
    private TextField addBookYearOfPublication;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // User Table
        userName.setCellValueFactory(new PropertyValueFactory<User,String>("userName"));
        name.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        fine.setCellValueFactory(new PropertyValueFactory<User,Double>("fine"));
        userTable.setItems(initialiseUserTable(searchUserText));

        // Book Table initialise column values
        book.setCellValueFactory(new PropertyValueFactory<BorrowedBook,Book>("book"));
        dateIssued.setCellValueFactory(new PropertyValueFactory<BorrowedBook, LocalDate>("dateIssued"));
        dateOfReturn.setCellValueFactory(new PropertyValueFactory<BorrowedBook, LocalDate>("dateOfReturn"));


        userTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        // Update the items in the bookTable based on the selected user
                        bookTable.setItems(FXCollections.observableArrayList(newSelection.getListOfActiveBorrowedBooks()));
                    }
                }
        );


        // Book Tab
        bookTitle.setCellValueFactory(new PropertyValueFactory<Book,String>("title"));
        bookAuthor.setCellValueFactory(new PropertyValueFactory<Book,String>("author"));
        bookISBN.setCellValueFactory(new PropertyValueFactory<Book,String>("isbn"));
        bookYear.setCellValueFactory(new PropertyValueFactory<Book, Integer>("YearOfPublication"));
        bookId.setCellValueFactory(new PropertyValueFactory<Book, Long>("bookId"));
        bookStatus.setCellValueFactory(new PropertyValueFactory<Book,Status>("status"));
        bookTable2.setItems(initialiseBookTable());

        // User table 2
        userName2.setCellValueFactory(new PropertyValueFactory<User,String>("userName"));
        name2.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        fine2.setCellValueFactory(new PropertyValueFactory<User,Double>("fine"));
        userTable2.setItems(initialiseUserTable(userSearchField));

    }

    @FXML
    void searchUser(ActionEvent event) {

        String searchInput = searchUserText.getText();

        List<String> stringsToMatch = new ArrayList<>();

        for (Person person : userService.getListOfUsers()) {
            stringsToMatch.add(person.getUserName());
            stringsToMatch.add(person.getName());
        }
    }

    @FXML
    public void issueReservedBooks(){
        bookService.issueReservedBooks(userTable.getSelectionModel().getSelectedItem());

        userTable.refresh();
        bookTable.refresh();
    }

    @FXML
    public void returnBook(){
        User user = userTable.getSelectionModel().getSelectedItem();
        long id = bookTable.getSelectionModel().getSelectedItem().getBook().getBookId();
        bookService.returnBook(user, id);

        bookTable.refresh();
        userTable.refresh();
    }
    public void logOff(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Scene.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void issueBookToUser(){
        bookService.issueBookToUser(userTable2.getSelectionModel().getSelectedItem(), bookTable2.getSelectionModel().getSelectedItem(), datePicker.getValue());
        userTable2.refresh();
        bookTable2.refresh();
    }

    public void deleteBookButton(){
        Book selectedBook = bookTable2.getSelectionModel().getSelectedItem();

        if(selectedBook == null){
            System.out.println("select a book first");
            return;
        }

        alert = new Alert(Alert.AlertType.WARNING,"Error message", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Book will be deleted PERMANENTLY !!!");
        alert.setContentText("continue anyway ?");

        if(alert.showAndWait().get() == ButtonType.YES){
            bookService.removeBook(selectedBook.getBookId());
            System.out.println("book deleted");
        }

        initialiseBookTable();
        bookTable2.refresh();
        bookTable.refresh();
    }
    
    public void addBook(){
        alert = new Alert(Alert.AlertType.NONE);

        if(addBookTitle.getText().isEmpty()
                || addBookAuthor.getText().isEmpty()
                || !addBookTitleIsbn.getText().matches("\\d{13}")
                || addBookYearOfPublication == null
                || !addBookYearOfPublication.getText().matches("\\d{4}")){
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Check if title and author are filled in, that ISBN is 13 digits " +
                    "and the year of publication matches YYYY");
            alert.show();
            return;
        }

        if(!bookService.getBookByIsbn(addBookTitleIsbn.getText()).isEmpty()){
            Book book = BookRepository.getBooksByIsbn(addBookTitleIsbn.getText()).get(0);
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("ISBN exits");
            alert.setContentText("Book with same ISBN but different title exists, Click OK to proceed anyway");

            if (alert.showAndWait().get() == ButtonType.CANCEL
                    && (!book.getTitle().matches(addBookTitle.getText()) || !book.getTitle().matches(addBookAuthor.getText()))) {
                return;
            }
        }

        bookService.addBook(addBookTitle.getText(), addBookAuthor.getText(),
                addBookTitleIsbn.getText(), (Integer.parseInt(addBookYearOfPublication.getText())));
        System.out.println("book added");


        initialiseBookTable();
        bookTable2.refresh();
        bookTable.refresh();
    }

    public SortedList<User> initialiseUserTable(TextField textField){
        ObservableList<User> userObservableList = FXCollections.observableArrayList();

        for (Person listOfUser : userService.getListOfUsers()) {
            if(listOfUser instanceof User){
                userObservableList.add((User) listOfUser);
            }
        }

        FilteredList<User> filteredData = new FilteredList<>(userObservableList, b -> true);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {

                if(newValue.isEmpty() || newValue.isBlank()){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(user.getUserName().toLowerCase().contains(searchKeyword)){
                    return true;
                } else return user.getName().toLowerCase().contains(searchKeyword);
            });
        });

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(userTable.comparatorProperty());

        return sortedData;
    }

    private SortedList<Book> initialiseBookTable(){
        observableBooksList.clear();
        observableBooksList.addAll(bookService.getListOfBooks());

        FilteredList<Book> filteredBookData = new FilteredList<>(observableBooksList, b -> true);

        bookSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredBookData.setPredicate(book -> {

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

        SortedList<Book> sortedBookData = new SortedList<>(filteredBookData);
//        sortedBookData.comparatorProperty().bind(bookTable2.comparatorProperty());

        return sortedBookData;
    }



}
