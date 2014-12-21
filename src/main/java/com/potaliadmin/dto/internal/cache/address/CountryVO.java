package com.potaliadmin.dto.internal.cache.address;

import com.potaliadmin.domain.address.Country;

/**
 * Created by Shakti Singh on 12/16/14.
 */
public class CountryVO {

  Long id;
  String name;
  String countryCode;
  String isoCode;

  public CountryVO(Country country) {
    this.id = country.getId();
    this.name = country.getName();
    this.countryCode = country.getCountryCode().toString();
    this.isoCode = country.getShortIsoCode();
  }



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

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getIsoCode() {
    return isoCode;
  }

  public void setIsoCode(String isoCode) {
    this.isoCode = isoCode;
  }
}
