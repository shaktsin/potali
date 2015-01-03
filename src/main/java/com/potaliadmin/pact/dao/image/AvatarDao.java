package com.potaliadmin.pact.dao.image;

import com.potaliadmin.constants.image.EnumImageSize;
import com.potaliadmin.domain.image.Avatar;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.pact.framework.BaseDao;

/**
 * Created by Shakti Singh on 1/3/15.
 */
public interface AvatarDao extends BaseDao {

  public Avatar createAvatar(EnumImageSize enumImageSize, UserResponse userResponse, String url);

  public Avatar findAvatar(EnumImageSize enumImageSize, UserResponse userResponse);
}
