package com.potaliadmin.dto.web.response.post;

import com.potaliadmin.dto.web.response.user.UserDto;

/**
 * Created by shaktsin on 3/8/15.
 */
public class UserPostResponse extends PostResponse {

  long totalCircles;

  public long getTotalCircles() {
    return totalCircles;
  }

  public void setTotalCircles(long totalCircles) {
    this.totalCircles = totalCircles;
  }

}
