package com.potaliadmin.framework.elasticsearch.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by shakti on 17/1/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ElasticEntity {

  public static final String DEFAULT_PARENT_TYPE = "none";

  public String type();
  public String parentType() default DEFAULT_PARENT_TYPE;
}
