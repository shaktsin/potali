package com.potaliadmin.pact.service.classified;

import com.potaliadmin.dto.web.request.classified.ClassifiedPostRequest;
import com.potaliadmin.dto.web.response.classified.PrepareClassifiedResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.util.List;

/**
 * Created by shaktsin on 4/5/15.
 */
public interface ClassifiedService {

  PrepareClassifiedResponse prepareClassifiedResponse();

  GenericPostResponse createClassifiedPost(ClassifiedPostRequest classifiedPostRequest,
                                     List<FormDataBodyPart> imgFiles,
                                     List<FormDataBodyPart> jFiles);
}
