package com.potaliadmin.dto.web.response.search;

/**
 * Created by shaktsin on 4/12/15.
 */
public class CircleDto {

  private long id;
  private String name;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    return (int)this.id;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null) return false;
    if(!(obj instanceof CircleDto)) return false;

    CircleDto other = (CircleDto) obj;
    return this.id == other.getId();
  }
}
