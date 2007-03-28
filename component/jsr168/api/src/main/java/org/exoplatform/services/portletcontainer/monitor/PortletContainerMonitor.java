package org.exoplatform.services.portletcontainer.monitor;

import java.util.Map;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 4 mai 2004
 */
public interface PortletContainerMonitor {

  Map getPortletRuntimeDataMap();

  long getPortletVersionNumber(String portletAppName);

  boolean isInitialized(String portletAppName, String portletName);

  boolean isBroken(String portletAppName, String portletName);

  boolean isDestroyed(String portletAppName, String portletName);

  boolean isAvailable(String portletApplicationName, String portletName, long l);
  
  boolean isAvailable(String portletApplicationName, String portletName);
  
  long whenAvailable(String portletApplicationName, String portletName);

  boolean isInitialisationAllowed(String portletApplicationName, String portletName, long l);

  boolean isDataCached(String portletApplicationName, String portletName,
                              String key, boolean isCacheGlobal);

  int getCacheExpirationPeriod(String portletApplicationName, String portletName);

  long getPortletLastAccessTime(String portletAppName, String portletName,
                                       String key, boolean isCacheGlobal);

  String getCachedTitle(String portletAppName, String portletName,
                               String key, boolean isCacheGlobal);

  char[] getCachedContent(String portletAppName, String portletName,
                          String key, boolean isCacheGlobal);

  void setInitializationTime(String portletAppName, String portletName, long accessTime);

  long getInitializationTime(String portletAppName, String portletName);
    
}
