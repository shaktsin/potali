package com.potaliadmin.impl.dao.job;

import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.domain.address.City;
import com.potaliadmin.domain.industry.IndustryRoles;
import com.potaliadmin.domain.job.Job;
import com.potaliadmin.dto.internal.hibernate.post.CreatePostBlobRequest;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.city.CityDao;
import com.potaliadmin.pact.dao.industry.IndustryRolesDao;
import com.potaliadmin.pact.dao.job.JobDao;
import com.potaliadmin.pact.dao.post.PostBlobDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by Shakti Singh on 12/20/14.
 */
@Repository
public class JobDaoImpl extends BaseDaoImpl implements JobDao {

  private static final int CONTENT_SIZE = 499;

  @Autowired
  CityDao cityDao;

  @Autowired
  IndustryRolesDao industryRolesDao;

  @Autowired
  PostBlobDao postBlobDao;

  @Override
  @Transactional
  public Job createJob(JobCreateRequest jobCreateRequest) {
    if (jobCreateRequest.getUserId() == null) {
      throw new InValidInputException("User id cannot be null");
    }
    if (jobCreateRequest.getUserInstituteId() == null) {
      throw new InValidInputException("User Institute Id cannot be null");
    }
    Job job = new Job();
    job.setUserId(jobCreateRequest.getUserId());
    job.setUserInstituteId(jobCreateRequest.getUserInstituteId());
    job.setSubject(jobCreateRequest.getSubject());
    job.setContent(StringUtils.substring(jobCreateRequest.getContent(), 0, CONTENT_SIZE));
    job.setReplyEmail(jobCreateRequest.getReplyEmail());
    job.setReplyPhone(jobCreateRequest.getReplyPhone());
    job.setReplyWatsApp(jobCreateRequest.getReplyWatsApp());
    job.setTo(jobCreateRequest.getTo());
    job.setFrom(jobCreateRequest.getFrom());
    job.setSalaryTo(jobCreateRequest.getSalaryTo());
    job.setSalaryFrom(jobCreateRequest.getSalaryFrom());
    job.setSalarySpecified(jobCreateRequest.isSalarySpecified());
    job.setTimeSpecified(jobCreateRequest.isTimeSpecified());
    job.setShareEmail(EnumReactions.isValidShareReaction(jobCreateRequest.getShareDto().getShareEmail()));
    job.setSharePhone(EnumReactions.isValidShareReaction(jobCreateRequest.getShareDto().getSharePhone()));
    job.setShareWatsApp(EnumReactions.isValidShareReaction(jobCreateRequest.getShareDto().getShareWatsApp()));


    // set location set
    Set<City> citySet = getCityDao().findListOfCity(jobCreateRequest.getLocationIdList());
    job.setCitySet(citySet);

    //set industry role set
    Set<IndustryRoles> industryRolesSet = getIndustryRolesDao().findIndustryRolesSetByIdList(jobCreateRequest.getIndustryRolesIdList());
    job.setIndustryRolesSet(industryRolesSet);

    //save job
    job = (Job) save(job);

    // save job post blob
    CreatePostBlobRequest createPostBlobRequest = new CreatePostBlobRequest();
    createPostBlobRequest.setPostId(job.getId());
    createPostBlobRequest.setContent(jobCreateRequest.getContent());
    getPostBlobDao().createPostBlob(createPostBlobRequest);

    return job;
  }

  public CityDao getCityDao() {
    return cityDao;
  }

  public void setCityDao(CityDao cityDao) {
    this.cityDao = cityDao;
  }

  public IndustryRolesDao getIndustryRolesDao() {
    return industryRolesDao;
  }

  public PostBlobDao getPostBlobDao() {
    return postBlobDao;
  }
}
