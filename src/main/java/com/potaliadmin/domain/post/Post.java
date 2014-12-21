package com.potaliadmin.domain.post;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Shakti Singh on 12/16/14.
 */
@Entity
@Table(name = "post")
@Inheritance(strategy = InheritanceType.JOINED)
public class Post implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "subject", nullable = false,length = 200)
  private String subject;

  @Column(name = "content", length = 500)
  private String content;

  @Column(name = "reply_email", length = 45)
  private String replyEmail;

  @Column(name = "reply_phone", length = 45)
  private String replyPhone;

  @Column(name = "reply_watspp", length = 45)
  private String replyWatsApp;

  @Temporal(TemporalType.DATE)
  @Column(name = "created_date", nullable = false)
  private Date createDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_date", nullable = false)
  private Date updatedDate;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "user_institute_id", nullable = false)
  private Long userInstituteId;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getReplyEmail() {
    return replyEmail;
  }

  public void setReplyEmail(String replyEmail) {
    this.replyEmail = replyEmail;
  }

  public String getReplyPhone() {
    return replyPhone;
  }

  public void setReplyPhone(String replyPhone) {
    this.replyPhone = replyPhone;
  }

  public String getReplyWatsApp() {
    return replyWatsApp;
  }

  public void setReplyWatsApp(String replyWatsApp) {
    this.replyWatsApp = replyWatsApp;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
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
}
