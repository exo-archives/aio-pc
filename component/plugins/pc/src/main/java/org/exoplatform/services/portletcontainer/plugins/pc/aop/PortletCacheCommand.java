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
package org.exoplatform.services.portletcontainer.plugins.pc.aop;

import javax.portlet.PortletMode;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;
import javax.portlet.WindowState;

import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.plugins.pc.monitor.PortletContainerMonitorImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ActionRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.EventRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortalContextImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RenderRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RenderResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceResponseImp;

/**
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
public class PortletCacheCommand extends BaseCommandUnit {

  /**
   * 1000 milliseconds in a second.
   */
  private static final int MILLISECONDS = 1000;

  /**
   * Portlet container conf.
   */
  private final PortletContainerConf conf;

  /**
   * Portlet container monitor.
   */
  private final PortletContainerMonitorImpl portletMonitor;

  /**
   * @param conf portlet container conf
   * @param monitor portlet container monitor
   */
  public PortletCacheCommand(final PortletContainerConf conf,
      final PortletContainerMonitorImpl monitor) {
    this.conf = conf;
    portletMonitor = monitor;
  }

  /**
   * Overridden method.
   *
   * @param rcontext execution context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#render(org.exoplatform.services.portletcontainer.plugins.pc.aop.RenderExecutionContext)
   */
  protected final Object render(final RenderExecutionContext rcontext) throws Throwable {
    if (!conf.isCacheEnable())
      return rcontext.executeNextUnit();

    log.debug("--> render method, call cache interceptor");
    RenderRequestImp req = (RenderRequestImp) rcontext.getRequest();
    RenderResponseImp res = (RenderResponseImp) rcontext.getResponse();
    if (req.getPortletDatas().getCaching() == null)
      return rcontext.executeNextUnit();
    String portletAppName = req.getPortletWindowInternal().getWindowID()
        .getPortletApplicationName();
    String portletName = req.getPortletWindowInternal().getWindowID().getPortletName();

    int expirationPeriod = portletMonitor.getCacheExpirationPeriod(portletAppName, portletName);
    if (expirationPeriod == 0) {
      // No cache use. 
      log.debug("Expiration period 0 before proceed");
      rcontext.executeNextUnit();
      log.debug("Expiration period 0 after proceed");
    } else {
       PortletMode mode = req.getPortletMode();
       WindowState window = req.getWindowState();

       boolean isCacheGlobal = resolveCache(req.getPortletDatas().getGlobalCache(), portletMonitor
           .getCacheScope(portletAppName, portletName));
       log.debug("Is cache global: " + isCacheGlobal);
       
       String key = generateKey(req, isCacheGlobal, "");
       long lastUpdateTime = 0;
       if (key != null)
         lastUpdateTime = portletMonitor.getPortletLastCacheUpdateTime(portletAppName, portletName, key,
             isCacheGlobal);
       long currentAccessTime = System.currentTimeMillis();

       if ((key != null)
           && portletMonitor.needsCacheInvalidation(portletAppName, portletName, key, mode, window,
               isCacheGlobal))
         portletMonitor.removeCachedData(portletAppName, portletName, key, isCacheGlobal);
       
       if (expirationPeriod == -1) {
         // Cache never expires
         if ((key != null)
             && portletMonitor.isDataCached(portletAppName, portletName, key, isCacheGlobal)) {
           log.debug("Use cache : Expiration period -1 data already cached");
           useCache(portletAppName, portletName, key, res, isCacheGlobal);
         } else {
           log.debug("Expiration period -1 data first cached, before proceed");
           rcontext.executeNextUnit();
           log.debug("Expiration period -1 data first cached, after proceed");
           updateCache(portletAppName, portletName, key, res, mode, window, isCacheGlobal);
         }
       } else if (currentAccessTime - lastUpdateTime > expirationPeriod * MILLISECONDS) {
         // Cache process
         log.debug("Expiration period currentAccessTime - lastCacheUpdateTime > expirationPeriod * 1000 before proceed");
         if (key != null && req.getPortletDatas().getApplication().getVer2()) {
           ((PortalContextImp) req.getPortalContext()).addProperty(RenderResponse.ETAG,
               portletMonitor.getCachedETag(portletAppName, portletName, key, isCacheGlobal));
         }
         rcontext.executeNextUnit();
         log.debug("Expiration period currentAccessTime - lastCacheUpdateTime > expirationPeriod * 1000 after proceed");
         if (res.getOutput().getProperties().get(RenderResponse.USE_CACHED_CONTENT) != null)
           useCache(portletAppName, portletName, key, res, isCacheGlobal);
         else
           updateCache(portletAppName, portletName, key, res, mode, window, isCacheGlobal);
         updateCacheParams(portletAppName, portletName, key, res, isCacheGlobal);
       } else if ((0 < currentAccessTime - lastUpdateTime)
           && (currentAccessTime - lastUpdateTime < expirationPeriod * MILLISECONDS)) {
         log.debug("Use cache : currentAccessTime - lastCacheUpdateTime < expirationPeriod * 1000");
         if (key != null)
           useCache(portletAppName, portletName, key, res, isCacheGlobal);
       }
    }
    return null;
  }

  /**
   * @param req request
   * @param isCacheGlobal TODO
   * @param modifier TODO
   * @return generated key
   */
  private String generateKey(final PortletRequestImp req, boolean isCacheGlobal, String modifier) {
    if (modifier == null)
      modifier = "";
    String uniqueId = "";
    if (isCacheGlobal) {
      uniqueId = String.valueOf((req.getInput().getInternalWindowID().getPortletApplicationName() +
          req.getInput().getInternalWindowID().getPortletName()).hashCode());
      return req.getInput().getInternalWindowID().getOwner() + uniqueId + modifier;
    }
    uniqueId = req.getInput().getInternalWindowID().getUniqueID();
    return req.getSession().getId() + req.getRemoteUser() + req.getInput().getInternalWindowID().getOwner() + uniqueId
      + modifier;
  }

  /**
   * Overridden method.
   *
   * @param rcontext execution context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#serveResource(org.exoplatform.services.portletcontainer.plugins.pc.aop.ResourceExecutionContext)
   */
  protected final Object serveResource(final ResourceExecutionContext rcontext) throws Throwable {
    log.debug("--> serveResource method, call cache interceptor");

    if (!conf.isCacheEnable())
      return rcontext.executeNextUnit();

    log.debug("--> render method, call cache interceptor");
    ResourceRequestImp req = (ResourceRequestImp) rcontext.getRequest();
    ResourceResponseImp res = (ResourceResponseImp) rcontext.getResponse();
    if (req.getPortletDatas().getCaching() == null)
      return rcontext.executeNextUnit();
    String portletAppName = req.getPortletWindowInternal().getWindowID()
        .getPortletApplicationName();
    String portletName = req.getPortletWindowInternal().getWindowID().getPortletName();
    String uniqueId = req.getPortletWindowInternal().getWindowID().getUniqueID();

    String resourceUID = null;
    if ( (req.getInput().getPropertyParams() != null) && (req.getInput().getPropertyParams().containsKey(ResourceURL.SHARED)) )
       resourceUID = (String)req.getInput().getPropertyParams().get(ResourceURL.SHARED);

    boolean isCacheGlobal = resolveCache(req.getPortletDatas().getGlobalCache(), portletMonitor
        .getCacheScope(portletAppName, portletName));
    log.debug("Is cache global: " + isCacheGlobal);
    String key = null;


    // In case of ResourceURL.SHARED is set in the ResourceInput,
    // cache will not expire and the key for storage will be value of this property
    // PLT 13.7
    int expirationPeriod = -1;
    if (resourceUID != null)
      key = resourceUID;
    else {
      if (isCacheGlobal)
        key = req.getInput().getInternalWindowID().getOwner() + uniqueId;
      else
        key = generateKey(req, isCacheGlobal, "r");
      expirationPeriod = portletMonitor.getCacheExpirationPeriod(portletAppName, portletName);
    }
    long lastUpdateTime = 0;
    if (key != null)
      lastUpdateTime = portletMonitor.getPortletLastCacheUpdateTime(portletAppName, portletName, key,
          isCacheGlobal);
    long currentAccessTime = System.currentTimeMillis();
    if (expirationPeriod == 0) {
      log.debug("Expiration period 0 before proceed");
      rcontext.executeNextUnit();
      log.debug("Expiration period 0 after proceed");
    } else if (expirationPeriod == -1) {
      if ((key != null)
          && portletMonitor.isDataCached(portletAppName, portletName, key, isCacheGlobal)) {
        log.debug("Use cache : Expiration period -1 data already cached");
        useRCache(portletAppName, portletName, key, res, isCacheGlobal);
      } else {
        log.debug("Expiration period -1 data first cached, before proceed");
        rcontext.executeNextUnit();
        key = generateKey(req, isCacheGlobal, "r");
        log.debug("Expiration period -1 data first cached, after proceed");
        updateRCache(portletAppName, portletName, key, res, isCacheGlobal);
      }
    } else if (currentAccessTime - lastUpdateTime > expirationPeriod * MILLISECONDS) {
      log.debug("Expiration period currentAccessTime - lastCacheUpdateTime > expirationPeriod * 1000 before proceed");
      if (key != null && req.getPortletDatas().getApplication().getVer2())
        ((PortalContextImp) req.getPortalContext()).addProperty(RenderResponse.ETAG,
            portletMonitor.getCachedETag(portletAppName, portletName, key, isCacheGlobal));
      rcontext.executeNextUnit();
      key = generateKey(req, isCacheGlobal, "r");
      log.debug("Expiration period currentAccessTime - lastCacheUpdateTime > expirationPeriod * 1000 after proceed");
      if (res.getOutput().getProperties().get(RenderResponse.USE_CACHED_CONTENT) != null)
        useRCache(portletAppName, portletName, key, res, isCacheGlobal);
      else
        updateRCache(portletAppName, portletName, key, res, isCacheGlobal);
      updateCacheParams(portletAppName, portletName, key, res, isCacheGlobal);
    } else if ((0 < currentAccessTime - lastUpdateTime)
        && (currentAccessTime - lastUpdateTime < expirationPeriod * MILLISECONDS)) {
      log.debug("Use cache : currentAccessTime - lastCacheUpdateTime < expirationPeriod * 1000");
      if (key != null)
        useRCache(portletAppName, portletName, key, res, isCacheGlobal);
    }
    return null;
  }

  /**
   * Overridden method.
   *
   * @param acontext execution context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#processAction(org.exoplatform.services.portletcontainer.plugins.pc.aop.ActionExecutionContext)
   */
  protected final Object processAction(final ActionExecutionContext acontext) throws Throwable {
    if (!conf.isCacheEnable())
      return acontext.executeNextUnit();
    log.debug("--> processAction method, call cache interceptor");
    ActionRequestImp req = (ActionRequestImp) acontext.getRequest();
    if (req.getPortletDatas().getCaching() == null)
      return acontext.executeNextUnit();

    String portletAppName = req.getPortletWindowInternal().getWindowID()
        .getPortletApplicationName();
    String portletName = req.getPortletWindowInternal().getWindowID().getPortletName();
    boolean isCacheGlobal = resolveCache(req.getPortletDatas().getGlobalCache(), portletMonitor
        .getCacheScope(portletAppName, portletName));
    log.debug("Is cache global: " + isCacheGlobal);
    // invalidate cache
    String uniqueID = req.getPortletWindowInternal().getWindowID().getUniqueID();
    String key = generateKey(req, isCacheGlobal, "");
    if (key != null)
      portletMonitor.removeCachedData(portletAppName, portletName, key, isCacheGlobal);
    key = generateKey(req, isCacheGlobal, "r");
    if (key != null)
      portletMonitor.removeCachedData(portletAppName, portletName, key, isCacheGlobal);

    return acontext.executeNextUnit();
  }

  /**
   * Overridden method.
   *
   * @param econtext execution context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.services.portletcontainer.plugins.pc.aop.BaseCommandUnit#processEvent(org.exoplatform.services.portletcontainer.plugins.pc.aop.EventExecutionContext)
   */
  protected final Object processEvent(final EventExecutionContext econtext) throws Throwable {
    if (!conf.isCacheEnable())
      return econtext.executeNextUnit();
    log.debug("--> processEvent method, call cache interceptor");
    EventRequestImp req = (EventRequestImp) econtext.getRequest();
    if (req.getPortletDatas().getCaching() == null)
      return econtext.executeNextUnit();

    String portletAppName = req.getPortletWindowInternal().getWindowID()
        .getPortletApplicationName();
    String portletName = req.getPortletWindowInternal().getWindowID().getPortletName();
    boolean isCacheGlobal = resolveCache(req.getPortletDatas().getGlobalCache(), portletMonitor
        .getCacheScope(portletAppName, portletName));
    log.debug("Is cache global: " + isCacheGlobal);
    // invalidate cache
    String uniqueID = req.getPortletWindowInternal().getWindowID().getUniqueID();
    String key = generateKey(req, isCacheGlobal, "");
    if (key != null)
      portletMonitor.removeCachedData(portletAppName, portletName, key, isCacheGlobal);
    key = generateKey(req, isCacheGlobal, "r");
    if (key != null)
      portletMonitor.removeCachedData(portletAppName, portletName, key, isCacheGlobal);

    return econtext.executeNextUnit();
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param response response
   * @param isCacheGlobal is cache global
   */
  private void useCache(final String portletAppName,
      final String portletName,
      final String key,
      final PortletResponseImp response,
      final boolean isCacheGlobal) {
    ((RenderOutput) response.getOutput()).setContent(portletMonitor.getCachedContent(
        portletAppName, portletName, key, isCacheGlobal));
    ((RenderOutput) response.getOutput()).setTitle(portletMonitor.getCachedTitle(portletAppName,
        portletName, key, isCacheGlobal));
    portletMonitor.setPortletLastAccessTime(portletAppName, portletName, key, System
        .currentTimeMillis(), isCacheGlobal);
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param response response
   * @param isCacheGlobal is cache global
   */
  private void useRCache(final String portletAppName,
      final String portletName,
      final String key,
      final PortletResponseImp response,
      final boolean isCacheGlobal) {
    ((RenderOutput) response.getOutput()).setContent(portletMonitor.getCachedContent(
        portletAppName, portletName, key, isCacheGlobal));
    portletMonitor.setPortletLastAccessTime(portletAppName, portletName, key, System
        .currentTimeMillis(), isCacheGlobal);
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param response response
   * @param isCacheGlobal is cache global
   */
  private void updateCacheParams(final String portletAppName,
      final String portletName,
      final String key,
      final PortletResponseImp response,
      final boolean isCacheGlobal) {
    String s = (String) response.getOutput().getProperties().get(RenderResponse.ETAG);
    if (s != null)
      portletMonitor.setCachedETag(portletAppName, portletName, key, s, isCacheGlobal);
    s = (String) response.getOutput().getProperties().get(RenderResponse.EXPIRATION_CACHE);
    if (s != null)
      try {
        int i = Integer.parseInt(s);
        portletMonitor.setCacheExpirationPeriod(portletAppName, portletName, i);
      } catch (Exception e) {
        log.debug("Invalid value of EXPIRATION_CACHE property returned by " + portletAppName + "/"
            + portletName + "[" + s + "]");
      }
    s = (String) response.getOutput().getProperties().get(RenderResponse.CACHE_SCOPE);
    if (s != null)
      portletMonitor.setCacheScope(portletAppName, portletName, s);
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param response response
   * @param mode portlet mode
   * @param window window state
   * @param isCacheGlobal is cache global
   */
  private void updateCache(final String portletAppName,
      final String portletName,
      final String key,
      final PortletResponseImp response,
      final PortletMode mode,
      final WindowState window,
      final boolean isCacheGlobal) {
    log.debug("Update cache");
    portletMonitor.setCachedTitle(portletAppName, portletName, key, ((RenderOutput) response
        .getOutput()).getTitle(), isCacheGlobal);
    portletMonitor.setCachedContent(portletAppName, portletName, key, ((RenderOutput) response
        .getOutput()).getBinContent(), isCacheGlobal);
    portletMonitor.setCachedMode(portletAppName, portletName, key, mode, isCacheGlobal);

    portletMonitor.setCachedWindowState(portletAppName, portletName, key, window, isCacheGlobal);

    portletMonitor.setPortletLastAccessTime(portletAppName, portletName, key, System
        .currentTimeMillis(), isCacheGlobal);
    updateCacheParams(portletAppName, portletName, key, response, isCacheGlobal);
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param key key
   * @param response response
   * @param isCacheGlobal is cache global
   */
  private void updateRCache(final String portletAppName,
      final String portletName,
      final String key,
      final PortletResponseImp response,
      final boolean isCacheGlobal) {
    log.debug("Update cache");
    portletMonitor.setCachedContent(portletAppName, portletName, key, ((RenderOutput) response
        .getOutput()).getBinContent(), isCacheGlobal);

    portletMonitor.setPortletLastAccessTime(portletAppName, portletName, key, System
        .currentTimeMillis(), isCacheGlobal);
    updateCacheParams(portletAppName, portletName, key, response, isCacheGlobal);
  }

  /**
   * @param s is cache global
   * @param s1 cache scope
   * @return is cache global
   */
  private boolean resolveCache(final String s, final String s1) {
    return ((s != null) && s.equals("true"))
        || ((s1 != null) && s1.equals(RenderResponse.PUBLIC_SCOPE));
  }

}
