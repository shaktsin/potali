package com.potaliadmin.pact.dao.job;

import com.potaliadmin.domain.job.Job;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.pact.framework.BaseDao;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public interface JobDao extends BaseDao {

  Job createJob(JobCreateRequest jobCreateRequest);
}
