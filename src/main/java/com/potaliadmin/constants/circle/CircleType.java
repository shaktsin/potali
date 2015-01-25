package com.potaliadmin.constants.circle;

/**
 * Created by shakti on 24/1/15.
 */
public enum CircleType {

  YEAR(0, "Year"),
  CHAPTER(1, "Chapter"),
  CLUB(2, "Clubs"),

  ;

  private Integer id;
  private String name;

  CircleType(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }


}
