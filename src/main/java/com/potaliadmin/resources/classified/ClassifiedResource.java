package com.potaliadmin.resources.classified;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.constants.request.RequestConstants;
import com.potaliadmin.dto.web.request.classified.ClassifiedEditRequest;
import com.potaliadmin.dto.web.request.classified.ClassifiedPostRequest;
import com.potaliadmin.dto.web.request.classified.ClassifiedSearchRequest;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.request.jobs.JobEditRequest;
import com.potaliadmin.dto.web.response.classified.ClassifiedPostResponse;
import com.potaliadmin.dto.web.response.classified.ClassifiedSearchResponse;
import com.potaliadmin.dto.web.response.classified.PrepareClassifiedResponse;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.newsfeed.PrepareNewsFeedResponse;
import com.potaliadmin.pact.service.classified.ClassifiedService;
import com.potaliadmin.util.BaseUtil;
import com.potaliadmin.util.rest.InputParserUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by shaktsin on 4/28/15.
 */
@Path("/classified")
@Component
public class ClassifiedResource {

  private Logger logger = LoggerFactory.getLogger(ClassifiedResource.class);

  @Autowired
  ClassifiedService classifiedService;

  @GET
  @Path("/prepare")
  @Produces("application/json")
  @RequiresAuthentication
  public PrepareClassifiedResponse prepareClassified() {
    try {
      return getClassifiedService().prepareClassifiedResponse();
    } catch (Exception e) {
      PrepareClassifiedResponse prepareClassifiedResponse = new PrepareClassifiedResponse();
      prepareClassifiedResponse.setException(true);
      prepareClassifiedResponse.addMessage(e.getMessage());
      return prepareClassifiedResponse;
    }
  }

  @POST
  @Path("/create")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces("application/json")
  @RequiresAuthentication
  public ClassifiedPostResponse createClassified(@FormDataParam("classified") FormDataBodyPart classified,
                               @FormDataParam("iFile") List<FormDataBodyPart> imgFiles,
                               @FormDataParam("jFile") List<FormDataBodyPart> jFile) {
    try {

      ClassifiedPostRequest createJobRequest = (ClassifiedPostRequest)
          InputParserUtil.parseMultiPartObject(classified.getValue(), ClassifiedPostRequest.class);



      //return getJobService().createJob(createJobRequest, imgFiles, jFile);
      return getClassifiedService().createClassifiedPost(createJobRequest, imgFiles, jFile);
    } catch (Exception e) {
      logger.error("Error while creating classified ",e);
      ClassifiedPostResponse classifiedPostResponse = new ClassifiedPostResponse();
      classifiedPostResponse.setException(Boolean.TRUE);
      classifiedPostResponse.addMessage(e.getMessage());
      return classifiedPostResponse;
    }
  }

  @GET
  @Path("/{id}")
  @Produces("application/json")
  @RequiresAuthentication
  public ClassifiedPostResponse getClassified(@PathParam(RequestConstants.ID) Long id) {
    try {
      return getClassifiedService().getClassified(id);
    } catch (Exception e) {
      ClassifiedPostResponse classifiedPostResponse = new ClassifiedPostResponse();
      classifiedPostResponse.setException(Boolean.TRUE);
      classifiedPostResponse.addMessage(e.getMessage());
      return classifiedPostResponse;
    }
  }


  @POST
  @Path("/list")
  @Produces("application/json")
  @RequiresAuthentication
  public ClassifiedSearchResponse getJobs(ClassifiedSearchRequest classifiedSearchRequest){

    String[] locationFilterList=null;
    String[] primaryCatList = null;
    String[] secondaryCatList=null;
    String[] salaryFilterList=null;
    String[] experienceFilterList=null;
    String[] circleFilterList = null;
    try {
      if (StringUtils.isNotBlank(classifiedSearchRequest.getLocationFilter())) {
        locationFilterList = classifiedSearchRequest.getLocationFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }

      if (StringUtils.isNotBlank(classifiedSearchRequest.getPrimaryCatFilter())) {
        primaryCatList = classifiedSearchRequest.getPrimaryCatFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }

      if (StringUtils.isNotBlank(classifiedSearchRequest.getSecondaryCatFilter())) {
        secondaryCatList = classifiedSearchRequest.getSecondaryCatFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }

      if (StringUtils.isNotBlank(classifiedSearchRequest.getCircleFilter())) {
        circleFilterList = classifiedSearchRequest.getCircleFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }

      EnumSearchOperation enumSearchOperation = EnumSearchOperation.getById(classifiedSearchRequest.getOperation());
      Long postId = null;
      if (enumSearchOperation != null) {
        //date = DateUtils.convertFromString(jobSearchRequest.getPostDate());
        postId = classifiedSearchRequest.getPostId();
      }

      return getClassifiedService().searchClassified(BaseUtil.convertToLong(circleFilterList),
          BaseUtil.convertToLong(locationFilterList), BaseUtil.convertToLong(primaryCatList),
          BaseUtil.convertToLong(secondaryCatList), enumSearchOperation, postId, classifiedSearchRequest.getPerPage(),
          classifiedSearchRequest.getPageNo());




    } catch (Exception e) {
      ClassifiedSearchResponse classifiedSearchResponse = new ClassifiedSearchResponse();
      classifiedSearchResponse.setException(true);
      classifiedSearchResponse.addMessage("Some internal exception occurred");
      return classifiedSearchResponse;
    }
  }

  @POST
  @Path("/edit")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces("application/json")
  @RequiresAuthentication
  public ClassifiedPostResponse editJob(@FormDataParam("classified") FormDataBodyPart classified,
                             @FormDataParam("iFile") List<FormDataBodyPart> imgFiles,
                             @FormDataParam("jFile") List<FormDataBodyPart> jFiles
  ) {
    try {
      ClassifiedEditRequest classifiedEditRequest = (ClassifiedEditRequest)
          InputParserUtil.parseMultiPartObject(classified.getValue(), ClassifiedEditRequest.class);
      return getClassifiedService().editClassifiedPost(classifiedEditRequest, imgFiles, jFiles);
    } catch (Exception e) {
      ClassifiedPostResponse classifiedPostResponse = new ClassifiedPostResponse();
      classifiedPostResponse.setException(Boolean.TRUE);
      classifiedPostResponse.addMessage(e.getMessage());
      return classifiedPostResponse;
    }
  }


  public ClassifiedService getClassifiedService() {
    return classifiedService;
  }
}
