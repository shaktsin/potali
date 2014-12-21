package com.potaliadmin.pact.dao.city;

import com.potaliadmin.domain.address.City;
import com.potaliadmin.pact.framework.BaseDao;

import java.util.List;
import java.util.Set;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public interface CityDao extends BaseDao {

  City findById(Long id);

  Set<City> findListOfCity(List<Long> idList);
}
