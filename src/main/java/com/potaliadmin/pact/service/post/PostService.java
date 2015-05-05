package com.potaliadmin.pact.service.post;

import com.potaliadmin.dto.internal.image.CreateAttachmentResponseDto;
import com.potaliadmin.dto.web.request.posts.*;
import com.potaliadmin.dto.web.response.post.*;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.util.Date;
import java.util.List;

/**
 * Created by Shakti Singh on 12/28/14.
 */
public interface PostService {

  GenericPostReactionResponse postReaction(PostReactionRequest postReactionRequest);

  PostSyncResponse syncPost(Date currentDate);

  CommentResponse postComment(PostCommentRequest postCommentRequest);

  PostResponse fetchPostsByReactionId(BookMarkPostRequest bookMarkPostRequest);

  UserPostResponse fetchUsersPosts(UserProfileRequest userProfileRequest);

  UserPostResponse fetchCirclePosts(CirclePostRequest circlePostRequest);

  PostResponse fetchMyPosts(BookMarkPostRequest bookMarkPostRequest);

  CommentListResponse getAllComments(AllPostReactionRequest allPostReactionRequest);

  boolean isPostImportantForUser(Long postId, Long userId);

  boolean isPostLikedForUser(Long postId, Long userId);

  boolean isPostMarkHiddenOrSpammed(Long postId, Long userId);

  boolean postHasComments(Long postId);

  List<CreateAttachmentResponseDto> postImages(List<FormDataBodyPart> imageList, Long postId);

  List<CreateAttachmentResponseDto> postRawFiles(List<FormDataBodyPart> docList, Long postId);

  PostFiltersResponse getPostFilters();

  GenericPostReactionResponse reverseReaction(PostReactionRequest postReactionRequest);

  UserProfileResponse fetchUserProfile(UserProfileRequest userProfileRequest);

  CircleProfileResponse fetchCircleProfile(CirclePostRequest circlePostRequest);

}
