package com.potaliadmin.resources.filters;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.exceptions.UnAuthorizedAccessException;
import com.potaliadmin.security.SecurityToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Shakti Singh on 12/16/14.
 */
@Provider
public class SecurityRequestFilter implements ContainerRequestFilter {


  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    String authorization = containerRequestContext.getHeaderString("Authorization");
    try {
      String[] credentials = stripCredentials(authorization);
      if (credentials != null) {
        // do authentication
        if (!credentials[1].equalsIgnoreCase(DefaultConstants.APP_NAME)) {
          throw new UnAuthorizedAccessException("Access Denied!");
        }
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(credentials[0], credentials[3]);
        //usernamePasswordToken.setRememberMe(true);
        SecurityUtils.getSubject().login(usernamePasswordToken);
      }
    } catch (Exception e) {
      containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Login Required!").build());

    }
  }

  private String[] stripCredentials(String authorization) throws Exception {
    if (authorization != null) {
      return SecurityToken.getCredentials(authorization);
    } else {
      return null;
    }
  }
}
