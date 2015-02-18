package com.potaliadmin.impl.service.attachment;

import com.potaliadmin.domain.attachment.Attachment;
import com.potaliadmin.domain.post.Post;
import com.potaliadmin.dto.web.request.attachment.AttachmentRequest;
import com.potaliadmin.dto.web.response.base.GenericSuccessResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.exceptions.UnAuthorizedAccessException;
import com.potaliadmin.pact.dao.attachment.AttachmentDao;
import com.potaliadmin.pact.framework.BaseDao;
import com.potaliadmin.pact.framework.aws.UploadService;
import com.potaliadmin.pact.service.attachment.AttachmentService;
import com.potaliadmin.pact.service.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by shakti on 18/2/15.
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

  private Logger logger = LoggerFactory.getLogger(AttachmentServiceImpl.class);

  @Autowired
  UserService userService;

  @Autowired
  AttachmentDao attachmentDao;

  @Autowired
  BaseDao baseDao;

  @Autowired
  UploadService uploadService;

  @Override
  @Transactional
  public GenericSuccessResponse deleteAttachment(AttachmentRequest attachmentRequest) {

    if (!attachmentRequest.validate()) {
      throw new InValidInputException("Please enter valid input");
    }

    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access");
    }

    Attachment attachment = getAttachmentDao().get(Attachment.class, attachmentRequest.getAttachmentId());
    if (attachment == null) {
      throw new InValidInputException("No attachment found");
    }

    Post post = getBaseDao().get(Post.class, attachment.getPostId());
    if (post == null) {
      throw new InValidInputException("Attachment with no post associated");
    }

    UserResponse postUser = getUserService().findById(post.getUserId());
    if (postUser == null) {
      throw new RuntimeException("Something unexpected occurred, please try again");
    }

    if (!postUser.getId().equals(userResponse.getId())) {
      throw new UnAuthorizedAccessException("Operation not permitted");
    }
    String publicId = attachment.getPublicId();


    boolean isDeleted = false;
    try {
      getAttachmentDao().delete(attachment);

      isDeleted = getUploadService().deleteImage(publicId);
      if (!isDeleted) {
        throw new PotaliRuntimeException("Could not delete attachment from cloud");
      }

    } catch (Throwable e) {
      logger.error("Error in deleting attachment ",e);
    }
    GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
    genericSuccessResponse.setSuccess(isDeleted);


    return genericSuccessResponse;
  }

  public UserService getUserService() {
    return userService;
  }

  public AttachmentDao getAttachmentDao() {
    return attachmentDao;
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }

  public UploadService getUploadService() {
    return uploadService;
  }
}
