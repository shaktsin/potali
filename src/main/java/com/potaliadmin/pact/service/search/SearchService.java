package com.potaliadmin.pact.service.search;

import com.potaliadmin.dto.web.request.search.SearchRequest;
import com.potaliadmin.dto.web.response.search.SearchResponse;

/**
 * Created by shaktsin on 4/12/15.
 */
public interface SearchService {

  SearchResponse search(SearchRequest searchRequest);
}
