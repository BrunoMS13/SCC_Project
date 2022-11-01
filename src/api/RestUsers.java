package api;

import data_classes.User;
import jakarta.ws.rs.core.MediaType;

import javax.ws.rs.*;

@Path("/user")
public interface RestUsers {
    String ID = "id";
    String PASSWORD = "password";

    //void createUserWithPhoto(User user, byte[] photo) throws WebApplicationException;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void createUser(User user); //throws WebApplicationException;

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void deleteUser(@PathParam("id") String id, @QueryParam(PASSWORD) String password) throws WebApplicationException;

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateUser(@PathParam(ID) String id, @QueryParam(PASSWORD) String password, User user) throws WebApplicationException;

    @GET
    @Path("/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    User getUser(@PathParam(ID) String id, @QueryParam(PASSWORD) String password) throws WebApplicationException;
}
