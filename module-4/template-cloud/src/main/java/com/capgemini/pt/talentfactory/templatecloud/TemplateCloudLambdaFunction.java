package com.capgemini.pt.talentfactory.templatecloud;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public class TemplateCloudLambdaFunction
    implements RequestHandler<SQSEvent, Void> {
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  DynamoDbClient client = DynamoDbClient.builder()
      .endpointOverride(URI.create("http://localhost:4566"))
      .region(Region.US_EAST_1)
      .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
      .build();

  @Override
  public Void handleRequest(SQSEvent event, Context context) {
    LambdaLogger logger = context.getLogger();
    System.out.println(">>> sout test");
    logger.log("Stuff logged");

    String numeroFatura = "";
    String descricaoFatura = "";
    String estadoFatura = "";
    String response = "Java Lambda invocation response";

    for (SQSEvent.SQSMessage msg : event.getRecords()) {
      System.out.println(new String(msg.getBody()));
      JsonObject body = new Gson().fromJson(msg.getBody(), JsonObject.class);
      numeroFatura = body.get("numeroFatura").getAsString();
      descricaoFatura = body.get("descricaoFatura").getAsString();
      estadoFatura = body.get("estadoFatura").getAsString();
    }

    logger.log("EVENT TYPE: " + event.getClass());
    Map<String, String> hashReturn = new java.util.HashMap<String, String>();
    hashReturn.put("response", response);

    for (String table : client.listTables().tableNames()) {
      System.out.println("table: " + table);
    }

    Map<String, AttributeValue> filterKeyMap = new HashMap<>();
    filterKeyMap.put("numeroFatura", AttributeValue.fromS(numeroFatura));
    filterKeyMap.put("descricaoFatura", AttributeValue.fromS(descricaoFatura));

    Map<String, AttributeValue> expAttrValue = new HashMap<>();
    expAttrValue.put(":newval", AttributeValue.fromS(estadoFatura));

    UpdateItemRequest request = UpdateItemRequest.builder()
        .tableName("PagamentoFaturas")
        .key(filterKeyMap)
        .updateExpression("SET estadoFatura = :newval")
        .expressionAttributeValues(expAttrValue)
        .returnValues(ReturnValue.UPDATED_NEW)
        .build();

    client.updateItem(request);

    showPostgresData(logger);
    updatePostgresData(logger, numeroFatura, descricaoFatura, estadoFatura);

    return null;

  }

  private void showPostgresData(LambdaLogger logger) {
    try {
      Connection conn = null;
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://postgres-server:5432/postgres", "postgres", "postgres");

      PreparedStatement ps = conn.prepareStatement("select * from pagamentoFaturas;");
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        logger.log(rs.getString("numeroFatura") + " " + rs.getString("descricaoFatura") + " " + rs.getString("estadoFatura"));
      }
      rs.close();
      ps.close();
      conn.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void updatePostgresData(LambdaLogger logger, String numeroFatura, String descricaoFatura, String estadoFatura) {
    try {
      Connection conn = null;
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://postgres-server:5432/postgres", "postgres", "postgres");

      PreparedStatement ps = conn.prepareStatement("update pagamentoFaturas set estadoFatura=? where numeroFatura=? and descricaoFatura=?;");
      ps.setString(1, estadoFatura);
      ps.setString(2, numeroFatura);
      ps.setString(3, descricaoFatura);
      ps.executeUpdate();
      ps.close();
      conn.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
