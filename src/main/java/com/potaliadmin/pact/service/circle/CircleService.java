package com.potaliadmin.pact.service.circle;

import com.potaliadmin.dto.web.request.circle.CircleCreateRequest;
import com.potaliadmin.dto.web.request.circle.CircleJoinRequest;
import com.potaliadmin.dto.web.response.base.GenericSuccessResponse;
import com.potaliadmin.dto.web.response.circle.CreateCircleResponse;

/**
 * Created by shakti on 28/1/15.
 */
public interface CircleService {

  CreateCircleResponse createCircle(CircleCreateRequest circleCreateRequest);

  GenericSuccessResponse joinCircle(CircleJoinRequest circleJoinRequest);
}
