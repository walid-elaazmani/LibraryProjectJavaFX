package model;

import java.util.Objects;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private int yearOfPublication;
    private long bookId;
    static private long count = 0;
    private Status status;

    public Book(String title, String author, String isbn, int yearOfPublication) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.yearOfPublication = yearOfPublication;
        this.status = Status.AVAILABLE;
        setBookId();
    }


    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public long getBookId() {
        return bookId;
    }

    private void setBookId() {
        this.bookId = count ++;
    }

    @Override
    public String toString() {
        return "title='" + title  +
                ", author='" + author  +
                ", isbn='" + isbn  +
                ", yearOfPublication=" + yearOfPublication +
                ", bookId=" + bookId +
                ", status=" + status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return getBookId() == book.getBookId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookId());
    }
}
