package com.potaliadmin.dto.web.response.institute;

import com.potaliadmin.constants.json.DtoJsonConstants;
import com.potaliadmin.dto.internal.cache.institute.InstituteVO;
import com.potaliadmin.framework.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakti Singh on 12/15/14.
 */
public class InstituteDto extends JSONObject {

  private Long id;
  private String nm;
  private String emSuffix;
  private Boolean thPartyAuth;
  private Integer provider;

  public InstituteDto(InstituteVO instituteVO) {
    id = instituteVO.getId();
    nm = instituteVO.getNm();
    emSuffix = instituteVO.getEmSuffix();
    thPartyAuth = instituteVO.getThPartyAuth();
    provider = instituteVO.getProvider();
  }

  public static List<InstituteDto> createList(List<InstituteVO> instituteVOs) {
    List<InstituteDto> instituteDtoList = new ArrayList<InstituteDto>();
    for (InstituteVO instituteVO : instituteVOs) {
      instituteDtoList.add(new InstituteDto(instituteVO));
    }
    return instituteDtoList;
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

  @Override
  protected List<String> getKeys() {
    List<String> keys = new ArrayList<String>();
    keys.add(DtoJsonConstants.ID);
    keys.add(DtoJsonConstants.NAME);
    keys.add(DtoJsonConstants.EMAIL_SUFFIX);
    keys.add(DtoJsonConstants.THIRD_PARTY_AUTH);
    keys.add(DtoJsonConstants.PROVIDER);
    return keys;
  }

  @Override
  protected List<Object> getValues() {
    List<Object> values = new ArrayList<Object>();
    values.add(this.id);
    values.add(this.nm);
    values.add(this.emSuffix);
    values.add(this.thPartyAuth);
    values.add(this.provider);
    return values;
  }
}
