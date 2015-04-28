package com.potaliadmin.impl.dao.classified;

import com.potaliadmin.domain.classified.SecondaryCategory;
import com.potaliadmin.domain.industry.IndustryRoles;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.classified.SecondaryCategoryDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by shaktsin on 4/26/15.
 */
@Repository
public class SecondaryCategoryDaoImpl extends BaseDaoImpl implements SecondaryCategoryDao {

  @Override
  public Set<SecondaryCategory> findSecondaryCategoryByIdList(List<Long> idList) {
    Set<SecondaryCategory> secondaryCategorySet = new HashSet<SecondaryCategory>();
    DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecondaryCategory.class);
    detachedCriteria.add(Restrictions.in("id", idList));
    secondaryCategorySet.addAll(findByCriteria(detachedCriteria));
    return secondaryCategorySet;
  }
}
