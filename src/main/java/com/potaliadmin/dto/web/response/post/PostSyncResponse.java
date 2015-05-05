package com.potaliadmin.dto.web.response.post;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

/**
 * Created by Shakti Singh on 1/7/15.
 */
public class PostSyncResponse extends GenericBaseResponse {

  long jobCount;
  long classCount;
  long newsFeedCount;

  public long getJobCount() {
    return jobCount;
  }

  public void setJobCount(long jobCount) {
    this.jobCount = jobCount;
  }

  public long getClassCount() {
    return classCount;
  }

  public void setClassCount(long classCount) {
    this.classCount = classCount;
  }

  public long getNewsFeedCount() {
    return newsFeedCount;
  }

  public void setNewsFeedCount(long newsFeedCount) {
    this.newsFeedCount = newsFeedCount;
  }
}
