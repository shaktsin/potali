package com.potaliadmin.pact.framework.aws;

import com.potaliadmin.dto.internal.image.ImageDto;

import java.io.File;
import java.util.List;

/**
 * Created by Shakti Singh on 1/3/15.
 */
public interface UploadService {

  String getCanonicalPathOfResource(String folderName, String fileName);

  boolean uploadProfileImageFiles(String secondaryBucketName, List<ImageDto> imageDtoList, boolean shouldDecrypt);

  boolean uploadPostImages(Long postId, List<ImageDto> imageDtoList);
}
