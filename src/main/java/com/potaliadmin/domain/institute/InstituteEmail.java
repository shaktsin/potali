package com.potaliadmin.domain.institute;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by shaktsin on 6/28/15.
 */
@Entity
@Table(name = "institute_email")
public class InstituteEmail implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "email_suffix", nullable = false, length = 45)
  private String emailSuffix;

  @Column(name = "institute_id", nullable = false)
  private Long instituteId;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmailSuffix() {
    return emailSuffix;
  }

  public void setEmailSuffix(String emailSuffix) {
    this.emailSuffix = emailSuffix;
  }

  public Long getInstituteId() {
    return instituteId;
  }

  public void setInstituteId(Long instituteId) {
    this.instituteId = instituteId;
  }
}
