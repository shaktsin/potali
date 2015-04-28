package com.potaliadmin.dto.web.response.classified;

import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryDto;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;

import java.util.List;

/**
 * Created by shaktsin on 4/26/15.
 */
public class ClassifiedPostResponse extends GenericPostResponse {

  private List<PrimaryCategoryDto> primaryCategoryDtoList;

  public List<PrimaryCategoryDto> getPrimaryCategoryDtoList() {
    return primaryCategoryDtoList;
  }

  public void setPrimaryCategoryDtoList(List<PrimaryCategoryDto> primaryCategoryDtoList) {
    this.primaryCategoryDtoList = primaryCategoryDtoList;
  }
}
