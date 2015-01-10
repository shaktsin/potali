package com.potaliadmin.framework.cache;

import com.potaliadmin.constants.cache.MemCacheNS;
import com.potaliadmin.constants.cluster.EnumCluster;
import com.potaliadmin.dto.internal.cache.cluster.ClusterVO;
import com.potaliadmin.framework.cache.cluster.ClusterCache;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.OperationTimeoutException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Shakti Singh on 1/10/15.
 * Thanks to Vaibhav AD, for his code - Call him once
 */
public class MemcacheClientFactory {

  private static Logger logger = LoggerFactory.getLogger(MemcacheClientFactory.class);

  private final static int POOL_SIZE = 10;
  private final static int GET_TIMEOUT = 1; //in seconds

  private static boolean factoryInit = false;
  private static Random random = new Random();
  private static MemcachedClient[] dataGrid = new MemcachedClient[POOL_SIZE];

  private static MemcachedClient getInstance() {

    if (!factoryInit) {
      List<ClusterVO> memCacheServers = ClusterCache.getCache().getAllActiveClusterByType(EnumCluster.MEMCAHCE.getName());
      StringBuilder memCacheIpListBuilder = new StringBuilder();

      for (ClusterVO clusterInfoVO : memCacheServers) {
        String address = clusterInfoVO.getHost() + ":"+clusterInfoVO.getPort();
        if (clusterInfoVO != null && StringUtils.isNotBlank(address)) {
          memCacheIpListBuilder.append(address).append(" ");
        }
      }


      MemcacheClientFactory.setInstance(memCacheIpListBuilder.toString());
    }
    return dataGrid[random.nextInt(POOL_SIZE)];
  }

  public static synchronized void setInstance(String iplist) {
    try {
      for (int i = 0; i < dataGrid.length; i++) {
        dataGrid[i] = new MemcachedClient(AddrUtil.getAddresses(iplist));
      }
      factoryInit = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void set(String key, int expirySecs, Object obj) {
    try {
      key = key.replace(" ", "");
      getInstance().set(key, expirySecs, obj);
    } catch (OperationTimeoutException chke) {
      logger.error("Memcache threw OperationTimeoutException while setting key :" + key);
    }
  }

  public static Object get(String key) {
    key = key.replace(" ", "");
    Future<Object> f = getInstance().asyncGet(key);
    try {
      return f.get(GET_TIMEOUT, TimeUnit.SECONDS);
    } catch (InterruptedException ie) {
      logger.error("{\"cause\":\"Cancelling get from memcached, exception encountered " + ie.getMessage() + " \", \"req\":\"" + key + "\"}");
      f.cancel(true);
    } catch (ExecutionException ee) {
//      logger.error("Cancelling get from memcached, exception encountered: " + ee.getMessage() + " key: " + key);
      logger.error("{\"cause\":\"Cancelling get from memcached, exception encountered " + ee.getMessage() + " \", \"req\":\"" + key + "\"}");
      f.cancel(true);
    } catch (TimeoutException te) {
      logger.error("{\"cause\":\"Cancelling get from memcached, operation exceeded " + GET_TIMEOUT + " second(s) \", \"req\":\"" + key + "\"}");
      f.cancel(true);
    }
    return null;
  }

  public static long incr(String key, int by) {
    try {
      key = key.replace(" ", "");
      return getInstance().incr(key, by);
    } catch (OperationTimeoutException chke) {
      throw new RuntimeException("Memcache threw OperationTimeoutException");
    }
  }

  public static void delete(String key) {
    try {
      key = key.replace(" ", "");
      getInstance().delete(key);
    } catch (OperationTimeoutException chke) {
      throw new RuntimeException("Memcache threw OperationTimeoutException");
    }
  }

  private static String getKeyForCache(MemCacheNS memCacheNS, String key) {
    String nameSpace = memCacheNS.getBucket();
    String keyToPutInCache = nameSpace.concat(key);
    keyToPutInCache = keyToPutInCache.replace(" ", "");

    return keyToPutInCache;
  }
}
