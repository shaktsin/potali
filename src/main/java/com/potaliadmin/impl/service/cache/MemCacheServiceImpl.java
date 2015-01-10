package com.potaliadmin.impl.service.cache;

import com.potaliadmin.constants.cache.MemCacheNS;
import com.potaliadmin.framework.cache.MemcacheClientFactory;
import com.potaliadmin.pact.service.cache.MemCacheService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Shakti Singh on 1/10/15.
 */
@Service
public class MemCacheServiceImpl implements MemCacheService {

  private int MAX_TIME_IN_CACHE_IN_SECONDS = 60 * 60 * 24 * 28;   //28 days


  private String getKeyForCache(MemCacheNS cacheConfig, String key) {
    String nameSpace = cacheConfig.getBucket();
    String keyToPutInCache = nameSpace.concat(key);
    keyToPutInCache = keyToPutInCache.replace(" ", "");

    return keyToPutInCache;
  }

  @Override
  public void put(MemCacheNS cache, String key, Object value) {
    put(cache, key, value, MAX_TIME_IN_CACHE_IN_SECONDS);
  }

  @Override
  public void put(MemCacheNS cache, String key, Object value, int maxLifeInSeconds) {
    MemcacheClientFactory.set(getKeyForCache(cache, key), maxLifeInSeconds, value);
  }

  @Override
  public Object get(MemCacheNS cache, String key) {
    return MemcacheClientFactory.get(getKeyForCache(cache, key));
  }

  @Override
  public void remove(MemCacheNS cache, String key) {
    MemcacheClientFactory.delete(getKeyForCache(cache, key));
  }

  @Override
  public void remove(MemCacheNS cache, List<String> keys) {
    for (String key : keys) {
      remove(cache, key);
    }
  }
}
