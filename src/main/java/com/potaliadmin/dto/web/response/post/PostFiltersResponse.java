package com.potaliadmin.dto.web.response.post;

import com.potaliadmin.dto.internal.filter.BaseFilterDto;
import com.potaliadmin.dto.internal.filter.GeneralFilterDto;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

import java.util.Map;

/**
 * Created by shakti on 31/1/15.
 */
public class PostFiltersResponse extends GenericBaseResponse {

  private GeneralFilterDto genFilterDto;

  private Map<String, BaseFilterDto> specificFilterMap;

  public GeneralFilterDto getGenFilterDto() {
    return genFilterDto;
  }

  public void setGenFilterDto(GeneralFilterDto genFilterDto) {
    this.genFilterDto = genFilterDto;
  }

  public Map<String, BaseFilterDto> getSpecificFilterMap() {
    return specificFilterMap;
  }

  public void setSpecificFilterMap(Map<String, BaseFilterDto> specificFilterMap) {
    this.specificFilterMap = specificFilterMap;
  }
}
