package Tables;

// This class is used to populate the table My Books.
public class Book {

    private final String bookID;
    private final String bookName;
    private final String bookAuthor;
    private final String bookDateBought;
    private final String bookCategory;
    private final int bookRead;
    private final int bookAvailable;

    public Book(String bookID, String bookName, String bookAuthor, String bookDateBought, String bookCategory, int bookRead, int bookAvailable) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookDateBought = bookDateBought;
        this.bookCategory = bookCategory;
        this.bookRead = bookRead;
        this.bookAvailable = bookAvailable;
    }

    public String getBookID() {
        return bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookDateBought() {
        return bookDateBought;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public int getBookRead() {
        return bookRead;
    }

    public int getBookAvailable() {
        return bookAvailable;
    }

}
