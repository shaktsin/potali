package com.potaliadmin.impl.dao.address;

import com.potaliadmin.domain.address.City;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.city.CityDao;
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
public class CityDaoImpl extends BaseDaoImpl implements CityDao {

  @Override
  public City findById(Long id) {
    return get(City.class, id);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Set<City> findListOfCity(List<Long> idList) {
    Set<City> citySet = new HashSet<City>();
    DetachedCriteria detachedCriteria = DetachedCriteria.forClass(City.class);
    detachedCriteria.add(Restrictions.in("id", idList));
    citySet.addAll(findByCriteria(detachedCriteria));
    return citySet;
  }
}
