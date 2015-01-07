package com.potaliadmin.constants.query;

/**
 * Created by Shakti Singh on 1/6/15.
 */
public enum EnumSearchOperation {

  NEWER(1,"Newer Posts"),
  OLDER(2, "Old Posts");

  int id;
  String name;

  EnumSearchOperation(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public static boolean contains(int id) {
    boolean contains = Boolean.FALSE;
    for (EnumSearchOperation enumSearchOperation : EnumSearchOperation.values()) {
      if (enumSearchOperation.id == id) {
        contains = Boolean.TRUE;
        break;
      }
    }
    return contains;
  }

  public static EnumSearchOperation getById(int id) {
    for (EnumSearchOperation enumSearchOperation : EnumSearchOperation.values()) {
      if (enumSearchOperation.id == id) {
        return enumSearchOperation;
      }
    }
    return null;
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
