package com.potaliadmin.pact.service.classified;

import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.dto.web.request.classified.ClassifiedEditRequest;
import com.potaliadmin.dto.web.request.classified.ClassifiedPostRequest;
import com.potaliadmin.dto.web.response.classified.ClassifiedPostResponse;
import com.potaliadmin.dto.web.response.classified.ClassifiedSearchResponse;
import com.potaliadmin.dto.web.response.classified.PrepareClassifiedResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.util.List;

/**
 * Created by shaktsin on 4/5/15.
 */
public interface ClassifiedService {

  PrepareClassifiedResponse prepareClassifiedResponse();

  ClassifiedPostResponse createClassifiedPost(ClassifiedPostRequest classifiedPostRequest,
                                     List<FormDataBodyPart> imgFiles,
                                     List<FormDataBodyPart> jFiles);

  ClassifiedPostResponse editClassifiedPost(ClassifiedEditRequest classifiedPostRequest,
                                              List<FormDataBodyPart> imgFiles,
                                              List<FormDataBodyPart> jFiles);

  ClassifiedSearchResponse searchClassified(Long[] circleList,Long[] locationList, Long[] primaryCatList,
                                                   Long[] secondaryCatList,EnumSearchOperation searchOperation,
                                                   Long postId, int perPage, int pageNo);


  ClassifiedPostResponse getClassified(Long id);
}
