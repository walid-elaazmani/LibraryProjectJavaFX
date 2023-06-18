package service;

import model.*;
import repository.BookRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BookService {
    public BookService() {
        BookRepository.bookRepository();
    }
    public List<Book> getListOfBooks(){
        return BookRepository.getListOfBooks();
    }

    public void addBook(String title, String author, String isbn, int yearOfPublication) {

        if (checkBook(title, author, isbn, yearOfPublication)) {
            BookRepository.addBook(new Book(title, author, isbn, yearOfPublication));
            System.out.println("The book has been added to the system");
        } else {
            System.out.println("Data entered incorrectly");
        }
    }

    public void reserveBooks(User user){
        if(user.getFine()>0){
            System.out.println("Can not borrow books, has a fine of");
            return;
        }

        for (Book cartBook : user.getCart().getListOfBooksInCart()) {

            if(user.getListOfActiveBorrowedBooks().size()>3){
                System.out.println("You already have 4 borrowed books");
                return;
            }

            changeStatus(cartBook.getBookId(), Status.RESERVED);
            BorrowedBook newBorrowedBook = new BorrowedBook(cartBook,user);
            newBorrowedBook.setActive(true);
            user.addBorrowedBook(newBorrowedBook);
            System.out.println(getBookById(cartBook.getBookId()));
            System.out.println(cartBook);
        }
        user.getCart().clearCart();
    }

    private BorrowedBook createBorrowedBook(User user, Book book){
        return new BorrowedBook(book, user);
    }

    public void issueBookToUser(User user, Book book, LocalDate dateOfReturn){
        if(user == null || book == null || dateOfReturn == null || dateOfReturn.isBefore(LocalDate.now())){
            return;
        }

        if(getBookById(book.getBookId()).isEmpty()){
            return;
        }

        if(getBookById(book.getBookId()).get().getStatus().equals(Status.UNAVAILABLE)
                || getBookById(book.getBookId()).get().getStatus().equals(Status.RESERVED)){
            System.out.println("book unavailable");
            return;
        }

        if(user.getListOfActiveBorrowedBooks().size()>3){
            System.out.println("You already have 4 borrowed books");
            return;
        }

        changeStatus(book.getBookId(), Status.UNAVAILABLE);
        BorrowedBook newBorrowedBook = new BorrowedBook(book,user,dateOfReturn);
        newBorrowedBook.setActive(true);
        user.addBorrowedBook(newBorrowedBook);
    }

    private  boolean checkBook (String title, String author, String isbn, int yearOfPublication) {
        LocalDate date = LocalDate.now();
        int current_year = date.getYear();

        if (isbn.length() != 13) {
            System.out.println("Please, select the correct ISBN");
            return false;
        } else if (yearOfPublication < 1000 || yearOfPublication > current_year) {
            System.out.println("Please, select the correct year");
            return false;
        } else if (author == null || author.isEmpty()) {
            System.out.println("Please, select the author");
            return false;
        } else if (title == null || title.isEmpty()) {
            System.out.println("Please, select the title");
            return false;
        } else {
            return true;
        }
    }

    public void returnAllBooks(User user){
        for (BorrowedBook borrowedBook : user.getListOfActiveBorrowedBooks()) {
            changeStatus(borrowedBook.getBook().getBookId(), Status.UNAVAILABLE);
            borrowedBook.setActive(false);
        }
    }
    public void returnBook(User user, long id){
        for (BorrowedBook borrowedBook : user.getListOfActiveBorrowedBooks()) {
            if(borrowedBook.getBook().getBookId() == id){
                changeStatus(borrowedBook.getBook().getBookId(), Status.AVAILABLE);
                borrowedBook.setActive(false);
            }
        }
    }

    public String issueReservedBooks(User user){
        if(user.getFine()>0){
            return "User has a fine !";
        }
        for (BorrowedBook borrowedBook : user.getListOfBorrowedBooks()) {
            if (borrowedBook.getBook().getStatus().equals(Status.RESERVED)){
                changeStatus(borrowedBook.getBook().getBookId(), Status.UNAVAILABLE);
                borrowedBook.setActive(true);
                return "Book issued";
            }
        }
        return "Book unavailable or already issued";
    }

    public void changeStatus(long id, Status status){
        BookRepository.changeStatus(id, status);
    }

    public void removeBook(long id){
        BookRepository.removeBook(id);
    }
    public Optional<Book> getBookById(long id){
        return BookRepository.searchById(id);
    }

    public List<Book> getBookByTitle(String title){
        return BookRepository.getBooksByTitle(title);
    }

    public List<Book> getBookByAuthor(String Author){
        return BookRepository.getBooksByAuthor(Author);
    }

    public List<Book> getBookByIsbn(String ISBN){
        return BookRepository.getBooksByIsbn(ISBN);
    }

    public List<Book> getBookByYearOfPublication(int yOP){
        return BookRepository.getBooksByYearOfPublication(yOP);
    }

    public List<Book> sortByTitle(List<Book> listToBeSorted){
        return listToBeSorted.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public List<Book> sortByAuthor(List<Book> listToBeSorted){
        return listToBeSorted.stream()
                .sorted(Comparator.comparing(Book::getAuthor))
                .collect(Collectors.toList());
    }

    public List<Book> sortByISBN(List<Book> listToBeSorted){
        return listToBeSorted.stream()
                .sorted(Comparator.comparing(Book::getIsbn))
                .collect(Collectors.toList());
    }
}
