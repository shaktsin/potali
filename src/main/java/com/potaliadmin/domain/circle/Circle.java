package com.potaliadmin.domain.circle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shakti on 22/1/15.
 */
//@Entity
//@Table(name = "circle")
public class Circle implements Serializable {

  /*@Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "circle_type_id",nullable = false)
  private Integer circleType;

  @Column(name = "public",nullable = false)
  private boolean publicCircle;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date",nullable = false)
  private Date createdDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_date",nullable = false)
  private Date updatedDate = new Date();

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

  public Integer getCircleType() {
    return circleType;
  }

  public void setCircleType(Integer circleType) {
    this.circleType = circleType;
  }

  public boolean isPublicCircle() {
    return publicCircle;
  }

  public void setPublicCircle(boolean publicCircle) {
    this.publicCircle = publicCircle;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }*/
}
