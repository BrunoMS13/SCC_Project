package utils;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.util.CosmosPagedIterable;
import data_classes.AuctionDAO;
import data_classes.AuctionNumBidsDAO;
import data_classes.UserDAO;

public class CosmosDBLayer {
	private static final String CONNECTION_URL = "https://scc58569.documents.azure.com:443/";
	private static final String DB_KEY = "0lSn4NbbZ761rnCA1QPbDZl6dUCFwkZ9ir0KlsFTytdCmRg2dZvud59kiFIpdS63dkZAnF1HBhIiACDbOVjdqQ==";
	private static final String DB_NAME = "scc23db58569";
	
	private static CosmosDBLayer instance;

	public static synchronized CosmosDBLayer getInstance() {
		if( instance != null)
			return instance;

		CosmosClient client = new CosmosClientBuilder()
		         .endpoint(CONNECTION_URL)
		         .key(DB_KEY)
		         //.directMode()
		         .gatewayMode()		
		         // replace by .directMode() for better performance
		         .consistencyLevel(ConsistencyLevel.SESSION)
		         .connectionSharingAcrossClientsEnabled(true)
		         .contentResponseOnWriteEnabled(true)
		         .buildClient();
		instance = new CosmosDBLayer( client);
		return instance;
		
	}
	
	private CosmosClient client;
	private CosmosDatabase db;
	protected CosmosContainer users, auctions, trendingAuctions;
	
	public CosmosDBLayer(CosmosClient client) {
		this.client = client;
	}
	
	protected synchronized void init() {
		if( db != null)
			return;
		db = client.getDatabase(DB_NAME);
		users = db.getContainer("users");
		auctions = db.getContainer("auctions");
		trendingAuctions = db.getContainer("auctionsTrend");
		
	}

	// ------------------------- User Methods ------------------------- //

	public CosmosItemResponse<Object> delUserById(String id) {
		init();
		PartitionKey key = new PartitionKey( id);
		return users.deleteItem(id, key, new CosmosItemRequestOptions());
	}
	
	public CosmosItemResponse<Object> delUser(UserDAO user) {
		init();
		return users.deleteItem(user, new CosmosItemRequestOptions());
	}
	
	public CosmosItemResponse<UserDAO> putUser(UserDAO user) {
		init();
		return users.createItem(user);
	}

	public CosmosItemResponse<UserDAO> updateUser(UserDAO user) {
		init();
		return users.upsertItem(user);
	}

	public CosmosPagedIterable<UserDAO> getUserById( String id) {
		init();
		return users.queryItems("SELECT * FROM users WHERE users.id=\"" + id + "\"", new CosmosQueryRequestOptions(), UserDAO.class);
	}

	public CosmosPagedIterable<UserDAO> getUsers() {
		init();
		return users.queryItems("SELECT * FROM users ", new CosmosQueryRequestOptions(), UserDAO.class);
	}

	// ------------------------- Auctions Methods ------------------------- //

	public CosmosItemResponse<AuctionDAO> putAuction(AuctionDAO auction) {
		init();
		return auctions.createItem(auction);
	}

	public CosmosItemResponse<AuctionDAO> updateAuction(AuctionDAO auction) {
		init();
		return auctions.upsertItem(auction);
	}

	public CosmosPagedIterable<AuctionDAO> getAuctionById(String id) {
		init();
		return auctions.queryItems("SELECT * FROM auctions WHERE auctions.id=\"" + id + "\"", new CosmosQueryRequestOptions(), AuctionDAO.class);
	}

	public CosmosPagedIterable<AuctionDAO> getAuctions() {
		init();
		return auctions.queryItems("SELECT * FROM auctions ", new CosmosQueryRequestOptions(), AuctionDAO.class);
	}

	public CosmosPagedIterable<AuctionNumBidsDAO> getTrendingAuctions() {
		init();
		return trendingAuctions.queryItems("SELECT * FROM auctionsTrend ORDER BY auctionsTrend.bid_count DESC", new CosmosQueryRequestOptions(), AuctionNumBidsDAO.class);
	}

	public void close() {
		client.close();
	}
	
	
}
