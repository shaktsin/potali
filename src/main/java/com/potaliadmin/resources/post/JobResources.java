package com.potaliadmin.resources.post;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.constants.request.RequestConstants;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.request.jobs.JobSearchRequest;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;
import com.potaliadmin.dto.web.response.job.PrepareJobCreateResponse;
import com.potaliadmin.pact.service.job.JobService;
import com.potaliadmin.util.BaseUtil;
import com.potaliadmin.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by Shakti Singh on 12/16/14.
 */
@Path("/jobs")
@Component
public class JobResources {

  @Autowired
  JobService jobService;


  @GET
  @Path("/prepare")
  @Produces("application/json")
  @RequiresAuthentication
  public PrepareJobCreateResponse prepareJobCreate() {
    try {
      return getJobService().prepareJobCreateRequest();
    } catch (Exception e) {
      PrepareJobCreateResponse prepareJobCreateResponse = new PrepareJobCreateResponse();
      prepareJobCreateResponse.setException(true);
      prepareJobCreateResponse.addMessage(e.getMessage());
      return prepareJobCreateResponse;
    }
  }


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


  @POST
  @Path("/list")
  @Produces("application/json")
  @RequiresAuthentication
  public JobSearchResponse getJobs(JobSearchRequest jobSearchRequest){

    String[] locationFilterList=null;
    String[] industryFiltersList = null;
    String[] rolesFilterList=null;
    String[] salaryFilterList=null;
    String[] experienceFilterList=null;
    try {
      if (StringUtils.isNotBlank(jobSearchRequest.getLocationFilter())) {
        locationFilterList = jobSearchRequest.getLocationFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(jobSearchRequest.getRolesFilter())) {
        rolesFilterList = jobSearchRequest.getLocationFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(jobSearchRequest.getSalaryFilter())) {
        salaryFilterList = jobSearchRequest.getSalaryFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(jobSearchRequest.getExperienceFilter())) {
        experienceFilterList = jobSearchRequest.getExperienceFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(jobSearchRequest.getIndustryFilter())) {
        industryFiltersList = jobSearchRequest.getExperienceFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }

      EnumSearchOperation enumSearchOperation = EnumSearchOperation.getById(jobSearchRequest.getOperation());
      Long postId = null;
      if (enumSearchOperation != null) {
        //date = DateUtils.convertFromString(jobSearchRequest.getPostDate());
        postId = jobSearchRequest.getPostId();
      }

      return getJobService().searchJob(BaseUtil.convertToLong(locationFilterList),BaseUtil.convertToLong(rolesFilterList),
                                        BaseUtil.convertToLong(industryFiltersList),BaseUtil.convertToDouble(salaryFilterList),
                                        BaseUtil.convertToInteger(experienceFilterList),enumSearchOperation, postId,jobSearchRequest.getPerPage(),jobSearchRequest.getPageNo());




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
