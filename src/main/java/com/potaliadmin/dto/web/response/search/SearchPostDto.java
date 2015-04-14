package com.potaliadmin.dto.web.response.search;

/**
 * Created by shaktsin on 4/12/15.
 */
public class SearchPostDto {

  private long id;
  private String subject;
  private int type;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  @Override
  public int hashCode() {
    return (int)this.id;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null) return false;
    if(!(obj instanceof SearchPostDto)) return false;

    SearchPostDto other = (SearchPostDto) obj;
    return this.id == other.getId();
  }
}
