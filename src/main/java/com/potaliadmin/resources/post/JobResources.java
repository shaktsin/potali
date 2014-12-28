package com.potaliadmin.resources.post;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.request.RequestConstants;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;
import com.potaliadmin.pact.service.job.JobService;
import com.potaliadmin.util.BaseUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

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
  public JobResponse createJob(JobCreateRequest jobCreateRequest) {
    try {
      return getJobService().createJob(jobCreateRequest);
    } catch (Exception e) {
      JobResponse jobResponse = new JobResponse();
      jobResponse.setException(Boolean.TRUE);
      jobResponse.addMessage(e.getMessage());
      return jobResponse;
    }
  }

  @GET
  @Path("/{id}")
  @Produces("application/json")
  @RequiresAuthentication
  public JobResponse getJob(@PathParam(RequestConstants.ID) Long id) {
    try {
      return getJobService().getJob(id);
    } catch (Exception e) {
      JobResponse jobResponse = new JobResponse();
      jobResponse.setException(Boolean.TRUE);
      jobResponse.addMessage(e.getMessage());
      return jobResponse;
    }
  }


  @GET
  @Path("/list")
  @Produces("application/json")
  @RequiresAuthentication
  public JobSearchResponse getJobs(@QueryParam(RequestConstants.LOCATION) String locationFilter,
                      @QueryParam(RequestConstants.INDUSTRY) String industryFilter,
                      @QueryParam(RequestConstants.ROLES) String rolesFilter,
                      @QueryParam(RequestConstants.SALARY) String salaryFilter,
                      @QueryParam(RequestConstants.EXP) String experienceFilter,
                      @QueryParam(RequestConstants.PER_PAGE) @DefaultValue(DefaultConstants.AND_APP_PER_PAGE)int perPage,
                      @QueryParam(RequestConstants.PAGE_NO) @DefaultValue(DefaultConstants.AND_APP_PAGE_NO)int pageNo,
                      @QueryParam(RequestConstants.PLATE_FORM) @DefaultValue(DefaultConstants.PLATE_FROM) Long plateFormId) {

    String[] locationFilterList=null;
    String[] industryFiltersList = null;
    String[] rolesFilterList=null;
    String[] salaryFilterList=null;
    String[] experienceFilterList=null;
    try {
      if (StringUtils.isNotBlank(locationFilter)) {
        locationFilterList = locationFilter.split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(rolesFilter)) {
        rolesFilterList = rolesFilter.split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(salaryFilter)) {
        salaryFilterList = salaryFilter.split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(experienceFilter)) {
        experienceFilterList = experienceFilter.split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(industryFilter)) {
        industryFiltersList = industryFilter.split(DefaultConstants.REQUEST_SEPARATOR);
      }

      return getJobService().searchJob(BaseUtil.convertToLong(locationFilterList),BaseUtil.convertToLong(rolesFilterList),
                                        BaseUtil.convertToLong(industryFiltersList),BaseUtil.convertToDouble(salaryFilterList),
                                        BaseUtil.convertToInteger(experienceFilterList),perPage, pageNo);




    } catch (Exception e) {
      JobSearchResponse jobSearchResponse = new JobSearchResponse();
      jobSearchResponse.setException(true);
      jobSearchResponse.addMessage("Some internal exception occurred");
      return jobSearchResponse;
    }
  }

  /*@POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public void uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
                         @FormDataParam("file") FormDataContentDisposition fileDetail) {



  }*/





  public JobService getJobService() {
    return jobService;
  }
}
