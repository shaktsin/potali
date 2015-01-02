package com.potaliadmin.impl.service.job;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.domain.job.Job;
import com.potaliadmin.domain.post.PostBlob;
import com.potaliadmin.dto.internal.cache.address.CityVO;
import com.potaliadmin.dto.internal.cache.es.job.CityDto;
import com.potaliadmin.dto.internal.cache.es.job.FullJobVO;
import com.potaliadmin.dto.internal.cache.es.job.IndustryDto;
import com.potaliadmin.dto.internal.cache.es.job.IndustryRolesDto;
import com.potaliadmin.dto.internal.cache.job.IndustryRolesVO;
import com.potaliadmin.dto.internal.cache.job.IndustryVO;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;
import com.potaliadmin.dto.web.response.post.ReplyDto;
import com.potaliadmin.dto.web.response.user.UserDto;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.framework.cache.ESCacheManager;
import com.potaliadmin.framework.cache.address.CityCache;
import com.potaliadmin.framework.cache.industry.IndustryCache;
import com.potaliadmin.framework.cache.industry.IndustryRolesCache;
import com.potaliadmin.pact.dao.job.JobDao;
import com.potaliadmin.pact.dao.post.PostBlobDao;
import com.potaliadmin.pact.service.cache.ESCacheService;
import com.potaliadmin.pact.service.job.JobService;
import com.potaliadmin.pact.service.users.LoginService;
import com.potaliadmin.util.BaseUtil;
import com.potaliadmin.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

  @Autowired
  ESCacheService esCacheService;

  private static final String INDEX = "ofc";
  private static final String TYPE = "job";
  private static final String POST_REACTIONS = "post_reactions";
  private static final Long[] REMOVE_LIST = {EnumReactions.HIDE_THIS_POST.getId(), EnumReactions.MARK_AS_SPAM.getId()};



  public JobResponse createJob(JobCreateRequest jobCreateRequest) {
    if (!jobCreateRequest.validate()) {
      throw new InValidInputException("Please input valid parameters");
    }

    UserResponse userResponse = getLoginService().getLoggedInUser();
    jobCreateRequest.setUserId(userResponse.getId());
    jobCreateRequest.setUserInstituteId(userResponse.getInstituteId());

    // create job
    Job job = getJobDao().createJob(jobCreateRequest);


    // set blob
    PostBlob postBlob = getPostBlobDao().findByPostId(job.getId());
    if (postBlob == null) {
      JobResponse jobResponse = new JobResponse();
      jobResponse.setException(Boolean.TRUE);
      jobResponse.addMessage("Some Internal Exception Occurred!");
      return jobResponse;
    }

    //push in ES
    FullJobVO fullJobVO = new FullJobVO(job, postBlob);
    boolean published = getEsCacheService().put("job", fullJobVO, null);

    if (!published) {
      JobResponse jobResponse = new JobResponse();
      jobResponse.setException(Boolean.TRUE);
      jobResponse.addMessage("Some Internal Exception Occurred!");
      return jobResponse;
    }

    return createJobResponse(fullJobVO, userResponse);
  }

  @Override
  public JobResponse getJob(Long postId) {
    UserResponse userResponse = getLoginService().getLoggedInUser();
    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    if (postId == null) {
      throw new InValidInputException("Post Id cannot be null");
    }

    GetResponse getResponse = ESCacheManager.getInstance().getClient()
        .prepareGet(INDEX, TYPE, postId.toString()).execute().actionGet();


    if (getResponse.isExists()) {
      FullJobVO fullJobVO = (FullJobVO) getEsCacheService().parseResponse(getResponse, FullJobVO.class);
      return createJobResponse(fullJobVO, userResponse);
    } else {
      JobResponse jobResponse = new JobResponse();
      jobResponse.setException(Boolean.TRUE);
      jobResponse.addMessage("Don't find any record with specified id");
      return jobResponse;
    }
  }


  public JobSearchResponse searchJob(Long[] locationList, Long[] rolesList,Long[] industryList,
                                     Double[] salaryRange,Integer[] experienceRange, int perPage, int pageNo) {

    long totalHits=0;

    UserResponse userResponse = getLoginService().getLoggedInUser();

    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    JobSearchResponse jobSearchResponse= new JobSearchResponse();
    AndFilterBuilder andFilterBuilder = new AndFilterBuilder();

    // hide all mark as hidden and spam posts from list
    OrFilterBuilder orFilterBuilder = new OrFilterBuilder();
    //NotFilterBuilder notFilterBuilder = new NotFilterBuilder()
    MatchAllFilterBuilder matchAllFilterBuilder = new MatchAllFilterBuilder();
    HasChildFilterBuilder hasChildFilterBuilder = new HasChildFilterBuilder(POST_REACTIONS, matchAllFilterBuilder);
    orFilterBuilder.add(FilterBuilders.notFilter(hasChildFilterBuilder));
    orFilterBuilder.add(FilterBuilders.notFilter(FilterBuilders.termFilter("userId", userResponse.getId())));


    BoolFilterBuilder boolFilterBuilder = new BoolFilterBuilder();
    boolFilterBuilder.mustNot(FilterBuilders.inFilter("reactionId", REMOVE_LIST));
    boolFilterBuilder.must(FilterBuilders.termFilter("userId", userResponse.getId()));
    orFilterBuilder.add(FilterBuilders.hasChildFilter(POST_REACTIONS, boolFilterBuilder));
    andFilterBuilder.add(orFilterBuilder);


    if (locationList != null && locationList.length > 0) {
      andFilterBuilder.add(FilterBuilders.inFilter("locationList.id", locationList));
      Arrays.asList(locationList);
    }
    if (rolesList != null && rolesList.length > 0) {
      andFilterBuilder.add(FilterBuilders.inFilter("industryRolesList.id", rolesList));
    }
    if (industryList != null && industryList.length > 0) {
      andFilterBuilder.add(FilterBuilders.inFilter("industryRolesList.industryId", industryList));
    }
    if (salaryRange != null && salaryRange.length > 0) {
      double minSalary =salaryRange[0]!= null ? salaryRange[0] : DefaultConstants.MIN_SALARY;
      double maxSalary = (salaryRange.length > 1 && salaryRange[1] != null) ?
          salaryRange[1] : DefaultConstants.MAX_SALARY;

      if (minSalary > maxSalary) {
        throw new InValidInputException("Please send correct input values");
      }

      andFilterBuilder.add(FilterBuilders.orFilter(
          FilterBuilders.andFilter(
              FilterBuilders.rangeFilter("salaryFrom").gte(minSalary),
              FilterBuilders.rangeFilter("salaryTo").lt(maxSalary)
          ),
          FilterBuilders.termFilter("salarySpecified", Boolean.FALSE)

      ));
    }
    if (experienceRange != null && experienceRange.length > 0) {
      int minExp = experienceRange[0] != null ? experienceRange[0] :DefaultConstants.MIN_EXP_YEAR;
      int maxExp = (experienceRange.length > 1 && experienceRange[1] != null) ? experienceRange[1] :DefaultConstants.MAX_EXP_YEAR;

      if (minExp > maxExp) {
        throw new InValidInputException("Please send correct input values");
      }

      andFilterBuilder.add(FilterBuilders.orFilter(
          FilterBuilders.andFilter(
              FilterBuilders.rangeFilter("from").gte(minExp),
              FilterBuilders.rangeFilter("to").lt(maxExp)
          ),
          FilterBuilders.termFilter("timeSpecified", Boolean.FALSE)

      ));
    }

    // put sorting
    SearchResponse response = ESCacheManager.getInstance().getClient().prepareSearch(INDEX)
        .setPostFilter(andFilterBuilder).addSort("createdDate", SortOrder.DESC)
        .setFrom(pageNo).setSize(perPage).execute().actionGet();

    List<GenericPostResponse> genericPostResponseList = new ArrayList<GenericPostResponse>();
    if (response != null && RestStatus.OK.getStatus() == response.status().getStatus()) {
      SearchHits searchHits = response.getHits();
      totalHits = searchHits.getTotalHits();
      for (SearchHit searchHit : searchHits) {
        FullJobVO fullJobVO = (FullJobVO)getEsCacheService().parseResponse(searchHit, FullJobVO.class);
        if (fullJobVO != null) {
          GenericPostResponse genericPostResponse = new GenericPostResponse();
          genericPostResponse.setPostId(fullJobVO.getPostId());
          genericPostResponse.setSubject(fullJobVO.getSubject());
          //genericPostResponse.setReplyEmail(fullJobVO.getReplyEmail());
          //genericPostResponse.setReplyPhone(fullJobVO.getReplyPhone());
          //genericPostResponse.setReplyWatsApp(fullJobVO.getReplyWatsApp());
          genericPostResponse.setPostedOn(DateUtils.getPostedOnDate(fullJobVO.getCreatedDate()));
          genericPostResponse.setContent(BaseUtil.trimContent(fullJobVO.getContent()));

          // set User
          UserDto userDto = new UserDto();
          userDto.setName(userResponse.getName());
          userDto.setId(userResponse.getId());
          genericPostResponse.setUserDto(userDto);
          genericPostResponseList.add(genericPostResponse);
        }
      }
    }
    jobSearchResponse.setJobCreateResponseList(genericPostResponseList);
    jobSearchResponse.setTotalResults(totalHits);

    // set all cities
    List<CityDto> cityDtoList = new ArrayList<CityDto>();
    List<CityVO> cityVOList = CityCache.getCache().getCityVO();

    for (CityVO cityVO : cityVOList) {
      CityDto cityDto = new CityDto();
      cityDto.setId(cityVO.getId());
      cityDto.setName(cityVO.getName());
      if (locationList != null && locationList.length > 0) {
        boolean isSelected = Arrays.asList(locationList).contains(cityVO.getId());
        cityDto.setSelected(isSelected);
      }
      cityDtoList.add(cityDto);
    }
    jobSearchResponse.setCityDtoList(cityDtoList);

    // set all industry roles
    List<IndustryRolesVO> industryRolesVOList = IndustryRolesCache.getCache().getAllIndustryRolesVO();
    List<IndustryRolesDto> industryRolesDtoList = new ArrayList<IndustryRolesDto>();
    for (IndustryRolesVO industryRolesVO : industryRolesVOList) {
      IndustryRolesDto industryRolesDto = new IndustryRolesDto();
      industryRolesDto.setId(industryRolesVO.getId());
      industryRolesDto.setName(industryRolesVO.getName());
      industryRolesDto.setIndustryId(industryRolesVO.getIndustryId());
      if (rolesList != null && rolesList.length > 0) {
        boolean isSelected = Arrays.asList(rolesList).contains(industryRolesVO.getId());
        industryRolesDto.setSelected(isSelected);
      }
      industryRolesDtoList.add(industryRolesDto);
    }
    jobSearchResponse.setIndustryRolesDtoList(industryRolesDtoList);

    //set all industries
    List<IndustryDto> industryDtoList = new ArrayList<IndustryDto>();
    List<IndustryVO> industryVOList = IndustryCache.getCache().getAllIndustryVO();
    for (IndustryVO industryVO : industryVOList) {
      IndustryDto industryDto = new IndustryDto();
      industryDto.setId(industryVO.getId());
      industryDto.setName(industryVO.getName());
      if (industryList != null && industryList.length > 0) {
        boolean isSelected = Arrays.asList(industryList).contains(industryVO.getId());
        industryDto.setSelected(isSelected);
      }
      industryDtoList.add(industryDto);
    }
    jobSearchResponse.setIndustryDtoList(industryDtoList);


    return jobSearchResponse;
  }

  private JobResponse createJobResponse(FullJobVO fullJobVO, UserResponse userResponse) {
    JobResponse jobResponse = new JobResponse();
    jobResponse.setPostId(fullJobVO.getPostId());
    jobResponse.setSubject(fullJobVO.getSubject());
    jobResponse.setContent(fullJobVO.getContent());
    jobResponse.setTo(fullJobVO.getTo());
    jobResponse.setFrom(fullJobVO.getFrom());
    jobResponse.setSalaryFrom(fullJobVO.getSalaryFrom());
    jobResponse.setSalaryTo(fullJobVO.getSalaryTo());
    jobResponse.setPostedOn(DateUtils.getPostedOnDate(fullJobVO.getCreatedDate()));
    //jobResponse.setReplyEmail(fullJobVO.getReplyEmail());
    //jobResponse.setReplyPhone(fullJobVO.getReplyPhone());
    //jobResponse.setReplyWatsApp(fullJobVO.getReplyWatsApp());

    ReplyDto replyDto = new ReplyDto(-1, -1, -1);
    if (StringUtils.isNotBlank(fullJobVO.getReplyEmail())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_EMAIL.getId());
    }
    if (StringUtils.isNotBlank(fullJobVO.getReplyPhone())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_PHONE.getId());
    }
    if (StringUtils.isNotBlank(fullJobVO.getReplyWatsApp())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_WATSAPP.getId());
    }
    jobResponse.setReplyDto(replyDto);

    jobResponse.setShareDto(fullJobVO.getShareDto());

    jobResponse.setLocations(fullJobVO.getLocationList());
    jobResponse.setIndustryRolesDtoList(fullJobVO.getIndustryRolesList());
    UserDto userDto = new UserDto();
    userDto.setId(userResponse.getId());
    userDto.setName(userResponse.getName());
    jobResponse.setUserDto(userDto);
    return jobResponse;
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

  public ESCacheService getEsCacheService() {
    return esCacheService;
  }
}
