package api;

import data_classes.Auction;
import data_classes.Bid;
import data_classes.Question;
import data_classes.Text;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;

import java.util.Collection;
import java.util.List;

@Path("/auction")
public interface RestAuctions {
    String ID = "id";
    String START = "st";
    String LENGTH = "len";
    String QUESTIONID = "questionId";

    /**
    @Path("/withPhoto")
    @POST
    @Consumes({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON})
    void createAuctionWithPhoto(Auction auction, byte[] photo);**/

    /**
     * Creates an auction.
     * @param auction - auction being created.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Auction createAuction(@CookieParam("scc:session") Cookie session, Auction auction);

    /**
     * Updates an auction.
     * @param auction - auction being updated.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void updateAuction(@CookieParam("scc:session") Cookie session, Auction auction);

    /**
     * Creates a bid for the respective auction ID.
     * @param session - auth session.
     * @param id - auction ID.
     * @param bid - created bid.
     */
    @Path("/{" + ID + "}/bid")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Bid createBid(@CookieParam("scc:session") Cookie session, @PathParam(ID) String id, Bid bid);

    /**
     * Lists all the bids for the auction with the respective ID.
     * @param id - auction ID.
     * @return list of bids.
     */
    @Path("/{" + ID + "}/bid")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Collection<Bid> listBids(@PathParam(ID) String id);;

    /**
     * Creates a question.
     * @param id - auction ID.
     * @param question - created question.
     */
    @Path("/{" + ID + "}/question")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Question createQuestion(@CookieParam("scc:session") Cookie session, @PathParam(ID) String id, Question question);

    /**
     * Update question replies.
     * @param id - auction ID.
     * @param reply - reply to question.
     */
    @Path("/{" + ID + "}/question/{" + QUESTIONID + "}/reply")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    String replyToQuestion(@CookieParam("scc:session") Cookie session, @PathParam(ID) String id, @PathParam(QUESTIONID) String questionId, Text reply);


    /**
     * Lists all the questions to the respective auction ID.
     * @param id - auction ID.
     * @return list of questions.
     */
    @Path("/{" + ID + "}/question")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Collection<Question> listQuestions(@PathParam(ID) String id);

    /**
     * Returns first seen question in the auction.
     * @param id - auction ID.
     * @return First of the questions.
     */
    @Path("/{" + ID + "}/question_one")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Question getOneQuestion(@PathParam(ID) String id);

    /**
     * Updates auction of deleted user with "Deleted User".
     * @param userId - user id that has been deleted.
     */
    void updateAuctionsOfDeletedUser(String userId);

    /**
     * Gets the user auctions with the given status.
     * @param userId - owner of the auctions.
     * @param status - status of the auction.
     * @return - list of the auctions.
     */
    List<Auction> getUserAuctions(String userId, String status);

    /**
     * Returns a list of auctions that are about to close.
     * @return - list of auctions.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Collection<Auction> getAuctionsAboutToClose();

    /**
     *
     * @param start
     * @param length
     * @return
     * @throws WebApplicationException
     */
    @Path("/any/popular")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Collection<Auction> trendingAuctions(@QueryParam(START) int start, @QueryParam(LENGTH) int length) throws WebApplicationException;
}
