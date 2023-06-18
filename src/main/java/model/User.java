package model;

import model.Person;
import service.BookService;

import java.util.ArrayList;
import java.util.List;

public class User extends Person {
    private Cart cart;
    private List<BorrowedBook> listOfBorrowedBooks;
    private double fine;

    public User(String userName, String name, double fine){
        userName = getUserName();
        name = super.getName();
        fine = this.fine;
    }

    public User(String name, String password, String username) {
        super(name, password, username);
        listOfBorrowedBooks = new ArrayList<>();
        this.cart = new Cart();
    }


    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<BorrowedBook> getListOfBorrowedBooks() {
        return listOfBorrowedBooks;
    }

    public List<BorrowedBook> getListOfActiveBorrowedBooks() {
        return getListOfBorrowedBooks().stream().filter(BorrowedBook::isActive).toList();
    }

    public void addBorrowedBook(BorrowedBook borrowedBook) {
        this.listOfBorrowedBooks.add(borrowedBook);
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }


    @Override
    public String toString() {
        return "User{" +
                "cart=" + cart +
                ", listOfBorrowedBooks=" + listOfBorrowedBooks +
                ", fine=" + fine +
                '}';
    }
}
