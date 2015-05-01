package com.potaliadmin.vo.classified;

import com.potaliadmin.domain.address.City;
import com.potaliadmin.domain.classified.ClassifiedPost;
import com.potaliadmin.domain.classified.SecondaryCategory;
import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryDto;
import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryVO;
import com.potaliadmin.dto.internal.cache.classified.SecondaryCategoryDto;
import com.potaliadmin.dto.internal.cache.classified.SecondaryCategoryVO;
import com.potaliadmin.dto.internal.cache.es.job.CityDto;
import com.potaliadmin.framework.cache.classified.PrimaryCategoryCache;
import com.potaliadmin.framework.cache.classified.SecondaryCategoryCache;
import com.potaliadmin.framework.elasticsearch.annotation.ElasticEntity;
import com.potaliadmin.vo.BaseElasticVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by shaktsin on 4/27/15.
 */
@ElasticEntity(type = "classified", parentType = "post")
public class ClassifiedVO extends BaseElasticVO {

  private List<PrimaryCategoryDto> primaryCategoryDtoList;
  private List<SecondaryCategoryDto> secondaryCategoryDtoList;
  private List<CityDto> locationList;

  public ClassifiedVO() {}

  public ClassifiedVO(ClassifiedPost classifiedPost) {
    super(classifiedPost.getId());
    this.setParentId(classifiedPost.getId().toString());

    List<CityDto> cityDtoList = new ArrayList<CityDto>();
    for (City city : classifiedPost.getCitySet()) {
      CityDto cityDto = new CityDto();
      cityDto.setId(city.getId());
      cityDto.setName(city.getName());
      cityDtoList.add(cityDto);
    }
    this.setLocationList(cityDtoList);

    Set<SecondaryCategory> secondaryCategorySet = classifiedPost.getSecondaryCategorySet();
    List<PrimaryCategoryDto> primaryCategoryDtoList = new ArrayList<PrimaryCategoryDto>();
    List<SecondaryCategoryDto> secondaryCategoryDtoList = new ArrayList<SecondaryCategoryDto>();

    for (SecondaryCategory secondaryCategory : secondaryCategorySet) {
      SecondaryCategoryDto secondaryCategoryDto = new SecondaryCategoryDto();
      SecondaryCategoryVO secondaryCategoryVO =
          SecondaryCategoryCache.getCache().getSecondaryCategoryVO(secondaryCategory.getId());
      secondaryCategoryDto.setId(secondaryCategoryVO.getId());
      secondaryCategoryDto.setName(secondaryCategoryVO.getName());
      secondaryCategoryDtoList.add(secondaryCategoryDto);

      PrimaryCategoryVO primaryCategoryVO = PrimaryCategoryCache.getCache().getPrimaryCategoryVO(secondaryCategory.getPrimaryCategoryId());
      PrimaryCategoryDto primaryCategoryDto = new PrimaryCategoryDto();
      primaryCategoryDto.setId(primaryCategoryVO.getId());
      primaryCategoryDto.setName(primaryCategoryDto.getName());
      primaryCategoryDtoList.add(primaryCategoryDto);
    }

    this.primaryCategoryDtoList = primaryCategoryDtoList;
    this.secondaryCategoryDtoList = secondaryCategoryDtoList;
  }

  public List<PrimaryCategoryDto> getPrimaryCategoryDtoList() {
    return primaryCategoryDtoList;
  }

  public void setPrimaryCategoryDtoList(List<PrimaryCategoryDto> primaryCategoryDtoList) {
    this.primaryCategoryDtoList = primaryCategoryDtoList;
  }

  public List<SecondaryCategoryDto> getSecondaryCategoryDtoList() {
    return secondaryCategoryDtoList;
  }

  public void setSecondaryCategoryDtoList(List<SecondaryCategoryDto> secondaryCategoryDtoList) {
    this.secondaryCategoryDtoList = secondaryCategoryDtoList;
  }

  public List<CityDto> getLocationList() {
    return locationList;
  }

  public void setLocationList(List<CityDto> locationList) {
    this.locationList = locationList;
  }
}
