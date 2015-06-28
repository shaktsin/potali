package com.potaliadmin.impl.framework.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.cloudinary.Cloudinary;
import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.attachment.EnumAttachmentType;
import com.potaliadmin.dto.internal.image.ImageDto;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.impl.framework.properties.AppProperties;
import com.potaliadmin.pact.framework.aws.UploadService;
import com.potaliadmin.util.image.ImageSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Shakti Singh on 1/3/15.
 */
@Service
public class UploadServiceImpl implements UploadService {

  private static Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);
  private static final String AMZ_PATH = "https://s3-ap-southeast-1.amazonaws.com";
  private static final String CLOUD_IMAGE_PATH = "http://res.cloudinary.com/shaktsin/image/upload/";
  private static final String CLOUD_DOC_PATH = "http://res.cloudinary.com/shaktsin/raw/upload/";

  public static AWSCredentials basicAWSCredentials;

  @Autowired
  AppProperties appProperties;

  @PostConstruct
  public void init() {
    basicAWSCredentials = new BasicAWSCredentials(appProperties.getS3AccessKey(), appProperties.getS3SecretKey());

  }

  @Override
  public String getCanonicalPathOfResource(String folderName, String fileName) {
    return AMZ_PATH + DefaultConstants.PATH_SEPARATOR +
        getAppProperties().getAmazonPrimaryBucketName() + DefaultConstants.PATH_SEPARATOR + folderName +
        DefaultConstants.PATH_SEPARATOR + fileName;
  }

  @Override
  public String getCanonicalPathOfCloudResource(String publicId, Long version, String format, EnumAttachmentType enumAttachmentType) {
    String primaryPath = getAppProperties().getCloudImagePath();
    if (enumAttachmentType.getId() == EnumAttachmentType.DOC.getId()) {
      primaryPath = getAppProperties().getCloudDocPath();
      return primaryPath  + "v" + version + DefaultConstants.PATH_SEPARATOR
          + publicId ;
    }
    return primaryPath  + "v" + version + DefaultConstants.PATH_SEPARATOR
        + publicId + DefaultConstants.FILE_EXT_SEPARATOR +format;
  }

  @Override
  public boolean uploadProfileImageFiles(String secondaryBucketName, List<ImageDto> imageDtoList,boolean shouldDecrypt) {
    boolean uploaded = false;
    TransferManager transferManager = new TransferManager(basicAWSCredentials);
    try {

      List<File> fileList = new ArrayList<File>();
      for (ImageDto imageDto : imageDtoList) {
        String fileName = imageDto.getCanonicalName();
        if (shouldDecrypt) {
          fileName = ImageSecurity.decodeImage(fileName);
        }
        File file = new File(fileName);
        fileList.add(file);
      }

      String parentDirFilePath = getAppProperties().getUploadPicPath()+File.separator+DefaultConstants.PROFILE;
      File parentDir = new File(parentDirFilePath) ;

      MultipleFileUpload multipleFileUpload = transferManager.uploadFileList(appProperties.getAmazonPrimaryBucketName(),
          secondaryBucketName, parentDir, fileList);

      multipleFileUpload.waitForCompletion();

      if (multipleFileUpload.isDone()) {
        uploaded = true;
      }

    } catch (Exception e) {
      logger.error("Error while uploading files to amazon " + imageDtoList, e);
    } finally {
      transferManager.shutdownNow();
    }
    return uploaded;
  }

  @Override
  public boolean uploadPostImages(Long postId, List<ImageDto> imageDtoList) {
    boolean uploaded = false;
    TransferManager transferManager = new TransferManager(basicAWSCredentials);
    try {
      String parentDirFilePath = getAppProperties().getUploadPicPath();

      //String secondaryBucketName = File.separator + EnumBucket.POST_BUCKET.getName();

      List<File> fileList = new ArrayList<File>();
      for (ImageDto imageDto : imageDtoList) {
        String fileName = imageDto.getCanonicalName();
        File file = new File(fileName);
        fileList.add(file);
      }

      File parentDir = new File(parentDirFilePath) ;

      MultipleFileUpload multipleFileUpload = transferManager.uploadFileList(appProperties.getAmazonPrimaryBucketName(),
          null, parentDir, fileList);

      multipleFileUpload.waitForCompletion();

      if (multipleFileUpload.isDone()) {
        uploaded = true;
      }

    } catch (Exception e) {
      logger.error("Error while uploading images to ",e);
      uploaded = false;
    }



    return uploaded;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> uploadImageToCloud(Long postId, ImageDto imageDto) {
    try {
      Cloudinary cloudinary = new Cloudinary(Cloudinary.asMap(
          "cloud_name", getAppProperties().getCloudName(),
          "api_key", getAppProperties().getCloudApiKey(),
          "api_secret", getAppProperties().getCloudSecKey()));

      Map params = Cloudinary.asMap("public_id", imageDto.getAttachmentId().toString());
      params.put("use_filename",true);
      String folder = DefaultConstants.POST + File.separator + imageDto.getRelativePath();
      params.put("folder", folder);

      String fileName = imageDto.getCanonicalName();
      File toUpload = new File(fileName);
      return cloudinary.uploader().upload(toUpload, params);

    } catch (Throwable e) {
      logger.error("Error occurred while uploading image is cloud",e);
      throw new PotaliRuntimeException("Error occurred while uploading image is cloud");
    }

  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> uploadImageToCloud(Long postId, Long attachmentId, String relativePath, String path) {
    try {
      Cloudinary cloudinary = new Cloudinary(Cloudinary.asMap(
          "cloud_name", getAppProperties().getCloudName(),
          "api_key", getAppProperties().getCloudApiKey(),
          "api_secret", getAppProperties().getCloudSecKey()));

      Map params = Cloudinary.asMap("public_id", attachmentId.toString());
      params.put("use_filename",true);
      String folder = DefaultConstants.POST + File.separator + relativePath;
      params.put("folder", folder);

      //String fileName = imageDto.getCanonicalName();
      File toUpload = new File(path);
      return cloudinary.uploader().upload(toUpload, params);

    } catch (Throwable e) {
      logger.error("Error occurred while uploading image is cloud",e);
      throw new PotaliRuntimeException("Error occurred while uploading image is cloud");
    }

  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> uploadProfImageToCloud(Long userId, File file) {
    try {
      Cloudinary cloudinary = new Cloudinary(Cloudinary.asMap(
          "cloud_name", getAppProperties().getCloudName(),
          "api_key", getAppProperties().getCloudApiKey(),
          "api_secret", getAppProperties().getCloudSecKey()));

      Map params = Cloudinary.asMap("public_id","prof");
      params.put("use_filename",true);
      String folder = DefaultConstants.PROFILE + File.separator + userId;
      params.put("folder", folder);

      return cloudinary.uploader().upload(file, params);

    } catch (Throwable e) {
      logger.error("Error occurred while uploading image is cloud",e);
      throw new PotaliRuntimeException("Error occurred while uploading image is cloud");
    }

  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> uploadRawFilesToCloud(File file, EnumAttachmentType enumAttachmentType, Long attachmentId, String relativePath) {
    try {
      Cloudinary cloudinary = new Cloudinary(Cloudinary.asMap(
          "cloud_name", getAppProperties().getCloudName(),
          "api_key", getAppProperties().getCloudApiKey(),
          "api_secret", getAppProperties().getCloudSecKey()));

      Map params = Cloudinary.asMap("public_id",attachmentId.toString());
      params.put("use_filename",true);
      String folder = enumAttachmentType.getName() + File.separator + relativePath;
      params.put("folder", folder);
      params.put("resource_type", "raw");

      return cloudinary.uploader().upload(file, params);

    } catch (Throwable e) {
      logger.error("Error occurred while uploading image is cloud",e);
      throw new PotaliRuntimeException("Error occurred while uploading image is cloud");
    }
  }

  @Override
  public boolean deleteImage(String publicId) {
    boolean deleted = false;
    try {
      Cloudinary cloudinary = new Cloudinary(Cloudinary.asMap(
          "cloud_name", getAppProperties().getCloudName(),
          "api_key", getAppProperties().getCloudApiKey(),
          "api_secret", getAppProperties().getCloudSecKey()));

      cloudinary.uploader().destroy(publicId, Cloudinary.asMap("invalidate", true));
      deleted = true;
    } catch (Throwable e) {
      logger.error("Error occurred while uploading image is cloud",e);
      throw new PotaliRuntimeException("Error occurred while uploading image is cloud");
    }
    return deleted;
  }

  public AppProperties getAppProperties() {
    return appProperties;
  }
}
