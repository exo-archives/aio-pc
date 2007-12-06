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

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 4 mai 2004
 */
public interface PortletRuntimeData {
  String getPortletAppName();
  String getPortletName();
  boolean isInitialized();
  long getInitializationTime();
  long getLastAccessTime();
  long getLastFailureAccessTime();
  long getLastInitFailureAccessTime();
  void setLastInitFailureAccessTime(long lastInitFailureAccessTime);

  long getUnavailabilityPeriod();
  boolean isDataCached(String key, boolean isCacheGlobal);
  String getCacheScope();
  CachedData getCachedData(String key, boolean isCacheGlobal);
  int getCacheExpirationPeriod();
  PortletRequestMonitorData[] getPortletRequestMonitorData();
}