package com.potaliadmin.resources.circle;

import com.potaliadmin.dto.web.request.circle.*;
import com.potaliadmin.dto.web.response.base.GenericSuccessResponse;
import com.potaliadmin.dto.web.response.circle.CircleGetResponse;
import com.potaliadmin.dto.web.response.circle.CircleRequestListResponse;
import com.potaliadmin.dto.web.response.circle.CreateCircleResponse;
import com.potaliadmin.pact.service.circle.CircleService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by shakti on 28/1/15.
 */
@Path("/circle")
@Component
public class CircleResource {

  @Autowired
  CircleService circleService;

  @POST
  @Path("/create")
  @Produces("application/json")
  @RequiresAuthentication
  public CreateCircleResponse createCircle(CircleCreateRequest circleCreateRequest) {
    try {
      return getCircleService().createCircle(circleCreateRequest);
    } catch (Exception e) {
      CreateCircleResponse createCircleResponse = new CreateCircleResponse();
      createCircleResponse.setException(true);
      createCircleResponse.addMessage(e.getMessage());
      return createCircleResponse;
    }
  }

  @POST
  @Path("/join")
  @Produces("application/json")
  @RequiresAuthentication
  public GenericSuccessResponse joinCircle(CircleJoinRequest circleJoinRequest) {
    try {
      return getCircleService().joinCircle(circleJoinRequest);
    } catch (Exception e) {
      GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
      genericSuccessResponse.setException(true);
      genericSuccessResponse.addMessage(e.getMessage());
      return genericSuccessResponse;
    }
  }

  @POST
  @Path("/all")
  @Produces("application/json")
  @RequiresAuthentication
  public CircleGetResponse getAllCircles(CircleGetRequest circleGetRequest) {
    try {
      return getCircleService().fetchAllCircle(circleGetRequest);
    } catch (Exception e) {
      CircleGetResponse circleGetResponse = new CircleGetResponse();
      circleGetResponse.setException(true);
      circleGetResponse.addMessage(e.getMessage());
      return circleGetResponse;
    }
  }

  @POST
  @Path("/user/all")
  @Produces("application/json")
  @RequiresAuthentication
  public CircleGetResponse getUserAllCircles(CircleGetRequest circleGetRequest) {
    try {
      return getCircleService().getUsersCircle(circleGetRequest);
    } catch (Exception e) {
      CircleGetResponse circleGetResponse = new CircleGetResponse();
      circleGetResponse.setException(true);
      circleGetResponse.addMessage(e.getMessage());
      return circleGetResponse;
    }
  }

  @POST
  @Path("/requests")
  @Produces("application/json")
  @RequiresAuthentication
  public CircleRequestListResponse getAllCirclesRequests(CircleJoinListRequest circleJoinRequest) {
    try {
      return getCircleService().fetchAllRequest(circleJoinRequest);
    } catch (Exception e) {
      CircleRequestListResponse circleGetResponse = new CircleRequestListResponse();
      circleGetResponse.setException(true);
      circleGetResponse.addMessage(e.getMessage());
      return circleGetResponse;
    }
  }

  @POST
  @Path("/authorize")
  @Produces("application/json")
  @RequiresAuthentication
  public GenericSuccessResponse authorizeCircle(CircleAuthorizeRequest circleAuthorizeRequest) {
    try {
      return getCircleService().authorizeCircle(circleAuthorizeRequest);
    } catch (Exception e) {
      GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
      genericSuccessResponse.setException(true);
      genericSuccessResponse.addMessage(e.getMessage());
      return genericSuccessResponse;
    }
  }

  @POST
  @Path("/authorize/revoke")
  @Produces("application/json")
  @RequiresAuthentication
  public GenericSuccessResponse authorizeRevokeCircle(CircleAuthorizeRequest circleAuthorizeRequest) {
    try {
      return getCircleService().authorizeRevokeCircle(circleAuthorizeRequest);
    } catch (Exception e) {
      GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
      genericSuccessResponse.setException(true);
      genericSuccessResponse.addMessage(e.getMessage());
      return genericSuccessResponse;
    }
  }

  @POST
  @Path("/unjoin")
  @Produces("application/json")
  @RequiresAuthentication
  public GenericSuccessResponse unJoinCircle(CircleJoinRequest circleJoinRequest) {
    try {
      return getCircleService().unJoinCircle(circleJoinRequest);
    } catch (Exception e) {
      GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
      genericSuccessResponse.setException(true);
      genericSuccessResponse.addMessage(e.getMessage());
      return genericSuccessResponse;
    }
  }

  @POST
  @Path("/deactivate")
  @Produces("application/json")
  @RequiresAuthentication
  public GenericSuccessResponse deactivate(CircleJoinRequest circleJoinRequest) {
    try {
      return getCircleService().deactivateCircle(circleJoinRequest);
    } catch (Exception e) {
      GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
      genericSuccessResponse.setException(true);
      genericSuccessResponse.addMessage(e.getMessage());
      return genericSuccessResponse;
    }
  }

  public CircleService getCircleService() {
    return circleService;
  }


}
