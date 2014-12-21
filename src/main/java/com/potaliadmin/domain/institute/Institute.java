package com.potaliadmin.domain.institute;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Shakti Singh on 11/25/14.
 */
@Entity
@Table(name = "institute")
public class Institute implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 45)
  private String name;

  @Column(name = "email_suffix", nullable = false, length = 45)
  private String emailSuffice;

  @Column(name = "third_party_auth", nullable = false)
  private Boolean thirdPartyAuth;

  @Column(name="provider", nullable = false)
  private Integer provider;


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

  public String getEmailSuffice() {
    return emailSuffice;
  }

  public void setEmailSuffice(String emailSuffice) {
    this.emailSuffice = emailSuffice;
  }

  public Boolean getThirdPartyAuth() {
    return thirdPartyAuth;
  }

  public void setThirdPartyAuth(Boolean thirdPartyAuth) {
    this.thirdPartyAuth = thirdPartyAuth;
  }

  public Integer getProvider() {
    return provider;
  }

  public void setProvider(Integer provider) {
    this.provider = provider;
  }
}
