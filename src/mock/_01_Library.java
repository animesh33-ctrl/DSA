package mock;
import java.util.*;
// Book class
class Book {
    String bookId;
    String title;
    String author;
    boolean isAvailable;

    public Book() {
    }

    public Book(String bookId, String title, String author, boolean isAvailable) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId='" + bookId + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }

}

// Main interface
interface ILibrary {
    void addBook(Book book);
    boolean borrowBook(String bookId, String userId);
    boolean returnBook(String bookId);
    List<Book> searchByAuthor(String author);
    List<Book> getAvailableBooks();
    int getTotalBooks();
}

/*
Requirements:

Add books to library
Borrow book (mark as unavailable)
Return book (mark as available)
Search books by author
Show all available books
Count total books
 */

public class _01_Library implements ILibrary{
    List<Book> books = new ArrayList<>();

    @Override
    public void addBook(Book book) {
        if (!book.isAvailable) {
            book.isAvailable = true;
        }
        books.add(book);
        System.out.println("Successfully Added!!");
    }

    @Override
    public boolean borrowBook(String bookId, String userId) {
//        books.stream().filter(obj -> obj.bookId.equals(bookId) && obj.isAvailable).forEach(obj->obj.isAvailable=false);


        for(Book b : books){
            if(b.bookId.equals(bookId)){
                if(b.isAvailable){
                    b.isAvailable = false;
                    System.out.println("Book Successfully Borrowed");
                    return true;
                }
                else{
                    System.out.println("Book is not Available");
                    return false;
                }
            }
        }
        System.out.println("Book Not Found");
        return false;
    }

    @Override
    public boolean returnBook(String bookId) {

        for(Book b : books){
            if(b.bookId.equals(bookId)){
                if(!b.isAvailable){
                    b.isAvailable=true;
                    System.out.println("Returned Successfully");
                    return true;
                }
                else{
                    System.out.println("Book was not borrowed");
                    return false;
                }
            }
        }
        System.out.println("Book Not Found");
        return false;
    }

    @Override
    public List<Book> searchByAuthor(String author) {
        return books.stream().filter(obj -> obj.author.equalsIgnoreCase(author)).toList();
    }

    @Override
    public List<Book> getAvailableBooks() {
        return books.stream().filter(obj -> obj.isAvailable).toList();
    }

    @Override
    public int getTotalBooks() {
        return books.size();
    }
}

class Main1{
    public static void main(String[] args) {
        ILibrary library = new _01_Library();

        // Add books
        library.addBook(new Book("B001", "Harry Potter", "JK Rowling", true));
        library.addBook(new Book("B002", "Lord of the Rings", "Tolkien", true));
        library.addBook(new Book("B003", "The Hobbit", "Tolkien", true));

        System.out.println("\nTotal books: " + library.getTotalBooks());

        // Borrow books
        System.out.println("\n--- Borrowing Books ---");
        library.borrowBook("B001", "Alice");
        library.borrowBook("B001", "Bob"); // Should fail

        // Available books
        System.out.println("\n--- Available Books ---");
        for (Book book : library.getAvailableBooks()) {
            System.out.println(book);
        }

        // Search by author
        System.out.println("\n--- Books by Tolkien ---");
        for (Book book : library.searchByAuthor("Tolkien")) {
            System.out.println(book);
        }

        // Return book
        System.out.println("\n--- Returning Books ---");
        library.returnBook("B001");

        System.out.println("\n--- Available After Return ---");
        for (Book book : library.getAvailableBooks()) {
            System.out.println(book.title);
        }
    }
}