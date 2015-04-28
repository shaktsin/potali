package com.potaliadmin.resources.classified;

import com.potaliadmin.dto.web.request.classified.ClassifiedPostRequest;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.response.classified.ClassifiedPostResponse;
import com.potaliadmin.dto.web.response.classified.PrepareClassifiedResponse;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.newsfeed.PrepareNewsFeedResponse;
import com.potaliadmin.pact.service.classified.ClassifiedService;
import com.potaliadmin.util.rest.InputParserUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
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
  public ClassifiedPostResponse createJob(@FormDataParam("classified") FormDataBodyPart classified,
                               @FormDataParam("iFile") List<FormDataBodyPart> imgFiles,
                               @FormDataParam("jFile") List<FormDataBodyPart> jFile) {
    try {

      ClassifiedPostRequest createJobRequest = (ClassifiedPostRequest)
          InputParserUtil.parseMultiPartObject(classified.getValue(), ClassifiedPostRequest.class);



      //return getJobService().createJob(createJobRequest, imgFiles, jFile);
      return getClassifiedService().createClassifiedPost(createJobRequest, imgFiles, jFile);
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
