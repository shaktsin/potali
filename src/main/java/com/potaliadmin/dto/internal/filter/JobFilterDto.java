package com.potaliadmin.dto.internal.filter;

import com.potaliadmin.dto.internal.cache.es.job.BaseRangeDto;
import com.potaliadmin.dto.internal.cache.es.job.CityDto;
import com.potaliadmin.dto.internal.cache.es.job.IndustryDto;
import com.potaliadmin.dto.internal.cache.es.job.IndustryRolesDto;
import com.potaliadmin.dto.web.response.circle.CircleDto;

import java.util.List;
import java.util.Map;

/**
 * Created by shakti on 31/1/15.
 */
public class JobFilterDto extends BaseFilterDto {

  List<IndustryDto> indList;
  List<IndustryRolesDto> roDtoList;
  List<CityDto> cityList;
  List<CircleDto> circleDtoList;

  Map<String, BaseRangeDto> rangeDtoMap;


  public JobFilterDto(String name) {
    super(name);
  }

  public List<IndustryDto> getIndList() {
    return indList;
  }

  public void setIndList(List<IndustryDto> indList) {
    this.indList = indList;
  }

  public List<IndustryRolesDto> getRoDtoList() {
    return roDtoList;
  }

  public void setRoDtoList(List<IndustryRolesDto> roDtoList) {
    this.roDtoList = roDtoList;
  }

  public List<CityDto> getCityList() {
    return cityList;
  }

  public void setCityList(List<CityDto> cityList) {
    this.cityList = cityList;
  }

  public Map<String, BaseRangeDto> getRangeDtoMap() {
    return rangeDtoMap;
  }

  public void setRangeDtoMap(Map<String, BaseRangeDto> rangeDtoMap) {
    this.rangeDtoMap = rangeDtoMap;
  }

  public List<CircleDto> getCircleDtoList() {
    return circleDtoList;
  }

  public void setCircleDtoList(List<CircleDto> circleDtoList) {
    this.circleDtoList = circleDtoList;
  }
}
