package api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/media")
public interface RestMedia {

    /**
     * Uploads media.
     * @param contents - bytes of the media content.
     * @param id - media ID.
     */
    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    void upload(byte[] contents, String id);

    /**
     * Downloads media.
     * @param id - media ID.
     * @return byte array of the media with the respective ID.
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    byte[] download(String id);
}
