package com.potaliadmin.constants.reactions;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Shakti Singh on 12/28/14.
 */
public enum  EnumReactions {

  HIDE_THIS_POST(1L, "hide this Post", -1),
  MARK_AS_IMPORTANT(2L, "Mark As Important", 5),
  MARK_AS_SPAM(3L, "Mark As Spam", -5),
  REPLY_VIA_EMAIL(4L, "Reply Via Email", 1),
  REPLY_VIA_WATSAPP(5L, "Reply Via WatsApp", 1),
  REPLY_VIA_PHONE(6L, "Reply Via Phone", 1),
  SHARE_VIA_PHONE(7L, "Share Via Phone", 2),
  SHARE_VIA_EMAIL(8L, "Share Via Email", 2),
  SHARE_VIA_WATSAPP(9L, "Share Via WatsApp", 2),
  COMMENT(10L, "Mark As Important", 5);

  private Long id;
  private String name;
  private int priority;

  EnumReactions(Long id, String name, int priority) {
    this.id = id;
    this.name = name;
    this.priority = priority;
  }

  public static boolean contains(Long id) {
    boolean contains = Boolean.FALSE;
    for (EnumReactions enumReactions : EnumReactions.values()) {
      if (enumReactions.id.equals(id)) {
        contains = Boolean.TRUE;
        break;
      }
    }
    return contains;
  }

  public static Long isValidShareReaction(Long id) {
    List<Long> shareList = Arrays.asList(SHARE_VIA_EMAIL.getId(), SHARE_VIA_PHONE.getId(), SHARE_VIA_WATSAPP.getId());
    return shareList.contains(id) ? id : -1;
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

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }
}
