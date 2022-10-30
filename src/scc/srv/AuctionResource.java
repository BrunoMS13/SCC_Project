package scc.srv;

import api.RestAuctions;
import com.azure.cosmos.util.CosmosPagedIterable;
import data_classes.*;
import javax.ws.rs.WebApplicationException;
import utils.CosmosDBLayer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashMap;
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
    public void createAuction(Auction auction) throws WebApplicationException {
        System.out.println("Creating auction...");
        if (badAuction(auction))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        if (getAuctionHelper(auction.getId()) != null)
            throw new WebApplicationException(Response.Status.CONFLICT);
        db.putAuction(new AuctionDAO(auction));
    }

    @Override
    public void updateAuction(Auction auction) throws WebApplicationException {
        System.out.println("Updating auction...");
        AuctionDAO auc = getAuction(auction.getId());
        if (!auc.getId().equals(auction.getId()))
            throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
        db.updateAuction(new AuctionDAO(auction));
    }

    public AuctionDAO getAuction(String id) throws WebApplicationException {
        AuctionDAO auction = getAuctionHelper(id);
        if (auction == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return auction;
    }

    @Override
    public void createBid(String id, String password, Bid bid) throws WebApplicationException {
        AuctionDAO auc = getAuction(id);
        ur.getUser(bid.getUserId(), password);
        if (badParam(bid.getBidId()) || bid.getBidValue() < auc.getMinPrice())
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        auc.addBid(bid);
        db.updateAuction(auc);
    }

    @Override
    public Collection<Bid> listBids(String id) throws WebApplicationException {
        System.out.println("Printing this auction (ID: "+ id +") bids...");
        AuctionDAO auc = getAuction(id);
        for (Bid bid: auc.getBids().values()) {
            System.out.println(bid);
        }
        return auc.getBids().values();
    }

    @Override
    public void createQuestion(String id, String password, Question question) throws WebApplicationException {
        AuctionDAO auc = getAuction(id);
        ur.getUser(question.getUserId(), password);
        if (badParam(question.getQuestionId()) || badParam(question.getText()))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        if (auc.getQuestions().keySet().contains(question.getQuestionId()))
            throw new WebApplicationException(Response.Status.CONFLICT);
        auc.addQuestion(question);
        db.updateAuction(auc);
    }

    @Override
    public void replyToQuestion(String id, String password, Question question) throws WebApplicationException {
        AuctionDAO auc = getAuction(id);
        if (badParam(question.getQuestionBeingRespondedId()))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        if (!auc.getQuestions().keySet().contains(question.getQuestionBeingRespondedId()))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        createQuestion(id, password, question);
    }

    @Override
    public Collection<Question> listQuestions(String id) {
        System.out.println("Printing this auction (ID: "+id+") questions...");
        AuctionDAO auc = getAuction(id);
        for (Question question: auc.getQuestions().values()) {
            System.out.println(question);
        }
        return auc.getQuestions().values();
    }

    private AuctionDAO getAuctionHelper(String id) {
        CosmosPagedIterable<AuctionDAO> resGet = db.getAuctionById(id);
        Iterator<AuctionDAO> it = resGet.iterator();
        if (!it.hasNext()) return null;
        AuctionDAO temp = resGet.iterator().next();
        return temp;
    }

    private boolean badParam(String str) {
        return str == null;
    }
    private boolean badNumber(long lg) {return lg <= 0;}
    private boolean badAuction(Auction auction) {
        return auction == null || badParam(auction.getId()) || badParam(auction.getDescription()) || badParam(auction.getImageId()) || badParam(auction.getOwnerId()) || badParam(auction.getTitle()) || badNumber(auction.getMinPrice()) || badNumber(auction.getEndingTime());
    }

    public static void main(String[] args) {

        AuctionResource ar = new AuctionResource();

        //ar.createAuction(new Auction("a","b","c","c","c",100000,1000));

        //System.out.println(ar.getAuction("a").toString());

        ar.createBid("a", "cc", new Bid("aaaa", "aaaa", 800300));

        System.out.println(ar.getAuction("a").toString());
    }
}
