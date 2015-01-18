package com.potaliadmin.dto.web.response.post;

import com.potaliadmin.constants.DefaultConstants;

/**
 * Created by shakti on 18/1/15.
 */
public class ShareReactionResponse extends GenericPostReactionResponse {

  private String content;

  public String getContent() {
    return content + " " +DefaultConstants.SHARE_SUFFIX;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
