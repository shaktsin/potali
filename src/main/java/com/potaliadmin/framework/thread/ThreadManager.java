package com.potaliadmin.framework.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shakti Singh on 12/23/14.
 */
public class ThreadManager {

  private int corePoolSize = 10;
  private int maxPoolSize = 30;

  //when the number of threads is greater than the core,
  // this is the maximum time that excess idle threads will wait for new tasks before terminating.
  private int keepAliveTime = 10000;

  ExecutorService threadPoolExecutor =
      new ThreadPoolExecutor(
          corePoolSize,
          maxPoolSize,
          keepAliveTime,
          TimeUnit.MILLISECONDS,
          new LinkedBlockingQueue<Runnable>()
      );

  private static ThreadManager threadManager;

  private ThreadManager() {}

  static {
    threadManager = new ThreadManager();
  }

  public static ThreadManager getInstance() {
    return threadManager;
  }

  public ExecutorService getExecutorService() {
    return threadPoolExecutor;
  }
}
