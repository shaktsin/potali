package com.potaliadmin.dto.web.response.search;

/**
 * Created by shaktsin on 4/12/15.
 */
public class SearchUserDto {

  private long id;
  private String firstName;
  private String lastName;
  private String accountName;

  public long getId() {
    return id;
  }

  public void setId(long id) {
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

  @Override
  public int hashCode() {
    return (int)this.id;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null) return false;
    if(!(obj instanceof SearchUserDto)) return false;

    SearchUserDto other = (SearchUserDto) obj;
    return this.id == other.getId();
  }

}
