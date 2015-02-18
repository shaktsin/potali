package com.potaliadmin.resources.post;

import com.amazonaws.services.elastictranscoder.model.CreateJobRequest;
import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.constants.request.RequestConstants;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.request.jobs.JobEditRequest;
import com.potaliadmin.dto.web.request.jobs.JobSearchRequest;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;
import com.potaliadmin.dto.web.response.job.PrepareJobCreateResponse;
import com.potaliadmin.pact.service.job.JobService;
import com.potaliadmin.util.BaseUtil;
import com.potaliadmin.util.DateUtils;
import com.potaliadmin.util.rest.InputParserUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

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


  /*@POST
  @Path("/create")
  //@Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces("application/json")
  @RequiresAuthentication
  public JobResponse createJob(JobCreateRequest jobCreateRequest,
                               @FormDataParam("iFile") List<FormDataBodyPart> imgFiles,
                               @FormDataParam("jFile") FormDataBodyPart jFile) {
    try {
      return getJobService().createJob(jobCreateRequest);
    } catch (Exception e) {
      JobResponse jobResponse = new JobResponse();
      jobResponse.setException(Boolean.TRUE);
      jobResponse.addMessage(e.getMessage());
      return jobResponse;
    }
  }*/

  @POST
  @Path("/create")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces("application/json")
  @RequiresAuthentication
  public JobResponse createJob(@FormDataParam("jobs") FormDataBodyPart jobs,
                               @FormDataParam("iFile") List<FormDataBodyPart> imgFiles,
                               @FormDataParam("jFile") FormDataBodyPart jFile) {
    try {

      JobCreateRequest createJobRequest = (JobCreateRequest)
          InputParserUtil.parseMultiPartObject(jobs.getValue(), JobCreateRequest.class);



      return getJobService().createJob(createJobRequest, imgFiles, jFile);
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
    String[] circleFilterList = null;
    try {
      if (StringUtils.isNotBlank(jobSearchRequest.getLocationFilter())) {
        locationFilterList = jobSearchRequest.getLocationFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(jobSearchRequest.getRolesFilter())) {
        rolesFilterList = jobSearchRequest.getRolesFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(jobSearchRequest.getSalaryFilter())) {
        salaryFilterList = jobSearchRequest.getSalaryFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(jobSearchRequest.getExperienceFilter())) {
        experienceFilterList = jobSearchRequest.getExperienceFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(jobSearchRequest.getIndustryFilter())) {
        industryFiltersList = jobSearchRequest.getIndustryFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }
      if (StringUtils.isNotBlank(jobSearchRequest.getCircleFilter())) {
        circleFilterList = jobSearchRequest.getCircleFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }

      EnumSearchOperation enumSearchOperation = EnumSearchOperation.getById(jobSearchRequest.getOperation());
      Long postId = null;
      if (enumSearchOperation != null) {
        //date = DateUtils.convertFromString(jobSearchRequest.getPostDate());
        postId = jobSearchRequest.getPostId();
      }

      return getJobService().searchJob(BaseUtil.convertToLong(circleFilterList),BaseUtil.convertToLong(locationFilterList),BaseUtil.convertToLong(rolesFilterList),
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

  @GET
  @Path("/edit")
  @Produces("application/json")
  @RequiresAuthentication
  JobResponse editJob(JobEditRequest jobEditRequest) {
    try {
      return getJobService().editJob(jobEditRequest);
    } catch (Exception e) {
      JobResponse jobResponse = new JobResponse();
      jobResponse.setException(Boolean.TRUE);
      jobResponse.addMessage(e.getMessage());
      return jobResponse;
    }
  }



  public JobService getJobService() {
    return jobService;
  }
}
