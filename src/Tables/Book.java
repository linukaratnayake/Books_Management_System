package Tables;

public class Book {

    // MyBooks related fields.
    private final String bookID;
    private String bookName;
    private String bookAuthor;
    private String bookDateBought;
    private String bookCategory;
    private boolean bookRead;
    private boolean bookAvailable;

    // BorrowedFromMe related fields.
    private String borrowerName;
    private String bookDateBorrowed;
    private String bookDateReturned;
    private boolean bookReturned;

    public Book(String bookID, String bookName, String bookAuthor, String bookDateBought, String bookCategory, int bookRead, int bookAvailable) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookDateBought = bookDateBought;
        this.bookCategory = bookCategory;
        this.bookRead = (bookRead == 1);
        this.bookAvailable = (bookAvailable == 1);
    }

    public Book(String borrowerName, String bookID, String bookName, String bookAuthor, String bookDateBorrowed, String bookDateReturned, int bookReturned) {
        this.borrowerName = borrowerName;
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookDateBorrowed = bookDateBorrowed;
        this.bookDateReturned = bookDateReturned;
        this.bookReturned = (bookReturned == 1);
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

    public String getBookRead() {
        return bookRead ? "Yes" : "No";
    }

    public String getBookAvailable() {
        return bookAvailable ? "Yes" : "No";
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public String getBookDateBorrowed() {
        return bookDateBorrowed;
    }

    public String getBookDateReturned() {
        return bookDateReturned;
    }

    public String getBookReturned() {
        return bookReturned ? "Yes" : "No";
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public void setBookDateBought(String bookDateBought) {
        this.bookDateBought = bookDateBought;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public void setBookRead(boolean bookRead) {
        this.bookRead = bookRead;
    }

    public void setBookAvailable(boolean bookAvailable) {
        this.bookAvailable = bookAvailable;
    }
}
