package scc.srv;

import api.RestAuctions;
import com.azure.cosmos.util.CosmosPagedIterable;
import jakarta.ws.rs.*;
import temppackage.*;

public class AuctionResource implements RestAuctions {

    CosmosDBAuctions db;

    public AuctionResource() {
        this.db = CosmosDBAuctions.getInstance();
    }

    @Override
    public void createAuction(String id, String title, String description, String imageId, String ownerId, long endTime, int minPrice) {
        db.putAuction(new AuctionDAO(id,title,description,imageId,ownerId,endTime,minPrice));
    }

    @Override
    public void updateAuction(String id, String title, String description, String imageId, int endTime, int minPrice, String winnerId, AuctionStatus status) {

    }

    @Override
    public void createBid(String id, String bidID, String bidderId, int bidValue) {
        CosmosPagedIterable<AuctionDAO> resGet = db.getAuctionById(id);
        AuctionDAO temp = resGet.iterator().next();

        temp.addBid(new Bid(bidID, bidderId, bidValue));
        // TODO
    }

    @Override
    public String[] listBids(String id) {
        return new String[0];
    }

    @Override
    public void createQuestion(String id, String questionId, String userId, String text) {

    }

    @Override
    public void replyToQuestion(String id, String questionId, String userId, String text) {

    }

    @Override
    public String[] listQuestions(String id) {
        return new String[0];
    }

    public static void main(String[] args) {

        AuctionResource ar = new AuctionResource();
        ar.createAuction("a","b","c","d","e",1,2);

    }
}
