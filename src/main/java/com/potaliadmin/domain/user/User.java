package com.potaliadmin.domain.user;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Shakti Singh on 10/2/14.
 */
@Entity
@Table(name = "user",uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
@NamedQueries(
    @NamedQuery(name = "findByEmail",query = "from User u where u.email = :email")
)
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "first_name", unique = false, nullable = false,length = 45)
  private String firstName;

  @Column(name = "last_name", unique = false, nullable = false,length = 45)
  private String lastName;

  @Column(name = "account_name", unique = false, nullable = false,length = 45)
  private String accountName;

  @Column(name = "email", unique = true, nullable = false,length = 80)
  private String email;

  @Column(name = "gender")
  private Integer gender;

  @Column(name = "verified", nullable = false)
  private Boolean verified;

  @Column(name = "hash", nullable = false)
  private String passwordChecksum;

  @Column(name = "institute_id", nullable = false)
  private Long instituteId;

  @Column(name = "year_of_graduation", nullable = false)
  private int yearOfGraduation;

  @Column(name = "ver_token", nullable = false)
  private int verificationToken;

  @Column(name = "profile_image", nullable = false)
  private String profileImage;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date", nullable = false)
  private Date createDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_date", nullable = true)
  private Date updateDate = new Date();

  @Column(name = "gcm_id", nullable = false, length = 512)
  private String gcmId;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getGender() {
    return gender;
  }

  public void setGender(Integer gender) {
    this.gender = gender;
  }

  public Boolean getVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  public String getPasswordChecksum() {
    return passwordChecksum;
  }

  public void setPasswordChecksum(String passwordChecksum) {
    this.passwordChecksum = passwordChecksum;
  }

  public Long getInstituteId() {
    return instituteId;
  }

  public void setInstituteId(Long instituteId) {
    this.instituteId = instituteId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public int getYearOfGraduation() {
    return yearOfGraduation;
  }

  public void setYearOfGraduation(int yearOfGraduation) {
    this.yearOfGraduation = yearOfGraduation;
  }

  public String getProfileImage() {
    return profileImage;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  public int getVerificationToken() {
    return verificationToken;
  }

  public void setVerificationToken(int verificationToken) {
    this.verificationToken = verificationToken;
  }

  public String getGcmId() {
    return gcmId;
  }

  public void setGcmId(String gcmId) {
    this.gcmId = gcmId;
  }
}
