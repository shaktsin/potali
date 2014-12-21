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

  public boolean isDev() {
    return DEV.equalsIgnoreCase(env);
  }

  public boolean isProd() {
    return PROD.equalsIgnoreCase(env);
  }
}
