package com.potaliadmin.dto.internal.cache.es.job;

/**
 * Created by shakti on 31/1/15.
 */
public class SalaryRangeDto extends BaseRangeDto {

  private Double min;
  private Double max;

  public SalaryRangeDto(String name) {
    super(name);
  }

  public Double getMin() {
    return min;
  }

  public void setMin(Double min) {
    this.min = min;
  }

  public Double getMax() {
    return max;
  }

  public void setMax(Double max) {
    this.max = max;
  }
}
