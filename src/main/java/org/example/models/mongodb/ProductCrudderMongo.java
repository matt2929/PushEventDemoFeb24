package org.example.models.mongodb;


import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.json.JsonWriterSettings;
import org.example.models.coupons.ProductEntity;

public class ProductCrudderMongo implements AutoCloseable {


  public static final String MONGODB_MONGO_27017 = "mongodb://mongo:27017";

  public static final String EMPLOYEE_COLLECTION_NAME = "ProductCollection";
  public static final String EMPLOYEE_DB_NAME = "ProductInventory";
  MongoClient mongoClient;
  MongoDatabase db;
  CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
  CodecRegistry pojoCodecRegistry =
      fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));


  public ProductCrudderMongo() throws RuntimeException {
    mongoClient = MongoClients.create(MONGODB_MONGO_27017);
    if (!preFlightChecks(mongoClient)) {
      throw new RuntimeException(String.format("Failure to Instantiate %s", MONGODB_MONGO_27017));
    }
    final String dbNames = mongoClient.listDatabaseNames().into(new ArrayList<>())
        .stream()
        .collect(Collectors.joining(",", "[", "]"));
    System.out.printf("Successfully Connected to %s %s...%n", MONGODB_MONGO_27017, dbNames);
    db = mongoClient.getDatabase(EMPLOYEE_DB_NAME).withCodecRegistry(pojoCodecRegistry);
    db.createCollection(EMPLOYEE_COLLECTION_NAME);
    final IndexOptions indexOptions = new IndexOptions().unique(true);
    String resultCreateIndex = db
        .getCollection(EMPLOYEE_COLLECTION_NAME)
        .createIndex(Indexes.ascending("uuid"), indexOptions);
    System.out.printf("Index created: %s%n", resultCreateIndex);
  }

  private boolean preFlightChecks(MongoClient mongoClient) {
    Document pingCommand = new Document("ping", 1);
    Document response = mongoClient.getDatabase(EMPLOYEE_DB_NAME).runCommand(pingCommand);
    System.out.println("=> Print result of the '{ping: 1}' command.");
    System.out.println(response.toJson(JsonWriterSettings.builder().indent(true).build()));
    return response.get("ok", Number.class).intValue() == 1;
  }

  public void insertEntity(final ProductEntity productEntity) {
    MongoCollection<ProductEntity> collection =
        db.getCollection(EMPLOYEE_COLLECTION_NAME, ProductEntity.class);
    try {
      collection.insertOne(productEntity);
    } catch (MongoWriteException e) {
      System.out.println(e.getMessage());
    }
  }

  public List<ProductEntity> getAllProducts() {
    return db.getCollection(EMPLOYEE_COLLECTION_NAME, ProductEntity.class).find()
        .into(new ArrayList<>());
  }

  public Optional<ProductEntity> getProductById(final UUID uuid) {
    Document searchQuery = new Document();
    searchQuery.put("uuid", uuid.toString());
    FindIterable<ProductEntity>
        cursor = db.getCollection(EMPLOYEE_COLLECTION_NAME, ProductEntity.class).find(searchQuery);
    ArrayList<ProductEntity> entities = new ArrayList<>();
    try (final MongoCursor<ProductEntity> cursorIterator = cursor.cursor()) {
      while (cursorIterator.hasNext()) {
        entities.add(cursorIterator.next());
      }
    }
    if (entities.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(entities.get(0));
  }

  public void printProducts() {
    getAllProducts().forEach(System.out::println);
  }

  @Override
  public void close() {
    if (mongoClient != null) {
      mongoClient.close();
    }
  }

}


