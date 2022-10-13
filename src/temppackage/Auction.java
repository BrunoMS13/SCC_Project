package temppackage;

public class Auction {
    private User owner;
    private User winner;
    private String title;
    private String description;

    private byte[] image;

    private int minPrice;
    private long endingTime;

    private AuctionStatus status;

    public Auction(String title, String description, User owner, byte[] image, int minPrice, long time) {
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.image = image;
        this.minPrice = minPrice;
        this.endingTime = System.nanoTime() + time;
    }
}
