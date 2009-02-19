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
package org.exoplatform.services.portletcontainer.monitor;

import java.util.Map;

/**
 * Created y the eXo platform team User: Benjamin Mestrallet Date: 4 mai 2004 .
 */
public interface PortletContainerMonitor {

  /**
   * @return portlet runtime data map
   */
  Map getPortletRuntimeDataMap();

  /**
   * @param portletAppName app name
   * @return portlet app definition version number
   */
  long getPortletVersionNumber(String portletAppName);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @return is the portlet initialized
   */
  boolean isInitialized(String portletAppName, String portletName);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @return is the portlet broken
   */
  boolean isBroken(String portletAppName, String portletName);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @return is the portlet destroyed
   */
  boolean isDestroyed(String portletAppName, String portletName);

  /**
   * @param portletApplicationName app name
   * @param portletName portlet name
   * @param l l
   * @return is the portlet available
   */
  boolean isAvailable(String portletApplicationName, String portletName, long l);

  /**
   * @param portletApplicationName app name
   * @param portletName portlet name
   * @return is the portlet available
   */
  boolean isAvailable(String portletApplicationName, String portletName);

  /**
   * @param portletApplicationName app name
   * @param portletName portlet name
   * @return when the portlet will be available
   */
  long whenAvailable(String portletApplicationName, String portletName);

  /**
   * @param portletApplicationName app name
   * @param portletName portlet name
   * @param l l
   * @return either initialization is allowed
   */
  boolean isInitialisationAllowed(String portletApplicationName, String portletName, long l);

  /**
   * @param portletApplicationName app name
   * @param portletName portlet name
   * @param key cache key
   * @param isCacheGlobal either use global cache
   * @return either data were cached
   */
  boolean isDataCached(String portletApplicationName,
      String portletName,
      String key,
      boolean isCacheGlobal);

  /**
   * @param portletApplicationName app name
   * @param portletName portlet name
   * @return cache expiration period
   */
  int getCacheExpirationPeriod(String portletApplicationName, String portletName);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @param key cache key
   * @param isCacheGlobal either use global cache
   * @return last access time
   */
  long getPortletLastAccessTime(String portletAppName,
      String portletName,
      String key,
      boolean isCacheGlobal);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @param key cache key
   * @param isCacheGlobal either use global cache
   * @return last cache update time
   */
  long getPortletLastCacheUpdateTime(final String portletAppName,
      final String portletName,
      final String key,
      final boolean isCacheGlobal);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @param key cache key
   * @param isCacheGlobal either use global cache
   * @return cached data
   */
  String getCachedTitle(String portletAppName, String portletName, String key, boolean isCacheGlobal);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @param key cache key
   * @param isCacheGlobal either use global cache
   * @return cached data
   */
  String getCachedETag(String portletAppName, String portletName, String key, boolean isCacheGlobal);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @param key cache key
   * @param isCacheGlobal either use global cache
   * @return cached data
   */
  byte[] getCachedContent(String portletAppName,
      String portletName,
      String key,
      boolean isCacheGlobal);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @param accessTime access time
   */
  void setInitializationTime(String portletAppName, String portletName, long accessTime);

  /**
   * @param portletAppName app name
   * @param portletName portlet name
   * @return initialization time
   */
  long getInitializationTime(String portletAppName, String portletName);

}
