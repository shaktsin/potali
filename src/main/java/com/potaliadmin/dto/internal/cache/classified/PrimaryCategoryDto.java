package com.potaliadmin.dto.internal.cache.classified;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaktsin on 4/5/15.
 */
public class PrimaryCategoryDto {

  private Long id;
  private String name;
  private Boolean selected;
  private List<SecondaryCategoryDto> secondaryCategoryDtoList = new ArrayList<SecondaryCategoryDto>();


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getSelected() {
    return selected;
  }

  public void setSelected(Boolean selected) {
    this.selected = selected;
  }

  public List<SecondaryCategoryDto> getSecondaryCategoryDtoList() {
    return secondaryCategoryDtoList;
  }

  public void setSecondaryCategoryDtoList(List<SecondaryCategoryDto> secondaryCategoryDtoList) {
    this.secondaryCategoryDtoList = secondaryCategoryDtoList;
  }

  public void addSecondaryCategoryDto(SecondaryCategoryDto secondaryCategoryDto) {
    if (secondaryCategoryDto != null) {
      this.secondaryCategoryDtoList.add(secondaryCategoryDto);
    }
  }
}
