package com.potaliadmin.dto.web.response.circle;

/**
 * Created by shakti on 28/1/15.
 */
public class CircleDto {

  private Long id;
  private String name;
  private boolean selected;
  private Long members;
  private Long posts;
  private boolean joined;
  private boolean admin;
  private boolean moderate;

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

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public Long getMembers() {
    return members;
  }

  public void setMembers(Long members) {
    this.members = members;
  }

  public Long getPosts() {
    return posts;
  }

  public void setPosts(Long posts) {
    this.posts = posts;
  }

  public boolean isJoined() {
    return joined;
  }

  public void setJoined(boolean joined) {
    this.joined = joined;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public boolean isModerate() {
    return moderate;
  }

  public void setModerate(boolean moderate) {
    this.moderate = moderate;
  }
}
