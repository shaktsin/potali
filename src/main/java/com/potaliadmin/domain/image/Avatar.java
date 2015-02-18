package com.potaliadmin.domain.image;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Shakti Singh on 1/3/15.
 */
@Entity
@Table(name = "avatar")
public class Avatar implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "type",nullable = false)
  private int type;

  @Column(name = "public_id", nullable = false, length = 100)
  private String publicId;

  @Column(name = "height",nullable = false)
  private int height;

  @Column(name = "width",nullable = false)
  private int width;

  @Column(name = "version", nullable = true)
  private Long version;

  @Column(name = "format", nullable = true)
  private Integer format;

  @Column(name = "url",nullable = false, length = 100)
  private String url;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date",nullable = false)
  private Date createdDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_date",nullable = false)
  private Date updatedDate = new Date();

  @Column(name = "user_id",nullable = false)
  private Long userId;

  @Column(name = "user_institute_id",nullable = false)
  private Long userInstituteId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getUserInstituteId() {
    return userInstituteId;
  }

  public void setUserInstituteId(Long userInstituteId) {
    this.userInstituteId = userInstituteId;
  }

  public String getPublicId() {
    return publicId;
  }

  public void setPublicId(String publicId) {
    this.publicId = publicId;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Integer getFormat() {
    return format;
  }

  public void setFormat(Integer format) {
    this.format = format;
  }
}
