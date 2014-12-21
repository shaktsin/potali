package com.potaliadmin.impl.service.job;

import com.potaliadmin.domain.job.Job;
import com.potaliadmin.domain.post.PostBlob;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.response.job.JobCreateResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.pact.dao.city.CityDao;
import com.potaliadmin.pact.dao.job.JobDao;
import com.potaliadmin.pact.dao.post.PostBlobDao;
import com.potaliadmin.pact.service.job.JobService;
import com.potaliadmin.pact.service.users.LoginService;
import com.potaliadmin.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Shakti Singh on 12/20/14.
 */
@Service
public class JobServiceImpl implements JobService {

  @Autowired
  LoginService loginService;

  @Autowired
  JobDao jobDao;

  @Autowired
  PostBlobDao postBlobDao;



  public JobCreateResponse createJob(JobCreateRequest jobCreateRequest) {
    if (!jobCreateRequest.validate()) {
      throw new InValidInputException("Please input valid parameters");
    }

    UserResponse userResponse = getLoginService().getLoggedInUser();
    jobCreateRequest.setUserId(userResponse.getId());
    jobCreateRequest.setUserInstituteId(userResponse.getInstituteId());

    // create job
    Job job = getJobDao().createJob(jobCreateRequest);

    JobCreateResponse jobCreateResponse = new JobCreateResponse();
    // set blob
    PostBlob postBlob = getPostBlobDao().findByPostId(job.getId());
    if (postBlob == null) {
      jobCreateResponse.setException(Boolean.TRUE);
      jobCreateResponse.addMessage("Some Internal Exception Occurred!");
      return jobCreateResponse;
    }

    jobCreateResponse.setPostId(job.getId());
    jobCreateResponse.setSubject(job.getSubject());
    jobCreateResponse.setTo(job.getTo());
    jobCreateResponse.setFrom(job.getFrom());
    jobCreateResponse.setSalaryFrom(job.getSalaryFrom());
    jobCreateResponse.setSalaryTo(job.getSalaryTo());
    jobCreateResponse.setPostedOn(DateUtils.getPostedOnDate(job.getCreateDate()));
    jobCreateResponse.setReplyEmail(job.getReplyEmail());
    jobCreateResponse.setReplyPhone(job.getReplyPhone());
    jobCreateResponse.setReplyWatsApp(job.getReplyWatsApp());
    jobCreateResponse.setContent(postBlob.getContent());
    return jobCreateResponse;
  }

  public LoginService getLoginService() {
    return loginService;
  }

  public JobDao getJobDao() {
    return jobDao;
  }

  public PostBlobDao getPostBlobDao() {
    return postBlobDao;
  }
}
