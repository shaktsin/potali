package com.potaliadmin.resources.user;

import com.potaliadmin.dto.web.request.user.UserSignUpRequest;
import com.potaliadmin.dto.web.response.user.UserResourceResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.pact.service.users.LoginService;
import com.potaliadmin.security.SecurityToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by Shakti Singh on 12/20/14.
 */
@Path("/user")
@Component
public class UserResource {

  @Autowired
  LoginService loginService;

  @POST
  @Path("/signUp")
  @Produces("application/json")
  public UserResourceResponse signUp(UserSignUpRequest userSignUpRequest) {
    try {
      UserResponse userResponse = getLoginService().signUp(userSignUpRequest);
      if (!userResponse.isException()) {
        UserResourceResponse userResourceResponse = new UserResourceResponse();
        userResourceResponse.setName(userResponse.getName());
        userResourceResponse.setEmail(userResponse.getEmail());
        userResourceResponse.setAuthToken(SecurityToken.getSecurityToken(userResponse.getEmail(), userSignUpRequest.getPassword(), userSignUpRequest.getInstituteId()));
        return userResourceResponse;
      } else {
        UserResourceResponse userResourceResponse = new UserResourceResponse();
        userResourceResponse.setException(Boolean.TRUE);
        userResourceResponse.setMessages(userResponse.getMessages());
        return userResourceResponse;

      }

    } catch (Exception e) {
      UserResourceResponse userResourceResponse = new UserResourceResponse();
      userResourceResponse.setException(Boolean.TRUE);
      userResourceResponse.addMessage(e.getMessage());
      return userResourceResponse;
    }
  }


  @POST
  @Path("/login")
  @Produces("application/json")
  public UserResourceResponse login(@FormParam("email") String email,
                                    @FormParam("password")String password) {
    try {
      UserResponse userResponse = getLoginService().login(email, password);
      if (!userResponse.isException()) {
        UserResourceResponse userResourceResponse = new UserResourceResponse();
        userResourceResponse.setName(userResponse.getName());
        userResourceResponse.setEmail(userResponse.getEmail());
        userResourceResponse.setAuthToken(SecurityToken.getSecurityToken(email, password, userResponse.getInstituteId()));
        return userResourceResponse;
      } else {
        UserResourceResponse userResourceResponse = new UserResourceResponse();
        userResourceResponse.setException(Boolean.TRUE);
        userResourceResponse.addMessage(userResponse.getMessages().get(0));
        return userResourceResponse;
      }

    } catch (Exception e) {
      UserResourceResponse userResourceResponse = new UserResourceResponse();
      userResourceResponse.setException(Boolean.TRUE);
      userResourceResponse.addMessage(e.getMessage());
      return userResourceResponse;
    }

  }

  public LoginService getLoginService() {
    return loginService;
  }
}
