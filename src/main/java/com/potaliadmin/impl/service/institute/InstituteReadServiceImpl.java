package com.potaliadmin.impl.service.institute;

import com.potaliadmin.dto.internal.cache.institute.InstituteVO;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.framework.cache.institute.InstituteCache;
import com.potaliadmin.pact.service.institute.InstituteReadService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Shakti Singh on 12/15/14.
 */
@Service
public class InstituteReadServiceImpl implements InstituteReadService {


  public List<InstituteVO> getAllInstitute() {
    InstituteCache instituteCache = InstituteCache.getCache();
    if (instituteCache == null) {
      throw new PotaliRuntimeException("Cache is null, server problem");
    }
    return instituteCache.getAllInstitute();
  }
}
