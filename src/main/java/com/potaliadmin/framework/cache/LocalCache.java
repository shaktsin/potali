package com.potaliadmin.framework.cache;

import java.io.Serializable;

/**
 * Created by Shakti Singh on 12/14/14.
 */
public interface LocalCache extends Serializable {

  public void reset();

  public void freeze();
}
