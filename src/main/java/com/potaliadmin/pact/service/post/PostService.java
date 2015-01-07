package com.potaliadmin.pact.service.post;

import com.potaliadmin.dto.web.request.posts.PostReactionRequest;
import com.potaliadmin.dto.web.response.post.GenericPostReactionResponse;
import com.potaliadmin.dto.web.response.post.PostSyncResponse;

import java.util.Date;

/**
 * Created by Shakti Singh on 12/28/14.
 */
public interface PostService {

  GenericPostReactionResponse postReaction(PostReactionRequest postReactionRequest);

  PostSyncResponse syncPost(Long postId);
}
