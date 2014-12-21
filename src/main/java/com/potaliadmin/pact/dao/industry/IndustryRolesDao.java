package com.potaliadmin.pact.dao.industry;

import com.potaliadmin.domain.industry.IndustryRoles;
import com.potaliadmin.pact.framework.BaseDao;

import java.util.List;
import java.util.Set;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public interface IndustryRolesDao extends BaseDao {

  IndustryRoles findById(Long id);

  Set<IndustryRoles> findIndustryRolesSetByIdList(List<Long> idList);


}
