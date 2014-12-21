package com.potaliadmin.pact.service.job;

import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.response.job.JobCreateResponse;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public interface JobService {

  public JobCreateResponse createJob(JobCreateRequest jobCreateRequest);
}
