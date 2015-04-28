package com.potaliadmin.dto.web.response.classified;

import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryDto;
import com.potaliadmin.dto.internal.cache.es.job.CityDto;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.circle.CircleDto;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;

import java.util.List;

/**
 * Created by shaktsin on 4/5/15.
 */
public class PrepareClassifiedResponse extends GenericBaseResponse {

  private List<CircleDto> circleDtoList;
  private List<PrimaryCategoryDto> primaryCategoryDtoList;
  private List<CityDto> cityDtoList;

  public List<CircleDto> getCircleDtoList() {
    return circleDtoList;
  }

  public void setCircleDtoList(List<CircleDto> circleDtoList) {
    this.circleDtoList = circleDtoList;
  }

  public List<PrimaryCategoryDto> getPrimaryCategoryDtoList() {
    return primaryCategoryDtoList;
  }

  public void setPrimaryCategoryDtoList(List<PrimaryCategoryDto> primaryCategoryDtoList) {
    this.primaryCategoryDtoList = primaryCategoryDtoList;
  }

  public List<CityDto> getCityDtoList() {
    return cityDtoList;
  }

  public void setCityDtoList(List<CityDto> cityDtoList) {
    this.cityDtoList = cityDtoList;
  }
}
