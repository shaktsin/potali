package com.potaliadmin.dto.web.response.user;

import com.potaliadmin.constants.json.DtoJsonConstants;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

import java.util.List;

/**
 * Created by Shakti Singh on 10/6/14.
 */
public class UserResponse extends GenericBaseResponse {

  private Long id;
  private String name;
  private String email;
  private String passwordChecksum;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPasswordChecksum() {
    return passwordChecksum;
  }

  public void setPasswordChecksum(String passwordChecksum) {
    this.passwordChecksum = passwordChecksum;
  }

  @Override
  protected List<String> getKeys() {
    List<String> keys = super.getKeys();
    keys.add(DtoJsonConstants.ID);
    keys.add(DtoJsonConstants.NAME);
    keys.add(DtoJsonConstants.EMAIL);
    return keys;
  }

  @Override
  protected List<Object> getValues() {
    List<Object> values = super.getValues();
    values.add(id);
    values.add(name);
    values.add(email);
    return values;
  }
}
