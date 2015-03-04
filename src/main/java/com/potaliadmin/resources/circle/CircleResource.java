package com.potaliadmin.resources.circle;

import com.potaliadmin.dto.web.request.circle.CircleAuthorizeRequest;
import com.potaliadmin.dto.web.request.circle.CircleCreateRequest;
import com.potaliadmin.dto.web.request.circle.CircleGetRequest;
import com.potaliadmin.dto.web.request.circle.CircleJoinRequest;
import com.potaliadmin.dto.web.response.base.GenericSuccessResponse;
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
  public GenericSuccessResponse getAllCircles(CircleGetRequest circleGetRequest) {
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


  public CircleService getCircleService() {
    return circleService;
  }


}
