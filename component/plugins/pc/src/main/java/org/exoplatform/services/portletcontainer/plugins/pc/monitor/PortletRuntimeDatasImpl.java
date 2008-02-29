/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.plugins.pc.monitor;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.portletcontainer.monitor.CachedData;
import org.exoplatform.services.portletcontainer.monitor.PortletRequestMonitorData;
import org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData;

/**
 * Created y the eXo platform team.
 * User: Benjamin Mestrallet
 * Date: 6 mai 2004
 */
public class PortletRuntimeDatasImpl implements PortletRuntimeData {

  /**
   * Number of request monitors.
   */
  private static int NUMBER_OF_REQUEST_MONITOR = 10;

  /**
   * Time range.
   */
  private static long TIME_RANGE = 100;

  /**
   * Portlet application name.
   */
  private String portletAppName;

  /**
   * Portlet name.
   */
  private String portletName;

  /**
   * Is initialized.
   */
  private boolean initialized;

  /**
   * Initialization time.
   */
  private long initializationTime;

  /**
   * Access time.
   */
  private long lastAccessTime;

  /**
   * Failure access time.
   */
  private long lastFailureAccessTime;

  /**
   * Init failure access time.
   */
  private long lastInitFailureAccessTime;

  /**
   * Unavailability period.
   */
  private long unavailabilityPeriod = 0;

  /**
   * Expiration period.
   */
  private int cacheExpirationPeriod = 0;

  /**
   * Global key.
   */
  private final String globalKey;

  /**
   * Cache scope.
   */
  private String cacheScope;

  /**
   * User cache.
   */
  private ExoCache userCache;

  /**
   * Logger.
   */
  private final Log log;

  /**
   * Global cache.
   */
  private final ExoCache globalCache;

  /**
   * Portlet request monitor data.
   */
  private final PortletRequestMonitorData[] portletRequestMonitors;

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param cacheService cache service
   * @param globalCache global cache
   * @param log log
   */
  public PortletRuntimeDatasImpl(final String portletAppName,
      final String portletName,
      final CacheService cacheService,
      final ExoCache globalCache,
      final Log log) {
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

    portletRequestMonitors = new PortletRequestMonitorData[NUMBER_OF_REQUEST_MONITOR];
    long min = 0;
    long max = TIME_RANGE - 1;
    for (int i = 0; i < NUMBER_OF_REQUEST_MONITOR; i++) {
      portletRequestMonitors[i] = new PortletRequestMonitorData(min, max);
      min += TIME_RANGE;
      max += TIME_RANGE;
    }
  }

  /**
   * Overridden method.
   *
   * @return initialized
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#isInitialized()
   */
  public final boolean isInitialized() {
    return initialized;
  }

  /**
   * @param b initialized
   */
  public final synchronized void setInitialized(final boolean b) {
    this.initialized = b;
  }

  /**
   * Overridden method.
   *
   * @return portlet app name
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#getPortletAppName()
   */
  public final String getPortletAppName() {
    return portletAppName;
  }

  /**
   * @param s portlet app name
   */
  public final synchronized void setPortletAppName(final String s) {
    portletAppName = s;
  }

  /**
   * Overridden method.
   *
   * @return portlet name
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#getPortletName()
   */
  public final String getPortletName() {
    return portletName;
  }

  /**
   * @param s portlet name
   */
  public final synchronized void setPortletName(final String s) {
    portletName = s;
  }

  /**
   * Overridden method.
   *
   * @return time
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#getLastAccessTime()
   */
  public final long getLastAccessTime() {
    return lastAccessTime;
  }

  /**
   * @param l time
   */
  public final synchronized void setLastAccessTime(final long l) {
    lastAccessTime = l;
  }

  /**
   * Overridden method.
   *
   * @return time
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#getLastFailureAccessTime()
   */
  public final long getLastFailureAccessTime() {
    return lastFailureAccessTime;
  }

  /**
   * Overridden method.
   *
   * @return time
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#getLastInitFailureAccessTime()
   */
  public final long getLastInitFailureAccessTime() {
    return lastInitFailureAccessTime;
  }

  /**
   * Overridden method.
   *
   * @param l time
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#setLastInitFailureAccessTime(long)
   */
  public final synchronized void setLastInitFailureAccessTime(final long l) {
    lastInitFailureAccessTime = l;
  }

  /**
   * @param l time
   */
  public final synchronized void setLastFailureAccessTime(final long l) {
    lastFailureAccessTime = l;
  }

  /**
   * Overridden method.
   *
   * @return time
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#getUnavailabilityPeriod()
   */
  public final long getUnavailabilityPeriod() {
    return unavailabilityPeriod;
  }

  /**
   * @param l time
   */
  public final synchronized void setUnavailabilityPeriod(final long l) {
    unavailabilityPeriod = l;
  }

  /**
   * Overridden method.
   *
   * @param key key
   * @param isCacheGlobal is cache global
   * @return is data cached
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#isDataCached(java.lang.String, boolean)
   */
  public final boolean isDataCached(final String key, final boolean isCacheGlobal) {
    try {
      if (isCacheGlobal) {
        if (globalCache.get(key) != null)
          return true;
      } else if (userCache.get(key) != null)
        return true;
    } catch (Exception e) {
      log.error("Unable to load data from user cache", e);
    }
    return false;
  }

  /**
   * @param key key
   * @param cachedData cached data
   * @param isCacheGlobal is cache global
   */
  public final synchronized void setCachedData(final String key,
      final CachedData cachedData,
      final boolean isCacheGlobal) {
    try {
      if (isCacheGlobal)
        globalCache.put(key, cachedData);
      else
        userCache.put(key, cachedData);
    } catch (Exception e) {
      log.error("Unable to store data in user cache", e);
    }
  }

  /**
   * Overridden method.
   *
   * @param key key
   * @param isCacheGlobal is cache global
   * @return cached data
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#getCachedData(java.lang.String, boolean)
   */
  public final CachedData getCachedData(final String key, final boolean isCacheGlobal) {
    try {
      if (isCacheGlobal)
        return (CachedData) globalCache.get(key);
      return (CachedData) userCache.get(key);
    } catch (Exception e) {
      log.error("Unable to load data from user cache", e);
    }
    return null;
  }

  /**
   * @param key key
   * @param isCacheGlobal is cache global
   */
  public final synchronized void removeCachedData(final String key, final boolean isCacheGlobal) {
    try {
      if (isCacheGlobal)
        globalCache.remove(key);
      else
        userCache.remove(key);
    } catch (Exception e) {
      log.error("Unable to remove data from user cache", e);
    }
  }

  /**
   * Overridden method.
   *
   * @return cache expiration period
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#getCacheExpirationPeriod()
   */
  public final int getCacheExpirationPeriod() {
    return cacheExpirationPeriod;
  }

  /**
   * @param t cache expiration period
   */
  public final synchronized void setCacheExpirationPeriod(final int t) {
    cacheExpirationPeriod = t;
  }

  /**
   * Overridden method.
   *
   * @return time
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#getInitializationTime()
   */
  public final long getInitializationTime() {
    return initializationTime;
  }

  /**
   * @param l time
   */
  public final synchronized void setInitializationTime(final long l) {
    initializationTime = l;
  }

  /**
   * @param startTime start time
   * @param endTime end time
   */
  public final void logProcessActionRequest(final long startTime, final long endTime) {
    long executionTime = endTime - startTime;
    int index = (int) (executionTime / TIME_RANGE);
    if (index >= NUMBER_OF_REQUEST_MONITOR)
      index = NUMBER_OF_REQUEST_MONITOR - 1;
    portletRequestMonitors[index].logActionRequest(executionTime);
  }

  /**
   * @param startTime start time
   * @param endTime end time
   * @param cacheHit cache hit
   */
  public final void logRenderRequest(final long startTime,
      final long endTime,
      final boolean cacheHit) {
    long executionTime = endTime - startTime;
    int index = (int) (executionTime / TIME_RANGE);
    if (index >= NUMBER_OF_REQUEST_MONITOR)
      index = NUMBER_OF_REQUEST_MONITOR - 1;
    portletRequestMonitors[index].logRenderRequest(executionTime, cacheHit);
  }

  /**
   * @param startTime start time
   * @param endTime end time
   */
  public final void logProcessEventRequest(final long startTime, final long endTime) {
    long executionTime = endTime - startTime;
    int index = (int) (executionTime / TIME_RANGE);
    if (index >= NUMBER_OF_REQUEST_MONITOR)
      index = NUMBER_OF_REQUEST_MONITOR - 1;
    portletRequestMonitors[index].logEventRequest(executionTime);
  }

  /**
   * @param startTime start time
   * @param endTime end time
   * @param cacheHit cache hit
   */
  public final void logServeResourceRequest(final long startTime,
      final long endTime,
      final boolean cacheHit) {
    long executionTime = endTime - startTime;
    int index = (int) (executionTime / TIME_RANGE);
    if (index >= NUMBER_OF_REQUEST_MONITOR)
      index = NUMBER_OF_REQUEST_MONITOR - 1;
    portletRequestMonitors[index].logResourceRequest(executionTime, cacheHit);
  }

  /**
   * Overridden method.
   *
   * @return monitor data
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#getPortletRequestMonitorData()
   */
  public final PortletRequestMonitorData[] getPortletRequestMonitorData() {
    return portletRequestMonitors;
  }

  /**
   * @param l time
   * @return is available
   */
  public final synchronized boolean isAvailable(final long l) {
    if ((l - getLastFailureAccessTime() >= getUnavailabilityPeriod())) {
      setUnavailabilityPeriod(0);
      return true;
    } else
      return false;
  }

  /**
   * @param l time
   * @return is initialization allowed
   */
  public final synchronized boolean isInitialisationAllowed(final long l) {
    if ((l - getLastInitFailureAccessTime() > getUnavailabilityPeriod())) {
      setUnavailabilityPeriod(0);
      return true;
    }
    return false;
  }

  /**
   * @return when portlet gets available
   */
  public final long whenAvailable() {
    long period = getUnavailabilityPeriod()
        - (System.currentTimeMillis() - getLastInitFailureAccessTime());
    if (period < 0)
      return -1;
    return period;
  }

  /**
   * Overridden method.
   *
   * @return cache scope
   * @see org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData#getCacheScope()
   */
  public final String getCacheScope() {
    return cacheScope;
  }

  /**
   * @param cacheScope cache scope
   */
  public final synchronized void setCacheScope(final String cacheScope) {
    this.cacheScope = cacheScope;
  }

}
