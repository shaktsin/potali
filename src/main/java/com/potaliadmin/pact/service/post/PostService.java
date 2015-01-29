package com.potaliadmin.pact.service.post;

import com.potaliadmin.dto.internal.image.CreateImageResponseDto;
import com.potaliadmin.dto.web.request.posts.AllPostReactionRequest;
import com.potaliadmin.dto.web.request.posts.BookMarkPostRequest;
import com.potaliadmin.dto.web.request.posts.PostCommentRequest;
import com.potaliadmin.dto.web.request.posts.PostReactionRequest;
import com.potaliadmin.dto.web.response.post.*;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.util.Date;
import java.util.List;

/**
 * Created by Shakti Singh on 12/28/14.
 */
public interface PostService {

  GenericPostReactionResponse postReaction(PostReactionRequest postReactionRequest);

  PostSyncResponse syncPost(Long postId);

  CommentResponse postComment(PostCommentRequest postCommentRequest);

  PostResponse fetchPostsByReactionId(BookMarkPostRequest bookMarkPostRequest);

  PostResponse fetchMyPosts(BookMarkPostRequest bookMarkPostRequest);

  CommentListResponse getAllComments(AllPostReactionRequest allPostReactionRequest);

  boolean isPostImportantForUser(Long postId, Long userId);

  boolean postHasComments(Long postId);

  List<CreateImageResponseDto> postImages(List<FormDataBodyPart> imageList, Long postId);
}
