package com.anseong.fcm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.json.Json;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

public class FCM {

     private static final Logger log = LoggerFactory.getLogger(FCM.class);
     private final GoogleCredentials googleCredential;

     protected FCM(GoogleCredentials googleCredential) {
          this.googleCredential = googleCredential;
     }

     protected void sendToTopic(String topic, String description, String title, String thumbnail, String redirect) throws FirebaseMessagingException, IOException {
          Message message = Message.builder()
               .setAndroidConfig(AndroidConfig.builder()
                    .putData("experienceId", "@jung-ji-hyun/anseongapp")
                    .putData("title", URLDecoder.decode(title, "UTF-8"))
                    .putData("message", description)
                    .putData("image", thumbnail)
                    .putData("body", Json.createObjectBuilder()
                         .add("redirect", redirect)
                         .add("level", topic)
                         .build().toString())
                    .build()
               )
               .setApnsConfig(ApnsConfig.builder()
                    .setAps(Aps.builder()
                         .setAlert(ApsAlert.builder()
                              .setTitle(URLDecoder.decode(title, "UTF-8"))
                              .setBody(description)
                              .setLaunchImage(thumbnail)
                              .build()
                         )
                         .setSound("default")
                         .build()
                    )
                    .putCustomData("experienceId", "@jung-ji-hyun/anseongapp")
                    .putCustomData("body", Json.createObjectBuilder()
                         .add("redirect", redirect)
                         .add("level", topic)
                         .build().toString())
                    .build()
               )
               .setTopic(topic)
               .build();

          FirebaseMessaging.getInstance().send(message);
     }

     protected void sendToBoard(String title, String thumbnail, String redirect, boolean adult) throws FirebaseMessagingException, IOException {
          Message message = Message.builder()
               .setAndroidConfig(AndroidConfig.builder()
                    .putData("experienceId", "@jung-ji-hyun/anseongapp")
                    .putData("title", "🌍운영자 소식")
                    .putData("message", URLDecoder.decode(title, "UTF-8"))
                    .putData("image", thumbnail)
                    .putData("body", Json.createObjectBuilder()
                         .add("redirect", redirect)
                         .add("level", "board")
                         .build().toString())
                    .build()
               )
               .setApnsConfig(ApnsConfig.builder()
                    .setAps(Aps.builder()
                         .setAlert(ApsAlert.builder()
                              .setTitle("🌍운영자 소식")
                              .setBody(URLDecoder.decode(title, "UTF-8"))
                              .setLaunchImage(thumbnail)
                              .build()
                         )
                         .setSound("default")
                         .build()
                    )
                    .putCustomData("experienceId", "@jung-ji-hyun/anseongapp")
                    .putCustomData("body", Json.createObjectBuilder()
                         .add("redirect", redirect)
                         .add("level", "board")
                         .build().toString())
                    .build()
               )
               .setTopic("board" + (adult ? ".adult" : ""))
               .build();

          FirebaseMessaging.getInstance().send(message);
     }

     protected void sendToCeo(String title, String thumbnail, String redirect) throws FirebaseMessagingException, IOException {
          Message message = Message.builder()
               .setAndroidConfig(AndroidConfig.builder()
                    .putData("experienceId", "@jung-ji-hyun/anseongapp")
                    .putData("title", "👩‍🍳사장님 전용 소식")
                    .putData("message", URLDecoder.decode(title, "UTF-8"))
                    .putData("image", thumbnail)
                    .putData("body", Json.createObjectBuilder()
                         .add("redirect", redirect)
                         .add("level", "board")
                         .build().toString())
                    .build()
               )
               .setApnsConfig(ApnsConfig.builder()
                    .setAps(Aps.builder()
                         .setAlert(ApsAlert.builder()
                              .setTitle("👩‍🍳사장님 전용 소식")
                              .setBody(URLDecoder.decode(title, "UTF-8"))
                              .setLaunchImage(thumbnail)
                              .build()
                         )
                         .setSound("default")
                         .build()
                    )
                    .putCustomData("experienceId", "@jung-ji-hyun/anseongapp")
                    .putCustomData("body", Json.createObjectBuilder()
                         .add("redirect", redirect)
                         .add("level", "board")
                         .build().toString())
                    .build()
               )
               .setTopic("board.ceo")
               .build();

          FirebaseMessaging.getInstance().send(message);
     }

     protected String testToUser(String token) throws FirebaseMessagingException {
          Message message = Message.builder()
               .setAndroidConfig(AndroidConfig.builder()
                    .putData("experienceId", "@jung-ji-hyun/anseongapp")
                    .putData("title", "✔테스트 메세지")
                    .putData("message", "이 메세지가 보인다면 성공입니다.👌")
                    .putData("image", "https://image.anseongapp.com/anseongapp-board-images-all/20210622-141950/V0srGTpq5AEcdTT3ESJ8.thumb.jpeg")
                    .putData("body", Json.createObjectBuilder()
                         .add("redirect", "/boards/1?redirect=true")
                         .add("level", "success")
                         .build().toString())
                    .build()
               )
               .setApnsConfig(ApnsConfig.builder()
                    .setAps(Aps.builder()
                         .setAlert(ApsAlert.builder()
                              .setTitle("✔APNs 테스트")
                              .setBody("아이폰에서 이 메세지가 보인다면 성공입니다.👌")
                              .setLaunchImage("https://image.anseongapp.com/anseongapp-board-images-all/20210622-141950/V0srGTpq5AEcdTT3ESJ8.thumb.jpeg")
                              .build()
                         )
                         .setSound("default")
                         .putCustomData("body", Json.createObjectBuilder()
                              .add("redirect", "/boards/1?redirect=true")
                              .add("level", "success")
                              .build().toString())
                         .build()
                    )
                    .putCustomData("experienceId", "@jung-ji-hyun/anseongapp")
                    .putCustomData("body", Json.createObjectBuilder()
                         .add("redirect", "/boards/1?redirect=true")
                         .add("level", "success")
                         .build().toString())
                    .build()
               )
               .putData("body", Json.createObjectBuilder()
                    .add("redirect", "/boards/1?redirect=true")
                    .add("level", "success")
                    .build().toString())
               .setToken(token)
               .build();

          return FirebaseMessaging.getInstance().send(message);
     }

     protected void referTo(Set<String> tokens, String thumbnail, String redirect) throws FirebaseMessagingException {
          if (tokens.size() > 0) {
               for (String token : tokens) {
                    Message message = Message.builder()
                         .setAndroidConfig(AndroidConfig.builder()
                              .putData("experienceId", "@jung-ji-hyun/anseongapp")
                              .putData("title", "✅운영자 소식에서 사장님 가게를 언급하였습니다.")
                              .putData("message", "알림을 클릭하여 오늘 소식을 확인해보세요!")
                              .putData("image", thumbnail)
                              .putData("body", Json.createObjectBuilder()
                                   .add("redirect", redirect)
                                   .add("level", "board")
                                   .build().toString())
                              .build()
                         )
                         .setApnsConfig(ApnsConfig.builder()
                              .setAps(Aps.builder()
                                   .setAlert(ApsAlert.builder()
                                        .setTitle("✅운영자 소식에서 사장님 가게를 언급하였습니다.")
                                        .setBody("알림을 클릭하여 오늘 소식을 확인해보세요!")
                                        .setLaunchImage(thumbnail)
                                        .build()
                                   )
                                   .setSound("default")
                                   .build()
                              )
                              .putCustomData("experienceId", "@jung-ji-hyun/anseongapp")
                              .putCustomData("body", Json.createObjectBuilder()
                                   .add("redirect", redirect)
                                   .add("level", "board")
                                   .build().toString())
                              .build()
                         )
                         .setToken(token)
                              .build();

                    FirebaseMessaging.getInstance().send(message);
               }
          }
     }

     protected String batchImportFromAPNsToken(String APNs) {
          try {
               this.googleCredential.refresh();

               final URL url = new URL("https://iid.googleapis.com/iid/v1:batchImport");
               final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               conn.setRequestMethod("POST");
               conn.setRequestProperty("Content-Type", "application/json; UTF-8");
               conn.setRequestProperty("access_token_auth", "true");
               conn.setRequestProperty("Authorization", "Bearer " + this.googleCredential.getAccessToken().getTokenValue());
               conn.setDoOutput(true);

               final String requestBody = Json.createObjectBuilder()
                         .add("application", "ios.com.anseong.app")
                         .add("sandbox", false)
                         .add("apns_tokens", Json.createArrayBuilder()
                              .add(APNs))
                         .build().toString();

               final DataOutputStream output = new DataOutputStream(conn.getOutputStream());
               final OutputStreamWriter writer = new OutputStreamWriter(output);
               writer.write(requestBody);
               writer.flush();
               writer.close();
               output.close();
               conn.disconnect();

               final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
               return Json.createReader(br).readObject().getJsonArray("results").getJsonObject(0).getString("registration_token");

          } catch (Exception e) {
               log.warn("APNs 토큰 BatchImport 실패 : [{}]", e.getMessage());
               return APNs;
          }
     }

     protected void subscribeToTopic(String topic, String userToken, boolean isCeo, boolean adult) throws FirebaseMessagingException, IOException {
          FirebaseMessaging.getInstance().subscribeToTopic(List.of(userToken), topic);

          if (adult)
               FirebaseMessaging.getInstance().subscribeToTopic(List.of(userToken), topic + ".adult");

          if (isCeo)
               FirebaseMessaging.getInstance().subscribeToTopic(List.of(userToken), topic + ".ceo");

     }

     protected void subscribeToTopicAsync(String topic, String userToken, boolean isCeo, boolean adult) throws FirebaseMessagingException, IOException {
          FirebaseMessaging.getInstance().subscribeToTopicAsync(List.of(userToken), topic);

          if (adult)
               FirebaseMessaging.getInstance().subscribeToTopicAsync(List.of(userToken), topic + ".adult");

          if (isCeo)
               FirebaseMessaging.getInstance().subscribeToTopicAsync(List.of(userToken), topic + ".ceo");

     }

     protected void unsubscribeFromTopic(String topic, String userToken, boolean adult, boolean isCeo) throws FirebaseMessagingException, IOException {
          FirebaseMessaging.getInstance().unsubscribeFromTopic(List.of(userToken), topic);

          if (adult)
               FirebaseMessaging.getInstance().unsubscribeFromTopic(List.of(userToken), topic + ".adult");

          if (isCeo)
               FirebaseMessaging.getInstance().unsubscribeFromTopic(List.of(userToken), topic + ".ceo");
     }

     protected void unsubscribeFromTopicAsync(String topic, String userToken, boolean adult, boolean isCeo) throws FirebaseMessagingException, IOException {
          FirebaseMessaging.getInstance().unsubscribeFromTopicAsync(List.of(userToken), topic);

          if (adult)
               FirebaseMessaging.getInstance().unsubscribeFromTopicAsync(List.of(userToken), topic + ".adult");

          if (isCeo)
               FirebaseMessaging.getInstance().unsubscribeFromTopicAsync(List.of(userToken), topic + ".ceo");
     }
}
