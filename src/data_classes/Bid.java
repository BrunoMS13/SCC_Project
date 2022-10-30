package data_classes;

public class Bid {
    private String userId;
    private String bidId;
    private int bidValue;

    public Bid() {}
    public Bid(String userId, String bidId, int bidValue) {
        this.userId = userId;
        this.bidId = bidId;
        this.bidValue = bidValue;
    }
    public void setUserId(String userId) {this.userId = userId;}
    public void setBidId(String bidId) {this.bidId = bidId;}
    public void setBidValue(int bidValue) {this.bidValue = bidValue;}

    public String getUserId() {return this.userId;}
    public String getBidId() {return this.bidId;}
    public int getBidValue() {return this.bidValue;}

    @Override
    public String toString() {
        return "Bid [Bid ID: " + getBidId() + " User ID: " + getUserId() + " Bid Value: " + getBidValue() + "]";
    }
}

