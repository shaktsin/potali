package com.potaliadmin.pact.service.post;

import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.dto.web.request.newsfeed.NewsFeedCreateRequest;
import com.potaliadmin.dto.web.request.newsfeed.NewsFeedEditRequest;
import com.potaliadmin.dto.web.response.newsfeed.NewsFeedSearchResponse;
import com.potaliadmin.dto.web.response.newsfeed.PrepareNewsFeedResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.util.List;

/**
 * Created by shaktsin on 3/11/15.
 */
public interface NewsFeedService {

  PrepareNewsFeedResponse prepareNewsFeed();

  GenericPostResponse createNewsFeed(NewsFeedCreateRequest newsFeedCreateRequest,
                                     List<FormDataBodyPart> imgFiles,
                                     List<FormDataBodyPart> jFiles);


  GenericPostResponse getNewsFeed(Long postId);

  GenericPostResponse editNewsFeed(NewsFeedEditRequest newsFeedEditRequest,
                                   List<FormDataBodyPart> imgFiles,
                                   List<FormDataBodyPart> jFiles);

  public NewsFeedSearchResponse searchNewsFeed(Long[] circleList, EnumSearchOperation searchOperation,
                                               Long postId, int perPage, int pageNo);


}
