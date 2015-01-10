package com.potaliadmin.dto.internal.cache.es.job;


import com.fasterxml.jackson.annotation.JsonTypeName;
import com.potaliadmin.domain.address.City;
import com.potaliadmin.domain.industry.IndustryRoles;
import com.potaliadmin.domain.job.Job;
import com.potaliadmin.domain.post.PostBlob;
import com.potaliadmin.dto.internal.cache.es.framework.GenericPostVO;
import com.potaliadmin.dto.web.response.post.ShareDto;
import com.potaliadmin.framework.cache.industry.IndustryCache;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class FullJobVO extends GenericPostVO {

  private int to;
  private int from;
  private Double salaryTo;
  private Double salaryFrom;
  private boolean salarySpecified;
  private boolean timeSpecified;
  private List<CityDto> locationList;
  private List<IndustryRolesDto> industryRolesList;


  public FullJobVO() {
    super();
  }

  public FullJobVO(Long id,Long userId, Long userInstituteId, Long postId) {
    super(id,userId, userInstituteId, postId);
  }

  public FullJobVO(Job job, PostBlob postBlob) {
    super(job.getId(),job.getUserId(), job.getUserInstituteId(), job.getId());
    this.setSubject(job.getSubject());
    this.setContent(postBlob.getContent());
    this.setReplyEmail(job.getReplyEmail());
    this.setReplyPhone(job.getReplyPhone());
    this.setReplyWatsApp(job.getReplyWatsApp());
    this.setCreatedDate(job.getCreateDate());
    this.setUpdatedDate(job.getUpdatedDate());
    this.setTimeSpecified(job.isTimeSpecified());
    this.setTo(job.getTo());
    this.setFrom(job.getFrom());
    this.setSalarySpecified(job.isSalarySpecified());
    this.setSalaryTo(job.getSalaryTo());
    this.setSalaryFrom(job.getSalaryFrom());

    ShareDto tempShareDto = new ShareDto();
    tempShareDto.setShareEmail(job.getShareEmail());
    tempShareDto.setSharePhone(job.getSharePhone());
    tempShareDto.setShareWatsApp(job.getShareWatsApp());
    //super.setShareDto(tempShareDto);
    this.setShareDto(tempShareDto);

    List<CityDto> cityDtoList = new ArrayList<CityDto>();
    for (City city : job.getCitySet()) {
      CityDto cityDto = new CityDto();
      cityDto.setId(city.getId());
      cityDto.setName(city.getName());
      cityDtoList.add(cityDto);
    }
    this.setLocationList(cityDtoList);

    //this.set
    List<IndustryRolesDto> industryRolesDtoList = new ArrayList<IndustryRolesDto>();
    for (IndustryRoles industryRoles : job.getIndustryRolesSet()) {
      IndustryRolesDto industryRolesDto = new IndustryRolesDto();
      industryRolesDto.setId(industryRoles.getId());
      industryRolesDto.setName(industryRoles.getName());
      industryRolesDto.setIndustryId(industryRoles.getIndustryId());
      industryRolesDto.setIndustryName(IndustryCache.getCache().getIndustryVO(industryRoles.getIndustryId()).getName());
      industryRolesDtoList.add(industryRolesDto);
    }

    this.setIndustryRolesList(industryRolesDtoList);


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

  public boolean isSalarySpecified() {
    return salarySpecified;
  }

  public void setSalarySpecified(boolean salarySpecified) {
    this.salarySpecified = salarySpecified;
  }

  public boolean isTimeSpecified() {
    return timeSpecified;
  }

  public void setTimeSpecified(boolean timeSpecified) {
    this.timeSpecified = timeSpecified;
  }

  public List<CityDto> getLocationList() {
    return locationList;
  }

  public void setLocationList(List<CityDto> locationList) {
    this.locationList = locationList;
  }

  public List<IndustryRolesDto> getIndustryRolesList() {
    return industryRolesList;
  }

  public void setIndustryRolesList(List<IndustryRolesDto> industryRolesList) {
    this.industryRolesList = industryRolesList;
  }
}
