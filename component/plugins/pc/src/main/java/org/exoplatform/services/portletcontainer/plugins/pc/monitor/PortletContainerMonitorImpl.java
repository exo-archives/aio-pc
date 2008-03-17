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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.apache.commons.logging.Log;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.monitor.CachedData;
import org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor;
import org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData;

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Sep 10, 2003
 * Time: 2:36:19 PM
 */
public class PortletContainerMonitorImpl implements PortletContainerMonitor {

  /**
   * Separator.
   */
  public static final char SEPARATOR = '/';

  /**
   * Version numbers.
   */
  public static Map versionNumberMap = new HashMap();

  /**
   * Runtime metadatas.
   */
  private final Map runtimeDatas;

  /**
   * Destroyed portlets.
   */
  private final Map destroyedPortlets;

  /**
   * Broken portlets.
   */
  private final Map brokenPortlets;

  /**
   * Logger.
   */
  private final Log log;

  /**
   * Global cache.
   */
  private final ExoCache globalCache;

  /**
   * Cache service.
   */
  private final CacheService cacheService;

  /**
   * @param cacheService cache service
   * @throws Exception exception
   */
  public PortletContainerMonitorImpl(final CacheService cacheService) throws Exception {
    this.log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
    this.cacheService = cacheService;
    globalCache = cacheService.getCacheInstance(PCConstants.GLOBAL_SCOPE_CACHE);
    runtimeDatas = Collections.synchronizedMap(new HashMap());
    brokenPortlets = Collections.synchronizedMap(new HashMap());
    destroyedPortlets = Collections.synchronizedMap(new HashMap());
  }

  /**
   * Overridden method.
   *
   * @return runtime datas
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#getPortletRuntimeDataMap()
   */
  public final Map getPortletRuntimeDataMap() {
    return runtimeDatas;
  }

  /**
   * @param appName portlet application name
   * @param portletName portlet name
   * @return runtime datas
   */
  public final PortletRuntimeDatasImpl getPortletRuntimeData(final String appName,
      final String portletName) {
    return (PortletRuntimeDatasImpl) runtimeDatas.get(appName + SEPARATOR + portletName);
  }

  /**
   * @param portletApplicationName portlet application name
   */
  public final synchronized void registerPortletApp(final String portletApplicationName) {
    long versionNumber = 1;
    if (versionNumberMap.get(portletApplicationName) != null)
      versionNumber = ((Long) versionNumberMap.get(portletApplicationName)).longValue() + 1;
    versionNumberMap.put(portletApplicationName, new Long(versionNumber));
  }

  /**
   * Overridden method.
   *
   * @param portletAppName portlet app name
   * @return portlet version number
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#getPortletVersionNumber(java.lang.String)
   */
  public final long getPortletVersionNumber(final String portletAppName) {
    return ((Long) versionNumberMap.get(portletAppName)).longValue();
  }

  /**
   * @param portletApplicationName portlet app name
   * @param portletName portlet name
   */
  public final synchronized void register(final String portletApplicationName, final String portletName) {
    PortletRuntimeData rD = new PortletRuntimeDatasImpl(portletApplicationName,
        portletName,
        cacheService,
        globalCache,
        log);
    runtimeDatas.put(portletApplicationName + SEPARATOR + portletName, rD);
    brokenPortlets.remove(portletApplicationName + SEPARATOR + portletName);
    destroyedPortlets.remove(portletApplicationName + SEPARATOR + portletName);
  }

  /**
   * Overridden method.
   *
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @return is initialized
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#isInitialized(java.lang.String, java.lang.String)
   */
  public final boolean isInitialized(final String portletAppName, final String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas.get(portletAppName + SEPARATOR
        + portletName);
    if (datas == null)
      return false;
    if (datas.isInitialized())
      return true;
    return false;
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param cacheExpirationTime cache expiration time
   */
  public final synchronized void init(final String portletAppName,
      final String portletName,
      final int cacheExpirationTime) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    datas.setInitialized(true);
    datas.setCacheExpirationPeriod(cacheExpirationTime);
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   */
  public final synchronized void brokePortlet(final String portletAppName, final String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas.get(portletAppName + SEPARATOR
        + portletName);
    if (datas == null) {
      datas = (PortletRuntimeData) destroyedPortlets.get(portletAppName + SEPARATOR + portletName);
      destroyedPortlets.remove(portletAppName + SEPARATOR + portletName);
    }
    runtimeDatas.remove(portletAppName + SEPARATOR + portletName);
    brokenPortlets.put(portletAppName + SEPARATOR + portletName, datas);
  }

  /**
   * Overridden method.
   *
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @return is broken
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#isBroken(java.lang.String, java.lang.String)
   */
  public final boolean isBroken(final String portletAppName, final String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) brokenPortlets.get(portletAppName + SEPARATOR
        + portletName);
    if (datas != null)
      return true;
    return false;
  }

  /**
   * Overridden method.
   *
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @return is destroyed
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#isDestroyed(java.lang.String, java.lang.String)
   */
  public final boolean isDestroyed(final String portletAppName, final String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) destroyedPortlets.get(portletAppName
        + SEPARATOR + portletName);
    if (datas != null)
      return true;
    return false;
  }

  /**
   * Overridden method.
   *
   * @param portletApplicationName portlet app name
   * @param portletName portlet name
   * @param l time
   * @return is available
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#isAvailable(java.lang.String, java.lang.String, long)
   */
  public final boolean isAvailable(final String portletApplicationName,
      final String portletName,
      final long l) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas
        .get(portletApplicationName + SEPARATOR + portletName);
    if (datas == null)
      return false;
    return datas.isAvailable(l);
  }

  /**
   * Overridden method.
   *
   * @param portletApplicationName portlet app name
   * @param portletName portlet name
   * @return is available
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#isAvailable(java.lang.String, java.lang.String)
   */
  public final boolean isAvailable(final String portletApplicationName, final String portletName) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas
        .get(portletApplicationName + SEPARATOR + portletName);
    if (datas == null)
      return false;
    return datas.isAvailable(System.currentTimeMillis());
  }

  /**
   * Overridden method.
   *
   * @param portletApplicationName portlet app name
   * @param portletName portlet name
   * @return when available
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#whenAvailable(java.lang.String, java.lang.String)
   */
  public final long whenAvailable(final String portletApplicationName, final String portletName) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas
        .get(portletApplicationName + SEPARATOR + portletName);
    if (datas == null)
      return -1;
    if (datas.isAvailable(System.currentTimeMillis()))
      return 0;
    else
      return datas.whenAvailable();
  }

  /**
   * Overridden method.
   *
   * @param portletApplicationName portlet app name
   * @param portletName portlet name
   * @param l time
   * @return is initialization allowed
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#isInitialisationAllowed(java.lang.String, java.lang.String, long)
   */
  public final boolean isInitialisationAllowed(final String portletApplicationName,
      final String portletName,
      final long l) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas
        .get(portletApplicationName + SEPARATOR + portletName);
    if (datas == null)
      return false;
    return datas.isInitialisationAllowed(l);
  }

  /**
   * @param portletApplicationName portlet app name
   * @param portletName portlet name
   */
  public final synchronized void destroy(final String portletApplicationName, final String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas.get(portletApplicationName
        + SEPARATOR + portletName);
    if (datas == null)
      return; //already destroyed or broke
    runtimeDatas.remove(portletApplicationName + SEPARATOR + portletName);
    destroyedPortlets.put(portletApplicationName + SEPARATOR + portletName, datas);
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param l last access time
   */
  public final void setLastAccessTime(final String portletAppName, final String portletName, final long l) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    if (datas == null)
      datas = (PortletRuntimeDatasImpl) brokenPortlets.get(portletAppName + SEPARATOR
          + portletName);
    if (datas == null)
      datas = (PortletRuntimeDatasImpl) destroyedPortlets.get(portletAppName + SEPARATOR
          + portletName);
    datas.setLastAccessTime(l);
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param l last init failure access time
   */
  public final void setLastInitFailureAccessTime(final String portletAppName,
      final String portletName,
      final long l) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas.get(portletAppName + SEPARATOR
        + portletName);
    datas.setLastInitFailureAccessTime(l);
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param l last failure access time
   */
  public final void setLastFailureAccessTime(final String portletAppName,
      final String portletName,
      final long l) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    datas.setLastFailureAccessTime(l);
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param unavailableSeconds unavailability time in seconds
   */
  public final void setUnavailabilityPeriod(final String portletAppName,
      final String portletName,
      final int unavailableSeconds) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    datas.setUnavailabilityPeriod(unavailableSeconds * 1000);
  }

  /**
   * Overridden method.
   *
   * @param portletApplicationName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param isCacheGlobal is cache global
   * @return is data cached
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#isDataCached(java.lang.String, java.lang.String, java.lang.String, boolean)
   */
  public final boolean isDataCached(final String portletApplicationName,
      final String portletName,
      final String key,
      final boolean isCacheGlobal) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas.get(portletApplicationName
        + SEPARATOR + portletName);
    return datas.isDataCached(key, isCacheGlobal);
  }

  /**
   * @param portletApplicationName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param isCacheGlobal is cache global
   */
  public final void removeCachedData(final String portletApplicationName,
      final String portletName,
      final String key,
      final boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas
        .get(portletApplicationName + SEPARATOR + portletName);
    datas.removeCachedData(key, isCacheGlobal);
  }

  /**
   * Overridden method.
   *
   * @param portletApplicationName portlet app name
   * @param portletName portlet name
   * @return cache expiration period
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#getCacheExpirationPeriod(java.lang.String, java.lang.String)
   */
  public final int getCacheExpirationPeriod(final String portletApplicationName, final String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas.get(portletApplicationName
        + SEPARATOR + portletName);
    return datas.getCacheExpirationPeriod();
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param i cache expiration period
   */
  public final void setCacheExpirationPeriod(final String portletAppName,
      final String portletName,
      final int i) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    datas.setCacheExpirationPeriod(i);
  }

  /**
   * Overridden method.
   *
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param isCacheGlobal is cache global
   * @return last access time
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#getPortletLastAccessTime(java.lang.String, java.lang.String, java.lang.String, boolean)
   */
  public final long getPortletLastAccessTime(final String portletAppName,
      final String portletName,
      final String key,
      final boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    CachedData cachedData = datas.getCachedData(key, isCacheGlobal);
    if (cachedData != null)
      return ((CachedDataImpl) cachedData).getLastAccessTime();
    return 0;
  }

  /**
   * Overridden method.
   *
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param isCacheGlobal is cache global
   * @return last cache update time
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#getPortletLastCacheUpdateTime(java.lang.String, java.lang.String, java.lang.String, boolean)
   */
  public final long getPortletLastCacheUpdateTime(final String portletAppName,
      final String portletName,
      final String key,
      final boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    CachedData cachedData = datas.getCachedData(key, isCacheGlobal);
    if (cachedData != null)
      return ((CachedDataImpl) cachedData).getLastUpdateTime();
    return 0;
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param lastAccessTime last access time
   * @param isCacheGlobal is cache global
   */
  public final void setPortletLastAccessTime(final String portletAppName,
      final String portletName,
      final String key,
      final long lastAccessTime,
      final boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    CachedDataImpl cachedData = (CachedDataImpl) datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null) {
      cachedData = new CachedDataImpl();
      cachedData.setLastAccessTime(lastAccessTime);
      datas.setCachedData(key, cachedData, isCacheGlobal);
    } else
      cachedData.setLastAccessTime(lastAccessTime);
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param title title
   * @param isCacheGlobal is cache global
   */
  public final void setCachedTitle(final String portletAppName,
      final String portletName,
      final String key,
      final String title,
      final boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    CachedDataImpl cachedData = (CachedDataImpl) datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null) {
      cachedData = new CachedDataImpl();
      cachedData.setTitle(title);
      datas.setCachedData(key, cachedData, isCacheGlobal);
    } else
      cachedData.setTitle(title);
  }

  /**
   * Overridden method.
   *
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param isCacheGlobal is cache global
   * @return cached title
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#getCachedTitle(java.lang.String, java.lang.String, java.lang.String, boolean)
   */
  public final String getCachedTitle(final String portletAppName,
      final String portletName,
      final String key,
      final boolean isCacheGlobal) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas.get(portletAppName + SEPARATOR
        + portletName);
    CachedData cachedData = datas.getCachedData(key, isCacheGlobal);
    if (cachedData != null)
      return cachedData.getTitle();
    return null;
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param etag ETag
   * @param isCacheGlobal is cache global
   */
  public final void setCachedETag(final String portletAppName,
      final String portletName,
      final String key,
      final String etag,
      final boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    CachedDataImpl cachedData = (CachedDataImpl) datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null) {
      cachedData = new CachedDataImpl();
      cachedData.setETag(etag);
      datas.setCachedData(key, cachedData, isCacheGlobal);
    } else
      cachedData.setETag(etag);
  }

  /**
   * Overridden method.
   *
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param isCacheGlobal is cache global
   * @return ETag
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#getCachedETag(java.lang.String, java.lang.String, java.lang.String, boolean)
   */
  public final String getCachedETag(final String portletAppName,
      final String portletName,
      final String key,
      final boolean isCacheGlobal) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas.get(portletAppName + SEPARATOR
        + portletName);
    CachedData cachedData = datas.getCachedData(key, isCacheGlobal);
    if (cachedData != null)
      return cachedData.getETag();
    return null;
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param content content
   * @param isCacheGlobal is cache global
   * @param isCacheGlobal
   */
  public final void setCachedContent(final String portletAppName,
      final String portletName,
      final String key,
      final byte[] content,
      final boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    CachedDataImpl cachedData = (CachedDataImpl) datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null) {
      cachedData = new CachedDataImpl();
      cachedData.setContent(content);
      datas.setCachedData(key, cachedData, isCacheGlobal);
    } else
      cachedData.setContent(content);
    cachedData.setLastUpdateTime(System.currentTimeMillis());
  }

  /**
   * Overridden method.
   *
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param isCacheGlobal is cache global
   * @return cached content
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#getCachedContent(java.lang.String, java.lang.String, java.lang.String, boolean)
   */
  public final byte[] getCachedContent(final String portletAppName,
      final String portletName,
      final String key,
      final boolean isCacheGlobal) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas.get(portletAppName + SEPARATOR
        + portletName);
    CachedData cachedData = datas.getCachedData(key, isCacheGlobal);
    if (cachedData != null)
      return cachedData.getContent();
    return null;
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param mode mode
   * @param isCacheGlobal is cache global
   */
  public final void setCachedMode(final String portletAppName,
      final String portletName,
      final String key,
      final PortletMode mode,
      final boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    CachedDataImpl cachedData = (CachedDataImpl) datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null) {
      cachedData = new CachedDataImpl();
      cachedData.setMode(mode);
      datas.setCachedData(key, cachedData, isCacheGlobal);
    } else
      cachedData.setMode(mode);
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param window window
   * @param isCacheGlobal is cache global
   */
  public final void setCachedWindowState(final String portletAppName,
      final String portletName,
      final String key,
      final WindowState window,
      final boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    CachedDataImpl cachedData = (CachedDataImpl) datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null) {
      cachedData = new CachedDataImpl();
      cachedData.setWindowState(window);
      datas.setCachedData(key, cachedData, isCacheGlobal);
    } else
      cachedData.setWindowState(window);
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param mode mode
   * @param window window
   * @param isCacheGlobal is cache global
   * @return needs cache invalidation
   */
  public final boolean needsCacheInvalidation(final String portletAppName,
      final String portletName,
      final String key,
      final PortletMode mode,
      final WindowState window,
      final boolean isCacheGlobal) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas.get(portletAppName + SEPARATOR
        + portletName);
    CachedData cachedData = datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null)
      return false;
    if ((cachedData.getMode() != mode) || (cachedData.getWindowState() != window))
      return true;
    return false;
  }

  /**
   * Overridden method.
   *
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param accessTime access time
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#setInitializationTime(java.lang.String, java.lang.String, long)
   */
  public final void setInitializationTime(final String portletAppName,
      final String portletName,
      final long accessTime) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    datas.setInitializationTime(accessTime);
  }

  /**
   * Overridden method.
   *
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @return initialization time
   * @see org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor#getInitializationTime(java.lang.String, java.lang.String)
   */
  public final long getInitializationTime(final String portletAppName, final String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas.get(portletAppName + SEPARATOR
        + portletName);
    return datas.getInitializationTime();
  }

  /**
   * @param portletApplicationName portlet application name
   * @param portletName portlet name
   * @return scope
   */
  public final String getCacheScope(final String portletApplicationName, final String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas.get(portletApplicationName
        + SEPARATOR + portletName);
    return datas.getCacheScope();
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param s scope
   */
  public final void setCacheScope(final String portletAppName, final String portletName, final String s) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas.get(portletAppName
        + SEPARATOR + portletName);
    datas.setCacheScope(s);
  }

}
