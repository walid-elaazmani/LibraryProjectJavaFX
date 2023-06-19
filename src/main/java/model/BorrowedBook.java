package model;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;


public class BorrowedBook {
    private boolean isActive;
    private Book book;
    private User user;
    private LocalDate dateIssued;
    private LocalDate dateOfReturn;
    private double fine;


    //Model for JavaFX table
    public BorrowedBook(Book book, LocalDate dateIssued, LocalDate dateOfReturn) {
        book = getBook();
        dateIssued = this.dateIssued;
        dateOfReturn = this.dateOfReturn;
    }


    public BorrowedBook(Book book, User user) {
        this.book = book;
        this.user = user;
        this.dateIssued = LocalDate.now();
        this.dateOfReturn = LocalDate.now().plusDays(7);
        this.fine = 0;
    }

    public BorrowedBook(Book book, User user, LocalDate dateOfReturn) {
        this.book = book;
        this.user = user;
        this.dateIssued = LocalDate.now();
        this.dateOfReturn = dateOfReturn;
        this.fine = 0;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(LocalDate dateIssued) {
        this.dateIssued = dateIssued;
    }

    public LocalDate getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(LocalDate dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    @Override
    public String toString() {
        return "book: " + book.getTitle() + " by " + book.getAuthor() +
                " | user: " + user.getUserName() +
                " | dateIssued: " + dateIssued +
                " | dateOfReturn: " + dateOfReturn +
                " | " + book.getStatus();
    }
}
