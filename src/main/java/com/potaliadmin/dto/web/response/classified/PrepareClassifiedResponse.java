package com.potaliadmin.dto.web.response.classified;

import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryDto;
import com.potaliadmin.dto.web.response.circle.CircleDto;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;

import java.util.List;

/**
 * Created by shaktsin on 4/5/15.
 */
public class PrepareClassifiedResponse extends GenericPostResponse {

  private List<CircleDto> circleDtoList;
  private List<PrimaryCategoryDto> primaryCategoryDtoList;


  @Override
  public List<CircleDto> getCircleDtoList() {
    return circleDtoList;
  }

  @Override
  public void setCircleDtoList(List<CircleDto> circleDtoList) {
    this.circleDtoList = circleDtoList;
  }

  public List<PrimaryCategoryDto> getPrimaryCategoryDtoList() {
    return primaryCategoryDtoList;
  }

  public void setPrimaryCategoryDtoList(List<PrimaryCategoryDto> primaryCategoryDtoList) {
    this.primaryCategoryDtoList = primaryCategoryDtoList;
  }
}
