package com.potaliadmin.pact.dao.post;

import com.potaliadmin.domain.post.PostBlob;
import com.potaliadmin.dto.internal.hibernate.post.CreatePostBlobRequest;
import com.potaliadmin.pact.framework.BaseDao;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public interface PostBlobDao extends BaseDao {

  PostBlob findByPostId(Long postId);

  PostBlob createPostBlob(CreatePostBlobRequest createPostBlobRequest);
}
