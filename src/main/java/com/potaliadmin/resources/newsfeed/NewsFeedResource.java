package com.potaliadmin.resources.newsfeed;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.constants.request.RequestConstants;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.request.jobs.JobEditRequest;
import com.potaliadmin.dto.web.request.jobs.JobSearchRequest;
import com.potaliadmin.dto.web.request.newsfeed.NewsFeedCreateRequest;
import com.potaliadmin.dto.web.request.newsfeed.NewsFeedEditRequest;
import com.potaliadmin.dto.web.request.newsfeed.NewsFeedSearchRequest;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;
import com.potaliadmin.dto.web.response.job.PrepareJobCreateResponse;
import com.potaliadmin.dto.web.response.newsfeed.NewsFeedSearchResponse;
import com.potaliadmin.dto.web.response.newsfeed.PrepareNewsFeedResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;
import com.potaliadmin.pact.service.post.NewsFeedService;
import com.potaliadmin.util.BaseUtil;
import com.potaliadmin.util.rest.InputParserUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by shaktsin on 3/28/15.
 */
@Path("/feed")
@Component
public class NewsFeedResource {

  @Autowired
  NewsFeedService newsFeedService;

  @GET
  @Path("/prepare")
  @Produces("application/json")
  @RequiresAuthentication
  public PrepareNewsFeedResponse prepareNewsFeed() {
    try {
      return getNewsFeedService().prepareNewsFeed();
    } catch (Exception e) {
      PrepareNewsFeedResponse prepareNewsFeedResponse = new PrepareNewsFeedResponse();
      prepareNewsFeedResponse.setException(true);
      prepareNewsFeedResponse.addMessage(e.getMessage());
      return prepareNewsFeedResponse;
    }
  }

  @POST
  @Path("/create")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces("application/json")
  @RequiresAuthentication
  public GenericPostResponse createNewsFeed(@FormDataParam("feed") FormDataBodyPart feed,
                               @FormDataParam("iFile") List<FormDataBodyPart> imgFiles,
                               @FormDataParam("jFile") List<FormDataBodyPart> jFile) {
    try {

      NewsFeedCreateRequest newsFeedCreateRequest = (NewsFeedCreateRequest)
          InputParserUtil.parseMultiPartObject(feed.getValue(), NewsFeedCreateRequest.class);



      return getNewsFeedService().createNewsFeed(newsFeedCreateRequest, imgFiles, jFile);
    } catch (Exception e) {
      GenericPostResponse postResponse = new GenericPostResponse();
      postResponse.setException(Boolean.TRUE);
      postResponse.addMessage(e.getMessage());
      return postResponse;
    }
  }

  @GET
  @Path("/{id}")
  @Produces("application/json")
  @RequiresAuthentication
  public GenericPostResponse getNewsFeed(@PathParam(RequestConstants.ID) Long id) {
    try {
      return getNewsFeedService().getNewsFeed(id);
    } catch (Exception e) {
      GenericPostResponse postResponse = new GenericPostResponse();
      postResponse.setException(Boolean.TRUE);
      postResponse.addMessage(e.getMessage());
      return postResponse;
    }
  }

  @POST
  @Path("/edit")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces("application/json")
  @RequiresAuthentication
  public GenericPostResponse editNewsFeed(@FormDataParam("feed") FormDataBodyPart feed,
                             @FormDataParam("iFile") List<FormDataBodyPart> imgFiles,
                             @FormDataParam("jFile") List<FormDataBodyPart> jFiles
  ) {
    try {
      NewsFeedEditRequest newsFeedEditRequest = (NewsFeedEditRequest)
          InputParserUtil.parseMultiPartObject(feed.getValue(), NewsFeedEditRequest.class);
      return getNewsFeedService().editNewsFeed(newsFeedEditRequest, imgFiles, jFiles);
    } catch (Exception e) {
      GenericPostResponse postResponse = new GenericPostResponse();
      postResponse.setException(Boolean.TRUE);
      postResponse.addMessage(e.getMessage());
      return postResponse;
    }
  }


  @POST
  @Path("/list")
  @Produces("application/json")
  @RequiresAuthentication
  public NewsFeedSearchResponse getNewsFeeds(NewsFeedSearchRequest newsFeedSearchRequest){


    String[] circleFilterList = null;
    try {

      if (StringUtils.isNotBlank(newsFeedSearchRequest.getCircleFilter())) {
        circleFilterList = newsFeedSearchRequest.getCircleFilter().split(DefaultConstants.REQUEST_SEPARATOR);
      }

      EnumSearchOperation enumSearchOperation = EnumSearchOperation.getById(newsFeedSearchRequest.getOperation());
      Long postId = null;
      if (enumSearchOperation != null) {
        //date = DateUtils.convertFromString(jobSearchRequest.getPostDate());
        postId = newsFeedSearchRequest.getPostId();
      }

      return getNewsFeedService().searchNewsFeed(BaseUtil.convertToLong(circleFilterList),
          enumSearchOperation, postId, newsFeedSearchRequest.getPerPage(), newsFeedSearchRequest.getPageNo());




    } catch (Exception e) {
      NewsFeedSearchResponse newsFeedSearchResponse = new NewsFeedSearchResponse();
      newsFeedSearchResponse.setException(true);
      newsFeedSearchResponse.addMessage("Some internal exception occurred");
      return newsFeedSearchResponse;
    }
  }





  public NewsFeedService getNewsFeedService() {
    return newsFeedService;
  }
}
