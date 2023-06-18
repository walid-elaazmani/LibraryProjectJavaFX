package model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    List<Book> listOfBooksInCart;

    public Cart() {
        this.listOfBooksInCart = new ArrayList<>();
    }

    public List<Book> getListOfBooksInCart() {
        return listOfBooksInCart;
    }
    public void viewCart() {
        if (listOfBooksInCart.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            for (int i = 0; i < listOfBooksInCart.size(); i++) {
                System.out.println((i + 1) + ". " + listOfBooksInCart.get(i));
            }
        }
    }

    public void addBookToCart(Book book) {
        this.listOfBooksInCart.add(book);
    }

    public void removeBook(Book book){
        listOfBooksInCart.remove(book);
    }

    public void removeBook(int index) {
        if (index >= 0 && index < listOfBooksInCart.size()) {
            listOfBooksInCart.remove(index);
            System.out.println("Book removed from cart.");
        } else {
            System.out.println("Invalid book number.");
        }
    }

    public void clearCart(){
        listOfBooksInCart.clear();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "listOfBooksInCart=" + listOfBooksInCart +
                '}';
    }
}
