package com.potaliadmin.dto.web.response.post;

import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.dto.internal.cache.es.framework.GenericPostVO;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.circle.CircleDto;
import com.potaliadmin.dto.web.response.user.UserDto;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.util.BaseUtil;
import com.potaliadmin.util.DateUtils;
import com.potaliadmin.vo.circle.CircleVO;
import com.potaliadmin.vo.post.PostVO;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class GenericPostResponse extends GenericBaseResponse {

  private Long postId;
  private String subject;
  private String content;
  private String postedOn;
  private UserDto userDto;
  private ReplyDto replyDto;
  private ShareDto shareDto;
  private int postType;
  public boolean important;
  private List<String> images;
  private List<CircleDto> circleDtoList;
  private CommentListResponse commentListResponse;


  public GenericPostResponse() {
  }

  public GenericPostResponse(GenericPostVO genericPostVO, UserResponse postUser) {
    this.setPostId(genericPostVO.getPostId());
    this.setSubject(genericPostVO.getSubject());

    ReplyDto replyDto = new ReplyDto(-1, -1, -1);
    if (StringUtils.isNotBlank(genericPostVO.getReplyEmail())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_EMAIL.getId());
    }
    if (StringUtils.isNotBlank(genericPostVO.getReplyPhone())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_PHONE.getId());
    }
    if (StringUtils.isNotBlank(genericPostVO.getReplyWatsApp())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_WATSAPP.getId());
    }
    this.setReplyDto(replyDto);

    this.setShareDto(genericPostVO.getShareDto());

    this.setPostedOn(DateUtils.getPostedOnDate(genericPostVO.getCreatedDate()));
    this.setContent(BaseUtil.trimContent(genericPostVO.getContent()));

    // set User
    //UserResponse postUser = getUserService().findById(fullJobVO.getUserId());
    UserDto userDto = new UserDto();
    userDto.setName(postUser.getName());
    userDto.setId(postUser.getId());
    userDto.setImage(postUser.getImage());
    this.setUserDto(userDto);

  }

  public GenericPostResponse(PostVO postVO, UserResponse postUser) {
    this.setPostId(postVO.getPostId());
    this.setSubject(postVO.getSubject());

    ReplyDto replyDto = new ReplyDto(-1, -1, -1);
    if (StringUtils.isNotBlank(postVO.getReplyEmail())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_EMAIL.getId());
    }
    if (StringUtils.isNotBlank(postVO.getReplyPhone())) {
      replyDto.setReplyPhone(EnumReactions.REPLY_VIA_PHONE.getId());
    }
    if (StringUtils.isNotBlank(postVO.getReplyWatsApp())) {
      replyDto.setReplyWatsApp(EnumReactions.REPLY_VIA_WATSAPP.getId());
    }
    this.setReplyDto(replyDto);

    this.setShareDto(postVO.getShareDto());

    this.setPostedOn(DateUtils.getPostedOnDate(postVO.getCreatedDate()));
    this.setContent(BaseUtil.trimContent(postVO.getContent()));

    // set User
    //UserResponse postUser = getUserService().findById(fullJobVO.getUserId());
    UserDto userDto = new UserDto();
    userDto.setName(postUser.getName());
    userDto.setId(postUser.getId());
    userDto.setImage(postUser.getImage());
    this.setUserDto(userDto);

    if (postVO.getImageList() != null && !postVO.getImageList().isEmpty()) {
      this.images = postVO.getImageList();
    }


    List<CircleDto> circleDtos = new ArrayList<CircleDto>();
    if (postVO.getCircleList() != null) {
      for (CircleVO circleVO : postVO.getCircleList()) {
        CircleDto circleDto = new CircleDto();
        circleDto.setId(circleVO.getId());
        circleDto.setName(circleVO.getName());
        circleDtos.add(circleDto);
      }
    }


    this.setCircleDtoList(circleDtos);

  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getPostedOn() {
    return postedOn;
  }

  public void setPostedOn(String postedOn) {
    this.postedOn = postedOn;
  }

  public UserDto getUserDto() {
    return userDto;
  }

  public void setUserDto(UserDto userDto) {
    this.userDto = userDto;
  }

  public ReplyDto getReplyDto() {
    return replyDto;
  }

  public void setReplyDto(ReplyDto replyDto) {
    this.replyDto = replyDto;
  }

  public ShareDto getShareDto() {
    return shareDto;
  }

  public void setShareDto(ShareDto shareDto) {
    this.shareDto = shareDto;
  }

  public int getPostType() {
    return postType;
  }

  public void setPostType(int postType) {
    this.postType = postType;
  }

  public boolean isImportant() {
    return important;
  }

  public void setImportant(boolean important) {
    this.important = important;
  }

  public List<String> getImages() {
    return images;
  }

  public void setImages(List<String> images) {
    this.images = images;
  }

  public List<CircleDto> getCircleDtoList() {
    return circleDtoList;
  }

  public void setCircleDtoList(List<CircleDto> circleDtoList) {
    this.circleDtoList = circleDtoList;
  }

  public CommentListResponse getCommentListResponse() {
    return commentListResponse;
  }

  public void setCommentListResponse(CommentListResponse commentListResponse) {
    this.commentListResponse = commentListResponse;
  }
}
