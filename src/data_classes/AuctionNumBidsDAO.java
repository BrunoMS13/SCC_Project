package data_classes;

public class AuctionNumBidsDAO {

    private String _rid;
    private String _ts;
    private String id;
    private int numBids;

    public AuctionNumBidsDAO() {

    }

    public String getId() {
        return id;
    }

    public int getNumBids() {
        return numBids;
    }
}
