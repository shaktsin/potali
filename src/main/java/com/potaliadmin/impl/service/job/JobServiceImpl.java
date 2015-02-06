package com.potaliadmin.impl.service.job;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.image.EnumBucket;
import com.potaliadmin.constants.json.DtoJsonConstants;
import com.potaliadmin.constants.post.EnumPostType;
import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.domain.industry.Industry;
import com.potaliadmin.domain.job.Job;
import com.potaliadmin.domain.post.PostBlob;
import com.potaliadmin.dto.internal.cache.address.CityVO;
import com.potaliadmin.dto.internal.cache.es.job.*;
import com.potaliadmin.dto.internal.cache.job.IndustryRolesVO;
import com.potaliadmin.dto.internal.cache.job.IndustryVO;
import com.potaliadmin.dto.internal.filter.JobFilterDto;
import com.potaliadmin.dto.internal.image.CreateImageResponseDto;
import com.potaliadmin.dto.web.request.jobs.JobCreateRequest;
import com.potaliadmin.dto.web.response.circle.CircleDto;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;
import com.potaliadmin.dto.web.response.job.PrepareJobCreateResponse;
import com.potaliadmin.dto.web.response.post.CommentListResponse;
import com.potaliadmin.dto.web.response.post.CommentResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;
import com.potaliadmin.dto.web.response.post.ReplyDto;
import com.potaliadmin.dto.web.response.user.UserDto;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.framework.cache.ESCacheManager;
import com.potaliadmin.framework.cache.address.CityCache;
import com.potaliadmin.framework.cache.industry.IndustryCache;
import com.potaliadmin.framework.cache.industry.IndustryRolesCache;
import com.potaliadmin.framework.elasticsearch.BaseESService;
import com.potaliadmin.framework.elasticsearch.ESSearchFilter;
import com.potaliadmin.framework.elasticsearch.response.ESSearchResponse;
import com.potaliadmin.pact.dao.job.JobDao;
import com.potaliadmin.pact.dao.post.PostBlobDao;
import com.potaliadmin.pact.dao.post.PostCommentDao;
import com.potaliadmin.pact.framework.aws.UploadService;
import com.potaliadmin.pact.service.cache.ESCacheService;
import com.potaliadmin.pact.service.job.JobService;
import com.potaliadmin.pact.service.post.PostService;
import com.potaliadmin.pact.service.users.LoginService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.util.BaseUtil;
import com.potaliadmin.util.DateUtils;
import com.potaliadmin.vo.BaseElasticVO;
import com.potaliadmin.vo.circle.CircleVO;
import com.potaliadmin.vo.comment.CommentVO;
import com.potaliadmin.vo.job.JobVO;
import com.potaliadmin.vo.post.PostVO;
import com.potaliadmin.vo.user.UserVO;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.max.MaxBuilder;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregator;
import org.elasticsearch.search.aggregations.metrics.min.MinBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

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
  @Autowired
  BaseESService baseESService;

  @Autowired
  UserService userService;
  @Autowired
  PostService postService;
  @Autowired
  PostCommentDao postCommentDao;
  @Autowired
  UploadService uploadService;

  private static final String INDEX = "ofc";
  private static final String JOB = "job";
  private static final String POST_REACTIONS = "post_reactions";
  private static final Long[] REMOVE_LIST = {EnumReactions.HIDE_THIS_POST.getId(), EnumReactions.MARK_AS_SPAM.getId()};


  @Override
  public PrepareJobCreateResponse prepareJobCreateRequest() {
    UserResponse userResponse = getLoginService().getLoggedInUser();

    List<CityDto> cityDtoList = new ArrayList<CityDto>();
    List<CityVO> cityVOList = CityCache.getCache().getCityVO();
    for (CityVO cityVO : cityVOList) {
      CityDto cityDto = new CityDto();
      cityDto.setId(cityVO.getId());
      cityDto.setName(cityVO.getName());
      cityDtoList.add(cityDto);
    }

    //List<IndustryRolesVO> industryRolesVOList = IndustryRolesCache.getCache().getAllIndustryRolesVO();

    /*for (IndustryRolesVO industryRolesVO : industryRolesVOList) {
      IndustryRolesDto industryRolesDto = new IndustryRolesDto();
      industryRolesDto.setId(industryRolesVO.getId());
      industryRolesDto.setName(industryRolesVO.getName());
      industryRolesDto.setIndustryId(industryRolesVO.getId());
      industryRolesDtoList.add(industryRolesDto);
    }*/

    List<IndustryDto> industryDtoList = new ArrayList<IndustryDto>();
    List<IndustryVO> industryVOList = IndustryCache.getCache().getAllIndustryVO();
    for (IndustryVO industryVO : industryVOList) {
      IndustryDto industryDto = new IndustryDto();
      industryDto.setId(industryVO.getId());
      industryDto.setName(industryVO.getName());
      industryDtoList.add(industryDto);

      // get all roles
      List<Long> rolesList = IndustryCache.getCache().getIndustryRolesListFromIndustryId(industryVO.getId());
      List<IndustryRolesDto> industryRolesDtoList = new ArrayList<IndustryRolesDto>();
      for (Long rolesId : rolesList) {
        IndustryRolesVO industryRolesVO = IndustryRolesCache.getCache().getIndustryRolesVO(rolesId);
        IndustryRolesDto industryRolesDto = new IndustryRolesDto();
        industryRolesDto.setId(industryRolesVO.getId());
        industryRolesDto.setName(industryRolesVO.getName());
        industryRolesDto.setIndustryId(industryRolesVO.getId());
        industryRolesDtoList.add(industryRolesDto);
      }
      industryDto.setIndustryRolesDtoList(industryRolesDtoList);
    }


    UserVO userVO = (UserVO) getBaseESService().get(userResponse.getId(), null, UserVO.class);
    if (userVO == null) {
      PrepareJobCreateResponse prepareJobCreateResponse = new PrepareJobCreateResponse();
      prepareJobCreateResponse.setException(true);
      prepareJobCreateResponse.addMessage("Some exception occurred, please try again!");
      return prepareJobCreateResponse;
    }

    List<Long> circleList = userVO.getCircleList();
    List<CircleDto> circleDtoList = new ArrayList<CircleDto>();
    for (Long circle : circleList) {
      CircleVO circleVO = (CircleVO) getBaseESService().get(circle, null, CircleVO.class);
      if (circleVO != null) {
        CircleDto circleDto = new CircleDto();
        circleDto.setId(circleVO.getId());
        circleDto.setName(circleVO.getName());
        circleDto.setSelected(false);
        circleDtoList.add(circleDto);
      }
    }


    PrepareJobCreateResponse prepareJobCreateResponse = new PrepareJobCreateResponse();
    prepareJobCreateResponse.setCityDtoList(cityDtoList);
    prepareJobCreateResponse.setIndustryDtoList(industryDtoList);
    //prepareJobCreateResponse.setIndustryRolesDtoList(industryRolesDtoList);
    prepareJobCreateResponse.setReplyEmail(getUserService().findById(userResponse.getId()).getEmail());
    prepareJobCreateResponse.setCircleDtoList(circleDtoList);

    return prepareJobCreateResponse;
  }

  @Transactional
  public JobResponse createJob(JobCreateRequest jobCreateRequest,List<FormDataBodyPart> imgFiles,FormDataBodyPart jFile) {
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

    // now upload images, trim images and upload them to amazon
    List<CreateImageResponseDto> imageResponseDtoList = null;
    if (imgFiles != null && !imgFiles.isEmpty()) {
      imageResponseDtoList = getPostService().postImages(imgFiles, job.getId());
      if (imageResponseDtoList == null || imageResponseDtoList.isEmpty()) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setException(Boolean.TRUE);
        jobResponse.addMessage("Some Internal Exception Occurred!");
        return jobResponse;
      }
    }


    PostVO postVO = new PostVO(job, postBlob);
    postVO.setPostType(EnumPostType.JOBS.getId());

    // set images link
    if (imageResponseDtoList != null) {
      List<String> imageLinks = new ArrayList<String>();
      for (CreateImageResponseDto createImageResponseDto : imageResponseDtoList) {
        String imageLink = getUploadService().getCanonicalPathOfResource(EnumBucket.POST_BUCKET.getName(),
            createImageResponseDto.getPath());
        imageLinks.add(imageLink);
      }
      postVO.setImageList(imageLinks);
    }

    // set circle
    List<CircleVO> circleVOList = new ArrayList<CircleVO>();
    for (Long circleId : jobCreateRequest.getCircleList()) {
      CircleVO circleVO = (CircleVO) getBaseESService().get(circleId, null , CircleVO.class);
      circleVOList.add(circleVO);
    }
    postVO.setCircleList(circleVOList);

    boolean published = getBaseESService().put(postVO);
    if (published) {
      JobVO jobVO = new JobVO(job);
      boolean isJobPublished = getBaseESService().put(jobVO);
      if (!isJobPublished) {
        getBaseESService().delete(postVO.getId(), PostVO.class);
        JobResponse jobResponse = new JobResponse();
        jobResponse.setException(true);
        jobResponse.addMessage("Something unexpected occurred ! Try Again");
        return jobResponse;
      }
      return createJobResponse(postVO, jobVO, userResponse);
    } else {
      JobResponse jobResponse = new JobResponse();
      jobResponse.setException(true);
      jobResponse.addMessage("Something unexpected occurred ! Try Again");
      return jobResponse;
    }


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

    PostVO postVO = (PostVO)getBaseESService().get(postId, null,PostVO.class);
    JobVO jobVO = (JobVO)getBaseESService().get(postId, postId,JobVO.class);
    if (postVO != null && jobVO != null) {
      JobResponse jobResponse = createJobResponse(postVO, jobVO, userResponse);

      boolean hasComments = getPostService().postHasComments(postId);
      if (hasComments) {
        TermFilterBuilder termFilterBuilder = FilterBuilders.termFilter("parentId", postVO.getPostId());

        ESSearchFilter esSearchFilter =
            new ESSearchFilter().setFilterBuilder(termFilterBuilder).addSortedMap("id", SortOrder.DESC)
                .setPageNo(DefaultConstants.AND_APP_PAGE_NO).setPerPage(DefaultConstants.AND_APP_PER_PAGE);

        ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, CommentVO.class);
        if (esSearchResponse.getTotalResults() > 0) {
          CommentListResponse commentListResponse = new CommentListResponse();
          List<CommentResponse> commentResponses = new ArrayList<CommentResponse>();
          for (BaseElasticVO baseElasticVO : esSearchResponse.getBaseElasticVOs()) {
            CommentVO commentVO = (CommentVO) baseElasticVO;
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setCommentId(commentVO.getId());
            commentResponse.setContent(commentVO.getComment());
            UserResponse commentUser = getUserService().findById(commentVO.getUserId());
            UserDto userDto = new UserDto(commentUser);
            commentResponse.setUserDto(userDto);
            commentResponse.setPostId(Long.parseLong(commentVO.getParentId()));
            commentResponse.setCommentedOn(DateUtils.getPostedOnDate(commentVO.getCommentedOn()));
            commentResponses.add(commentResponse);
          }
          commentListResponse.setReorderCommentResponse(commentResponses);
          commentListResponse.setPageNo(DefaultConstants.AND_APP_PAGE_NO);
          commentListResponse.setPerPage(DefaultConstants.AND_APP_PER_PAGE);
          commentListResponse.setTotalResults(esSearchResponse.getTotalResults());
          jobResponse.setCommentListResponse(commentListResponse);
        }
      }
      return jobResponse;
    } else {
      JobResponse jobResponse = new JobResponse();
      jobResponse.setException(true);
      jobResponse.addMessage("Something unexpected occurred ! Try Again");
      return jobResponse;
    }
  }


  public JobSearchResponse searchJob(Long[] circleList,Long[] locationList, Long[] rolesList,Long[] industryList,
                                     Double[] salaryRange,Integer[] experienceRange, EnumSearchOperation searchOperation,
                                     Long postId,int perPage, int pageNo) {

    long totalHits=0;

    UserResponse userResponse = getLoginService().getLoggedInUser();

    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    JobSearchResponse jobSearchResponse= new JobSearchResponse();
    AndFilterBuilder andFilterBuilder = new AndFilterBuilder();
    andFilterBuilder.add(FilterBuilders.termFilter("userInstituteId", userResponse.getInstituteId()));
    // load more
    if (searchOperation != null && postId != null) {
      if (EnumSearchOperation.NEWER.getId() == searchOperation.getId()) {
        andFilterBuilder.add(FilterBuilders.rangeFilter("postId").gt(postId).lte(postId+perPage));
      } else {
        andFilterBuilder.add(FilterBuilders.rangeFilter("postId").lt(postId).gte(postId-perPage));
      }
    }

    // hide all mark as hidden and spam posts from list
    OrFilterBuilder orFilterBuilder = new OrFilterBuilder();
    //NotFilterBuilder notFilterBuilder = new NotFilterBuilder()
    MatchAllFilterBuilder matchAllFilterBuilder = new MatchAllFilterBuilder();
    HasChildFilterBuilder hasChildFilterBuilder = new HasChildFilterBuilder(POST_REACTIONS, matchAllFilterBuilder);
    orFilterBuilder.add(FilterBuilders.notFilter(hasChildFilterBuilder));
    //orFilterBuilder.add(FilterBuilders.notFilter(FilterBuilders.termFilter("userId", userResponse.getId())));


    BoolFilterBuilder boolFilterBuilder = new BoolFilterBuilder();
    boolFilterBuilder.mustNot(FilterBuilders.inFilter("reactionId", REMOVE_LIST));
    boolFilterBuilder.must(FilterBuilders.termFilter("userId", userResponse.getId()));
    orFilterBuilder.add(FilterBuilders.hasChildFilter(POST_REACTIONS, boolFilterBuilder));
    andFilterBuilder.add(orFilterBuilder);


    if (locationList != null && locationList.length > 0) {
      TermsFilterBuilder locationFilter = FilterBuilders.inFilter("locationList.id", locationList);
      HasChildFilterBuilder hasLocationChild = FilterBuilders.hasChildFilter(JOB, locationFilter);
      andFilterBuilder.add(hasLocationChild);
      //Arrays.asList(locationList);
    }
    if (rolesList != null && rolesList.length > 0) {
      TermsFilterBuilder rolesFilter = FilterBuilders.inFilter("industryRolesList.id", rolesList);
      HasChildFilterBuilder hasRolesChild = FilterBuilders.hasChildFilter(JOB, rolesFilter);
      andFilterBuilder.add(hasRolesChild);
      //andFilterBuilder.add(FilterBuilders.inFilter("industryRolesList.id", rolesList));
    }
    if (industryList != null && industryList.length > 0) {
      TermsFilterBuilder industryFilter = FilterBuilders.inFilter("industryRolesList.id", rolesList);
      HasChildFilterBuilder hasIndustryChild = FilterBuilders.hasChildFilter(JOB, industryFilter);
      andFilterBuilder.add(hasIndustryChild);
      //andFilterBuilder.add(FilterBuilders.inFilter("industryRolesList.industryId", industryList));
    }

    if (circleList != null && circleList.length > 0) {
      andFilterBuilder.add(FilterBuilders.inFilter("circleList.id", circleList));
    } else {
      if (userResponse.getCircleList() != null && userResponse.getCircleList().size() > 0) {
        //Long[] circleArrayList = (Long[])userResponse.getCircleList().toArray();
        //if (circleList != null && circleList.)
        andFilterBuilder.add(FilterBuilders.inFilter("circleList.id", userResponse.getCircleList().toArray()));
      }
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


    ESSearchFilter esSearchFilter = new ESSearchFilter().setFilterBuilder(andFilterBuilder)
        .addSortedMap("id", SortOrder.DESC).setPageNo(pageNo).setPerPage(perPage);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, PostVO.class);

    List<BaseElasticVO> postVOList = esSearchResponse.getBaseElasticVOs();

    List<GenericPostResponse> genericPostResponseList = new ArrayList<GenericPostResponse>();
    for (BaseElasticVO baseElasticVO : postVOList) {
      PostVO postVO = (PostVO) baseElasticVO;
      UserResponse postUser = getUserService().findById(postVO.getUserId());
      GenericPostResponse genericPostResponse = new GenericPostResponse(postVO, postUser);
      boolean isImp = getPostService().isPostImportantForUser(postVO.getPostId(), userResponse.getId());
      genericPostResponse.setImportant(isImp);
      genericPostResponseList.add(genericPostResponse);
    }
    jobSearchResponse.setJobCreateResponseList(genericPostResponseList);
    jobSearchResponse.setTotalResults(esSearchResponse.getTotalResults());
    jobSearchResponse.setPerPage(perPage);
    jobSearchResponse.setPageNo(pageNo);

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

  @Override
  public JobFilterDto getJobFilters(UserResponse userResponse) {
    JobFilterDto jobFilterDto = new JobFilterDto(DtoJsonConstants.JOBS);

    List<CityDto> cityDtoList = new ArrayList<CityDto>();
    List<CityVO> cityVOList = CityCache.getCache().getCityVO();
    for (CityVO cityVO : cityVOList) {
      CityDto cityDto = new CityDto();
      cityDto.setId(cityVO.getId());
      cityDto.setName(cityVO.getName());
      cityDtoList.add(cityDto);
    }

    //List<IndustryRolesVO> industryRolesVOList = IndustryRolesCache.getCache().getAllIndustryRolesVO();

    /*for (IndustryRolesVO industryRolesVO : industryRolesVOList) {
      IndustryRolesDto industryRolesDto = new IndustryRolesDto();
      industryRolesDto.setId(industryRolesVO.getId());
      industryRolesDto.setName(industryRolesVO.getName());
      industryRolesDto.setIndustryId(industryRolesVO.getId());
      industryRolesDtoList.add(industryRolesDto);
    }*/

    List<IndustryDto> industryDtoList = new ArrayList<IndustryDto>();
    List<IndustryVO> industryVOList = IndustryCache.getCache().getAllIndustryVO();
    List<IndustryRolesDto> industryRolesDtoList = new ArrayList<IndustryRolesDto>();
    for (IndustryVO industryVO : industryVOList) {
      IndustryDto industryDto = new IndustryDto();
      industryDto.setId(industryVO.getId());
      industryDto.setName(industryVO.getName());
      industryDtoList.add(industryDto);

      // get all roles
      List<Long> rolesList = IndustryCache.getCache().getIndustryRolesListFromIndustryId(industryVO.getId());

      for (Long rolesId : rolesList) {
        IndustryRolesVO industryRolesVO = IndustryRolesCache.getCache().getIndustryRolesVO(rolesId);
        IndustryRolesDto industryRolesDto = new IndustryRolesDto();
        industryRolesDto.setId(industryRolesVO.getId());
        industryRolesDto.setName(industryRolesVO.getName());
        industryRolesDto.setIndustryId(industryRolesVO.getId());
        industryRolesDtoList.add(industryRolesDto);
      }
      industryDto.setIndustryRolesDtoList(industryRolesDtoList);
    }


    UserVO userVO = (UserVO) getBaseESService().get(userResponse.getId(), null, UserVO.class);
    if (userVO == null) {
      return jobFilterDto;
    }

    List<Long> circleList = userVO.getCircleList();
    List<CircleDto> circleDtoList = new ArrayList<CircleDto>();
    for (Long circle : circleList) {
      CircleVO circleVO = (CircleVO) getBaseESService().get(circle, null, CircleVO.class);
      if (circleVO != null) {
        CircleDto circleDto = new CircleDto();
        circleDto.setId(circleVO.getId());
        circleDto.setName(circleVO.getName());
        circleDto.setSelected(false);
        circleDtoList.add(circleDto);
      }
    }

    // max salary and min salary, max exp and min exp
    MinBuilder minFrom = AggregationBuilders.min("from_aggs").field("from");
    MaxBuilder maxTo = AggregationBuilders.max("to_aggs").field("to");

    MinBuilder salaryMin = AggregationBuilders.min("salary_from").field("salaryFrom");
    MaxBuilder salaryMax = AggregationBuilders.max("salary_to").field("salaryTo");

    SearchResponse searchResponse = ESCacheManager.getInstance().getClient().prepareSearch("job")
        .setQuery(QueryBuilders.matchAllQuery()).addAggregation(minFrom).addAggregation(maxTo)
        .addAggregation(salaryMin).addAggregation(salaryMax).execute().actionGet();

    jobFilterDto.setCityList(cityDtoList);
    jobFilterDto.setIndList(industryDtoList);
    jobFilterDto.setRoDtoList(industryRolesDtoList);

    if (searchResponse.status().getStatus() == HttpStatus.OK.value()) {
      Min minExp = searchResponse.getAggregations().get("from_aggs");
      Max maxExp = searchResponse.getAggregations().get("to_aggs");
      Min minSal = searchResponse.getAggregations().get("salary_from");
      Max maxSal = searchResponse.getAggregations().get("salary_to");

      SalaryRangeDto salaryRangeDto = new SalaryRangeDto(DtoJsonConstants.SALARY);
      salaryRangeDto.setMin(minSal.getValue());
      salaryRangeDto.setMax(maxSal.getValue());


      ExperienceRangeDto experienceRangeDto = new ExperienceRangeDto(DtoJsonConstants.EXPERIENCE);
      experienceRangeDto.setMin(new Double(minExp.getValue()).intValue());
      experienceRangeDto.setMax(new Double(maxExp.getValue()).intValue());

      Map<String, BaseRangeDto> rangeDtoMap = new HashMap<String, BaseRangeDto>();
      rangeDtoMap.put(salaryRangeDto.getName(), salaryRangeDto);
      rangeDtoMap.put(experienceRangeDto.getName(), experienceRangeDto);

      jobFilterDto.setRangeDtoMap(rangeDtoMap);
    }

    return jobFilterDto;
  }

  //TODO: Depricate it
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


  private JobResponse createJobResponse(PostVO postVO, JobVO jobVO, UserResponse userResponse) {
    JobResponse jobResponse = new JobResponse();
    jobResponse.setPostId(postVO.getPostId());
    jobResponse.setSubject(postVO.getSubject());
    jobResponse.setContent(postVO.getContent());
    jobResponse.setTo(jobVO.getTo());
    jobResponse.setFrom(jobVO.getFrom());
    jobResponse.setSalaryFrom(jobVO.getSalaryFrom());
    jobResponse.setSalaryTo(jobVO.getSalaryTo());
    jobResponse.setPostedOn(DateUtils.getPostedOnDate(postVO.getCreatedDate()));
    //jobResponse.setReplyEmail(fullJobVO.getReplyEmail());
    //jobResponse.setReplyPhone(fullJobVO.getReplyPhone());
    //jobResponse.setReplyWatsApp(fullJobVO.getReplyWatsApp());

    ReplyDto replyDto = new ReplyDto(-1, -1, -1);
    if (StringUtils.isNotBlank(postVO.getReplyEmail())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_EMAIL.getId());
    }
    if (StringUtils.isNotBlank(postVO.getReplyPhone())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_PHONE.getId());
    }
    if (StringUtils.isNotBlank(postVO.getReplyWatsApp())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_WATSAPP.getId());
    }
    jobResponse.setReplyDto(replyDto);

    jobResponse.setShareDto(postVO.getShareDto());

    jobResponse.setLocations(jobVO.getLocationList());
    jobResponse.setIndustryRolesDtoList(jobVO.getIndustryRolesList());
    UserDto userDto = new UserDto();
    userDto.setId(userResponse.getId());
    userDto.setName(userResponse.getName());
    jobResponse.setUserDto(userDto);

    jobResponse.setImages(postVO.getImageList());
    List<CircleVO> circleVOs = postVO.getCircleList();

    List<CircleDto> circleDtoList = new ArrayList<CircleDto>();
    for (CircleVO circleVO : circleVOs) {
      CircleDto circleDto = new CircleDto();
      circleDto.setId(circleVO.getId());
      circleDto.setName(circleVO.getName());
      circleDto.setSelected(false);
      circleDtoList.add(circleDto);
    }

    jobResponse.setCircleDtoList(circleDtoList);
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

  public UserService getUserService() {
    return userService;
  }

  public BaseESService getBaseESService() {
    return baseESService;
  }

  public PostService getPostService() {
    return postService;
  }

  public PostCommentDao getPostCommentDao() {
    return postCommentDao;
  }

  public UploadService getUploadService() {
    return uploadService;
  }
}
