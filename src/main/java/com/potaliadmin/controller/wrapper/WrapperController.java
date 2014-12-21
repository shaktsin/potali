package com.potaliadmin.controller.wrapper;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.wrapper.GenericResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;

/**
 * Created by Shakti Singh on 12/15/14.
 */
@RestController
@RequestMapping("/rest/wrapper")
public class WrapperController {

  @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON)
  public GenericResponseWrapper wrapResponse(Object object) {
    /*if (object instanceof GenericBaseResponse) {
      return new GenericResponseWrapper(HttpStatus.OK.value(), object);
    } else {
      GenericBaseResponse genericBaseResponse = new GenericBaseResponse();
      genericBaseResponse.setException(Boolean.TRUE);
      genericBaseResponse.addMessage("You should extend super class in responses ");
      return new GenericResponseWrapper(HttpStatus.INTERNAL_SERVER_ERROR.value(), genericBaseResponse);
    }*/
    return null;
  }
}
