package com.potaliadmin.impl.dao.industry;

import com.potaliadmin.domain.address.City;
import com.potaliadmin.domain.industry.IndustryRoles;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.industry.IndustryRolesDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Shakti Singh on 12/20/14.
 */
@Repository
public class IndustryRolesDaoImpl extends BaseDaoImpl implements IndustryRolesDao {

  @Override
  public IndustryRoles findById(Long id) {
    return get(IndustryRoles.class, id);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Set<IndustryRoles> findIndustryRolesSetByIdList(List<Long> idList) {
    Set<IndustryRoles> industryRolesSet = new HashSet<IndustryRoles>();
    DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IndustryRoles.class);
    detachedCriteria.add(Restrictions.in("id", idList));
    industryRolesSet.addAll(findByCriteria(detachedCriteria));
    return industryRolesSet;
  }
}
