package com.potaliadmin.dto.internal.filter;

import com.potaliadmin.dto.web.response.circle.CircleDto;

import java.util.List;

/**
 * Created by shakti on 31/1/15.
 */
public class GeneralFilterDto extends BaseFilterDto {

  private List<CircleDto> circleDtoList;

  public GeneralFilterDto(String name) {
    super(name);
  }

  public List<CircleDto> getCircleDtoList() {
    return circleDtoList;
  }

  public void setCircleDtoList(List<CircleDto> circleDtoList) {
    this.circleDtoList = circleDtoList;
  }
}
