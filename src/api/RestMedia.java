package api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/media")
public interface RestMedia {

    String FILENAME = "filename";

    /**
     * Uploads media.
     * @param contents - bytes of the media content.
     * @param filename - media ID.
     */
    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    void upload(byte[] contents, @QueryParam(FILENAME) String filename);

    /**
     * Downloads media.
     * @param filename - media ID.
     * @return byte array of the media with the respective ID.
     */
    @GET
    @Path("/{filename}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    byte[] download(@PathParam(FILENAME) String filename);
}
