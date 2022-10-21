package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import data_classes.Bid;
import data_classes.Question;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Collection;
import java.util.Enumeration;

@Path("/auction")
public interface RestAuctions {
    String ID = "id";
    String TITLE = "title";
    String IMAGEID = "imageId";
    String OWNERID = "ownerId";
    String ENDTIME = "endtime";
    String MINPRICE = "minPrice";
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
                                @QueryParam(IMAGEID) String imageId,
                                @QueryParam(OWNERID) String ownerId,
                                @QueryParam(ENDTIME) long endTime,
                                @QueryParam(MINPRICE) int minPrice,
                                byte[] photo);

    /**
     * Creates an auction.
     * @param id - auction ID.
     * @param title - auction title.
     * @param description - auction description.
     * @param imageId - auction image ID.
     * @param ownerId - owner ID of the auction.
     * @param endTime - auction end time.
     * @param minPrice - auction min bid.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void createAuction(@JsonProperty(ID) String id,
                       @JsonProperty(TITLE) String title,
                       @JsonProperty(DESCRIPTION) String description,
                       @JsonProperty(IMAGEID) String imageId,
                       @JsonProperty(OWNERID) String ownerId,
                       @JsonProperty(ENDTIME) long endTime,
                       @JsonProperty(MINPRICE) int minPrice);

    /**
     * Updates an auction.
     * @param id - new auction ID.
     * @param title - new auction title.
     * @param description - new auction description.S
     * @param imageId - new image ID.
     * @param endTime - new end time.
     * @param minPrice - auction min price for bid.
     * @param winnerId - winner of the auction.
     * @param status - current status of the auction
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void updateAuction(String id, String title, String description, String imageId, int endTime, int minPrice, String winnerId, String status);

    /**
     * Creates a bid for the respective auction ID.
     * @param id - auction ID.
     * @param bidID - bid ID.
     * @param bidderId - bidder user ID.
     * @param bidValue - bid amount.
     */
    @Path("/{" + ID + "}/bid")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void createBid(@PathParam(ID) String id, String bidID, String bidderId, String password, int bidValue);

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
     * @param questionId - question ID.
     * @param userId - owner of the question ID.
     * @param text - question text.
     */
    @Path("/{" + ID + "}/question")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void createQuestion(@PathParam(ID) String id, String questionId, String userId, String text);

    /**
     * Update question replies.
     * @param id - auction ID.
     * @param questionId - question ID.
     * @param userId - replier ID.
     * @param text - reply text.
     */
    @Path("/{" + ID + "}/question")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void replyToQuestion(@PathParam(ID) String id, String questionId, String userId, String questionBeingRespondedId, String text);

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
