package com.potaliadmin.impl.dao.image;

import com.potaliadmin.constants.image.EnumImageSize;
import com.potaliadmin.domain.image.Avatar;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.image.AvatarDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Shakti Singh on 1/3/15.
 */
@Repository
public class AvatarDaoImpl extends BaseDaoImpl implements AvatarDao {

  @Override
  @Transactional
  public Avatar createAvatar(EnumImageSize enumImageSize, UserResponse userResponse, String url) {
    Avatar avatar = findAvatar(enumImageSize, userResponse);
    if (avatar == null) {
      avatar = new Avatar();
      avatar.setType(enumImageSize.getId());
      avatar.setHeight(enumImageSize.getHeight());
      avatar.setWidth(enumImageSize.getHeight());
      avatar.setUrl(url);
      avatar.setUserId(userResponse.getId());
      avatar.setUserInstituteId(userResponse.getInstituteId());
    } else {
      avatar.setUrl(url);
    }
    return (Avatar)save(avatar);
  }

  @Override
  public Avatar findAvatar(EnumImageSize enumImageSize, UserResponse userResponse) {
    String query = "from Avatar a where a.type = ? and a.userId = ? and a.userInstituteId = ?";
    return (Avatar)findUnique(query, new Object[]{enumImageSize.getId(), userResponse.getId(), userResponse.getInstituteId()});
  }
}
