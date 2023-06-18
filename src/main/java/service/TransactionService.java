package service;

import model.*;
import service.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class TransactionService {
    public String addBookToCart(Cart cart, Book book){
        // check status
        if (!(book.getStatus().equals(Status.AVAILABLE))){
            System.out.println("book unavailable !");
            return "Unavailable";
        }
        // Check if book is already in Cart
        for (Book cartBook : cart.getListOfBooksInCart()) {
            if (cartBook.getBookId() == book.getBookId()){
                System.out.println("Book already in cart");
                return "Book already in cart !";
            }
        }
        // Add book to Cart
        cart.addBookToCart(book);
        return "Book added to cart !";
    }

    public double checkAccount(User user){
        List<BorrowedBook> bookToList = new ArrayList<>(user.getListOfActiveBorrowedBooks());


        double fineForBooks = 0;
        for (BorrowedBook borrowedBook : bookToList) {
            if (DAYS.between(borrowedBook.getDateOfReturn(), LocalDate.now())>30){
                fineForBooks += (DAYS.between(borrowedBook.getDateOfReturn(), LocalDate.now()) * 0.20);
                System.out.println("You have a fine of: " + DAYS.between(borrowedBook.getDateOfReturn(), LocalDate.now()) * 0.20 + "€" + " for: " + borrowedBook.getBook().getTitle());
            }
        }
        return fineForBooks;
    }
}
