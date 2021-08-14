package Tables;

public class Borrower {
    private String borrowerID;
    private String borrowerName;
    private String borrowerDescription;

    public Borrower(String borrowerID, String borrowerName, String borrowerDescription) {
        this.borrowerID = borrowerID;
        this.borrowerName = borrowerName;
        this.borrowerDescription = borrowerDescription;
    }

    public String getBorrowerID() {
        return borrowerID;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public String getBorrowerDescription() {
        return borrowerDescription;
    }
}
