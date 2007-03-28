/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.portletcontainer.impl.monitor;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.portletcontainer.monitor.*;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 6 mai 2004
 */
public class PortletRuntimeDatasImpl implements PortletRuntimeData {
  static int NUMBER_OF_REQUEST_MONITOR = 10;
  static long TIME_RANGE = 100; // 200ms

  private String portletAppName;
  private String portletName;
  private boolean initialized;
  private long initializationTime;
  private long lastAccessTime;
  private long lastFailureAccessTime;
  private long lastInitFailureAccessTime;
  private long unavailabilityPeriod = 0;
  private int cacheExpirationPeriod = 0;
  private String globalKey;
  private ExoCache userCache;
  private Log log;
  private ExoCache globalCache;
  private PortletRequestMonitorData[] portletRequestMonitors_;

  public PortletRuntimeDatasImpl(String portletAppName, String portletName,
                                 CacheService cacheService, ExoCache globalCache,
                                 Log log) {
    this.log = log;
    this.globalCache = globalCache;
    this.portletAppName = portletAppName;
    this.portletName = portletName;
    this.globalKey = portletAppName + Constants.PORTLET_HANDLE_ENCODER + portletName;
    try {
      userCache = cacheService.getCacheInstance(globalKey);
    } catch (Exception e) {
      log.error("Can not lookup user cache", e);
    }

    portletRequestMonitors_ = new PortletRequestMonitorData[NUMBER_OF_REQUEST_MONITOR];
    long min = 0;
    long max = TIME_RANGE - 1;
    for (int i = 0; i < NUMBER_OF_REQUEST_MONITOR; i++) {
      portletRequestMonitors_[i] = new PortletRequestMonitorData(min, max);
      min += TIME_RANGE;
      max += TIME_RANGE;
    }
  }

  public boolean isInitialized() { return initialized; }

  public synchronized void setInitialized(boolean b) { this.initialized = b; }

  public String getPortletAppName() { return portletAppName; }

  public synchronized void setPortletAppName(String s) { portletAppName = s; }

  public String getPortletName() {  return portletName; }

  public synchronized void setPortletName(String s) { portletName = s;}

  public long getLastAccessTime() {  return lastAccessTime; }

  public synchronized void setLastAccessTime(long l) { lastAccessTime = l; }

  public long getLastFailureAccessTime() { return lastFailureAccessTime; }

  public long getLastInitFailureAccessTime() { return lastInitFailureAccessTime; }

  public synchronized void setLastInitFailureAccessTime(long l) {lastInitFailureAccessTime = l; }

  public synchronized void setLastFailureAccessTime(long l) {lastFailureAccessTime = l; }

  public long getUnavailabilityPeriod() { return unavailabilityPeriod; }

  public synchronized void setUnavailabilityPeriod(long l) { unavailabilityPeriod = l; }

  public boolean isDataCached(String key, boolean isCacheGlobal) {
    try {
      if (isCacheGlobal) {
        if (globalCache.get(key) != null) {
          return true;
        }
      } else {
        if (userCache.get(key) != null) {
          return true;
        }
      }
    } catch (Exception e) {
      log.error("Unable to load data from user cache", e);
    }
    return false;
  }

  public synchronized void setCachedData(String key, CachedData cachedData, boolean isCacheGlobal) {
    try {
      if (isCacheGlobal) {
        globalCache.put(key, cachedData);
      } else {
        userCache.put(key, cachedData);
      }
    } catch (Exception e) {
      log.error("Unable to store data in user cache", e);
    }
  }

  public CachedData getCachedData(String key, boolean isCacheGlobal) {
    try {
      if (isCacheGlobal) {
        return (CachedData) globalCache.get(key);
      }
      return (CachedData) userCache.get(key);
    } catch (Exception e) {
      log.error("Unable to load data from user cache", e);
    }
    return null;
  }

  public synchronized void removeCachedData(String key, boolean isCacheGlobal) {
    try {
      if (isCacheGlobal) {
        globalCache.remove(key);
      } else {
        userCache.remove(key);
      }
    } catch (Exception e) {
      log.error("Unable to remove data from user cache", e);
    }
  }

  public int getCacheExpirationPeriod() {  return cacheExpirationPeriod; }

  public synchronized void setCacheExpirationPeriod(int t) { cacheExpirationPeriod = t;}

  public long getInitializationTime() { return initializationTime; }

  public synchronized void setInitializationTime(long l) {initializationTime = l; }

  final public void logProcessActionRequest(long startTime, long endTime) {
    long executionTime = endTime - startTime;
    int index = (int) (executionTime / TIME_RANGE);
    if (index >= NUMBER_OF_REQUEST_MONITOR) index = NUMBER_OF_REQUEST_MONITOR - 1;
    portletRequestMonitors_[index].logActionRequest(executionTime);
  }

  final public void logRenderRequest(long startTime, long endTime, boolean cacheHit) {
    long executionTime = endTime - startTime;
    int index = (int) (executionTime / TIME_RANGE);
    if (index >= NUMBER_OF_REQUEST_MONITOR) index = NUMBER_OF_REQUEST_MONITOR - 1;
    portletRequestMonitors_[index].logRenderRequest(executionTime, cacheHit);
  }

  public PortletRequestMonitorData[] getPortletRequestMonitorData() {
    return portletRequestMonitors_;
  }

  public synchronized boolean isAvailable(long l) {
    if ((l - getLastFailureAccessTime() > getUnavailabilityPeriod())) {
      setUnavailabilityPeriod(0);
      return true;
    } else {
      return false;
    }
  }

  public synchronized boolean isInitialisationAllowed(long l) {
    if ((l - getLastInitFailureAccessTime() > getUnavailabilityPeriod())) {
      setUnavailabilityPeriod(0);
      return true;
    } else {
      return false;
    }
  }

  public long whenAvailable() {
    long period = getUnavailabilityPeriod() - (System.currentTimeMillis() - getLastInitFailureAccessTime());
    if(period < 0)   return -1;
    else   return period;
  }
}