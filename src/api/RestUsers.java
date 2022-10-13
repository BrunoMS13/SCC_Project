package api;

import jakarta.ws.rs.core.MediaType;
import temppackage.UserDAO;

import javax.ws.rs.*;

@Path("/user")
public interface RestUsers {
    String PASSWORD = "password";

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    void createUser(UserDAO user);

    @DELETE
    @Path("/{nickname}")
    @Consumes(MediaType.APPLICATION_JSON)
    boolean deleteUser(String nickname, @QueryParam(PASSWORD) String password);

    @PUT
    @Path("/{nickname}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    void updateUser(String name, String nickname, @QueryParam(PASSWORD) String password, byte[] photo);

    @GET
    @Path("/{nickname}")
    @Consumes(MediaType.APPLICATION_JSON)
    UserDAO getUser(@PathParam("nickname") String nickname, @QueryParam(PASSWORD) String password);
}
