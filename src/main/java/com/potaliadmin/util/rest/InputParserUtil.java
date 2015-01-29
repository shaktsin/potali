package com.potaliadmin.util.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shakti on 26/1/15.
 */
public class InputParserUtil {

  private static Logger logger = LoggerFactory.getLogger(InputParserUtil.class);
  private static ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    //objectMapper.registerSubtypes(FullJobVO.class);
  }

  public static Object parseMultiPartObject(String objectStr, Class responseClass) {
    try {
      return objectMapper.readValue(objectStr.getBytes(), responseClass);
    } catch (Exception e) {
      logger.error("Error while parsing object str "+objectStr, e);
      return null;
    }

  }
}
