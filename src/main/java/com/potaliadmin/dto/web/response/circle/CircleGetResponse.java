package com.potaliadmin.dto.web.response.circle;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

import java.util.List;

/**
 * Created by shakti on 4/3/15.
 */
public class CircleGetResponse extends GenericBaseResponse {

  List<CircleDto> circleDtoList;
  private int pageNo;
  private int perPage;

  public List<CircleDto> getCircleDtoList() {
    return circleDtoList;
  }

  public void setCircleDtoList(List<CircleDto> circleDtoList) {
    this.circleDtoList = circleDtoList;
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
