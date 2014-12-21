package com.potaliadmin.impl.dao.post;

import com.potaliadmin.domain.post.PostBlob;
import com.potaliadmin.dto.internal.hibernate.post.CreatePostBlobRequest;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.post.PostBlobDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Shakti Singh on 12/20/14.
 */
@Repository
public class PostBlobDaoImpl extends BaseDaoImpl implements PostBlobDao {

  @Override
  public PostBlob findByPostId(Long postId) {
    return (PostBlob)findUnique("from PostBlob pb where pb.postId = ?",new Object[]{postId});
  }

  @Override
  @Transactional
  public PostBlob createPostBlob(CreatePostBlobRequest createPostBlobRequest) {
    if (createPostBlobRequest == null) {
      throw new RuntimeException("Create Post Blob Request cannot be null");
    }
    if (createPostBlobRequest.getPostId() == null) {
      throw new InValidInputException("Post Id cannot be null");
    }

    PostBlob postBlob = new PostBlob();
    postBlob.setPostId(createPostBlobRequest.getPostId());
    postBlob.setContent(createPostBlobRequest.getContent());

    postBlob = (PostBlob)save(postBlob);
    return postBlob;
  }
}
