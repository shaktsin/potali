package com.potaliadmin.constants.circle;

/**
 * Created by shakti on 24/1/15.
 */
public enum CircleType {

  ALL(0,"Everyone"),
  YEAR(1, "Year"),
  CHAPTER(2, "Chapter"),
  CLUB(3, "Clubs"),

  ;

  private Integer id;
  private String name;

  CircleType(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public static boolean validCircle(Integer id) {
    boolean valid = false;
    for (CircleType circleType : CircleType.values()) {
      if (circleType.getId().equals(id)) {
        valid = true;
        break;
      }
    }
    return valid;
  }

  public static CircleType getById(Integer id) {
    for (CircleType circleType : CircleType.values()) {
      if (circleType.getId().equals(id)) {
        return circleType;
      }
    }
    return null;
  }

  public static boolean shouldBeHidden(Integer id) {
    return ALL.getId().equals(id);
  }

  public static String getYearGroupName(String year) {
    return "Class" + " " + year;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }


}
