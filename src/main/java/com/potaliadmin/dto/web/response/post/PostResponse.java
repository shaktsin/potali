package com.potaliadmin.dto.web.response.post;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

import java.util.List;

/**
 * Created by Shakti Singh on 1/9/15.
 */
public class PostResponse extends GenericBaseResponse {

  private List<GenericPostResponse> posts;
  private long totalResults;
  private long pageNo;
  private long perPage;

  public List<GenericPostResponse> getPosts() {
    return posts;
  }

  public void setPosts(List<GenericPostResponse> posts) {
    this.posts = posts;
  }

  public long getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(long totalResults) {
    this.totalResults = totalResults;
  }

  public long getPageNo() {
    return pageNo;
  }

  public void setPageNo(long pageNo) {
    this.pageNo = pageNo;
  }

  public long getPerPage() {
    return perPage;
  }

  public void setPerPage(long perPage) {
    this.perPage = perPage;
  }
}
