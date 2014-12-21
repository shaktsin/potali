package com.potaliadmin.dto.internal.cache.institute;

import com.potaliadmin.domain.institute.Institute;

/**
 * Created by Shakti Singh on 12/14/14.
 */
public class InstituteVO {

  private Long id;
  private String nm;
  private String emSuffix;
  private Boolean thPartyAuth;
  private Integer provider;

  public InstituteVO(Institute institute) {
    id = institute.getId();
    nm = institute.getName();
    emSuffix = institute.getEmailSuffice();
    thPartyAuth = institute.getThirdPartyAuth();
    provider = institute.getProvider();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNm() {
    return nm;
  }

  public void setNm(String nm) {
    this.nm = nm;
  }

  public String getEmSuffix() {
    return emSuffix;
  }

  public void setEmSuffix(String emSuffix) {
    this.emSuffix = emSuffix;
  }

  public Boolean getThPartyAuth() {
    return thPartyAuth;
  }

  public void setThPartyAuth(Boolean thPartyAuth) {
    this.thPartyAuth = thPartyAuth;
  }

  public Integer getProvider() {
    return provider;
  }

  public void setProvider(Integer provider) {
    this.provider = provider;
  }
}
