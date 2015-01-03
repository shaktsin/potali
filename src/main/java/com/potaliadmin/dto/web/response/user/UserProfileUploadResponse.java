package com.potaliadmin.dto.web.response.user;

import com.potaliadmin.dto.internal.image.ImageDto;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakti Singh on 1/2/15.
 */
public class UserProfileUploadResponse extends GenericBaseResponse {

  private boolean uploaded;
  private List<ImageDto> imageDtoList = new ArrayList<ImageDto>();

  public boolean isUploaded() {
    return uploaded;
  }

  public void setUploaded(boolean uploaded) {
    this.uploaded = uploaded;
  }

  public List<ImageDto> getImageDtoList() {
    return imageDtoList;
  }

  public void setImageDtoList(List<ImageDto> imageDtoList) {
    this.imageDtoList = imageDtoList;
  }

  public void addImageDto(ImageDto imageDto) {
    imageDtoList.add(imageDto);
  }
}
