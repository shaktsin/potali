package com.potaliadmin.impl.dao.classified;

import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.domain.address.City;
import com.potaliadmin.domain.circle.Circle;
import com.potaliadmin.domain.classified.ClassifiedPost;
import com.potaliadmin.domain.classified.SecondaryCategory;
import com.potaliadmin.domain.industry.IndustryRoles;
import com.potaliadmin.domain.post.PostBlob;
import com.potaliadmin.dto.internal.hibernate.post.CreatePostBlobRequest;
import com.potaliadmin.dto.web.request.classified.ClassifiedEditRequest;
import com.potaliadmin.dto.web.request.classified.ClassifiedPostRequest;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.circle.CircleDao;
import com.potaliadmin.pact.dao.city.CityDao;
import com.potaliadmin.pact.dao.classified.ClassifiedDao;
import com.potaliadmin.pact.dao.classified.SecondaryCategoryDao;
import com.potaliadmin.pact.dao.post.PostBlobDao;
import com.potaliadmin.pact.service.users.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by shaktsin on 4/26/15.
 */
@Repository
public class ClassifiedDaoImpl extends BaseDaoImpl implements ClassifiedDao {


  private static final int CONTENT_SIZE = 499;

  @Autowired
  CityDao cityDao;

  @Autowired
  UserService userService;

  @Autowired
  PostBlobDao postBlobDao;

  @Autowired
  SecondaryCategoryDao secondaryCategoryDao;

  @Autowired
  CircleDao circleDao;

  @Override
  @Transactional
  public ClassifiedPost createClassified(ClassifiedPostRequest classifiedPostRequest) {
    if (classifiedPostRequest.getUserId() == null) {
      throw new InValidInputException("User id cannot be null");
    }
    if (classifiedPostRequest.getUserInstituteId() == null) {
      throw new InValidInputException("User Institute Id cannot be null");
    }
    ClassifiedPost classifiedPost = new ClassifiedPost();
    classifiedPost.setUserId(classifiedPostRequest.getUserId());
    classifiedPost.setUserInstituteId(classifiedPostRequest.getUserInstituteId());
    classifiedPost.setSubject(classifiedPostRequest.getSubject());
    classifiedPost.setContent(StringUtils.substring(classifiedPostRequest.getContent(), 0, CONTENT_SIZE));
    classifiedPost.setReplyEmail(classifiedPostRequest.getReplyEmail());
    classifiedPost.setReplyPhone(classifiedPostRequest.getReplyPhone());
    classifiedPost.setReplyWatsApp(classifiedPostRequest.getReplyWatsApp());
    classifiedPost.setShareEmail(EnumReactions.isValidShareReaction(classifiedPostRequest.getShareDto().getShareEmail()));
    classifiedPost.setSharePhone(EnumReactions.isValidShareReaction(classifiedPostRequest.getShareDto().getSharePhone()));
    classifiedPost.setShareWatsApp(EnumReactions.isValidShareReaction(classifiedPostRequest.getShareDto().getShareWatsApp()));

    Set<Circle> circles = new HashSet<Circle>();
    for (long circleId : classifiedPostRequest.getCircleList()) {
      Circle circle = getCircleDao().get(Circle.class, circleId);
      circles.add(circle);
    }
    classifiedPost.setCircleSet(circles);


    // set location set
    Set<City> citySet = getCityDao().findListOfCity(classifiedPostRequest.getLocationIdList());
    classifiedPost.setCitySet(citySet);

    Set<SecondaryCategory> industryRolesSet = getSecondaryCategoryDao().
        findSecondaryCategoryByIdList(classifiedPostRequest.getSecondaryCatList());
    classifiedPost.setSecondaryCategorySet(industryRolesSet);

    //save job
    classifiedPost = (ClassifiedPost) save(classifiedPost);
    CreatePostBlobRequest createPostBlobRequest = new CreatePostBlobRequest();
    createPostBlobRequest.setPostId(classifiedPost.getId());
    createPostBlobRequest.setContent(classifiedPost.getContent());
    getPostBlobDao().createPostBlob(createPostBlobRequest);



    return classifiedPost;
  }

  @Override
  public ClassifiedPost editClassified(ClassifiedEditRequest classifiedEditRequest) {
    ClassifiedPost classifiedPost = get(ClassifiedPost.class, classifiedEditRequest.getPostId());
    if (classifiedPost == null) {
      throw new InValidInputException("Attachment with no post associated");
    }

    UserResponse postUser = getUserService().findById(classifiedPost.getUserId());
    if (postUser == null) {
      throw new RuntimeException("Something unexpected occurred, please try again");
    }

    classifiedPost.setUserId(classifiedEditRequest.getUserId());
    classifiedPost.setUserInstituteId(classifiedEditRequest.getUserInstituteId());
    classifiedPost.setSubject(classifiedEditRequest.getSubject());
    classifiedPost.setContent(StringUtils.substring(classifiedEditRequest.getContent(), 0, CONTENT_SIZE));
    classifiedPost.setReplyEmail(classifiedEditRequest.getReplyEmail());
    classifiedPost.setReplyPhone(classifiedEditRequest.getReplyPhone());
    classifiedPost.setReplyWatsApp(classifiedEditRequest.getReplyWatsApp());
    classifiedPost.setShareEmail(EnumReactions.isValidShareReaction(classifiedEditRequest.getShareDto().getShareEmail()));
    classifiedPost.setSharePhone(EnumReactions.isValidShareReaction(classifiedEditRequest.getShareDto().getSharePhone()));
    classifiedPost.setShareWatsApp(EnumReactions.isValidShareReaction(classifiedEditRequest.getShareDto().getShareWatsApp()));

    Set<Circle> circles = new HashSet<Circle>();
    for (long circleId : classifiedEditRequest.getCircleList()) {
      Circle circle = getCircleDao().get(Circle.class, circleId);
      circles.add(circle);
    }
    classifiedPost.setCircleSet(circles);


    // set location set
    Set<City> citySet = getCityDao().findListOfCity(classifiedEditRequest.getLocationIdList());
    classifiedPost.setCitySet(citySet);

    Set<SecondaryCategory> industryRolesSet = getSecondaryCategoryDao().
        findSecondaryCategoryByIdList(classifiedEditRequest.getSecondaryCatList());
    classifiedPost.setSecondaryCategorySet(industryRolesSet);

    PostBlob postBlob = getPostBlobDao().findByPostId(classifiedEditRequest.getPostId());
    if (postBlob == null) {
      throw new PotaliRuntimeException("Some exception occurred, please try again");
    }
    postBlob.setContent(classifiedEditRequest.getContent());
    getPostBlobDao().save(postBlob);


    return (ClassifiedPost)save(classifiedPost);
  }


  public CityDao getCityDao() {
    return cityDao;
  }

  public UserService getUserService() {
    return userService;
  }

  public PostBlobDao getPostBlobDao() {
    return postBlobDao;
  }

  public SecondaryCategoryDao getSecondaryCategoryDao() {
    return secondaryCategoryDao;
  }

  public CircleDao getCircleDao() {
    return circleDao;
  }
}
