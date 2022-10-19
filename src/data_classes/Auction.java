package data_classes;

import utils.AuctionStatus;

public class Auction {
    private String ownerId;
    private String winnerId;
    private String title;
    private String description;
    private String imageId;
    private int minPrice;
    private long endingTime;

    private AuctionStatus status;

    public Auction(String id, String title, String description, String imageId, String ownerId, long endTime, int minPrice) {
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.imageId = imageId;
        this.minPrice = minPrice;
        this.endingTime = endTime;
        this.status = AuctionStatus.OPEN;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public String getOwnerId() {
        return this.ownerId;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public String getImageId() {
        return this.imageId;
    }
    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }
    public int getMinPrice() {
        return this.minPrice;
    }
    public void setEndingTime(long endingTime) {
        this.endingTime = endingTime;
    }
    public long getEndingTime() {
        return this.endingTime;
    }
    public void setStatus(AuctionStatus status) {
        this.status = status;
    }
    public AuctionStatus getStatus() {
        return this.status;
    }
    @Override
    public String toString() {
        return "Auction [title=" + this.title + " ownerId=" + this.ownerId + " status=" + this.status + "]";
    }

}
