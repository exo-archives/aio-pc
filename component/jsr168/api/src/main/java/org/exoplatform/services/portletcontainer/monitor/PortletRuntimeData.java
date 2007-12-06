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
  CachedData getCachedData(String key, boolean isCacheGlobal);
  int getCacheExpirationPeriod();
  PortletRequestMonitorData[] getPortletRequestMonitorData();
}