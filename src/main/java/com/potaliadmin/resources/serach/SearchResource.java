package com.potaliadmin.resources.serach;

import com.potaliadmin.dto.web.request.search.SearchRequest;
import com.potaliadmin.dto.web.response.search.SearchResponse;
import com.potaliadmin.pact.service.search.SearchService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by shaktsin on 4/12/15.
 */
@Path("/search")
@Component
public class SearchResource {

  @Autowired
  SearchService searchService;


  @POST
  @Path("/all")
  @Produces("application/json")
  @RequiresAuthentication
  public SearchResponse search(SearchRequest searchRequest) {


    try {
      return getSearchService().search(searchRequest);

    } catch (Exception e) {
      SearchResponse searchResponse = new SearchResponse();
      searchResponse.setException(Boolean.TRUE);
      searchResponse.addMessage(e.getMessage());
      return searchResponse;
    }

  }

  public SearchService getSearchService() {
    return searchService;
  }
}
