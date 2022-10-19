package api;

import jakarta.ws.rs.core.MediaType;
import data_classes.UserDAO;

import javax.ws.rs.*;

@Path("/user")
public interface RestUsers {
    String PASSWORD = "password";

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    void createUser(String id, String name, @QueryParam(PASSWORD) String password, String nickname, String photoId);

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void deleteUser(@PathParam("id") String id, @QueryParam(PASSWORD) String password);

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateUser(@PathParam("id") String id, String name, String nickname, @QueryParam(PASSWORD) String password, String photoId);

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    UserDAO getUser(@PathParam("id") String id, @QueryParam(PASSWORD) String password);
}
