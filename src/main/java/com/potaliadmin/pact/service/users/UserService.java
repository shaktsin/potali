package com.potaliadmin.pact.service.users;

import com.potaliadmin.dto.web.request.user.UserProfileUpdateRequest;
import com.potaliadmin.dto.web.request.user.UserSignUpRequest;
import com.potaliadmin.dto.web.response.user.UserProfileUpdateResponse;
import com.potaliadmin.dto.web.response.user.UserProfileUploadResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;

/**
 * Created by Shakti Singh on 10/6/14.
 */
public interface UserService {

  UserResponse findByEmail(String email);

  UserResponse signUp(UserSignUpRequest userSignUpRequest);

  UserResponse getLoggedInUser();

  UserProfileUpdateResponse updateProfile(UserProfileUpdateRequest userProfileUpdateRequest);
}
