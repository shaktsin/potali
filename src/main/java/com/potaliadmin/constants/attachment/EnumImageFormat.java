package com.potaliadmin.constants.attachment;

/**
 * Created by shakti on 16/2/15.
 */
public enum  EnumImageFormat {

  JPG(0, "jpg"),
  JPEG(1, "jpeg"),
  PNG(2, "png"),
  XLS(3, "xls"),
  PDF(4, "pdf"),
  DOC(5, "doc"),
  DOCX(6, "docx"),
  ;


  Integer id;
  String name;

  EnumImageFormat(int id, String name) {
    this.id = id;
    this.name = name;
  }


  public static Integer getImageFormatByString(String name) {
    for (EnumImageFormat enumImageFormat : EnumImageFormat.values()) {
      if (enumImageFormat.name.equals(name)) {
        return enumImageFormat.id;
      }
    }
    return null;
  }

  public static String getImageFormatById(Integer id) {
    for (EnumImageFormat enumImageFormat : EnumImageFormat.values()) {
      if (enumImageFormat.id.equals(id)) {
        return enumImageFormat.name;
      }
    }
    return null;
  }
}
