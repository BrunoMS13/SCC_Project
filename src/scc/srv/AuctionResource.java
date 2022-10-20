package scc.srv;

import api.RestAuctions;
import com.azure.cosmos.util.CosmosPagedIterable;
import data_classes.AuctionDAO;
import data_classes.Bid;
import data_classes.Question;
import utils.CosmosDBLayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

public class AuctionResource implements RestAuctions {

    private CosmosDBLayer db;
    private MediaResource mr;

    public AuctionResource() {
        this.db = CosmosDBLayer.getInstance();
        this.mr = new MediaResource();
    }

    @Override
    public void createAuctionWithPhoto(String id, String title, String description, String imageId, String ownerId, long endTime, int minPrice, byte[] photo) {
        mr.upload(photo, imageId);
        db.putAuction(new AuctionDAO(id,title,description,imageId,ownerId,endTime,minPrice));
    }

    @Override
    public void createAuction(String id, String title, String description, String imageId, String ownerId, long endTime, int minPrice) {
        db.putAuction(new AuctionDAO(id,title,description,imageId,ownerId,endTime,minPrice));
    }

    @Override
    public void updateAuction(String id, String title, String description, String imageId, int endTime, int minPrice, String winnerId, String status) {

    }

    public AuctionDAO getAuction(String id) {
        CosmosPagedIterable<AuctionDAO> resGet = db.getAuctionById(id);
        return resGet.iterator().next();
    }

    @Override
    public void createBid(String id, String bidID, String bidderId, int bidValue) {
        AuctionDAO a = getAuction(id);
        a.addBid(new Bid(id, bidderId, bidID, bidValue));
        db.updateAuction(a);
    }

    @Override
    public Collection<Bid> listBids(String id) {
        System.out.println("Printing this auction (ID: "+id+") bids...");
        CosmosPagedIterable<AuctionDAO> resGet = db.getAuctionById(id);
        AuctionDAO a = resGet.iterator().next();
        for (Bid bid: a.getBids().values()) {
            System.out.println(bid);
        }
        return a.getBids().values();
    }

    @Override
    public void createQuestion(String id, String questionId, String userId, String text) {
        System.out.println("Creating question ID:" + questionId + " in the auction ID: " + id + "...");
        AuctionDAO a = getAuction(id);
        a.addQuestion(new Question(id, userId, questionId, "", text));
        db.updateAuction(a);
    }

    @Override
    public void replyToQuestion(String id, String questionId, String userId, String questionBeingRespondedId, String text) {
        System.out.println("Creating reply ID:" + questionId + " in the auction ID: " + id + "...");
        AuctionDAO a = getAuction(id);
        a.addQuestion(new Question(id, userId, questionId, questionBeingRespondedId, text));
        db.updateAuction(a);
    }

    @Override
    public Collection<Question> listQuestions(String id) {
        System.out.println("Printing this auction (ID: "+id+") questions...");
        CosmosPagedIterable<AuctionDAO> resGet = db.getAuctionById(id);
        AuctionDAO a = resGet.iterator().next();
        for (Question question: a.getQuestions().values()) {
            System.out.println(question);
        }
        return a.getQuestions().values();
    }

    public static void main(String[] args) {

        AuctionResource ar = new AuctionResource();
        ar.createAuction("new_try","b","c","d","e",1,2);
        ar.createQuestion("new_try","aaabbba","adasd","asdasd");
        ar.createQuestion("new_try","aaddbba","adasd","asdasd");
        ar.createQuestion("new_try","addbbba","adasd","asdasd");
        ar.createQuestion("new_try","aaabbbadd","adasd","asdasd");
        ar.getAuction("new_try");
        ar.createBid("new_try", "avresd", "aasdg4sd", 1012340);
        ar.createBid("new_try", "avxcsdf", "a2fsadsd", 10130);
        ar.createBid("new_try", "cdshtw", "as6423asdd", 9756100);
                
        ar.listBids("new_try");
        ar.listQuestions("new_try");
        System.out.println("Over");
    }
}
