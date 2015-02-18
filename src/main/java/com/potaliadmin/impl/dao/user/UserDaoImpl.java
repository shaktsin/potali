package com.potaliadmin.impl.dao.user;

import com.potaliadmin.domain.user.User;
import com.potaliadmin.dto.internal.hibernate.user.UserSignUpQueryRequest;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.user.UserDao;
import com.potaliadmin.util.BaseUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Shakti Singh on 10/6/14.
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

  @Override
  public User findById(Long id) {
    return get(User.class, id);
  }

  @Override
  public User findByEmail(String email) {
    return (User) findUniqueByNamedQueryAndNamedParam("findByEmail", new String[]{"email"}, new Object[]{email});
  }

  @Transactional
  public User createUser(UserSignUpQueryRequest userSignUpQueryRequest) {
    if (!userSignUpQueryRequest.verify()) {
      throw new InValidInputException("User sign up parameters are invalid");
    }
    User user = findByEmail(userSignUpQueryRequest.getEmail());
    if (user != null) {
      throw new PotaliRuntimeException("You are already registered with us");
    }
    user = new User();
    user.setFirstName(userSignUpQueryRequest.getFirstName());
    user.setLastName(userSignUpQueryRequest.getLastName());
    user.setAccountName(userSignUpQueryRequest.getAccountName());
    user.setEmail(userSignUpQueryRequest.getEmail());
    user.setGender(userSignUpQueryRequest.getGender());
    user.setVerified(false);
    user.setVerificationToken(BaseUtil.generateVerificationToken());
    user.setPasswordChecksum(userSignUpQueryRequest.getHash());
    user.setInstituteId(userSignUpQueryRequest.getInstituteId());
    save(user);

    return user;
  }
}
