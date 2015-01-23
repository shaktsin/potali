package com.potaliadmin.dto.web.response.post;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by shakti on 18/1/15.
 */
public class CommentListResponse extends GenericBaseResponse {

  private List<CommentResponse> commentResponseList;
  private long totalResults;
  private int pageNo;
  private int perPage;


  public List<CommentResponse> getCommentResponseList() {
    return commentResponseList;
  }

  public void setCommentResponseList(List<CommentResponse> commentResponseList) {
    this.commentResponseList = commentResponseList;
  }

  public long getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(long totalResults) {
    this.totalResults = totalResults;
  }

  public int getPageNo() {
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  public int getPerPage() {
    return perPage;
  }

  public void setPerPage(int perPage) {
    this.perPage = perPage;
  }

  public void setReorderCommentResponse(List<CommentResponse> commentResponseList) {
    Collections.sort(commentResponseList, new Comparator<CommentResponse>() {
      @Override
      public int compare(CommentResponse o1, CommentResponse o2) {
        return o2.getPostId().intValue() - o1.getPostId().intValue();
      }
    });
    this.commentResponseList = commentResponseList;
  }
}
