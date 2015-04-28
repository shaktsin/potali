package com.potaliadmin.pact.dao.classified;

import com.potaliadmin.domain.classified.ClassifiedPost;
import com.potaliadmin.dto.web.request.classified.ClassifiedPostRequest;
import com.potaliadmin.pact.framework.BaseDao;

/**
 * Created by shaktsin on 4/26/15.
 */
public interface ClassifiedDao extends BaseDao {

  ClassifiedPost createClassified(ClassifiedPostRequest classifiedPostRequest);
}
