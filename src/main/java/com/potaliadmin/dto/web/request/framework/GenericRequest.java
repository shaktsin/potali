package com.potaliadmin.dto.web.request.framework;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.plateform.EnumPlateForm;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Shakti Singh on 12/18/14.
 */
public abstract class GenericRequest {

  private Long plateFormId;
  private String appName;

  protected GenericRequest() {
  }

  protected GenericRequest(Long plateFormId, String appName) {
    this.plateFormId = plateFormId;
    this.appName = appName;
  }

  public boolean validate() {
    boolean isValid = true;
    if (!EnumPlateForm.contains(plateFormId)) {
      isValid = false;
    }
    if (isValid && !StringUtils.isBlank(appName) && !appName.equalsIgnoreCase(DefaultConstants.APP_NAME)) {
      isValid = false;
    }
    return isValid;
  }

  public Long getPlateFormId() {
    return plateFormId;
  }

  public void setPlateFormId(Long plateFormId) {
    this.plateFormId = plateFormId;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }
}
