package com.potaliadmin.resources.filters;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.wrapper.GenericResponseWrapper;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by Shakti Singh on 12/7/14.
 */
@Provider
public class ResponseWrapperFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
    String contentType = containerResponseContext.getMediaType().toString();
    if (contentType != null && contentType.equalsIgnoreCase("application/json"))  {
      GenericResponseWrapper genericResponseWrapper = new GenericResponseWrapper();
      int status = containerResponseContext.getStatus();
      if (Response.Status.UNAUTHORIZED.getStatusCode() == status) {
        GenericBaseResponse genericBaseResponse = new GenericBaseResponse();
        genericBaseResponse.setException(Boolean.TRUE);
        genericBaseResponse.addMessage((String)containerResponseContext.getEntity());
        genericResponseWrapper.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
        genericResponseWrapper.setResults(genericBaseResponse);
      } else if (Response.Status.OK.getStatusCode() == status) {
        Object entity = containerResponseContext.getEntity();
        if (entity instanceof GenericBaseResponse) {
          if (!((GenericBaseResponse) entity).isException()) {
            genericResponseWrapper.setResults(entity);
            genericResponseWrapper.setStatus(Response.Status.OK.getStatusCode());
          } else {
            GenericBaseResponse genericBaseResponse = new GenericBaseResponse();
            genericBaseResponse.setException(Boolean.TRUE);
            genericBaseResponse.addMessage(((GenericBaseResponse) entity).getMessages().get(0));
            genericResponseWrapper.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
          }
        } else {
          GenericBaseResponse genericBaseResponse = new GenericBaseResponse();
          genericBaseResponse.setException(Boolean.TRUE);
          genericBaseResponse.addMessage("Are you out of your mind, extend some super class");
          genericResponseWrapper.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
      } else {
        GenericBaseResponse genericBaseResponse = new GenericBaseResponse();
        genericBaseResponse.setException(Boolean.TRUE);
        genericBaseResponse.addMessage("Some unexpected exception occurred");
        genericResponseWrapper.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        genericResponseWrapper.setResults(genericBaseResponse);
      }
      containerResponseContext.setEntity(genericResponseWrapper);
    }
  }


}
