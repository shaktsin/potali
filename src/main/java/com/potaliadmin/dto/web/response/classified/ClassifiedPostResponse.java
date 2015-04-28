package com.potaliadmin.dto.web.response.classified;

import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryDto;
import com.potaliadmin.dto.internal.cache.classified.SecondaryCategoryDto;
import com.potaliadmin.dto.internal.cache.es.job.CityDto;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;

import java.util.List;

/**
 * Created by shaktsin on 4/26/15.
 */
public class ClassifiedPostResponse extends GenericPostResponse {

  private List<SecondaryCategoryDto> secondaryCategoryDtoList;
  private List<CityDto> cityDtoList;

  public List<SecondaryCategoryDto> getSecondaryCategoryDtoList() {
    return secondaryCategoryDtoList;
  }

  public void setSecondaryCategoryDtoList(List<SecondaryCategoryDto> secondaryCategoryDtoList) {
    this.secondaryCategoryDtoList = secondaryCategoryDtoList;
  }

  public List<CityDto> getCityDtoList() {
    return cityDtoList;
  }

  public void setCityDtoList(List<CityDto> cityDtoList) {
    this.cityDtoList = cityDtoList;
  }
}
