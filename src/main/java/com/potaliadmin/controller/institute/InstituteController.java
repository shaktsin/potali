package com.potaliadmin.controller.institute;

import com.potaliadmin.dto.web.response.institute.AllInstituteResponse;
import com.potaliadmin.dto.web.response.institute.InstituteDto;
import com.potaliadmin.framework.cache.institute.InstituteCache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;

/**
 * Created by Shakti Singh on 12/15/14.
 */
@RestController
@RequestMapping("/rest/institute")
public class InstituteController {

  @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
  public AllInstituteResponse getAllInstitute() {
    InstituteCache instituteCache = InstituteCache.getCache();
    if (instituteCache == null) {
      AllInstituteResponse allInstituteResponse = new AllInstituteResponse();
      allInstituteResponse.setException(Boolean.TRUE);
      allInstituteResponse.addMessage("Server Cache Initialization Problem Occurred");
      return allInstituteResponse;
    }
    AllInstituteResponse allInstituteResponse = new AllInstituteResponse();
    allInstituteResponse.setInstituteDtoList(InstituteDto.createList(instituteCache.getAllInstitute()));
    return allInstituteResponse;
  }
}
