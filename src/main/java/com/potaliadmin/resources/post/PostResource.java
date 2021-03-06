package com.potaliadmin.resources.post;

import com.potaliadmin.dto.web.request.posts.*;
import com.potaliadmin.dto.web.response.post.*;
import com.potaliadmin.pact.service.post.PostService;
import com.potaliadmin.util.DateUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
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
  @Path("/react/reverse")
  @Produces("application/json")
  @RequiresAuthentication
  public GenericPostReactionResponse reverseReaction(PostReactionRequest postReactionRequest) {
    try {
        GenericPostReactionResponse genericPostReactionResponse = getPostService().reverseReaction(postReactionRequest);
      return genericPostReactionResponse;

    } catch (Exception e) {
      GenericPostReactionResponse genericPostReactionResponse = new GenericPostReactionResponse();
      genericPostReactionResponse.setException(Boolean.TRUE);
      genericPostReactionResponse.addMessage(e.getMessage());
      return genericPostReactionResponse;
    }
  }

  @POST
  @Path("/imp")
  @Produces("application/json")
  @RequiresAuthentication
  public PostResponse fetchPostsByReactionIds(BookMarkPostRequest bookMarkPostRequest) {
    try {
      return getPostService().fetchPostsByReactionId(bookMarkPostRequest);
    } catch (Exception e) {
      PostResponse postResponse = new PostResponse();
      postResponse.setException(Boolean.TRUE);
      postResponse.addMessage(e.getMessage());
      return postResponse;
    }
  }

  @POST
  @Path("/circle/posts")
  @Produces("application/json")
  @RequiresAuthentication
  public PostResponse fetchCirclePosts(CirclePostRequest circlePostRequest) {
    try {
      return getPostService().fetchCirclePosts(circlePostRequest);
    } catch (Exception e) {
      PostResponse postResponse = new PostResponse();
      postResponse.setException(Boolean.TRUE);
      postResponse.addMessage(e.getMessage());
      return postResponse;
    }
  }

  @POST
  @Path("/user/profile")
  @Produces("application/json")
  @RequiresAuthentication
  public UserProfileResponse fetchUserProfile(UserProfileRequest userProfileRequest) {
    try {
      return getPostService().fetchUserProfile(userProfileRequest);
    } catch (Exception e) {
      UserProfileResponse postResponse = new UserProfileResponse();
      postResponse.setException(Boolean.TRUE);
      postResponse.addMessage(e.getMessage());
      return postResponse;
    }
  }


  @POST
  @Path("/circle/profile")
  @Produces("application/json")
  @RequiresAuthentication
  public CircleProfileResponse fetchCircleProfile(CirclePostRequest circlePostRequest) {
    try {
      return getPostService().fetchCircleProfile(circlePostRequest);
    } catch (Exception e) {
      CircleProfileResponse postResponse = new CircleProfileResponse();
      postResponse.setException(Boolean.TRUE);
      postResponse.addMessage(e.getMessage());
      return postResponse;
    }
  }

  @POST
  @Path("/user/posts")
  @Produces("application/json")
  @RequiresAuthentication
  public UserPostResponse fetchUserPosts(UserProfileRequest userProfileRequest) {
    try {
      return getPostService().fetchUsersPosts(userProfileRequest);
    } catch (Exception e) {
      UserPostResponse postResponse = new UserPostResponse();
      postResponse.setException(Boolean.TRUE);
      postResponse.addMessage(e.getMessage());
      return postResponse;
    }
  }


  @POST
  @Path("/comment")
  @Produces("application/json")
  @RequiresAuthentication
  public CommentResponse postComment(PostCommentRequest postCommentRequest) {
    try {
      return getPostService().postComment(postCommentRequest);
    } catch (Exception e) {
      CommentResponse commentResponse = new CommentResponse();
      commentResponse.setException(Boolean.TRUE);
      commentResponse.addMessage(e.getMessage());
      return commentResponse;
    }
  }

  @POST
  @Path("/myposts")
  @Produces("application/json")
  @RequiresAuthentication
  public PostResponse myPosts(BookMarkPostRequest bookMarkPostRequest) {
    try {
      return getPostService().fetchMyPosts(bookMarkPostRequest);
    } catch (Exception e) {
      PostResponse postResponse = new PostResponse();
      postResponse.setException(Boolean.TRUE);
      postResponse.addMessage(e.getMessage());
      return postResponse;
    }
  }


  @POST
  @Path("/sync")
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
      Date date = DateUtils.convertFromString(postSyncRequest.getPostDate());
      return getPostService().syncPost(date);
    } catch (Exception e) {
      PostSyncResponse postSyncResponse = new PostSyncResponse();
      postSyncResponse.setException(true);
      postSyncResponse.addMessage(e.getMessage());
      return postSyncResponse;
    }
  }

  @POST
  @Path("/comment/all")
  @Produces("application/json")
  @RequiresAuthentication
  public CommentListResponse getAllComments(AllPostReactionRequest allPostReactionRequest) {
    try {
      return getPostService().getAllComments(allPostReactionRequest);
    } catch (Exception e) {
      CommentListResponse commentListResponse = new CommentListResponse();
      commentListResponse.setException(true);
      commentListResponse.addMessage(e.getMessage());
      return commentListResponse;
    }
  }


  @GET
  @Path("/filters/all")
  @Produces("application/json")
  @RequiresAuthentication
  public PostFiltersResponse getAllFilters() {
    try {
      return getPostService().getPostFilters();
    } catch (Exception e) {
      PostFiltersResponse postFiltersResponse = new PostFiltersResponse();
      postFiltersResponse.setException(true);
      postFiltersResponse.addMessage(e.getMessage());
      return postFiltersResponse;
    }
  }


  public PostService getPostService() {
    return postService;
  }
}
