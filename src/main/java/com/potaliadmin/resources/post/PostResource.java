package com.potaliadmin.resources.post;

import com.potaliadmin.dto.web.request.posts.PostReactionRequest;
import com.potaliadmin.dto.web.request.posts.PostSyncRequest;
import com.potaliadmin.dto.web.response.post.GenericPostReactionResponse;
import com.potaliadmin.dto.web.response.post.PostSyncResponse;
import com.potaliadmin.pact.service.post.PostService;
import com.potaliadmin.util.BaseUtil;
import com.potaliadmin.util.DateUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Date;

/**
 * Created by Shakti Singh on 12/28/14.
 */
@Path("/post")
@Component
public class PostResource {

  @Autowired
  PostService postService;

  @POST
  @Path("/react")
  @Produces("application/json")
  @RequiresAuthentication
  public GenericPostReactionResponse postReaction(PostReactionRequest postReactionRequest) {
    try {
      return getPostService().postReaction(postReactionRequest);

    } catch (Exception e) {
      GenericPostReactionResponse genericPostReactionResponse = new GenericPostReactionResponse();
      genericPostReactionResponse.setException(Boolean.TRUE);
      genericPostReactionResponse.addMessage(e.getMessage());
      return genericPostReactionResponse;
    }
  }


  @POST
  @Path("/post/sync")
  @Produces("application/json")
  @RequiresAuthentication
  public PostSyncResponse postSyncResponse(PostSyncRequest postSyncRequest) {
    if (!postSyncRequest.validate()) {
      PostSyncResponse postSyncResponse = new PostSyncResponse();
      postSyncResponse.setException(true);
      postSyncResponse.addMessage("Not a valid input");
      return postSyncResponse;
    }

    try {
      //Date date = DateUtils.convertFromString(postSyncRequest.getPostDate());
      return getPostService().syncPost(postSyncRequest.getPostId());
    } catch (Exception e) {
      PostSyncResponse postSyncResponse = new PostSyncResponse();
      postSyncResponse.setException(true);
      postSyncResponse.addMessage(e.getMessage());
      return postSyncResponse;
    }
  }


  public PostService getPostService() {
    return postService;
  }
}
