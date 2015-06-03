package com.potaliadmin.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by shaktsin on 6/1/15.
 */
public class SendNotificationUtil implements Runnable {

  private static Logger logger = LoggerFactory.getLogger(SendNotificationUtil.class);

  private static final String CONTENT_TYPE = "application/json";
  private static final String GOOGLE_URL = "https://android.googleapis.com/gcm/send";
  private static final String AUTH_KEY = "key=AIzaSyDFChuYp5OMPLAjDMSdEQjCCQCWxyw8d8I";
  private static final Integer BATCH_SIZE = 1000;
  private static final String UNINSTALLED_ERROR = "NotRegistered";

  private List<String> gcmIds;
  private String imageUrl;
  private String title;
  private String subject;



  public SendNotificationUtil(List<String> gcmIds, String imageUrl, String title, String subject) {
    this.gcmIds = gcmIds;
    this.imageUrl = imageUrl;
    this.title = title;
    this.subject = subject;
  }

  @Override
  public void run() {
    sendNotification(this.gcmIds, this.title, this.subject, this.imageUrl);
  }

  @SuppressWarnings("unchecked")
  private boolean sendNotification(List<String> gcmIds, String title, String message, String imageUrl) {
    boolean isSent = false;

    long success = 0L, failure = 0l;
    JSONObject finalObject = new JSONObject();
    JSONObject postObject = new JSONObject();

    postObject.put("title", title);
    postObject.put("message", message);

    if (imageUrl != null && !imageUrl.isEmpty()) {
      postObject.put("picture_url", imageUrl);
    }

    finalObject.put("time_to_live", 60 * 60 * 4); //4 hours
    finalObject.put("data", postObject);


    JSONArray gcmArray = new JSONArray();
    logger.info("Final size of the list " + gcmIds.size());
//        System.out.println("final size of the list " + gcmIdList.size());
    int batch = BATCH_SIZE;
    DefaultHttpClient httpClient = new DefaultHttpClient();
    JSONParser jsonParser = new JSONParser();

    for (int i = 0; i < gcmIds.size(); ) {
      isSent = true;
      if (i + batch < gcmIds.size()) {
        batch = BATCH_SIZE;
      } else if (i < gcmIds.size()) {
        batch = (gcmIds.size() - i);
      } else {
        break;
      }
      List<String> subList = gcmIds.subList(i, i + batch);
      gcmArray.clear();
      gcmArray.addAll(subList);
      finalObject.put("registration_ids", gcmArray);


      HttpPost httpPost = new HttpPost(GOOGLE_URL);
      httpPost.addHeader("Content-Type", CONTENT_TYPE);
      httpPost.addHeader("Authorization", AUTH_KEY);
      logger.info("Sending batch: " + i + " to " + (i + batch));
      try {
        StringEntity stringEntity = new StringEntity(finalObject.toJSONString());
        httpPost.setEntity(stringEntity);
//                System.out.println("FINAL OBJECT: " +finalObject);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
          String response = EntityUtils.toString(httpResponse.getEntity());
//          logger.info("Response " + response);
//                    System.out.println("Response "+response);
          JSONObject responseObject = (JSONObject) jsonParser.parse(response);
          success = success + (Long) responseObject.get("success");
          failure = failure + (Long) responseObject.get("failure");
          Long f = (Long) responseObject.get("failure");
          if (failure > 0L) {
            logger.info("Number of notification failures in batch: " + i + " to " + (i + batch) + " is: " + f);
          }

          if (success > 0L) {
            logger.info("Number of notification failures in batch: " + i + " to " + (i + batch) + " is: " + f);
          }
        } else {
          logger.info("Could not send notification");
        }
        i = i + batch;
      } catch (Exception e) {
        logger.error("Exception while sending notification: " + e);
        isSent = false;
      }
    }

    return isSent;
  }
}
