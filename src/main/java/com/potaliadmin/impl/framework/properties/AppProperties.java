package com.potaliadmin.impl.framework.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Shakti Singh on 12/14/14.
 */
@Component
public class AppProperties {

  private static final String DEV = "dev";
  private static final String PROD = "prod";

  @Value("${ENV}")
  private String env;

  @Value("${ES_CLUSTER}")
  private String esClusterName;

  @Value("${UPLOAD_PIC}")
  private String uploadPicPath;

  @Value("${AMAZON_PARENT_BUCKET}")
  private String amazonPrimaryBucketName;

  @Value("${S3_ACCESS_KEY}")
  private String s3AccessKey;

  @Value("${S3_SECRET_KEY}")
  private String s3SecretKey;

  @Value("${CLOUD_NAME}")
  private String cloudName;

  @Value("${CLOUD_API_KEY}")
  private String cloudApiKey;

  @Value("${CLOUD_SEC_KEY}")
  private String cloudSecKey;

  @Value("${CLOUD_IMAGE_PATH}")
  private String cloudImagePath;

  @Value("${CLOUD_DOC_PATH}")
  private String cloudDocPath;



  public boolean isDev() {
    return DEV.equalsIgnoreCase(env);
  }

  public boolean isProd() {
    return PROD.equalsIgnoreCase(env);
  }

  public String getEsClusterName() {
    return esClusterName;
  }

  public String getUploadPicPath() {
    return uploadPicPath;
  }

  public String getAmazonPrimaryBucketName() {
    return amazonPrimaryBucketName;
  }

  public String getS3AccessKey() {
    return s3AccessKey;
  }

  public String getS3SecretKey() {
    return s3SecretKey;
  }

  public String getCloudName() {
    return cloudName;
  }

  public void setCloudName(String cloudName) {
    this.cloudName = cloudName;
  }

  public String getCloudApiKey() {
    return cloudApiKey;
  }

  public void setCloudApiKey(String cloudApiKey) {
    this.cloudApiKey = cloudApiKey;
  }

  public String getCloudSecKey() {
    return cloudSecKey;
  }

  public void setCloudSecKey(String cloudSecKey) {
    this.cloudSecKey = cloudSecKey;
  }

  public String getCloudImagePath() {
    return cloudImagePath;
  }

  public void setCloudImagePath(String cloudImagePath) {
    this.cloudImagePath = cloudImagePath;
  }

  public String getCloudDocPath() {
    return cloudDocPath;
  }

  public void setCloudDocPath(String cloudDocPath) {
    this.cloudDocPath = cloudDocPath;
  }
}
