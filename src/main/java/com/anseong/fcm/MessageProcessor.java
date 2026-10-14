package com.anseong.fcm;

import java.io.FileInputStream;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.json.Json;

public class MessageProcessor implements RequestHandler<Map<String, String>, String> {

     private final ObjectMapper objectMapper = new ObjectMapper();
     protected final FCM fcm;

     public MessageProcessor() throws Exception {

          FileInputStream fcmCredential = new FileInputStream("./fcm-server.json");

          FirebaseOptions options = FirebaseOptions.builder()
               .setCredentials(GoogleCredentials.fromStream(fcmCredential))
               .build();

          FirebaseApp.initializeApp(options);

          fcmCredential = new FileInputStream("./fcm-server.json");
          GoogleCredentials googleCredential = GoogleCredentials
               .fromStream(fcmCredential)
               .createScoped("https://www.googleapis.com/auth/firebase.messaging");

          this.fcm = new FCM(googleCredential);
          fcmCredential.close();
     }

     @Override
     public String handleRequest(Map<String, String> input, Context context) {
          LambdaLogger logger = context.getLogger();
          String responseToken = "";

          logger.log("***** 페이로드 테스트2 *****");
          logger.log("type : " + input.get("type"));
          logger.log("token : " + input.get("token"));

          try {
               switch (input.get("type")) {
                    case "test":
                         fcm.testToUser(input.get("token"));
                         break;

                    case "sendToTopic":
                         fcm.sendToTopic(
                              input.get("topic"),
                              input.get("description"),
                              input.get("title"),
                              input.get("thumbnail"),
                              input.get("redirect"));
                         break;

                    case "sendToBoard":
                         fcm.sendToBoard(
                              input.get("title"),
                              input.get("thumbnail"),
                              input.get("redirect"),
                              false);
                         break;

                    case "sendToCeo":
                         fcm.sendToCeo(
                              input.get("title"),
                              input.get("thumbnail"),
                              input.get("redirect"));
                         break;

                    case "referTo":
                         fcm.referTo(
                              objectMapper.readValue(input.get("tokens"), new TypeReference<>(){}),
                              input.get("thumbnail"),
                              input.get("title"));
                         break;

                    case "batchImportFromAPNsToken":
                         responseToken = fcm.batchImportFromAPNsToken(
                              input.get("APNs"));
                         break;

                    case "subscribeToTopic":
                         fcm.subscribeToTopic(
                              input.get("topic"),
                              input.get("token"),
                              Boolean.parseBoolean(input.get("isCeo")),
                              Boolean.parseBoolean(input.get("adult")));
                         break;

                    case "subscribeToTopicAsync":
                         fcm.subscribeToTopicAsync(
                              input.get("topic"),
                              input.get("token"),
                              Boolean.parseBoolean(input.get("isCeo")),
                              Boolean.parseBoolean(input.get("adult")));
                         break;

                    case "unsubscribeFromTopic":
                         fcm.unsubscribeFromTopic(
                              input.get("topic"),
                              input.get("token"),
                              Boolean.parseBoolean(input.get("isCeo")),
                              Boolean.parseBoolean(input.get("adult")));
                         break;

                    case "unsubscribeFromTopicAsync":
                         fcm.unsubscribeFromTopicAsync(
                              input.get("topic"),
                              input.get("token"),
                              Boolean.parseBoolean(input.get("isCeo")),
                              Boolean.parseBoolean(input.get("adult")));
                         break;
               }

               return Json.createObjectBuilder()
                    .add("token", responseToken)
                    .build().toString();

          } catch (Exception e) {
               logger.log("프로세스 실패 : " + e.getMessage());
               e.printStackTrace();

               return Json.createObjectBuilder()
                    .add("errorMessage", e.getMessage())
                    .build().toString();
          }
     }
}
