package api;

import data_classes.Auction;
import data_classes.Bid;
import data_classes.Question;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;

import java.util.Collection;

@Path("/auction")
public interface RestAuctions {
    String ID = "id";
    String TITLE = "title";
    String IMAGEID = "imageId";
    String OWNERID = "ownerId";
    String ENDTIME = "endtime";
    String MINPRICE = "minPrice";
    String PASSWORD = "password";
    String DESCRIPTION = "description";

    /**
     * Creates an auction along with the uploaded image.
     * @param id - auction ID.
     * @param title - auction title.
     * @param description - auction description.
     * @param imageId - auction image ID.
     * @param ownerId - owner ID of the auction.
     * @param endTime - auction end time.
     * @param minPrice - auction min bid.
     * @param photo - contents of the photo being uploaded.
     */
    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    void createAuctionWithPhoto(@QueryParam(ID) String id,
                                @QueryParam(TITLE) String title,
                                @QueryParam(DESCRIPTION) String description,
                                @QueryParam(OWNERID) String ownerId,
                                @QueryParam(ENDTIME) long endTime,
                                @QueryParam(MINPRICE) int minPrice,
                                byte[] photo);

    /**
     * Creates an auction.
     * @param auction - auction being created.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void createAuction(@CookieParam("scc:session") Cookie session, Auction auction);

    /**
     * Updates an auction.
     * @param auction - auction being updated.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void updateAuction(@CookieParam("scc:session") Cookie session, Auction auction);

    /**
     * Creates a bid for the respective auction ID.
     * @param id - auction ID.
     * @param password - user password.
     * @param bid - created bid.
     */
    @Path("/{" + ID + "}/bid")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void createBid(@CookieParam("scc:session") Cookie session, @PathParam(ID) String id, @QueryParam(PASSWORD) String password, Bid bid);

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
     * @param password - user password.
     * @param question - created question.
     */
    @Path("/{" + ID + "}/question")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void createQuestion(@CookieParam("scc:session") Cookie session, @PathParam(ID) String id, @QueryParam(PASSWORD) String password, Question question);

    /**
     * Update question replies.
     * @param id - auction ID.
     * @param password - user password.
     * @param question - created question.
     */
    @Path("/{" + ID + "}/question")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void replyToQuestion(@CookieParam("scc:session") Cookie session, @PathParam(ID) String id, @QueryParam(PASSWORD) String password, Question question);

    /**
     * Lists all the questions to the respective auction ID.
     * @param id - auction ID.
     * @return list of questions.
     */
    @Path("/{" + ID + "}/question")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Collection<Question> listQuestions(@PathParam(ID) String id);
}
