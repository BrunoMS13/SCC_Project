package data_classes;

public class Bid {
    private String auctionId;
    private String userId;
    private String bidId;
    private int bidValue;

    public Bid(String auctionId, String userId, String bidId, int bidValue) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.bidId = bidId;
        this.bidValue = bidValue;
    }
    public void setAuctionId(String auctionId) {this.auctionId = auctionId;}
    public void setUserId(String userId) {this.userId = userId;}
    public void setBidId(String bidId) {this.bidId = bidId;}
    public void setBidValue(int bidValue) {this.bidValue = bidValue;}

    public String getAuctionId() {return this.auctionId;}
    public String getUserId() {return this.userId;}
    public String getBidId() {return this.bidId;}
    public int getBidValue() {return this.bidValue;}

    @Override
    public String toString() {
        return "Bid [Auction ID:" + getAuctionId() + ", User ID:" + getUserId() + ", Bid ID:" + getBidId() + ", Bid Value:" + getBidValue();
    }
}

