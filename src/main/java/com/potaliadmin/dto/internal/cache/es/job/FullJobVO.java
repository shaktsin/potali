package com.potaliadmin.dto.internal.cache.es.job;

import com.potaliadmin.dto.internal.cache.es.framework.GenericPostVO;

import java.util.Date;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class FullJobVO extends GenericPostVO {

  private int to;
  private int from;
  private Double salaryTo;
  private Double salaryFrom;


  public FullJobVO(Long userId, Long userInstituteId, Long postId) {
    super(userId, userInstituteId, postId);
  }

  public int getTo() {
    return to;
  }

  public void setTo(int to) {
    this.to = to;
  }

  public int getFrom() {
    return from;
  }

  public void setFrom(int from) {
    this.from = from;
  }

  public Double getSalaryTo() {
    return salaryTo;
  }

  public void setSalaryTo(Double salaryTo) {
    this.salaryTo = salaryTo;
  }

  public Double getSalaryFrom() {
    return salaryFrom;
  }

  public void setSalaryFrom(Double salaryFrom) {
    this.salaryFrom = salaryFrom;
  }
}
