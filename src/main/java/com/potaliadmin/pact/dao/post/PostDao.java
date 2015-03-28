package com.potaliadmin.pact.dao.post;

import com.potaliadmin.domain.post.Post;
import com.potaliadmin.dto.web.request.newsfeed.NewsFeedCreateRequest;
import com.potaliadmin.dto.web.request.newsfeed.NewsFeedEditRequest;
import com.potaliadmin.pact.framework.BaseDao;

/**
 * Created by shaktsin on 3/28/15.
 */
public interface PostDao extends BaseDao {

  Post createPost(NewsFeedCreateRequest newsFeedCreateRequest);

  Post editPost(NewsFeedEditRequest newsFeedEditRequest);
}
