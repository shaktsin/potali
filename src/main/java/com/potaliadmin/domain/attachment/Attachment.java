package com.potaliadmin.domain.attachment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shakti on 26/1/15.
 */
@Entity
@Table(name = "attachment")
public class Attachment implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "attachment_type", nullable = false)
  private Integer attachmentType;

  @Column(name = "path", nullable = false,length = 45)
  private String path;

  @Column(name = "size", nullable = false)
  private Integer size;

  @Column(name = "public_id", nullable = false, length = 100)
  private String publicId;

  @Column(name = "height", nullable = true)
  private Integer height;

  @Column(name = "width", nullable = true)
  private Integer width;

  @Column(name = "version", nullable = true)
  private Long version;

  @Column(name = "format", nullable = true)
  private Integer format;


  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date",nullable = false)
  private Date createdDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_date",nullable = false)
  private Date updatedDate = new Date();

  @Column(name = "post_id",nullable = false)
  private Long postId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getAttachmentType() {
    return attachmentType;
  }

  public void setAttachmentType(Integer attachmentType) {
    this.attachmentType = attachmentType;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
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

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public String getPublicId() {
    return publicId;
  }

  public void setPublicId(String publicId) {
    this.publicId = publicId;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
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
