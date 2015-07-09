package com.potaliadmin.impl.service.circle;

import com.potaliadmin.constants.circle.CircleType;
import com.potaliadmin.domain.circle.Circle;
import com.potaliadmin.domain.user.UserCircleMapping;
import com.potaliadmin.dto.web.request.circle.CircleAuthorizeRequest;
import com.potaliadmin.dto.web.request.circle.CircleCreateRequest;
import com.potaliadmin.dto.web.request.circle.CircleGetRequest;
import com.potaliadmin.dto.web.request.circle.CircleJoinListRequest;
import com.potaliadmin.dto.web.request.circle.CircleJoinRequest;
import com.potaliadmin.dto.web.response.base.GenericSuccessResponse;
import com.potaliadmin.dto.web.response.circle.CircleDto;
import com.potaliadmin.dto.web.response.circle.CircleGetResponse;
import com.potaliadmin.dto.web.response.circle.CircleRequestListResponse;
import com.potaliadmin.dto.web.response.circle.CreateCircleResponse;
import com.potaliadmin.dto.web.response.user.UserDto;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.exceptions.UnAuthorizedAccessException;
import com.potaliadmin.framework.elasticsearch.BaseESService;
import com.potaliadmin.framework.elasticsearch.ESSearchFilter;
import com.potaliadmin.framework.elasticsearch.response.ESSearchResponse;
import com.potaliadmin.pact.dao.circle.CircleDao;
import com.potaliadmin.pact.service.circle.CircleService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.util.BaseUtil;
import com.potaliadmin.vo.BaseElasticVO;
import com.potaliadmin.vo.circle.CircleVO;
import com.potaliadmin.vo.post.PostVO;
import com.potaliadmin.vo.user.UserVO;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.NotFilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shakti on 28/1/15.
 */
@Service
public class CircleServiceImpl implements CircleService {

  @Autowired
  UserService userService;
  @Autowired
  CircleDao circleDao;
  @Autowired
  BaseESService baseESService;

  @Override
  @Transactional
  public CreateCircleResponse createCircle(CircleCreateRequest circleCreateRequest) {
    if (!circleCreateRequest.validate()) {
      throw new InValidInputException("Please input valid parameters");
    }

    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access!");
    }

    //Circle circle = getCircleService().createCircle(circleCreateRequest);
    Circle circle = getCircleDao().createCircle(circleCreateRequest.getName(), circleCreateRequest.getDesc(),
        CircleType.getById(circleCreateRequest.getCircleId()), userResponse, false);

    CircleVO circleVO = new CircleVO(circle);
    circleVO.setAdmin(userResponse.getId());
    circleVO.setInstituteId(userResponse.getInstituteId());
    circleVO.setActive(true);


    boolean published = getBaseESService().put(circleVO);
    if (!published) {
      throw new PotaliRuntimeException("Couldn't create circle, Please Try Again!");
    }

    UserVO userVO = (UserVO) getBaseESService().get(userResponse.getId(), null , UserVO.class);
    if (userVO != null) {
      List<Long> circleList = userVO.getCircleList();
      if (circleList == null) {
        circleList = new ArrayList<Long>();
      }
      circleList.add(circleVO.getId());
      userVO.setCircleList(circleList);
      published = getBaseESService().put(userVO);
      if (!published) {
        getBaseESService().delete(circleVO.getId(), CircleVO.class);
        throw new PotaliRuntimeException("Couldn't create circle, Please Try Again!");
      }
    }

    CreateCircleResponse createCircleResponse = new CreateCircleResponse();
    createCircleResponse.setCircleId(circleVO.getId());
    createCircleResponse.setName(circleVO.getName());
    createCircleResponse.setModerate(circleVO.isModerate());


    return createCircleResponse;
  }

  @Override
  @Transactional
  public GenericSuccessResponse joinCircle(CircleJoinRequest circleJoinRequest) {
    GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
    if (!circleJoinRequest.validate()) {
      throw new InValidInputException("Please input valid parameters");
    }
    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access!");
    }
    CircleVO circleVO = (CircleVO)
        getBaseESService().get(circleJoinRequest.getCircleId(), null, CircleVO.class);

    if (circleVO == null) {
      throw new PotaliRuntimeException("No Circle found with Id "+circleJoinRequest.getCircleId());
    }

    if (!circleVO.getInstituteId().equals(userResponse.getInstituteId())) {
      throw new PotaliRuntimeException("Circle belongs to some other institute");
    }

    if (!circleVO.isActive()) {
      throw new PotaliRuntimeException("Circle is no more active");
    }

    UserCircleMapping userCircleMapping =
        getCircleDao().joinCircle(userResponse, circleJoinRequest.getCircleId(), !circleVO.isModerate(),false);

    if (userCircleMapping == null) {
      throw new PotaliRuntimeException("Couldn't create circle, Please Try Again!");
    }

    if (!circleVO.isModerate()) {
      UserVO userVO = (UserVO) getBaseESService().get(userResponse.getId(), null , UserVO.class);
      if (userVO != null) {
        List<Long> circleList = userVO.getCircleList();
        if (circleList == null) {
          circleList = new ArrayList<Long>();
        }
        circleList.add(circleVO.getId());
        userVO.setCircleList(circleList);
        boolean published = getBaseESService().put(userVO);
        if (!published) {
          throw new PotaliRuntimeException("Couldn't create circle, Please Try Again!");
        }

        genericSuccessResponse.setSuccess(true);
      }
    } else {
      List<Long> requestList = circleVO.getRequestList();
      if (requestList == null || requestList.isEmpty()) {
        requestList = new ArrayList<Long>();
      }
      requestList.add(userResponse.getId());
      circleVO.setRequestList(requestList);

      boolean published = getBaseESService().put(circleVO);
      if (!published) {
        throw new PotaliRuntimeException("Couldn't create circle, Please Try Again!");
      }

      genericSuccessResponse.setSuccess(true);
    }

    /*CircleGetResponse circleGetResponse = new CircleGetResponse();

    if (genericSuccessResponse.isSuccess()) {
      CircleGetRequest circleGetRequest = new CircleGetRequest();
      circleGetRequest.setCircleId(circleVO.getType());

      circleGetResponse = fetchAllCircle(circleGetRequest);
    }*/

    return genericSuccessResponse;
  }

  @Override
  @Transactional
  public GenericSuccessResponse authorizeCircle(CircleAuthorizeRequest circleAuthorizeRequest) {
    if (!circleAuthorizeRequest.validate()) {
      throw new InValidInputException("Please input valid parameters");
    }
    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access!");
    }
    CircleVO circleVO = (CircleVO)
        getBaseESService().get(circleAuthorizeRequest.getCircleId(), null, CircleVO.class);

    if (circleVO == null) {
      throw new PotaliRuntimeException("No Circle found with Id "+circleAuthorizeRequest.getCircleId());
    }

    if (!circleVO.getInstituteId().equals(userResponse.getInstituteId())) {
      throw new PotaliRuntimeException("Circle belongs to some other institute");
    }

    UserResponse requestUser = getUserService().findById(circleAuthorizeRequest.getUserId());
    if (requestUser == null) {
      throw new PotaliRuntimeException("A ghost cannot join circle");
    }

    if (!circleVO.getInstituteId().equals(requestUser.getInstituteId())) {
      throw new PotaliRuntimeException("Circle belongs to some other institute");
    }

    UserCircleMapping userCircleMapping =
        (UserCircleMapping) getCircleDao().findByNamedQueryAndNamedParam("findByUserAndCircle",
            new String[]{"userId", "circleId"}, new Object[]{requestUser.getId(),circleVO.getId()});


    if (userCircleMapping == null) {
      throw new PotaliRuntimeException("User has never requested to join this circle");
    }

    userCircleMapping.setAuthorised(true);
    getCircleDao().save(userCircleMapping);

    GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
    genericSuccessResponse.setSuccess(false);
    UserVO userVO = (UserVO) getBaseESService().get(userResponse.getId(), null , UserVO.class);
    if (userVO != null) {
      List<Long> circleList = userVO.getCircleList();
      if (circleList == null) {
        circleList = new ArrayList<Long>();
      }
      circleList.add(circleVO.getId());
      userVO.setCircleList(circleList);
      boolean published = getBaseESService().put(userVO);
      if (!published) {
        throw new PotaliRuntimeException("Couldn't create circle, Please Try Again!");
      }

      genericSuccessResponse.setSuccess(true);
    }


    genericSuccessResponse.setSuccess(true);

    return genericSuccessResponse;
  }

  @Override
  public GenericSuccessResponse authorizeRevokeCircle(CircleAuthorizeRequest circleAuthorizeRequest) {
    GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
    genericSuccessResponse.setSuccess(false);
    if (!circleAuthorizeRequest.validate()) {
      throw new InValidInputException("Please input valid parameters");
    }
    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access!");
    }
    CircleVO circleVO = (CircleVO)
        getBaseESService().get(circleAuthorizeRequest.getCircleId(), null, CircleVO.class);

    if (circleVO == null) {
      throw new PotaliRuntimeException("No Circle found with Id "+circleAuthorizeRequest.getCircleId());
    }

    if (!circleVO.getInstituteId().equals(userResponse.getInstituteId())) {
      throw new PotaliRuntimeException("Circle belongs to some other institute");
    }

    UserResponse requestUser = getUserService().findById(circleAuthorizeRequest.getUserId());
    if (requestUser == null) {
      throw new PotaliRuntimeException("A ghost cannot join circle");
    }

    if (!circleVO.getInstituteId().equals(requestUser.getInstituteId())) {
      throw new PotaliRuntimeException("Circle belongs to some other institute");
    }

    UserCircleMapping userCircleMapping =
        (UserCircleMapping) getCircleDao().findByNamedQueryAndNamedParam("findByUserAndCircle",
            new String[]{"userId", "circleId"}, new Object[]{requestUser.getId(),circleVO.getId()});


    if (userCircleMapping == null) {
      throw new PotaliRuntimeException("User has never requested to join this circle");
    }

    List<Long> requestList = circleVO.getRequestList();
    if (requestList == null || requestList.isEmpty()) {
      requestList = new ArrayList<Long>();
    }
    requestList.remove(userResponse.getId());
    circleVO.setRequestList(requestList);

    boolean published = getBaseESService().put(circleVO);
    if (!published) {
      throw new PotaliRuntimeException("Couldn't create circle, Please Try Again!");
    }

    circleDao.delete(userCircleMapping);
    genericSuccessResponse.setSuccess(true);

    return genericSuccessResponse;
  }

  @Override
  public CircleGetResponse fetchAllCircle(CircleGetRequest circleGetRequest) {

    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access!");
    }

    if (circleGetRequest.getCircleId() == null) {
      circleGetRequest.setCircleId(CircleType.CLUB.getId());
    }

    AndFilterBuilder andFilterBuilder = FilterBuilders.andFilter();
    BoolFilterBuilder boolFilterBuilder = FilterBuilders.boolFilter();
    boolFilterBuilder.must(FilterBuilders.termFilter("instituteId", userResponse.getInstituteId()));
    boolFilterBuilder.must(FilterBuilders.termFilter("type", circleGetRequest.getCircleId()));

    andFilterBuilder.add(boolFilterBuilder);
    NotFilterBuilder notFilterBuilder = FilterBuilders.notFilter(
        FilterBuilders.inFilter("id", userResponse.getCircleList().toArray())
    );

    andFilterBuilder.add(notFilterBuilder);

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(andFilterBuilder)
            .addSortedMap("id", SortOrder.DESC).setPageNo(circleGetRequest.getPageNo())
            .setPerPage(circleGetRequest.getPerPage());


    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, CircleVO.class);
    List<BaseElasticVO> baseElasticVOs = esSearchResponse.getBaseElasticVOs();
    List<CircleDto> circleDtoList = new ArrayList<CircleDto>();
    for (BaseElasticVO baseElasticVO : baseElasticVOs) {
      CircleVO circleVO = (CircleVO) baseElasticVO;

      if (!circleVO.isActive()) {
        continue;
      }

      // calculate no of members
      QueryBuilder queryBuilder = QueryBuilders.termQuery("circleList", circleVO.getId());
      long members = getBaseESService().count(queryBuilder, UserVO.class);

      // calculate number of posts
      queryBuilder = QueryBuilders.termQuery("circleList.id", circleVO.getId());
      long posts = getBaseESService().count(queryBuilder, PostVO.class);

      CircleDto circleDto = new CircleDto();
      circleDto.setId(circleVO.getId());
      circleDto.setName(circleVO.getName());
      circleDto.setJoined(false);

      if (circleVO.getAdmin().equals(userResponse.getId())) {
        circleDto.setAdmin(true);
        if (circleVO.getRequestList() != null) {
          circleDto.setRequests(circleVO.getRequestList().size());
        } else {
          circleDto.setRequests(0);
        }

      }

      circleDto.setPosts(posts);
      circleDto.setMembers(members);
      circleDto.setModerate(circleVO.isModerate());

      circleDtoList.add(circleDto);
    }

    // now set all circles of user
    List<CircleDto> finalList = new ArrayList<CircleDto>();
    List<Long> userCircleList = userResponse.getCircleList();
    for (long circleId : userCircleList) {

      CircleVO circleVO = (CircleVO) getBaseESService().get(circleId, null, CircleVO.class);
      if (circleVO == null || CircleType.ALL.getId().equals(circleVO.getType()) || CircleType.YEAR.getId().equals(circleVO.getType())) {
        continue;
      }

      // calculate no of members
      QueryBuilder queryBuilder = QueryBuilders.termQuery("circleList", circleVO.getId());
      long members = getBaseESService().count(queryBuilder, UserVO.class);

      // calculate number of posts
      queryBuilder = QueryBuilders.termQuery("circleList.id", circleVO.getId());
      long posts = getBaseESService().count(queryBuilder, PostVO.class);

      CircleDto circleDto = new CircleDto();
      circleDto.setId(circleVO.getId());
      circleDto.setName(circleVO.getName());
      circleDto.setJoined(true);
      finalList.add(circleDto);
    }

    finalList.addAll(circleDtoList);


    CircleGetResponse circleGetResponse = new CircleGetResponse();
    if (!finalList.isEmpty()) {
      circleGetResponse.setCircleDtoList(BaseUtil.getPaginatedList(finalList, circleGetRequest.getPageNo(), circleGetRequest.getPerPage()));
      circleGetResponse.setPageNo(circleGetRequest.getPageNo());
      circleGetResponse.setPerPage(circleGetRequest.getPerPage());
    } else {
      circleGetResponse.setException(true);
      circleGetResponse.addMessage("No circle found");
    }

    return circleGetResponse;

  }

  @Override
  public CircleRequestListResponse fetchAllRequest(CircleJoinListRequest circleJoinRequest) {
    if (!circleJoinRequest.validate()) {
      throw new InValidInputException("Invalid Inputs");
    }

    CircleRequestListResponse circleRequestListResponse = new CircleRequestListResponse();

    List<UserCircleMapping> userCircleMappingList =
        getCircleDao().getCircleMappingRequest(circleJoinRequest.getCircleId(),
            circleJoinRequest.getPageNo(), circleJoinRequest.getPerPage());

    List<UserDto> userDtoList = new ArrayList<UserDto>();
    if (userCircleMappingList != null && userCircleMappingList.size() > 0) {
      for (UserCircleMapping userCircleMapping : userCircleMappingList) {
        UserResponse userResponse = getUserService().findById(userCircleMapping.getUserCircleMappingKey().getUserId());
        UserDto userDto = new UserDto();
        userDto.setId(userResponse.getId());
        userDto.setName(userResponse.getName());
        userDto.setImage(userResponse.getImage());
        userDtoList.add(userDto);
      }
      circleRequestListResponse.setUserDtoList(userDtoList);
      circleRequestListResponse.setPageNo(circleJoinRequest.getPageNo());
      circleRequestListResponse.setPerPage(circleJoinRequest.getPerPage());
    } else {
      circleRequestListResponse.setException(true);
      circleRequestListResponse.addMessage("No more requests");
    }

    return circleRequestListResponse;
  }

  @Override
  @Transactional
  public GenericSuccessResponse unJoinCircle(CircleJoinRequest circleJoinRequest) {
    if (!circleJoinRequest.validate()) {
      throw new InValidInputException("Invalid input");
    }
    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access!");
    }
    CircleVO circleVO = (CircleVO)
        getBaseESService().get(circleJoinRequest.getCircleId(), null, CircleVO.class);

    if (circleVO == null) {
      throw new PotaliRuntimeException("No Circle found with Id "+circleJoinRequest.getCircleId());
    }

    if (!circleVO.getInstituteId().equals(userResponse.getInstituteId())) {
      throw new PotaliRuntimeException("Circle belongs to some other institute");
    }

    UserCircleMapping userCircleMapping =
        (UserCircleMapping) getCircleDao().findUniqueByNamedQueryAndNamedParam("findByUserAndCircle",
            new String[]{"userId", "circleId"}, new Object[]{userResponse.getId(),circleVO.getId()});


    if (userCircleMapping == null) {
      throw new PotaliRuntimeException("User has never requested to join this circle");
    }

    UserVO userVO = (UserVO) getBaseESService().get(userResponse.getId(), null , UserVO.class);
    if (userVO != null) {
      List<Long> circleList = userVO.getCircleList();
      if (circleList == null) {
        circleList = new ArrayList<Long>();
      }
      circleList.remove(circleVO.getId());
      userVO.setCircleList(circleList);
      boolean published = getBaseESService().put(userVO);
      if (!published) {
        throw new PotaliRuntimeException("Couldn't create circle, Please Try Again!");
      }
    }

    getCircleDao().delete(userCircleMapping);

    GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
    genericSuccessResponse.setSuccess(true);

    /*CircleGetResponse circleGetResponse = new CircleGetResponse();

    if (genericSuccessResponse.isSuccess()) {
      CircleGetRequest circleGetRequest = new CircleGetRequest();
      circleGetRequest.setCircleId(circleVO.getType());

      circleGetResponse = fetchAllCircle(circleGetRequest);
    }*/

    return genericSuccessResponse;
  }

  @Override
  public GenericSuccessResponse deactivateCircle(CircleJoinRequest circleJoinRequest) {
    if (!circleJoinRequest.validate()) {
      throw new InValidInputException("Invalid input");
    }
    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access!");
    }
    CircleVO circleVO = (CircleVO)
        getBaseESService().get(circleJoinRequest.getCircleId(), null, CircleVO.class);

    if (circleVO == null) {
      throw new PotaliRuntimeException("No Circle found with Id "+circleJoinRequest.getCircleId());
    }

    if (!circleVO.getInstituteId().equals(userResponse.getInstituteId())) {
      throw new PotaliRuntimeException("Circle belongs to some other institute");
    }

    if (!circleVO.getAdmin().equals(userResponse.getId())) {
      throw new PotaliRuntimeException("UnAuthorized Access!");
    }

    circleVO.setActive(false);

    boolean published = getBaseESService().put(circleVO);
    if (!published) {
      throw new PotaliRuntimeException("Couldn't create circle, Please Try Again!");
    }

    GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
    genericSuccessResponse.setSuccess(true);

    return genericSuccessResponse;
  }

  @Override
  public GenericSuccessResponse activateCircle(CircleJoinRequest circleJoinRequest) {
    if (!circleJoinRequest.validate()) {
      throw new InValidInputException("Invalid input");
    }
    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access!");
    }
    CircleVO circleVO = (CircleVO)
        getBaseESService().get(circleJoinRequest.getCircleId(), null, CircleVO.class);

    if (circleVO == null) {
      throw new PotaliRuntimeException("No Circle found with Id "+circleJoinRequest.getCircleId());
    }

    if (!circleVO.getInstituteId().equals(userResponse.getInstituteId())) {
      throw new PotaliRuntimeException("Circle belongs to some other institute");
    }

    if (!circleVO.getAdmin().equals(userResponse.getId())) {
      throw new PotaliRuntimeException("UnAuthorized Access!");
    }

    circleVO.setActive(true);

    boolean published = getBaseESService().put(circleVO);
    if (!published) {
      throw new PotaliRuntimeException("Couldn't create circle, Please Try Again!");
    }

    GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
    genericSuccessResponse.setSuccess(true);

    return genericSuccessResponse;
  }

  @Override
  public CircleGetResponse getUsersCircle(CircleGetRequest circleGetRequest) {
    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access!");
    }

    List<Long> circleList = userResponse.getCircleList();
    int firstIndex = circleGetRequest.getPageNo()*circleGetRequest.getPerPage();

    int lastIndex = firstIndex + circleGetRequest.getPerPage();
    if (circleList.size() < lastIndex) {
      lastIndex = circleList.size();
    }

    if (firstIndex > lastIndex) {
      throw new InValidInputException("Invalid request!, last index cannot be greater than first index");
    }

    List<Long> subList = circleList.subList(firstIndex, lastIndex);
    List<CircleDto> circleDtoList = new ArrayList<CircleDto>();

    for (long circleId : subList) {

      if (circleId == -1l) {
        continue;
      }

      CircleVO circleVO = (CircleVO)
          getBaseESService().get(circleId, null, CircleVO.class);

      if (circleVO.getType() != circleGetRequest.getCircleId()) {
        continue;
      }

      if (!circleVO.isActive()) {
        continue;
      }

      // calculate no of members
      QueryBuilder queryBuilder = QueryBuilders.termQuery("circleList", circleVO.getId());
      long members = getBaseESService().count(queryBuilder, UserVO.class);

      // calculate number of posts
      queryBuilder = QueryBuilders.termQuery("circleList.id", circleVO.getId());
      long posts = getBaseESService().count(queryBuilder, PostVO.class);

      CircleDto circleDto = new CircleDto();
      circleDto.setId(circleVO.getId());
      circleDto.setName(circleVO.getName());
      if (userResponse.getCircleList().contains(circleVO.getId())) {
        circleDto.setJoined(true);
      }

      if (circleVO.getAdmin().equals(userResponse.getId())) {
        circleDto.setAdmin(true);
        if (circleVO.getRequestList() != null) {
          circleDto.setRequests(circleVO.getRequestList().size());
        }
      }

      circleDto.setPosts(posts);
      circleDto.setMembers(members);
      circleDto.setModerate(circleVO.isModerate());
      circleDtoList.add(circleDto);


    }

    CircleGetResponse circleGetResponse = new CircleGetResponse();
    if (!circleDtoList.isEmpty()) {
      circleGetResponse.setCircleDtoList(circleDtoList);
      circleGetResponse.setPageNo(circleGetRequest.getPageNo());
      circleGetResponse.setPerPage(circleGetRequest.getPerPage());
    } else {
      circleGetResponse.setException(true);
      circleGetResponse.addMessage("No circle found");
    }

    return circleGetResponse;

  }

  public UserService getUserService() {
    return userService;
  }

  public CircleDao getCircleDao() {
    return circleDao;
  }

  public BaseESService getBaseESService() {
    return baseESService;
  }
}
