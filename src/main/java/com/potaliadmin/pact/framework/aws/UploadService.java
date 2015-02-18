package com.potaliadmin.pact.framework.aws;

import com.potaliadmin.dto.internal.image.ImageDto;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Shakti Singh on 1/3/15.
 */
public interface UploadService {

  String getCanonicalPathOfResource(String folderName, String fileName);

  String getCanonicalPathOfCloudResource(String publicId, Long version, String format);

  boolean uploadProfileImageFiles(String secondaryBucketName, List<ImageDto> imageDtoList, boolean shouldDecrypt);

  boolean uploadPostImages(Long postId, List<ImageDto> imageDtoList);

  Map<String, Object> uploadImageToCloud(Long postId, ImageDto imageDtoList);

  Map<String, Object> uploadProfImageToCloud(Long userId, File file);

  boolean deleteImage(String publicId);
}
