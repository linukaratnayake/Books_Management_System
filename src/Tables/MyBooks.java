package Tables;

public class MyBooks {

    private final String bkID;
    private final String bkName;
    private final String bkAuthor;
    private final String bkDateBought;
    private final String bkCategory;
    private final int bkRead;
    private final int bkAvailable;

    public MyBooks(String bkID, String bkName, String bkAuthor, String bkDateBought, String bkCategory, int bkRead, int bkAvailable) {
        this.bkID = bkID;
        this.bkName = bkName;
        this.bkAuthor = bkAuthor;
        this.bkDateBought = bkDateBought;
        this.bkCategory = bkCategory;
        this.bkRead = bkRead;
        this.bkAvailable = bkAvailable;
    }

    public String getBkID() {
        return bkID;
    }

    public String getBkName() {
        return bkName;
    }

    public String getBkAuthor() {
        return bkAuthor;
    }

    public String getBkDateBought() {
        return bkDateBought;
    }

    public String getBkCategory() {
        return bkCategory;
    }

    public int getBkRead() {
        return bkRead;
    }

    public int getBkAvailable() {
        return bkAvailable;
    }

}
