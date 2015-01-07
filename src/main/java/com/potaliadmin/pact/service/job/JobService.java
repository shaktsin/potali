package com.potaliadmin.pact.service.job;

import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;
import com.potaliadmin.dto.web.response.job.PrepareJobCreateResponse;

import java.util.Date;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public interface JobService {

  public PrepareJobCreateResponse prepareJobCreateRequest();

  public JobResponse createJob(JobCreateRequest jobCreateRequest);

  public JobResponse getJob(Long postId);

  public JobSearchResponse searchJob(Long[] locationList, Long[] rolesList, Long[] industryList,
                                     Double[] salaryRange, Integer[] experienceRage, EnumSearchOperation searchOperation,
                                     Long postId, int perPage, int pageNo);
}
