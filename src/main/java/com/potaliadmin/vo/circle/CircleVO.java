package com.potaliadmin.vo.circle;

import com.potaliadmin.domain.circle.Circle;
import com.potaliadmin.framework.elasticsearch.annotation.ElasticEntity;
import com.potaliadmin.vo.BaseElasticVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shakti on 28/1/15.
 */
@ElasticEntity(type = "circle")
public class CircleVO extends BaseElasticVO {

  private String name;
  private int type;
  private boolean moderate;
  private Long admin;
  private Long instituteId;
  private boolean active;
  private List<Long> requestList;

  public CircleVO() {
  }

  public CircleVO(Circle circle) {
    super(circle.getId());
    name = circle.getName();
    type = circle.getCircleType();
    moderate = !circle.isPublicCircle();
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public boolean isModerate() {
    return moderate;
  }

  public void setModerate(boolean moderate) {
    this.moderate = moderate;
  }

  public Long getAdmin() {
    return admin;
  }

  public void setAdmin(Long admin) {
    this.admin = admin;
  }

  public Long getInstituteId() {
    return instituteId;
  }

  public void setInstituteId(Long instituteId) {
    this.instituteId = instituteId;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public List<Long> getRequestList() {
    return requestList;
  }

  public void setRequestList(List<Long> requestList) {
    this.requestList = requestList;
  }
}
