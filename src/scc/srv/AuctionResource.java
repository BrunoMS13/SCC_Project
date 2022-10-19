package scc.srv;

import api.RestAuctions;
import com.azure.cosmos.util.CosmosPagedIterable;
import data_classes.AuctionDAO;
import data_classes.Bid;
import data_classes.BidDAO;
import data_classes.QuestionDAO;
import utils.AuctionStatus;
import utils.CosmosDBLayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AuctionResource implements RestAuctions {

    private CosmosDBLayer db;
    private MediaResource mr;

    public AuctionResource() {
        this.db = CosmosDBLayer.getInstance();
        this.mr = new MediaResource();
    }

    public void createAuctionWithPhoto(String id, String title, String description, String imageId, String ownerId, long endTime, int minPrice, byte[] photo) {
        mr.upload(photo, imageId);
        db.putAuction(new AuctionDAO(id,title,description,imageId,ownerId,endTime,minPrice));
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

    }

    @Override
    public List<BidDAO> listBids(String id) {
        CosmosPagedIterable<BidDAO> resGet = db.getBids(id);
        return resGet.stream().toList();
    }

    @Override
    public void createQuestion(String id, String questionId, String userId, String text) {
        System.out.println("Created question ---> " + questionId + " in the auction ---> " + id);
        db.putQuestion(new QuestionDAO(id, userId, questionId, "", text));
    }

    @Override
    public void replyToQuestion(String id, String questionId, String userId, String questionBeingRespondedId, String text) {

    }

    @Override
    public List<QuestionDAO> listQuestions(String id) {
        CosmosPagedIterable<QuestionDAO> resGet = db.getQuestions(id);
        return resGet.stream().toList();
    }

    public static void main(String[] args) {

        AuctionResource ar = new AuctionResource();
        //ar.createAuction("c","b","c","d","e",1,2);
        ar.createQuestion("aaaa","aaabbba","adasd","asdasd");
        ar.createQuestion("aaaa","aaddbba","adasd","asdasd");
        ar.createQuestion("aaabba","addbbba","adasd","asdasd");
        ar.createQuestion("aaabba","aaabbbadd","adasd","asdasd");

        for (QuestionDAO a : ar.listQuestions("aaabba")) {
            System.out.println(a.toString());
        }
    }
}
