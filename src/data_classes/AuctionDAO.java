package data_classes;

import java.util.*;

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
    private Map<String, Bid> bids;
    private Map<String, Question> questions;

    private String status;

    public AuctionDAO() {
    }
    public AuctionDAO(Auction a) {
        this(a.getId(), a.getTitle(), a.getDescription(), a.getImageId(), a.getOwnerId(), a.getEndingTime(), a.getMinPrice());
    }
    public AuctionDAO(String id, String title, String description, String imageId, String ownerId, long endTime, int minPrice) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.imageId = imageId;
        this.minPrice = minPrice;
        this.endingTime = endTime;
        this.status = "OPEN";
        this.bids = new HashMap<>();
        this.questions = new HashMap<>();
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
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }
    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }
    public String getWinnerId() {
        return this.winnerId;
    }

    public void addBid(Bid bid) {bids.put(bid.getBidId(), bid);}
    public void addQuestion(Question question) {questions.put(question.getQuestionId(), question);}

    public Map<String, Bid> getBids() {return this.bids;}
    public Map<String, Question> getQuestions() {return this.questions;}
    @Override
    public String toString() {
        return "Auction [title=" + this.title + " ownerId=" + this.ownerId + " status=" + this.status + "]";
    }

}
