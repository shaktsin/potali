package com.potaliadmin.pact.service.job;

import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public interface JobService {

  public JobResponse createJob(JobCreateRequest jobCreateRequest);

  public JobResponse getJob(Long postId);

  public JobSearchResponse searchJob(Long[] locationList, Long[] rolesList, Long[] industryList,
                                     Double[] salaryRange, Integer[] experienceRage, int perPage, int pageNo);
}
