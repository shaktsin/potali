package com.potaliadmin.pact.service.job;

import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.dto.internal.filter.JobFilterDto;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.request.jobs.JobEditRequest;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;
import com.potaliadmin.dto.web.response.job.PrepareJobCreateResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import java.util.Date;
import java.util.List;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public interface JobService {

  public PrepareJobCreateResponse prepareJobCreateRequest();

  public JobResponse createJob(JobCreateRequest jobCreateRequest,List<FormDataBodyPart> imgFiles,List<FormDataBodyPart> jFiles);

  public JobResponse getJob(Long postId);

  public JobSearchResponse searchJob(Long[] circleList,Long[] locationList, Long[] rolesList, Long[] industryList,
                                     Double[] salaryRange, Integer[] experienceRage, EnumSearchOperation searchOperation,
                                     Long postId, int perPage, int pageNo);

  public JobFilterDto getJobFilters(UserResponse userResponse);

  JobResponse editJob(JobEditRequest jobEditRequest,List<FormDataBodyPart> imgFiles,List<FormDataBodyPart> jFiles);
}
