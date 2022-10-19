package api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import utils.AuctionStatus;

@Path("/auction")
public interface RestAuctions {
    String ID = "id";

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
    void createAuction(String id, String title, String description, String imageId, String ownerId, long endTime, int minPrice);

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
    void updateAuction(String id, String title, String description, String imageId, int endTime, int minPrice, String winnerId, AuctionStatus status);

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
    void createBid(@PathParam(ID) String id, String bidID, String bidderId, int bidValue);

    /**
     * Lists all the bids for the auction with the respective ID.
     * @param id - auction ID.
     * @return list of bids.
     */
    @Path("/{" + ID + "}/bid")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    String[] listBids(@PathParam(ID) String id);;

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
    void replyToQuestion(@PathParam(ID) String id, String questionId, String userId, String text);

    /**
     * Lists all the questions to the respective auction ID.
     * @param id - auction ID.
     * @return list of questions.
     */
    @Path("/{" + ID + "}/question")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    String[] listQuestions(@PathParam(ID) String id);
}
