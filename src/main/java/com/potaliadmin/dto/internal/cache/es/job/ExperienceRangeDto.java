package com.potaliadmin.dto.internal.cache.es.job;

/**
 * Created by shakti on 31/1/15.
 */
public class ExperienceRangeDto extends BaseRangeDto {

  private Integer min;
  private Integer max;

  public ExperienceRangeDto(String name) {
    super(name);
  }

  public Integer getMin() {
    return min;
  }

  public void setMin(Integer min) {
    this.min = min;
  }

  public Integer getMax() {
    return max;
  }

  public void setMax(Integer max) {
    this.max = max;
  }
}
