package com.potaliadmin.domain.job;

import com.potaliadmin.domain.address.City;
import com.potaliadmin.domain.industry.IndustryRoles;
import com.potaliadmin.domain.post.Post;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by Shakti Singh on 12/16/14.
 */
@Entity
@Table(name = "job_post")
@PrimaryKeyJoinColumn(name = "id")
public class Job extends Post {

  @Column(name = "exp_to", nullable = true)
  private Integer to;

  @Column(name = "exp_from", nullable = true)
  private Integer from;

  @Column(name = "salary_to", nullable = false)
  private Double salaryTo;

  @Column(name = "salary_from", nullable = false)
  private Double salaryFrom;

  @Column(name = "time_specified", nullable = false)
  private boolean timeSpecified;

  @Column(name = "salary_specified", nullable = false)
  private boolean salarySpecified;


  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name="job_post_has_job_roles", joinColumns = {
      @JoinColumn(name = "job_post_to", nullable = false, updatable = false)} ,
      inverseJoinColumns = {@JoinColumn(name = "job_roles_id", nullable = false, updatable = false)})
  private Set<IndustryRoles> industryRolesSet;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name="job_post_has_city", joinColumns = {
      @JoinColumn(name = "job_post_to", nullable = false, updatable = false)} ,
      inverseJoinColumns = {@JoinColumn(name = "city_id", nullable = false, updatable = false)})
  private Set<City> citySet;

  public Integer getTo() {
    return to;
  }

  public void setTo(Integer to) {
    this.to = to;
  }

  public Integer getFrom() {
    return from;
  }

  public void setFrom(Integer from) {
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

  public Set<IndustryRoles> getIndustryRolesSet() {
    return industryRolesSet;
  }

  public void setIndustryRolesSet(Set<IndustryRoles> industryRolesSet) {
    this.industryRolesSet = industryRolesSet;
  }

  public Set<City> getCitySet() {
    return citySet;
  }

  public void setCitySet(Set<City> citySet) {
    this.citySet = citySet;
  }

  public boolean isTimeSpecified() {
    return timeSpecified;
  }

  public void setTimeSpecified(boolean timeSpecified) {
    this.timeSpecified = timeSpecified;
  }

  public boolean isSalarySpecified() {
    return salarySpecified;
  }

  public void setSalarySpecified(boolean salarySpecified) {
    this.salarySpecified = salarySpecified;
  }
}
