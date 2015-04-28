package com.potaliadmin.pact.dao.classified;

import com.potaliadmin.domain.classified.SecondaryCategory;
import com.potaliadmin.domain.industry.IndustryRoles;
import com.potaliadmin.pact.framework.BaseDao;

import java.util.List;
import java.util.Set;

/**
 * Created by shaktsin on 4/26/15.
 */
public interface SecondaryCategoryDao extends BaseDao {

  Set<SecondaryCategory> findSecondaryCategoryByIdList(List<Long> idList);
}
