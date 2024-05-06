package org.example.models;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoDbClient implements AutoCloseable {

  String uri = "mongodb://localhost:8080/";



  MongoClient mongoClient;

  public void init() {

    ServerApi serverApi = ServerApi.builder()
        .version(ServerApiVersion.V1)
        .build();

    MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(new ConnectionString(uri))
        .serverApi(serverApi)
        .build();

    mongoClient = MongoClients.create(settings);
    MongoDatabase database = mongoClient.getDatabase("admin");
    try {
      // Send a ping to confirm a successful connection
      Bson command = new BsonDocument("ping", new BsonInt64(1));
      Document commandResult = database.runCommand(command);
      System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
    } catch (MongoException me) {
      System.err.println(me);
    }
  }

  @Override
  public void close() {
    if (mongoClient != null) {
      mongoClient.close();
    }
  }
}


