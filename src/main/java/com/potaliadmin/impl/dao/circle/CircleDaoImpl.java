package com.potaliadmin.impl.dao.circle;

import com.potaliadmin.constants.circle.CircleType;
import com.potaliadmin.domain.circle.Circle;
import com.potaliadmin.domain.user.UserCircleMapping;
import com.potaliadmin.domain.user.UserCircleMappingKey;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.circle.CircleDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shakti on 28/1/15.
 */
@Repository
public class CircleDaoImpl extends BaseDaoImpl implements CircleDao {

  @Override
  @Transactional(propagation = Propagation.NESTED)
  public Circle createCircle(String name, CircleType circleType, UserResponse userResponse, boolean moderate) {
    if (StringUtils.isBlank(name)) {
      throw new InValidInputException("CIRCLE_NAME_CANNOT_BE_NULL");
    }
    if (circleType == null) {
      throw new InValidInputException("CIRCLE_TYPE_CANNOT_BE_NULL");
    }
    if (userResponse == null) {
      throw new InValidInputException("USER_RESPONSE_CANNOT_BE_NULL");
    }
    Circle circle = new Circle();
    circle.setName(name);
    circle.setCircleType(circleType.getId());
    circle.setPublicCircle(!moderate);

    circle = (Circle) save(circle);

    joinCircle(userResponse, circle.getId(), true);

    return circle;
  }

  @Override
  @Transactional(propagation = Propagation.NESTED)
  public UserCircleMapping joinCircle(UserResponse userResponse, Long circleId, boolean isAdmin) {
    if (userResponse == null) {
      throw new InValidInputException("USER_RESPONSE_CANNOT_BE_NULL");
    }
    if (circleId == null) {
      throw new InValidInputException("CIRCLE_ID_CANNOT_BE_NULL");
    }

    UserCircleMapping userCircleMapping = new UserCircleMapping();
    userCircleMapping.setAdmin(isAdmin);
    userCircleMapping.setAuthorised(true);

    UserCircleMappingKey userCircleMappingKey = new UserCircleMappingKey();
    userCircleMappingKey.setUserId(userResponse.getId());
    userCircleMappingKey.setUserInstituteId(userResponse.getInstituteId());
    userCircleMappingKey.setCircleId(circleId);

    userCircleMapping.setUserCircleMappingKey(userCircleMappingKey);
    //userCircleMapping.setCircleId(circleId);
    //userCircleMapping.setUserId(userResponse.getId());
    //userCircleMapping.setUserInstituteId(userResponse.getInstituteId());


    return (UserCircleMapping) save(userCircleMapping);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Circle> getUserCircle(Long userId) {
    List<UserCircleMapping> userCircleMappingList =
        findByNamedQueryAndNamedParam("findByUser", new String[]{"userId"}, new Object[]{userId});

    List<Circle> circleList = new ArrayList<Circle>();
    if (userCircleMappingList != null && !userCircleMappingList.isEmpty()) {
      for (UserCircleMapping userCircleMapping : userCircleMappingList) {
        Circle circle = get(Circle.class, userCircleMapping.getUserCircleMappingKey().getCircleId());
        circleList.add(circle);
      }
    }
    return circleList;
  }

  @Override
  public Long getAdminUser(Long circleId) {
    return (Long)findUniqueByNamedParams("findByCircleAdmin", new String[]{"circleId"}, new Object[]{circleId});
  }
}
