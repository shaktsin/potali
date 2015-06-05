package com.potaliadmin.impl.service.notification;

import com.potaliadmin.constants.notification.EnumNotificationType;
import com.potaliadmin.domain.comment.Comment;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.framework.elasticsearch.BaseESService;
import com.potaliadmin.pact.dao.post.PostCommentDao;
import com.potaliadmin.pact.dao.post.PostDao;
import com.potaliadmin.pact.service.notification.NotificationService;
import com.potaliadmin.pact.service.post.PostService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.util.BaseUtil;
import com.potaliadmin.util.SendNotificationUtil;
import com.potaliadmin.vo.post.PostVO;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shaktsin on 6/1/15.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

  private static Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

  private static final String CONTENT_TYPE = "application/json";
  private static final String GOOGLE_URL = "https://android.googleapis.com/gcm/send";
  private static final String AUTH_KEY = "key=AIzaSyAPo4ELrlJ852Df5jtzwjHsMJBx0ggZjMc";
  private static final Integer BATCH_SIZE = 1000;
  private static final String UNINSTALLED_ERROR = "NotRegistered";

  @Autowired
  PostCommentDao postCommentDao;

  @Autowired
  UserService userService;


  @Autowired
  BaseESService baseESService;



  @Override
  public boolean sendNotification(Long postId, EnumNotificationType enumNotificationType) {
    List<String> gcmIds = new ArrayList<String>();

    return false;
  }

  @Override
  public boolean sendCommentNotification(Long commentId) {
    List<String> gcmIds = new ArrayList<String>();
    boolean isSent = false;

    Comment comment = getPostCommentDao().get(Comment.class, commentId);
    if (comment == null) {
      isSent = false;
    } else {
      Long postId = comment.getPostId();
      PostVO postVO = (PostVO) getBaseESService().get(postId, null , PostVO.class);
      if (postVO == null) {
        throw new PotaliRuntimeException("NO POST FOUND FOR POST ID "+postId);
      }

      UserResponse commentUser = getUserService().findById(comment.getUserId());
      UserResponse postUser = getUserService().findById(postVO.getUserId());

      String title = commentUser.getName() + " has commented on " + BaseUtil.trimContent(postVO.getSubject(), 10);
      String subject = BaseUtil.trimContent(comment.getComment(), 200);
      List<Long> userIds = getPostCommentDao().getCommentsUser(postId);

      for (Long userId :  userIds) {
        if (!userId.equals(commentUser.getId())) {
          UserResponse otherCommentUsers = getUserService().findById(userId);

          gcmIds.add(otherCommentUsers.getGcmId());
        }
      }

      // add post's user too
      if (!postUser.getId().equals(commentUser.getId())) {
        gcmIds.add(postUser.getGcmId());
      }

      if (gcmIds.size() > 0) {
        SendNotificationUtil sendNotificationUtil = new SendNotificationUtil(gcmIds, null, title, subject, postId, postVO.getPostType());
        notificationService.execute(sendNotificationUtil);
      }

      //isSent = sendNotification(gcmIds, title, subject, null);

    }


    return isSent;
  }

  @Override
  public boolean sendLikeNotification(Long postId, Long userId) {
    List<String> gcmIds = new ArrayList<String>();
    boolean isSent = false;

    UserResponse commentUser = getUserService().findById(userId);

    PostVO postVO = (PostVO) getBaseESService().get(postId, null , PostVO.class);
    if (postVO == null) {
      throw new PotaliRuntimeException("NO POST FOUND FOR POST ID "+postId);
    }

    UserResponse postUser = getUserService().findById(postVO.getUserId());

    String title = commentUser.getName() + " has liked your post";
    String subject = BaseUtil.trimContent(postVO.getSubject(), 200);

    // add post's user too
    if (!postUser.getId().equals(commentUser.getId())) {
      gcmIds.add(postUser.getGcmId());
    }
    //gcmIds.add(postUser.getGcmId());

    if (gcmIds.size() > 0) {
      SendNotificationUtil sendNotificationUtil = new SendNotificationUtil(gcmIds, null, title, subject, postId, postVO.getPostType());
      notificationService.execute(sendNotificationUtil);
    }

    //isSent = sendNotification(gcmIds, title, subject, null);

    return isSent;
  }


  /*@SuppressWarnings("unchecked")
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
        }
        i = i + batch;
      } catch (Exception e) {
        logger.error("Exception while sending notification: " + e);
        isSent = false;
      }
    }

    return isSent;
  }*/

  public PostCommentDao getPostCommentDao() {
    return postCommentDao;
  }

  public UserService getUserService() {
    return userService;
  }

  public BaseESService getBaseESService() {
    return baseESService;
  }
}
