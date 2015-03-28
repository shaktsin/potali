package com.potaliadmin.dto.web.response.newsfeed;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.circle.CircleDto;

import java.util.List;

/**
 * Created by shaktsin on 3/28/15.
 */
public class PrepareNewsFeedResponse extends GenericBaseResponse {

  private List<CircleDto> circleDtoList;

  public List<CircleDto> getCircleDtoList() {
    return circleDtoList;
  }

  public void setCircleDtoList(List<CircleDto> circleDtoList) {
    this.circleDtoList = circleDtoList;
  }
}
