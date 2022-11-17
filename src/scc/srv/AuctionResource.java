package scc.srv;

import api.RestAuctions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.util.CosmosPagedIterable;
import data_classes.*;

import javax.ws.rs.*;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Cookie;
import utils.CosmosDBLayer;
import utils.RedisLayer;
import javax.ws.rs.core.Response;
import java.util.*;
import javax.ws.rs.core.NoContentException;
import java.util.stream.Stream;


public class AuctionResource implements RestAuctions {

    private final String DELETED_USER = "Deleted User";

    private CosmosDBLayer db;
    private MediaResource mr;
    private RedisLayer rl;

    public AuctionResource() {
        this.db = CosmosDBLayer.getInstance();
        this.rl = RedisLayer.getInstance();
        this.mr = new MediaResource();
    }

    @Override
    public Auction createAuction(Cookie session, Auction auction) throws WebApplicationException {
        System.out.println("Creating auction... " + auction.toString());
        try {
            // checking that auction is correct and can be created
            if (badAuction(auction))
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            if (getAuctionHelper(auction.getId()) != null)
                throw new WebApplicationException(Response.Status.CONFLICT);
            // checking cookies
            checkCookieUser(session, auction.getOwnerId());
            // creating auction
            CosmosItemResponse<AuctionDAO> aucDAO = db.putAuction(new AuctionDAO(auction));
            rl.addAuction(aucDAO.getItem());
            System.out.println(auction.toString());
            return auction;
        } catch (WebApplicationException e) {
            throw e;
        } catch (NotAuthorizedException e) {
            System.out.println("Not authorized exception caught --> " + session.getValue());
           throw e;
        }
    }

    @Override
    public Bid createBid(Cookie session, String id, Bid bid) throws WebApplicationException {
        System.out.println("Creating bid... " + bid.toString() + " in auction: " + id);
        checkCookieUser(session, bid.getUserId());
        AuctionDAO auc = getAuction(id);
        //if (bid.getBidValue() < auc.getMinPrice())
        //    throw new WebApplicationException(Response.Status.BAD_REQUEST);
        auc.addBid(bid);
        updateDataBases(auc);

        return bid;
    }

    @Override
    public Question createQuestion(Cookie session, String id, Question question) throws WebApplicationException {
        System.out.println("Creating question... " + question.toString() + " in auction: " + id);
        checkCookieUser(session, question.getUser());
        AuctionDAO auc = getAuction(id);
        if (badParam(question.getText()))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        auc.addQuestion(question);
        updateDataBases(auc);
        return question;
    }

    @Override
    public String replyToQuestion(Cookie session, String id, String questionId, Text text) throws WebApplicationException {
        System.out.println("Replying to question... " + questionId + " in auction: " + id + " with " + text.getReply());
        AuctionDAO auc = getAuction(id);
        checkCookieUser(session, auc.getOwnerId());
        if (badParam(text.getReply()))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        if (!auc.getQuestions().keySet().contains(questionId))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        auc.getQuestions().get(questionId).setReply(text.getReply());
        updateDataBases(auc);
        return text.getReply();
    }

    @Override
    public void updateAuction(Cookie session, Auction auction) throws WebApplicationException {
        System.out.println("Updating auction...");
        checkCookieUser(session, auction.getOwnerId());
        try {
            getAuction(auction.getId());
        } catch (WebApplicationException e) {
            System.out.println("Couldn't update");
            throw e;
        }
        updateDataBases(new AuctionDAO(auction));
    }

    public AuctionDAO getAuction(String id) throws WebApplicationException {
        AuctionDAO auction = getAuctionHelper(id);
        if (auction == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return auction;
    }

    @Override
    public Collection<Bid> listBids(String id) throws WebApplicationException {
        System.out.println("Getting this auction (ID: "+ id +") bids...");
        AuctionDAO auc = getAuction(id);
        for (Bid bid: auc.getBids().values()) {
            System.out.println(bid);
        }
        return auc.getBids().values();
    }

    public Question getOneQuestion(String id) {
        AuctionDAO auc = getAuction(id);
        for (Question question: auc.getQuestions().values()) {
           return question;
        }
        return null;
    }

    @Override
    public Collection<Question> listQuestions(String id) {
        System.out.println("Printing this auction (ID: " + id + ") questions...");
        AuctionDAO auc = getAuction(id);
        for (Question question: auc.getQuestions().values()) {
            System.out.println(question);
        }
        return auc.getQuestions().values();
    }

    @Override
    public void updateAuctionsOfDeletedUser(String userId) {
        Iterator<AuctionDAO> it = db.getAuctions().iterator();
        while (it.hasNext()) {
            boolean needUpdate = false;
            AuctionDAO auctionDAO = it.next();
            if (auctionDAO.getOwnerId().equals(userId)) {
                needUpdate = true;
                auctionDAO.setOwnerId(DELETED_USER);
            }
            Iterator<Bid> itBids = auctionDAO.getBids().values().iterator();
            while (itBids.hasNext()) {
                Bid bid = itBids.next();
                if (bid.getUserId().equals(userId)) {
                    bid.setUserId(DELETED_USER);
                    needUpdate = true;
                }
            }
            Iterator<Question> itQuestions = auctionDAO.getQuestions().values().iterator();
            while (itQuestions.hasNext()) {
                Question question = itQuestions.next();
                if (question.getUser().equals(userId)) {
                    needUpdate = true;
                    question.setUser(DELETED_USER);
                }
            }
            if (needUpdate)
                updateDataBases(auctionDAO);
        }
    }

    public List<Auction> getUserAuctions(String userId, String status) {
        List<Auction> list = new ArrayList<>();
        Iterator<AuctionDAO> it = db.getAuctions().iterator();
        while (it.hasNext()) {
            AuctionDAO auctionDAO = it.next();
            if (auctionDAO.getOwnerId().equals(userId) && ('"' + auctionDAO.getStatus() + '"').equals(status)) {
                Auction auction = new Auction(auctionDAO);
                list.add(auction);
                System.out.println(auction);
            }
        }
        return list;
    }

    public Collection<Auction> trendingAuctions(int start, int length) throws WebApplicationException {
        CosmosPagedIterable<AuctionNumBidsDAO> temp = db.getTrendingAuctions();

        int nAuctions = (int) temp.stream().count();
        if (nAuctions == 0 || start > nAuctions-1)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        // in case there aren't enough auctions, return as much as possible
        if (start + length > nAuctions)
            length = nAuctions - start;
        Stream<AuctionNumBidsDAO> stream = temp.stream().skip(start).limit(length);
        Collection<Auction> auctions = new ArrayList<>();

        for (AuctionNumBidsDAO aucInfo: stream.toList()) {
            AuctionDAO aucDAO = db.getAuctionById(aucInfo.getId()).iterator().next();
            auctions.add(new Auction(aucDAO));
        }
        return auctions;
    }

    public Session checkCookieUser(Cookie session, String id) {
        if (session == null || session.getValue() == null)
            throw new NotAuthorizedException("No session initialized");
        Session s = rl.getSession(session.getValue());;
        System.out.println(s.getUser() + " == " + id);
        if (s == null || s.getUser() == null || s.getUser().length() == 0)
            throw new NotAuthorizedException("No valid session initialized");
        if (!s.getUser().equals(id) && !s.getUser().equals("admin"))
            throw new NotAuthorizedException("Invalid user : " + s.getUser());
        return s;
    }

    private AuctionDAO getAuctionHelper(String id) {
        // try through cache
        AuctionDAO temp = rl.getAuction(id);
        if (temp != null)
            return temp;
        // try through database
        CosmosPagedIterable<AuctionDAO> resGet = db.getAuctionById(id);
        Iterator<AuctionDAO> it = resGet.iterator();
        if (!it.hasNext()) return null;
        temp = resGet.iterator().next();
        return temp;
    }

    private boolean badParam(String str) {
        return str == null;
    }
    private boolean badDate(Date date) {return date.getTime() < System.currentTimeMillis();}
    private boolean badNumber(float lg) {return lg <= 0;}
    private boolean badAuction(Auction auction) {
        return auction == null || badParam(auction.getDescription()) || badParam(auction.getImageId()) || badParam(auction.getOwnerId()) || badNumber(auction.getMinPrice()) || badDate(auction.getEndingTime());
    }

    private void updateDataBases(AuctionDAO auctionDAO) {
        db.updateAuction(auctionDAO);
        rl.updateAuction(auctionDAO);
    }

    private void printRedisContents() {
        rl.printContents();
    }

    private void clearRedis() {
        rl.clearCache();
    }

    public static void main(String[] args) throws NoContentException {
        AuctionResource ar = new AuctionResource();

        Collection<Auction> a = ar.trendingAuctions(50, 300);
        Iterator<Auction> b = a.iterator();
        while (b.hasNext()) {
            Auction c = b.next();
            System.out.println(c.getId());
            System.out.println(c.getBids().size());
        }

        //System.out.println(ar.(null, "1edbac3d-40db-420b-9ec0-e957cde2758b", new Question("aaa",null,"What is?")).toString());
        //System.out.println(ar.createQuestion(null, "1edbac3d-40db-420b-9ec0-e957cde2758b", new Question("aaa",null,"What is it?")).toString());
        //ar.replyToQuestion(null, "1edbac3d-40db-420b-9ec0-e957cde2758b", "9306aaea-d682-478c-8193-345a271b2bf5", "I don't know sorry ser. Actually I might know....");
        //ar.printRedisContents();
        //ar.getAuction("35ba3d9b-dbe0-4770-8b03-a7c2444cd1c4");
        //var x = ar.listBids("35ba3d9b-dbe0-4770-8b03-a7c2444cd1c4").iterator();
        //var a = new Auction("asdasd","dasdas","adadsa","aaaas",new Date(1130000000),5);
        //System.out.println(a.toString());
        //ar.createAuction(null, a);
        //var b = ar.getAuction(a.getId());
        //b.addBid(new Bid("monkey","id",5));
        //AuctionDAO auc = ar.getAuction("35ba3d9b-dbe0-4770-8b03-a7c2444cd1c4");
        //System.out.println(auc.toString());
        //ar.createBid(null, "35ba3d9b-dbe0-4770-8b03-a7c2444cd1c4", new Bid("dasdzzzssxxsas","dxxzzzxas",10));
        /**
        AuctionResource ar = new AuctionResource();
        UserResource ur = new UserResource();

        ar.clearRedis();

        ur.createUser(new User("123","b","c12312asdasd","d", "e"));

        ar.printRedisContents();

        ar.createAuction(new Auction("a","oldtitle","c","c","c",100000,1000));

        ar.printRedisContents();

        ur.deleteUser("123", "c12312asdasd");

        ar.updateAuction(new Auction("a","newtitle","c","c","c",100000,1000));

        ar.printRedisContents();

        //System.out.println(ar.getAuction("a").toString());

        //ar.createBid("a", "cc", new Bid("aaaa", "aaaa", 800300));

        System.out.println(ar.getAuction("a").toString());

        System.out.println("Finished...");
        */
    }
}
