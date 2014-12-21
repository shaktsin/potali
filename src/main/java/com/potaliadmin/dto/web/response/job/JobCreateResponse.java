package com.potaliadmin.dto.web.response.job;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class JobCreateResponse extends GenericPostResponse {

  private int to;
  private int from;
  private Double salaryTo;
  private Double salaryFrom;


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
