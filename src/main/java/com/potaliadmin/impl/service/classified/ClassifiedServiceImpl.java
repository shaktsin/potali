package com.potaliadmin.impl.service.classified;

import com.potaliadmin.domain.user.UserCircleMapping;
import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryDto;
import com.potaliadmin.dto.web.response.circle.CircleDto;
import com.potaliadmin.dto.web.response.classified.PrepareClassifiedResponse;
import com.potaliadmin.dto.web.response.newsfeed.PrepareNewsFeedResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.framework.elasticsearch.BaseESService;
import com.potaliadmin.pact.dao.circle.CircleDao;
import com.potaliadmin.pact.service.classified.ClassifiedService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.vo.circle.CircleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaktsin on 4/5/15.
 */
@Service
public class ClassifiedServiceImpl implements ClassifiedService {

  @Autowired
  UserService userService;
  @Autowired
  CircleDao circleDao;
  @Autowired
  BaseESService baseESService;

  @Override
  @SuppressWarnings("unchecked")
  public PrepareClassifiedResponse prepareClassifiedResponse() {
    UserResponse userResponse = getUserService().getLoggedInUser();

    // get circle list from DB
    List<UserCircleMapping> userCircleMappingList = (List<UserCircleMapping>)getCircleDao().findByNamedQueryAndNamedParam("findByUser",
        new String[]{"userId"}, new Object[]{userResponse.getId()});

    if (userCircleMappingList == null || userCircleMappingList.isEmpty()) {
      PrepareClassifiedResponse prepareClassifiedResponse = new PrepareClassifiedResponse();
      prepareClassifiedResponse.setException(true);
      prepareClassifiedResponse.addMessage("Some exception occurred, please try again!");
      return prepareClassifiedResponse;
    }

    List<Long> authCircleList = new ArrayList<Long>();
    List<Long> circleList = userResponse.getCircleList();
    if (circleList == null || circleList.isEmpty()) {
      PrepareClassifiedResponse prepareClassifiedResponse = new PrepareClassifiedResponse();
      prepareClassifiedResponse.setException(true);
      prepareClassifiedResponse.addMessage("Some exception occurred, please try again!");
      return prepareClassifiedResponse;
    }


    for (UserCircleMapping userCircleMapping : userCircleMappingList) {
      if (userCircleMapping.isAuthorised() && circleList.contains(userCircleMapping.getUserCircleMappingKey().getCircleId())) {
        authCircleList.add(userCircleMapping.getUserCircleMappingKey().getCircleId());
      }
    }

    List<CircleDto> circleDtoList = new ArrayList<CircleDto>();
    for (Long circle : authCircleList) {
      CircleVO circleVO = (CircleVO) getBaseESService().get(circle, null, CircleVO.class);
      if (circleVO != null) {
        if (!circleVO.isActive()) {
          continue;
        }
        CircleDto circleDto = new CircleDto();
        circleDto.setId(circleVO.getId());
        circleDto.setName(circleVO.getName());
        circleDto.setSelected(false);
        circleDtoList.add(circleDto);
      }
    }

    List<PrimaryCategoryDto> primaryCategoryDtoList = new ArrayList<PrimaryCategoryDto>();



    PrepareClassifiedResponse prepareClassifiedResponse = new PrepareClassifiedResponse();

    return null;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public CircleDao getCircleDao() {
    return circleDao;
  }

  public void setCircleDao(CircleDao circleDao) {
    this.circleDao = circleDao;
  }

  public BaseESService getBaseESService() {
    return baseESService;
  }

  public void setBaseESService(BaseESService baseESService) {
    this.baseESService = baseESService;
  }
}
