package temppackage;

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

public class CosmosDBAuctions {
    private static final String CONNECTION_URL = "https://scc58569.documents.azure.com:443/";
    private static final String DB_KEY = "Db0g6Zbdb7P9MTPhMppUn4toDTloc9a5p0323SavkQ2qM9HWSeipOJzRHLjo3BiQByHtN99tGDxKzVc0PLc4Fw==";
    private static final String DB_NAME = "scc23db";

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
    private CosmosContainer auctions;

    public CosmosDBAuctions(CosmosClient client) {
        this.client = client;
    }

    private synchronized void init() {
        if( db != null)
            return;
        db = client.getDatabase(DB_NAME);
        auctions = db.getContainer("auctions");

    }

    public CosmosItemResponse<AuctionDAO> putAuction(AuctionDAO auction) {
        init();
        return auctions.createItem(auction);
    }

    public CosmosItemResponse<AuctionDAO> updateAuction(AuctionDAO auction) {
        init();
        return auctions.upsertItem(auction);
    }

    public CosmosPagedIterable<AuctionDAO> getAuctionById( String id) {
        init();
        return auctions.queryItems("SELECT * FROM auctions WHERE auctions.id=\"" + id + "\"", new CosmosQueryRequestOptions(), AuctionDAO.class);
    }

    public CosmosPagedIterable<AuctionDAO> getAuctions() {
        init();
        return auctions.queryItems("SELECT * FROM auctions ", new CosmosQueryRequestOptions(), AuctionDAO.class);
    }

    public void close() {
        client.close();
    }


}
