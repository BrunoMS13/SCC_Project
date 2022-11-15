package scc.srv;

import api.RestMedia;
import jakarta.ws.rs.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

import jakarta.ws.rs.core.MediaType;
import utils.IdGenerator;

/**
 * Resource for managing media files, such as images.
 */
public class MediaResource implements RestMedia
{

	private String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=sccstwesteurope58569;AccountKey=xdWfFjojTkmXu9WalAAp1GyUm5HyiMinR6LmAY12SQSZkAb523mOHZWzhzeBapJ56IeRERo8DEQT+AStDepDfA==;EndpointSuffix=core.windows.net";

	public String upload(byte[] contents) {
		try {
			BinaryData data = BinaryData.fromBytes(contents);

			// Get container client.
			BlobContainerClient containerClient = new BlobContainerClientBuilder()
					.connectionString(storageConnectionString)
					.containerName("images")
					.buildClient();


			String id = "BlobID_" + IdGenerator.getInstance().generateUniqueId();;

			// Get client to blob
			BlobClient blob = containerClient.getBlobClient(id);

			// Upload contents from BinaryData (check documentation for other alternatives)
			blob.upload(data);
			return id;
		} catch( Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] download(String filename) {
		byte[] arr = null;
		try {
			// Get container client.
			BlobContainerClient containerClient = new BlobContainerClientBuilder()
					.connectionString(storageConnectionString)
					.containerName("images")
					.buildClient();

			// Get client to blob
			BlobClient blob = containerClient.getBlobClient(filename);

			// Download contents to BinaryData (check documentation for other alternatives)
			BinaryData data = blob.downloadContent();

			arr = data.toBytes();
			System.out.println( "Blob size : " + arr.length);
		} catch( Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	/**
	 * Lists the ids of images stored.
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> list() {
		return new ArrayList<String>();
	}

	public static void main(String[] args) {
		MediaResource mr = new MediaResource();
		// If already exists, it throws exception.
		mr.upload("asdasdasd".getBytes());
		mr.upload("asdffasdcasdxa".getBytes());
		mr.upload("xasxas".getBytes());
		mr.upload("asdasdasdsfaasd".getBytes());

		MediaResource mr2 = new MediaResource();
		mr2.upload("asdasdasdsfaasd".getBytes());
		mr2.upload("asdasdasdsfaasd".getBytes());

		//System.out.println(new String(mr.download("cats.1.jpeg")));
	}
}
