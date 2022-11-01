package data_classes;

import java.util.Dictionary;
import java.util.Hashtable;

public class Auction {

    private String id;
    private String ownerId;
    private String winnerId;
    private String title;
    private String description;
    private String imageId;
    private int minPrice;
    private long endingTime;
    private Dictionary<String, Bid> bids;
    private Dictionary<String, Question> questions;

    private String status;

    public Auction() {}
    public Auction(String id, String title, String description, String imageId, String ownerId, long endTime, int minPrice) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.imageId = imageId;
        this.minPrice = minPrice;
        this.endingTime = endTime;
        this.status = "OPEN";
        this.bids = new Hashtable<>();
        this.questions = new Hashtable<>();
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
    public void addBid(Bid bid) {bids.put(bid.getBidId(), bid);}
    public void addQuestion(Question question) {questions.put(question.getQuestionId(), question);}

    public Dictionary<String, Bid> getBids() {return this.bids;}
    public Dictionary<String, Question> getQuestions() {return this.questions;}
    @Override
    public String toString() {
        return "Auction [title=" + this.title + " ownerId=" + this.ownerId + " status=" + this.status + "]";
    }

}
