package com.potaliadmin.dto.web.response.circle;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.user.UserDto;

import java.util.List;

/**
 * Created by shakti on 6/3/15.
 */
public class CircleRequestListResponse extends GenericBaseResponse {

  List<UserDto> userDtoList;
  private int pageNo;
  private int perPage;

  public List<UserDto> getUserDtoList() {
    return userDtoList;
  }

  public void setUserDtoList(List<UserDto> userDtoList) {
    this.userDtoList = userDtoList;
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
}
