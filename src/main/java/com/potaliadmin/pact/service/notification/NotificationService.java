package com.potaliadmin.pact.service.notification;

import com.potaliadmin.constants.notification.EnumNotificationType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shaktsin on 6/1/15.
 */
public interface NotificationService {

  ExecutorService notificationService = Executors.newFixedThreadPool(10);


  boolean sendNotification(Long postId, EnumNotificationType enumNotificationType);

  boolean sendCommentNotification(Long commentId);

  boolean sendLikeNotification(Long postId, Long userId);
}
