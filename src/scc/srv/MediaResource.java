package scc.srv;

import api.RestMedia;
import jakarta.ws.rs.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.FileWriter;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

import jakarta.ws.rs.core.MediaType;

/**
 * Resource for managing media files, such as images.
 */
public class MediaResource implements RestMedia
{

	private final String PATH = "/mnt/vol/";

	public String upload(byte[] contents) {

			String id = "ImageID_" + UUID.randomUUID();

			try {
				FileOutputStream outputStream = new FileOutputStream(PATH + id);
				outputStream.write(contents);
				outputStream.close();
				return id;
			} catch (Exception e) {
				System.out.println("Couldn't upload...");
			}
			return null;
	}

	public byte[] download(String filename) {
		try (FileInputStream fis = new FileInputStream(PATH + filename)) {
			System.out.println("Found file, downloading...");
			return fis.readAllBytes();
		} catch (Exception e) {
			System.out.println("Couldn't download...");
		}
		return null;
	}

	public static void main(String[] args) {

	}
}
