package com.potaliadmin.pact.service.circle;

import com.potaliadmin.dto.web.request.circle.*;
import com.potaliadmin.dto.web.response.base.GenericSuccessResponse;
import com.potaliadmin.dto.web.response.circle.CircleGetResponse;
import com.potaliadmin.dto.web.response.circle.CircleRequestListResponse;
import com.potaliadmin.dto.web.response.circle.CreateCircleResponse;

/**
 * Created by shakti on 28/1/15.
 */
public interface CircleService {

  CreateCircleResponse createCircle(CircleCreateRequest circleCreateRequest);

  GenericSuccessResponse joinCircle(CircleJoinRequest circleJoinRequest);

  GenericSuccessResponse authorizeCircle(CircleAuthorizeRequest circleAuthorizeRequest);

  GenericSuccessResponse authorizeRevokeCircle(CircleAuthorizeRequest circleAuthorizeRequest);

  CircleGetResponse fetchAllCircle(CircleGetRequest circleGetRequest);

  CircleRequestListResponse fetchAllRequest(CircleJoinListRequest circleJoinRequest);

  GenericSuccessResponse unJoinCircle(CircleJoinRequest circleJoinRequest);

  GenericSuccessResponse deactivateCircle(CircleJoinRequest circleJoinRequest);

  GenericSuccessResponse activateCircle(CircleJoinRequest circleJoinRequest);

  CircleGetResponse getUsersCircle(CircleGetRequest circleGetRequest);
}
