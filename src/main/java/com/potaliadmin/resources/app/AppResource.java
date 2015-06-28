package com.potaliadmin.resources.app;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.dto.web.request.app.UpdateRequest;
import com.potaliadmin.dto.web.response.app.UpdateResponse;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by shaktsin on 6/28/15.
 */
@Path("/app")
@Component
public class AppResource {


  @POST
  @Path("/update")
  @Produces("application/json")
  @RequiresAuthentication
  public UpdateResponse updateApp(UpdateRequest updateRequest) {
    UpdateResponse updateResponse = new UpdateResponse();
    updateResponse.setAppUpdate(DefaultConstants.FORCE_UPDATE);
    updateResponse.setUpdateTitle(DefaultConstants.UPDATE_TITLE);
    updateResponse.setUpdateMessage(DefaultConstants.UPDATE_MESSAGE);
    if (updateRequest.getAppVersion() != null &&
        !DefaultConstants.APP_VERSION.equals(updateRequest.getAppVersion())) {
      updateResponse.setAppUpdate(true);
    } else {
      updateResponse.setAppUpdate(false);
    }
    return updateResponse;
  }

}
