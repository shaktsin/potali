package com.potaliadmin.constants.plateform;

/**
 * Created by Shakti Singh on 12/18/14.
 */
public enum  EnumPlateForm {

  AND_APP(0L, "Android App"),
  WEB(1L, "Web");

  private Long id;
  private String name;

  EnumPlateForm(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public static boolean contains(Long id) {
    boolean contains = Boolean.FALSE;
    for (EnumPlateForm enumPlateForm : EnumPlateForm.values()) {
      if (enumPlateForm.id.equals(id)) {
        contains = Boolean.TRUE;
        break;
      }
    }
    return contains;
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
}
