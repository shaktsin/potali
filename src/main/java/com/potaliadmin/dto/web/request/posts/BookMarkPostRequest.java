package com.potaliadmin.dto.web.request.posts;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by Shakti Singh on 1/10/15.
 */
public class BookMarkPostRequest extends GenericRequest {

  private int perPage = DefaultConstants.AND_APP_PER_PAGE;
  private int pageNo = DefaultConstants.AND_APP_PAGE_NO;
  private long actionId = EnumReactions.MARK_AS_IMPORTANT.getId();

  public int getPerPage() {
    return perPage;
  }

  public void setPerPage(int perPage) {
    this.perPage = perPage;
  }

  public int getPageNo() {
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  public long getActionId() {
    return actionId;
  }

  public void setActionId(long actionId) {
    this.actionId = actionId;
  }
}
