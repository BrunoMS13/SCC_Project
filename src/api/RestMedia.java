package api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/media")
public interface RestMedia {

    String FILENAME = "filename";

    /**
     * Uploads media.
     * @param contents - bytes of the media content.
     */
    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    String upload(byte[] contents);

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
