package com.potaliadmin.pact.dao.circle;

import com.potaliadmin.constants.circle.CircleType;
import com.potaliadmin.domain.circle.Circle;
import com.potaliadmin.domain.user.UserCircleMapping;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.pact.framework.BaseDao;

import java.util.List;

/**
 * Created by shakti on 28/1/15.
 */
public interface CircleDao extends BaseDao {

  Circle createCircle(String name, CircleType circleType, UserResponse userResponse, boolean moderate);

  UserCircleMapping joinCircle(UserResponse userResponse, Long circleId, boolean authorized,boolean isAdmin);

  List<Circle> getUserCircle(Long userId);

  Long getAdminUser(Long circleId);

}
