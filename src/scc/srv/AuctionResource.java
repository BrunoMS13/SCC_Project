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
    private UserResource ur;

    public AuctionResource() {
        this.db = CosmosDBLayer.getInstance();
        this.mr = new MediaResource();
        this.ur = new UserResource();
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
        System.out.println("Updating auction with ID: '" + id +"'");
        AuctionDAO auc = getAuction(id);
        if (auc != null) {
            if (!title.equals("")) auc.setTitle(title);
            if (!description.equals("")) auc.setDescription(description);
            if (!imageId.equals("")) auc.setImageId(imageId);
            if (endTime > 0) auc.setEndingTime(endTime);
            if (minPrice >= 0) auc.setMinPrice(minPrice);
            if (!winnerId.equals("")) auc.setWinnerId(winnerId);
            if (!status.equals("")) auc.setStatus(status);
            db.updateAuction(auc);
            System.out.println("Updated successfully.\n");
        }else {
                System.out.println("Did not update.\n");
        }
    }

    public AuctionDAO getAuction(String id) {
        try {
            CosmosPagedIterable<AuctionDAO> resGet = db.getAuctionById(id);
            return resGet.iterator().next();
        } catch (Exception e){
            System.out.println("No acution with id '" + id +"' found.");
            return null;
        }
    }

    @Override
    public void createBid(String id, String bidID, String bidderId, String password, int bidValue) {
        if (id == null || bidID == null || bidderId == null) {System.out.println("There is a null string.");}
        else if (bidValue <= 0) {System.out.println("Bid value must be above 0.");}
        else if (ur.getUser(bidderId, password) == null) {System.out.println("Either user doesn't exist or password is mismatched");}

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
        /*
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
        */
        //ar.createAuction("to update","test","not updated","a","a",1,2);
        ar.updateAuction("to update", "test_v2", "updated", "b", 1, 3, "winner", "");
        ar.updateAuction("not exists", "test_v2", "updated", "b", 1, 3, "winner", "");
    }
}
