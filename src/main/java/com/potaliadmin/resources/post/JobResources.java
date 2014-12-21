package com.potaliadmin.resources.post;

import com.potaliadmin.domain.user.User;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.response.job.JobCreateResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.pact.service.job.JobService;
import com.potaliadmin.pact.service.users.LoginService;
import com.potaliadmin.pact.service.users.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Shakti Singh on 12/16/14.
 */
@Path("/jobs")
@Component
public class JobResources {

  @Autowired
  JobService jobService;


  @POST
  @Path("/create")
  @Produces("application/json")
  @RequiresAuthentication
  public JobCreateResponse createJob(JobCreateRequest jobCreateRequest) {
    try {
      return getJobService().createJob(jobCreateRequest);
    } catch (Exception e) {
      JobCreateResponse jobCreateResponse = new JobCreateResponse();
      jobCreateResponse.setException(Boolean.TRUE);
      jobCreateResponse.addMessage(e.getMessage());
      return jobCreateResponse;
    }
  }

  public JobService getJobService() {
    return jobService;
  }
}
