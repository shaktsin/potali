package com.potaliadmin.pact.service.cache;

import com.potaliadmin.constants.cache.MemCacheNS;

import java.util.List;

/**
 * Created by Shakti Singh on 1/10/15.
 */
public interface MemCacheService {

  /**
   * Puts Element into the Cache Identified by Cache with the given key
   *
   * @param cache in which value has to be added
   * @param key   key to be used
   * @param value value to be associated
   */
  public void put(MemCacheNS cache, String key, Object value);

  /**
   * Puts Element into the Cache Identified by Cache with the given key and specifies a maxLifeInseconds after which
   * cache entry will be evicted
   *
   * @param cache            in which value has to be added
   * @param key              key to be used
   * @param value            value to be associated
   * @param maxLifeInSeconds max life of cache element
   */
  public void put(MemCacheNS cache, String key, Object value, int maxLifeInSeconds);

  /**
   * Reads from they cache entry specified by key
   *
   * @param cache
   * @param key
   * @return Value stored in cache for key, null if not found
   */
  public Object get(MemCacheNS cache, String key);

  /**
   * Removes the Value from Cache , entry specified by key
   *
   * @param cache
   * @param key
   */
  public void remove(MemCacheNS cache, String key);


  public void remove(MemCacheNS cache, List<String> keys);
}
