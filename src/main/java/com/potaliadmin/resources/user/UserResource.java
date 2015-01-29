package com.potaliadmin.resources.user;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.image.EnumBucket;
import com.potaliadmin.constants.image.EnumImageSize;
import com.potaliadmin.dto.internal.image.ImageDto;
import com.potaliadmin.dto.web.request.user.UserProfileUpdateRequest;
import com.potaliadmin.dto.web.request.user.UserSignUpRequest;
import com.potaliadmin.dto.web.response.user.UserProfileUpdateResponse;
import com.potaliadmin.dto.web.response.user.UserProfileUploadResponse;
import com.potaliadmin.dto.web.response.user.UserResourceResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.impl.framework.properties.AppProperties;
import com.potaliadmin.pact.service.job.JobService;
import com.potaliadmin.pact.service.users.LoginService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.security.SecurityToken;
import com.potaliadmin.util.image.ImageNameBuilder;
import com.potaliadmin.util.image.ImageProcessUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.text.ParseException;

/**
 * Created by Shakti Singh on 12/20/14.
 */
@Path("/user")
@Component
public class UserResource {

  private static Logger logger = LoggerFactory.getLogger(UserResource.class);

  @Autowired
  LoginService loginService;

  @Autowired
  AppProperties appProperties;

  @Autowired
  JobService jobService;

  @Autowired
  UserService userService;

  @POST
  @Path("/signUp")
  @Produces("application/json")
  public UserResourceResponse signUp(UserSignUpRequest userSignUpRequest/*, @QueryParam(RequestConstants.LOCATION) String locationFilter,
                                     @QueryParam(RequestConstants.INDUSTRY) String industryFilter,
                                     @QueryParam(RequestConstants.ROLES) String rolesFilter,
                                     @QueryParam(RequestConstants.SALARY) String salaryFilter,
                                     @QueryParam(RequestConstants.EXP) String experienceFilter,
                                     @QueryParam(RequestConstants.PER_PAGE) @DefaultValue(DefaultConstants.AND_APP_PER_PAGE)int perPage,
                                     @QueryParam(RequestConstants.PAGE_NO) @DefaultValue(DefaultConstants.AND_APP_PAGE_NO)int pageNo,
                                     @QueryParam(RequestConstants.PLATE_FORM) @DefaultValue(DefaultConstants.PLATE_FROM) Long plateFormId*/) {

    try {
      UserResponse userResponse = getLoginService().signUp(userSignUpRequest);
      if (!userResponse.isException()) {
        UserResourceResponse userResourceResponse = new UserResourceResponse();
        userResourceResponse.setName(userResponse.getName());
        userResourceResponse.setEmail(userResponse.getEmail());
        userResourceResponse.setAuthToken(SecurityToken.getSecurityToken(userResponse.getEmail(), userSignUpRequest.getPassword(), userSignUpRequest.getInstituteId()));

        /*String[] locationFilterList=null;
        String[] industryFiltersList = null;
        String[] rolesFilterList=null;
        String[] salaryFilterList=null;
        String[] experienceFilterList=null;
        try {
          if (StringUtils.isNotBlank(locationFilter)) {
            locationFilterList = locationFilter.split(DefaultConstants.REQUEST_SEPARATOR);
          }
          if (StringUtils.isNotBlank(rolesFilter)) {
            rolesFilterList = rolesFilter.split(DefaultConstants.REQUEST_SEPARATOR);
          }
          if (StringUtils.isNotBlank(salaryFilter)) {
            salaryFilterList = salaryFilter.split(DefaultConstants.REQUEST_SEPARATOR);
          }
          if (StringUtils.isNotBlank(experienceFilter)) {
            experienceFilterList = experienceFilter.split(DefaultConstants.REQUEST_SEPARATOR);
          }
          if (StringUtils.isNotBlank(industryFilter)) {
            industryFiltersList = industryFilter.split(DefaultConstants.REQUEST_SEPARATOR);
          }
          JobSearchResponse jobSearchResponse = getJobService().searchJob(BaseUtil.convertToLong(locationFilterList),BaseUtil.convertToLong(rolesFilterList),
              BaseUtil.convertToLong(industryFiltersList),BaseUtil.convertToDouble(salaryFilterList),
              BaseUtil.convertToInteger(experienceFilterList),perPage, pageNo);

          userResourceResponse.setJobSearchResponse(jobSearchResponse);
          return userResourceResponse;
        } catch (Exception e) {
          userResourceResponse = new UserResourceResponse();
          userResourceResponse.setException(Boolean.TRUE);
          userResourceResponse.addMessage(e.getMessage());
          return userResourceResponse;
        }*/

        return userResourceResponse;

      } else {
        UserResourceResponse userResourceResponse = new UserResourceResponse();
        userResourceResponse.setException(Boolean.TRUE);
        userResourceResponse.setMessages(userResponse.getMessages());
        return userResourceResponse;

      }

    } catch (Exception e) {
      UserResourceResponse userResourceResponse = new UserResourceResponse();
      userResourceResponse.setException(Boolean.TRUE);
      userResourceResponse.addMessage(e.getMessage());
      return userResourceResponse;
    }
  }


  @POST
  @Path("/login")
  @Produces("application/json")
  public UserResourceResponse login(@FormParam("email") String email,
                                    @FormParam("password")String password) {
    try {
      UserResponse userResponse = getLoginService().login(email, password);
      if (!userResponse.isException()) {
        UserResourceResponse userResourceResponse = new UserResourceResponse();
        userResourceResponse.setName(userResponse.getName());
        userResourceResponse.setEmail(userResponse.getEmail());
        userResourceResponse.setAuthToken(SecurityToken.getSecurityToken(email, password, userResponse.getInstituteId()));
        return userResourceResponse;
      } else {
        UserResourceResponse userResourceResponse = new UserResourceResponse();
        userResourceResponse.setException(Boolean.TRUE);
        userResourceResponse.addMessage(userResponse.getMessages().get(0));
        return userResourceResponse;
      }

    } catch (Exception e) {
      UserResourceResponse userResourceResponse = new UserResourceResponse();
      userResourceResponse.setException(Boolean.TRUE);
      userResourceResponse.addMessage(e.getMessage());
      return userResourceResponse;
    }

  }

  @POST
  @Path("/update")
  @Produces("application/json")
  @RequiresAuthentication
  public UserProfileUpdateResponse updateProfile(UserProfileUpdateRequest userProfileUpdateRequest) {
    try {
      return getUserService().updateProfile(userProfileUpdateRequest);
    } catch (Exception e) {
      UserProfileUpdateResponse userProfileUpdateResponse = new UserProfileUpdateResponse();
      userProfileUpdateResponse.setException(Boolean.TRUE);
      userProfileUpdateResponse.addMessage(e.getMessage());
      return userProfileUpdateResponse;
    }
  }




  @POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces("application/json")
  @RequiresAuthentication
  public UserProfileUploadResponse uploadProfilePicture(@FormDataParam("file") InputStream uploadedInputStream,
                                   @Context HttpServletRequest servletRequest) {


    FormDataContentDisposition fileDetail;
    try {
       fileDetail = new FormDataContentDisposition(servletRequest.getHeader("Content-Disposition")) ;
    } catch (ParseException e) {
      UserProfileUploadResponse userProfileUploadResponse = new UserProfileUploadResponse();
      userProfileUploadResponse.setException(Boolean.TRUE);
      userProfileUploadResponse.addMessage("INVALID FILE");
      return userProfileUploadResponse;
    }
    if (fileDetail.getSize() == -1) {
      UserProfileUploadResponse userProfileUploadResponse = new UserProfileUploadResponse();
      userProfileUploadResponse.setException(Boolean.TRUE);
      userProfileUploadResponse.addMessage("INVALID REQUEST");
      return userProfileUploadResponse;
    }

    if (fileDetail.getSize() == -1) {
      UserProfileUploadResponse userProfileUploadResponse = new UserProfileUploadResponse();
      userProfileUploadResponse.setException(Boolean.TRUE);
      userProfileUploadResponse.addMessage("INVALID REQUEST");
      return userProfileUploadResponse;
    }

    if (fileDetail.getSize() > DefaultConstants.MAX_IMAGE_UPLOAD_SIZE) {
      UserProfileUploadResponse userProfileUploadResponse = new UserProfileUploadResponse();
      userProfileUploadResponse.setException(Boolean.TRUE);
      userProfileUploadResponse.addMessage("Maximum Upload Size is "+ DefaultConstants.MAX_IMAGE_UPLOAD_SIZE + " KB");
      return userProfileUploadResponse;
    }

    if (StringUtils.isBlank(fileDetail.getFileName())) {
      UserProfileUploadResponse userProfileUploadResponse = new UserProfileUploadResponse();
      userProfileUploadResponse.setException(Boolean.TRUE);
      userProfileUploadResponse.addMessage("File without file name! Not Accepted");
      return userProfileUploadResponse;
    }

    if (StringUtils.isBlank(fileDetail.getName()) || !DefaultConstants.ALLOWED_IMAGE_CONTENT_TYPE.contains(fileDetail.getName())) {
      UserProfileUploadResponse userProfileUploadResponse = new UserProfileUploadResponse();
      userProfileUploadResponse.setException(Boolean.TRUE);
      userProfileUploadResponse.addMessage("Unsupported Media Type!");
      return userProfileUploadResponse;
    }

    UserResponse userResponse = getLoginService().getLoggedInUser();
    if (userResponse == null) {
      UserProfileUploadResponse userProfileUploadResponse = new UserProfileUploadResponse();
      userProfileUploadResponse.setException(Boolean.TRUE);
      userProfileUploadResponse.addMessage("UnAuthorized Access!");
      return userProfileUploadResponse;
    }

    String serverFileName = userResponse.getId() +DefaultConstants.NAME_SEPARATOR +fileDetail.getFileName();
    String rootPath = getAppProperties().getUploadPicPath() + File.separator + DefaultConstants.PROFILE;
    String fullFileName = rootPath + File.separator + serverFileName;

    try {

      File file = new File(fullFileName);
      if (!file.isDirectory() && file.exists()) {
        boolean isDeleted = file.delete();
        if (!isDeleted) {
          fullFileName = fullFileName + "_D";
          file = new File(fullFileName);
        }
      }

      OutputStream out = new FileOutputStream(file);
      int read = 0;
      byte[] bytes = new byte[1024];

      out = new FileOutputStream(file);
      while ((read = uploadedInputStream.read(bytes)) != -1) {
        out.write(bytes, 0, read);
      }
      out.flush();
      out.close();
      uploadedInputStream.close();


      // first resize the image
      String reSizeSmallFileName = ImageProcessUtil.reSize(rootPath, serverFileName, EnumImageSize.XS_SMALL,null );
      if (reSizeSmallFileName == null) {
        UserProfileUploadResponse userProfileUploadResponse = new UserProfileUploadResponse();
        userProfileUploadResponse.setException(Boolean.TRUE);
        userProfileUploadResponse.addMessage("Could not upload file, Please try Again!");
        return userProfileUploadResponse;
      }

      String mediumSizeSmallFileName = ImageProcessUtil.reSize(rootPath, serverFileName, EnumImageSize.MEDIUM,null );
      if (mediumSizeSmallFileName == null) {
        UserProfileUploadResponse userProfileUploadResponse = new UserProfileUploadResponse();
        userProfileUploadResponse.setException(Boolean.TRUE);
        userProfileUploadResponse.addMessage("Could not upload file, Please try Again!");
        return userProfileUploadResponse;
      }

      // now delete the original file
      //file.delete();
      //boolean isDeleted = file.delete();
      //logger.info("File is deleted ?"+isDeleted);

      //FileUtils.forceDelete(file);

      ImageDto smallImageDto = new ImageNameBuilder().addSize(EnumImageSize.XS_SMALL).addBucket(EnumBucket.PROFILE_BUCKET)
          .addRootFolder(getAppProperties().getUploadPicPath())
          .addUploadFolderName(DefaultConstants.PROFILE).addFileName(reSizeSmallFileName).build();

      ImageDto mediumImageDto = new ImageNameBuilder().addSize(EnumImageSize.MEDIUM).addBucket(EnumBucket.PROFILE_BUCKET)
          .addRootFolder(getAppProperties().getUploadPicPath())
          .addUploadFolderName(DefaultConstants.PROFILE).addFileName(mediumSizeSmallFileName).build();


      UserProfileUploadResponse userProfileUploadResponse = new UserProfileUploadResponse();
      userProfileUploadResponse.setUploaded(true);
      userProfileUploadResponse.addImageDto(smallImageDto);
      userProfileUploadResponse.addImageDto(mediumImageDto);
      return userProfileUploadResponse;

    } catch (IOException e) {
      UserProfileUploadResponse userProfileUploadResponse = new UserProfileUploadResponse();
      userProfileUploadResponse.setException(Boolean.TRUE);
      userProfileUploadResponse.addMessage("Could not upload file, Please try Again!");
      return userProfileUploadResponse;
    }
  }




  public LoginService getLoginService() {
    return loginService;
  }

  public AppProperties getAppProperties() {
    return appProperties;
  }

  public JobService getJobService() {
    return jobService;
  }

  public UserService getUserService() {
    return userService;
  }
}
