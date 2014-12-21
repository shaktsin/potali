package com.potaliadmin.resources.institute;

import com.potaliadmin.dto.internal.cache.institute.InstituteVO;
import com.potaliadmin.dto.web.response.institute.AllInstituteResponse;
import com.potaliadmin.dto.web.response.institute.InstituteDto;
import com.potaliadmin.framework.cache.institute.InstituteCache;
import com.potaliadmin.pact.service.institute.InstituteReadService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by Shakti Singh on 12/15/14.
 */
@Path("/institute")
@Component
public class InstituteResource {

  @Autowired
  private InstituteReadService instituteReadService;

  @GET
  @Path("/all")
  @Produces("application/json")
  public AllInstituteResponse getAllInstitute() {
    AllInstituteResponse allInstituteResponse = new AllInstituteResponse();
    try {
      List<InstituteVO> instituteVOs = getInstituteReadService().getAllInstitute();
      allInstituteResponse.setInstituteDtoList(InstituteDto.createList(instituteVOs));
    } catch (Exception e) {
      allInstituteResponse.setException(Boolean.TRUE);
      allInstituteResponse.addMessage(e.getMessage());
    }
    return allInstituteResponse;

  }

  public InstituteReadService getInstituteReadService() {
    return instituteReadService;
  }

  public void setInstituteReadService(InstituteReadService instituteReadService) {
    this.instituteReadService = instituteReadService;
  }
}
