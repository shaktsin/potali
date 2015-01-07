package com.potaliadmin.dto.web.response.post;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

/**
 * Created by Shakti Singh on 1/7/15.
 */
public class PostSyncResponse extends GenericBaseResponse {

  long jobCount;
  long classCount;
  long meetCount;

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

  public long getMeetCount() {
    return meetCount;
  }

  public void setMeetCount(long meetCount) {
    this.meetCount = meetCount;
  }
}
