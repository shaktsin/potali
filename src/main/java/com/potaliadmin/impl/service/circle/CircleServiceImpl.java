package com.potaliadmin.impl.service.circle;

import com.potaliadmin.constants.circle.CircleType;
import com.potaliadmin.domain.circle.Circle;
import com.potaliadmin.domain.user.User;
import com.potaliadmin.domain.user.UserCircleMapping;
import com.potaliadmin.dto.web.request.circle.CircleAuthorizeRequest;
import com.potaliadmin.dto.web.request.circle.CircleCreateRequest;
import com.potaliadmin.dto.web.request.circle.CircleGetRequest;
import com.potaliadmin.dto.web.request.circle.CircleJoinRequest;
import com.potaliadmin.dto.web.response.base.GenericSuccessResponse;
import com.potaliadmin.dto.web.response.circle.CircleDto;
import com.potaliadmin.dto.web.response.circle.CircleGetResponse;
import com.potaliadmin.dto.web.response.circle.CreateCircleResponse;
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
import com.potaliadmin.vo.BaseElasticVO;
import com.potaliadmin.vo.circle.CircleVO;
import com.potaliadmin.vo.post.PostVO;
import com.potaliadmin.vo.user.UserVO;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
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
    Circle circle = getCircleDao().createCircle(circleCreateRequest.getName(),
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
      genericSuccessResponse.setSuccess(true);
    }



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
  public CircleGetResponse fetchAllCircle(CircleGetRequest circleGetRequest) {

    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access!");
    }
    BoolFilterBuilder boolFilterBuilder = FilterBuilders.boolFilter();
    boolFilterBuilder.must(FilterBuilders.termFilter("instituteId", userResponse.getInstituteId()));
    boolFilterBuilder.must(FilterBuilders.termFilter("type", CircleType.CLUB.getId()));

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(boolFilterBuilder)
            .addSortedMap("id", SortOrder.DESC).setPageNo(circleGetRequest.getPageNo())
            .setPerPage(circleGetRequest.getPerPage());


    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, CircleVO.class);
    List<BaseElasticVO> baseElasticVOs = esSearchResponse.getBaseElasticVOs();
    List<CircleDto> circleDtoList = new ArrayList<CircleDto>();
    for (BaseElasticVO baseElasticVO : baseElasticVOs) {
      CircleVO circleVO = (CircleVO) baseElasticVO;

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
