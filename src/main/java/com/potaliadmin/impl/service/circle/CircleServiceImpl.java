package com.potaliadmin.impl.service.circle;

import com.potaliadmin.constants.circle.CircleType;
import com.potaliadmin.domain.circle.Circle;
import com.potaliadmin.dto.web.request.circle.CircleCreateRequest;
import com.potaliadmin.dto.web.request.circle.CircleJoinRequest;
import com.potaliadmin.dto.web.response.base.GenericSuccessResponse;
import com.potaliadmin.dto.web.response.circle.CreateCircleResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.exceptions.UnAuthorizedAccessException;
import com.potaliadmin.framework.elasticsearch.BaseESService;
import com.potaliadmin.pact.dao.circle.CircleDao;
import com.potaliadmin.pact.service.circle.CircleService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.vo.BaseElasticVO;
import com.potaliadmin.vo.circle.CircleVO;
import com.potaliadmin.vo.user.UserVO;
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


    return genericSuccessResponse;
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
