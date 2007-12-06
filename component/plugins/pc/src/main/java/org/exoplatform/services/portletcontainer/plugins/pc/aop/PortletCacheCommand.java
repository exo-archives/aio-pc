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
import javax.portlet.WindowState;

import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.plugins.pc.monitor.PortletContainerMonitorImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ActionRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.EventRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RenderRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RenderResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceResponseImp;

/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
public class PortletCacheCommand extends BaseCommandUnit {

	private PortletContainerConf conf;
  private PortletContainerMonitorImpl portletMonitor;

  public PortletCacheCommand(PortletContainerConf conf, PortletContainerMonitorImpl monitor) {
  	this.conf = conf;
  	portletMonitor =  monitor;
  }

  protected Object render(RenderExecutionContext rcontext) throws Throwable {
    if (!conf.isCacheEnable()) {
			return rcontext.executeNextUnit();
	  }

    log_.debug("--> render method, call cache interceptor");
    RenderRequestImp req = (RenderRequestImp) rcontext.request_;
    RenderResponseImp res = (RenderResponseImp) rcontext.response_;
    if (req.getPortletDatas().getCaching() == null) {
      return rcontext.executeNextUnit();
    }
    String portletAppName = req.getPortletWindowInternal().getWindowID().getPortletApplicationName();
    String portletName = req.getPortletWindowInternal().getWindowID().getPortletName();
	  String uniqueId = req.getPortletWindowInternal().getWindowID().getUniqueID();
	  PortletMode mode = req.getPortletMode();
    WindowState window = req.getWindowState();

    boolean isCacheGlobal = resolveCache(req.getPortletDatas().getGlobalCache(),
      portletMonitor.getCacheScope(portletAppName, portletName));
    log_.debug("Is cache global: " + isCacheGlobal);
    String key = null;
    if (isCacheGlobal)
      key = req.getInput().getInternalWindowID().getOwner() + uniqueId;
    else
      key = generateKey(req, uniqueId);

    if (key != null && portletMonitor.needsCacheInvalidation(portletAppName, portletName,
                                             key, mode, window, isCacheGlobal)) {
      portletMonitor.removeCachedData(portletAppName, portletName, key, isCacheGlobal);
    }

    int expirationPeriod = portletMonitor.getCacheExpirationPeriod(portletAppName, portletName);
    long lastAccessTime = 0;
    if (key != null)
      lastAccessTime = portletMonitor.getPortletLastAccessTime(portletAppName, portletName, key, isCacheGlobal);
    long currentAccessTime = System.currentTimeMillis();
    if (expirationPeriod == 0) {
      log_.debug("Expiration period 0 before proceed");
      rcontext.executeNextUnit();
      log_.debug("Expiration period 0 after proceed");
    } else if (expirationPeriod == -1) {
      if (key != null && portletMonitor.isDataCached(portletAppName, portletName, key, isCacheGlobal)) {
        log_.debug("Use cache : Expiration period -1 data already cached");
        useCache(portletAppName, portletName, key, res, isCacheGlobal);
      } else {
        log_.debug("Expiration period -1 data first cached, before proceed");
        rcontext.executeNextUnit();
        key = generateKey(req, uniqueId);
        log_.debug("Expiration period -1 data first cached, after proceed");
        updateCache(portletAppName, portletName, key, res, mode, window, isCacheGlobal);
      }
    } else if (currentAccessTime - lastAccessTime > expirationPeriod * 1000) {
      log_.debug("Expiration period currentAccessTime - lastAccessTime > expirationPeriod * 1000 before proceed");
      if (key != null)
        res.getOutput().getProperties().put(RenderResponse.ETAG,
            portletMonitor.getCachedETag(portletAppName, portletName, key, isCacheGlobal));
      rcontext.executeNextUnit();
      key = generateKey(req, uniqueId);
      log_.debug("Expiration period currentAccessTime - lastAccessTime > expirationPeriod * 1000 after proceed");
      if (res.getOutput().getProperties().get(RenderResponse.USE_CACHED_CONTENT) != null) {
        updateCacheParams(portletAppName, portletName, key, res, isCacheGlobal);
        useCache(portletAppName, portletName, key, res, isCacheGlobal);
      }
      else
        updateCache(portletAppName, portletName, key, res, mode, window, isCacheGlobal);
    } else if (0 < currentAccessTime - lastAccessTime &&
        currentAccessTime - lastAccessTime < expirationPeriod * 1000) {
        log_.debug("Use cache : currentAccessTime - lastAccessTime < expirationPeriod * 1000");
        if (key != null)
          useCache(portletAppName, portletName, key, res, isCacheGlobal);
    }
    return null;
  }

  private String generateKey(PortletRequestImp req, String uniqueId) {
    if (req.getSession(false) == null)
      return null;
    return req.getSession().getId() + req.getRemoteUser() + req.getInput().getInternalWindowID().getOwner() + uniqueId;
  }

  protected Object serveResource(ResourceExecutionContext rcontext)  throws Throwable {
    log_.debug("--> serveResource method, call cache interceptor");

    if (!conf.isCacheEnable()) {
			return rcontext.executeNextUnit();
	  }

    log_.debug("--> render method, call cache interceptor");
    ResourceRequestImp req = (ResourceRequestImp) rcontext.request_;
    ResourceResponseImp res = (ResourceResponseImp) rcontext.response_;
    if (req.getPortletDatas().getCaching() == null) {
      return rcontext.executeNextUnit();
    }
    String portletAppName = req.getPortletWindowInternal().getWindowID().getPortletApplicationName();
    String portletName = req.getPortletWindowInternal().getWindowID().getPortletName();
	  String uniqueId = req.getPortletWindowInternal().getWindowID().getUniqueID();

    boolean isCacheGlobal = resolveCache(req.getPortletDatas().getGlobalCache(),
      portletMonitor.getCacheScope(portletAppName, portletName));
    log_.debug("Is cache global: " + isCacheGlobal);
    String key = null;
    if (isCacheGlobal)
      key = req.getInput().getInternalWindowID().getOwner() + uniqueId;
    else
      key = generateKey(req, uniqueId);
    key = key + "r";
    int expirationPeriod = portletMonitor.getCacheExpirationPeriod(portletAppName, portletName);
    long lastAccessTime = 0;
    if (key != null)
      lastAccessTime = portletMonitor.getPortletLastAccessTime(portletAppName, portletName, key, isCacheGlobal);
    long currentAccessTime = System.currentTimeMillis();
    if (expirationPeriod == 0) {
      log_.debug("Expiration period 0 before proceed");
      rcontext.executeNextUnit();
      log_.debug("Expiration period 0 after proceed");
    } else if (expirationPeriod == -1) {
      if (key != null && portletMonitor.isDataCached(portletAppName, portletName, key, isCacheGlobal)) {
        log_.debug("Use cache : Expiration period -1 data already cached");
        useRCache(portletAppName, portletName, key, res, isCacheGlobal);
      } else {
        log_.debug("Expiration period -1 data first cached, before proceed");
        rcontext.executeNextUnit();
        key = generateKey(req, uniqueId);
        log_.debug("Expiration period -1 data first cached, after proceed");
        updateRCache(portletAppName, portletName, key, res, isCacheGlobal);
      }
    } else if (currentAccessTime - lastAccessTime > expirationPeriod * 1000) {
      log_.debug("Expiration period currentAccessTime - lastAccessTime > expirationPeriod * 1000 before proceed");
      if (key != null)
        res.getOutput().getProperties().put(RenderResponse.ETAG,
            portletMonitor.getCachedETag(portletAppName, portletName, key, isCacheGlobal));
      rcontext.executeNextUnit();
      key = generateKey(req, uniqueId);
      log_.debug("Expiration period currentAccessTime - lastAccessTime > expirationPeriod * 1000 after proceed");
      if (res.getOutput().getProperties().get(RenderResponse.USE_CACHED_CONTENT) != null) {
        updateCacheParams(portletAppName, portletName, key, res, isCacheGlobal);
        useRCache(portletAppName, portletName, key, res, isCacheGlobal);
      }
      else
        updateRCache(portletAppName, portletName, key, res, isCacheGlobal);
    } else if (0 < currentAccessTime - lastAccessTime &&
        currentAccessTime - lastAccessTime < expirationPeriod * 1000) {
        log_.debug("Use cache : currentAccessTime - lastAccessTime < expirationPeriod * 1000");
        if (key != null)
          useRCache(portletAppName, portletName, key, res, isCacheGlobal);
    }
    return null;
  }

  protected Object processAction(ActionExecutionContext acontext)  throws Throwable {
	  if (!conf.isCacheEnable()) {
			return acontext.executeNextUnit();
	  }
    log_.debug("--> processAction method, call cache interceptor");
    ActionRequestImp req = (ActionRequestImp) acontext.request_;
    if (req.getPortletDatas().getCaching() == null) {
      return acontext.executeNextUnit();
	  }

    String portletAppName = req.getPortletWindowInternal().getWindowID().getPortletApplicationName();
    String portletName = req.getPortletWindowInternal().getWindowID().getPortletName();
    boolean isCacheGlobal = resolveCache(req.getPortletDatas().getGlobalCache(),
      portletMonitor.getCacheScope(portletAppName, portletName));
    log_.debug("Is cache global: " + isCacheGlobal);
    //invalidate cache
    String uniqueID = req.getPortletWindowInternal().getWindowID().getUniqueID();
    String key = generateKey(req, uniqueID);

    if (key != null)
      portletMonitor.removeCachedData(portletAppName, portletName, key, isCacheGlobal);

    return acontext.executeNextUnit();
  }

  protected Object processEvent(EventExecutionContext econtext)  throws Throwable {
	  if (!conf.isCacheEnable()) {
			return econtext.executeNextUnit();
	  }
    log_.debug("--> processEvent method, call cache interceptor");
    EventRequestImp req = (EventRequestImp) econtext.request_;
    if (req.getPortletDatas().getCaching() == null) {
      return econtext.executeNextUnit();
	  }

    String portletAppName = req.getPortletWindowInternal().getWindowID().getPortletApplicationName();
    String portletName = req.getPortletWindowInternal().getWindowID().getPortletName();
    boolean isCacheGlobal = resolveCache(req.getPortletDatas().getGlobalCache(),
      portletMonitor.getCacheScope(portletAppName, portletName));
    log_.debug("Is cache global: " + isCacheGlobal);
    //invalidate cache
    String uniqueID = req.getPortletWindowInternal().getWindowID().getUniqueID();
    String key = generateKey(req, uniqueID);

    if (key != null)
      portletMonitor.removeCachedData(portletAppName, portletName, key, isCacheGlobal);

    return econtext.executeNextUnit();
  }

  private void useCache(String portletAppName, String portletName, String key,
                           PortletResponseImp response, boolean isCacheGlobal) {
    ((RenderOutput) response.getOutput()).setContent(portletMonitor.getCachedContent(portletAppName,
                                                                                    portletName, key,
                                                                                    isCacheGlobal));
    ((RenderOutput) response.getOutput()).setTitle(portletMonitor.getCachedTitle(portletAppName,
                                                                                portletName, key,
                                                                                isCacheGlobal));
    portletMonitor.setPortletLastAccessTime(portletAppName, portletName, key, System.currentTimeMillis(), isCacheGlobal);
  }

  private void useRCache(String portletAppName, String portletName, String key,
                           PortletResponseImp response, boolean isCacheGlobal) {
    ((RenderOutput) response.getOutput()).setContent(portletMonitor.getCachedContent(portletAppName,
                                                                                    portletName, key,
                                                                                    isCacheGlobal));
    portletMonitor.setPortletLastAccessTime(portletAppName, portletName, key, System.currentTimeMillis(), isCacheGlobal);
  }

  private void updateCacheParams(String portletAppName, String portletName, String key,
                           PortletResponseImp response, boolean isCacheGlobal) {
    String s = (String) response.getOutput().getProperties().get(RenderResponse.ETAG);
    if (s != null)
      portletMonitor.setCachedETag(portletAppName, portletName, key, s, isCacheGlobal);
    s = (String) response.getOutput().getProperties().get(RenderResponse.EXPIRATION_CACHE);
    if (s != null)
      try {
        int i = Integer.parseInt(s);
        portletMonitor.setCacheExpirationPeriod(portletAppName, portletName, i);
      } catch (Exception e) {
        log_.debug("Invalid value of EXPIRATION_CACHE property returned by " + portletAppName + "/" + portletName + "[" + s + "]");
      }
    s = (String) response.getOutput().getProperties().get(RenderResponse.CACHE_SCOPE);
    if (s != null)
      portletMonitor.setCacheScope(portletAppName, portletName, s);
  }

  private void updateCache(String portletAppName, String portletName, String key,
                           PortletResponseImp response, PortletMode mode, WindowState window,
                           boolean isCacheGlobal) {
    log_.debug("Update cache");
    portletMonitor.setCachedTitle(portletAppName, portletName,
                                  key, ((RenderOutput) response.getOutput()).getTitle(),
                                  isCacheGlobal);
    portletMonitor.setCachedContent(portletAppName, portletName,
                                    key, ((RenderOutput) response.getOutput()).getBinContent(),
                                    isCacheGlobal);
    portletMonitor.setCachedMode(portletAppName, portletName,
                                 key, mode, isCacheGlobal);

    portletMonitor.setCachedWindowState(portletAppName, portletName,
                                        key, window, isCacheGlobal);

    portletMonitor.setPortletLastAccessTime(portletAppName, portletName, key,
                                            System.currentTimeMillis(), isCacheGlobal);
    updateCacheParams(portletAppName, portletName, key, response, isCacheGlobal);
  }

  private void updateRCache(String portletAppName, String portletName, String key,
                           PortletResponseImp response, boolean isCacheGlobal) {
    log_.debug("Update cache");
    portletMonitor.setCachedContent(portletAppName, portletName,
                                    key, ((RenderOutput) response.getOutput()).getBinContent(),
                                    isCacheGlobal);

    portletMonitor.setPortletLastAccessTime(portletAppName, portletName, key,
                                            System.currentTimeMillis(), isCacheGlobal);
    updateCacheParams(portletAppName, portletName, key, response, isCacheGlobal);
  }

  private boolean resolveCache(String s, String s1) {
//    if (s == null)   return false;
//    if ("true".equals(s))  return true;
//    return false;
    return ((s != null) && s.equals("true")) || ((s1 != null) && s1.equals(RenderResponse.PUBLIC_SCOPE));
  }
}