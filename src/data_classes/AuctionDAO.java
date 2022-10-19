package data_classes;

import utils.AuctionStatus;

import java.util.HashSet;
import java.util.Set;

public class AuctionDAO {
    private String _rid;
    private String _ts;
    private String id;
    private String ownerId;
    private String winnerId;
    private String title;
    private String description;
    private String imageId;
    private int minPrice;
    private long endingTime;
    private Set<Bid> bids;

    private AuctionStatus status;

    public AuctionDAO(String id, String title, String description, String imageId, String ownerId, long endTime, int minPrice) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.imageId = imageId;
        this.minPrice = minPrice;
        this.endingTime = endTime;
        this.status = AuctionStatus.OPEN;
        this.bids = new HashSet<Bid>();
    }
    public String get_rid() {
        return _rid;
    }
    public void set_rid(String _rid) {
        this._rid = _rid;
    }
    public String get_ts() {
        return _ts;
    }
    public void set_ts(String _ts) {
        this._ts = _ts;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public void addBid(Bid bid) {
        this.bids.add(bid);
    }
    @Override
    public String toString() {
        return "Auction [title=" + this.title + " ownerId=" + this.ownerId + " status=" + this.status + "]";
    }

}
