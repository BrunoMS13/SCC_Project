package api;

import data_classes.Auction;
import data_classes.Login;
import data_classes.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;

import java.util.Collection;
import java.util.List;


@Path("/user")
public interface RestUsers {

    String ID = "id";
    String STATUS = "status";

    /**
     * Creates user.
     * @param user - user being created.
     * @return - returns user created.
     * @throws WebApplicationException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    User createUser(User user) throws WebApplicationException;

    /**
     * Deletes user.
     * @param session - active user session.
     * @param id - user id.
     * @return - returns deleted user.
     * @throws WebApplicationException
     */
    @DELETE
    @Path("/{"+ ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    User deleteUser(@CookieParam("scc:session") Cookie session, @PathParam(ID) String id) throws WebApplicationException;

    /**
     * Updates user.
     * @param session - active user session.
     * @param user - user with updates.
     * @return - returns updated user.
     * @throws WebApplicationException
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    User updateUser(@CookieParam("scc:session") Cookie session, User user) throws WebApplicationException;

    @GET
    @Path("/{" + ID + "}/auctions")
    @Produces(MediaType.APPLICATION_JSON)
    List<Auction> getUserAuctions(@CookieParam("scc:session") Cookie session, @QueryParam(STATUS) String status) throws WebApplicationException;

    /**
     * Login into session.
     * @param user - user logging in.
     * @return - returns response.
     */
    @POST
    @Path("/auth")
    @Consumes(MediaType.APPLICATION_JSON)
    jakarta.ws.rs.core.Response auth(Login user);
}
