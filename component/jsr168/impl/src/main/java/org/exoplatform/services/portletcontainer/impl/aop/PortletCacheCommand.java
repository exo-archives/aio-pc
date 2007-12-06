/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL    All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

import javax.portlet.PortletMode;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.exoplatform.services.portletcontainer.impl.PortletContainerConf;
import org.exoplatform.services.portletcontainer.impl.monitor.PortletContainerMonitorImpl;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.ActionRequestImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletResponseImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.RenderRequestImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.RenderResponseImp;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;

/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
public class PortletCacheCommand extends BaseCommandUnit {

	private PortletContainerConf conf;
  private PortletContainerMonitorImpl portletMonitor;

  public PortletCacheCommand(PortletContainerConf conf, PortletContainerMonitorImpl monitor ) {
  	this.conf = conf ;
  	portletMonitor =  monitor ; 
  }
  
  protected Object render(RenderExecutionContext rcontext)  throws Throwable {
	  if(!conf.isCacheEnable()){
			return rcontext.executeNextUnit() ;
	  }

    log_.debug("--> render method, call cache interceptor");
    RenderRequestImp req = (RenderRequestImp) rcontext.request_;
    RenderResponseImp res = (RenderResponseImp) rcontext.response_;
    if (req.getPortletDatas().getExpirationCache() == null){
      return rcontext.executeNextUnit() ;
    }
    String portletAppName = req.getPortletWindowInternal().getWindowID().getPortletApplicationName();
    String portletName = req.getPortletWindowInternal().getWindowID().getPortletName();
	  String uniqueId = req.getPortletWindowInternal().getWindowID().getUniqueID();
	  PortletMode mode = req.getPortletMode();
    WindowState window = req.getWindowState();

    boolean isCacheGlobal = resolveCache(req.getPortletDatas().getGlobalCache());
    log_.debug("Is cache global: " + isCacheGlobal);
    String key = null;
    if(isCacheGlobal){
      key = req.getInput().getWindowID().getOwner() + uniqueId;
    } else {
      key = req.getSession().getId() + req.getRemoteUser() + req.getInput().getWindowID().getOwner() + uniqueId;
    }

    if(req.isRenderRequest()){
      rcontext.executeNextUnit() ;
      updateCache(portletAppName, portletName, key, res, mode, window, isCacheGlobal);
      return null ;      
    }

    if(portletMonitor.needsCacheInvalidation(portletAppName, portletName,
                                             key, mode, window, isCacheGlobal)){
      portletMonitor.removeCachedData(portletAppName, portletName, key, isCacheGlobal);
    }

    int expirationPeriod = portletMonitor.getCacheExpirationPeriod(portletAppName, portletName);
    long lastAccessTime = portletMonitor.getPortletLastAccessTime(portletAppName, portletName,
                                                                  key, isCacheGlobal);
    long currentAccessTime = System.currentTimeMillis();
    if (expirationPeriod == 0) {
      log_.debug("Expiration period 0 before proceed");
      rcontext.executeNextUnit() ;
      log_.debug("Expiration period 0 after proceed");
    } else if (expirationPeriod == -1) {
      if (portletMonitor.isDataCached(portletAppName, portletName, key, isCacheGlobal)) {
        log_.debug("Use cache : Expiration period -1 data already cached");
        ((RenderOutput) res.getOutput()).setContent(portletMonitor.getCachedContent(portletAppName,
                                                                                    portletName, key,
                                                                                    isCacheGlobal));
        ((RenderOutput) res.getOutput()).setTitle(portletMonitor.getCachedTitle(portletAppName,
                                                                                portletName, key,
                                                                                isCacheGlobal));
      } else {
        log_.debug("Expiration period -1 data first cached, before proceed");
        rcontext.executeNextUnit() ;
        log_.debug("Expiration period -1 data first cached, after proceed");
        updateCache(portletAppName, portletName, key, res, mode, window, isCacheGlobal);
      }
    } else if (currentAccessTime - lastAccessTime > expirationPeriod * 1000) {
      log_.debug("Expiration period currentAccessTime - lastAccessTime > expirationPeriod * 1000 before proceed");
      rcontext.executeNextUnit();
      log_.debug("Expiration period currentAccessTime - lastAccessTime > expirationPeriod * 1000 after proceed");
      updateCache(portletAppName, portletName, key, res, mode, window, isCacheGlobal);
    } else if (0 < currentAccessTime - lastAccessTime &&
        currentAccessTime - lastAccessTime < expirationPeriod * 1000) {
        log_.debug("Use cache : currentAccessTime - lastAccessTime < expirationPeriod * 1000");
        ((RenderOutput) res.getOutput()).setContent(portletMonitor.getCachedContent(portletAppName,
                                                                                    portletName, key,
                                                                                    isCacheGlobal));
        ((RenderOutput) res.getOutput()).setTitle(portletMonitor.getCachedTitle(portletAppName,
                                                                                portletName, key,
                                                                                isCacheGlobal));
    }
    return null ;
  }

  protected Object processAction(ActionExecutionContext acontext)  throws Throwable {
	  if(!conf.isCacheEnable()) {
			return acontext.executeNextUnit() ;
	  }
    log_.debug("--> processAction method, call cache interceptor");
    ActionRequestImp req = (ActionRequestImp) acontext.request_ ;
    if (req.getPortletDatas().getExpirationCache() == null){
      return acontext.executeNextUnit() ;
	  } 
    
    boolean isCacheGlobal = resolveCache(req.getPortletDatas().getGlobalCache());
    log_.debug("Is cache global: " + isCacheGlobal);
    //invalidate cache
    String portletAppName = req.getPortletWindowInternal().getWindowID().getPortletApplicationName();
    String portletName = req.getPortletWindowInternal().getWindowID().getPortletName();
    String uniqueID = req.getPortletWindowInternal().getWindowID().getUniqueID();
    String key = req.getSession().getId() + req.getRemoteUser() +
    req.getInput().getWindowID().getOwner() + uniqueID;
    
    portletMonitor.removeCachedData(portletAppName, portletName, key, isCacheGlobal);
    
    return acontext.executeNextUnit() ;
  }

  private void updateCache(String portletAppName, String portletName, String key,
                           PortletResponseImp response, PortletMode mode, WindowState window,
                           boolean isCacheGlobal) {
    log_.debug("Update cache");
    portletMonitor.setCachedTitle(portletAppName, portletName,
                                  key, ((RenderOutput) response.getOutput()).getTitle(),
                                  isCacheGlobal);
    portletMonitor.setCachedContent(portletAppName, portletName,
                                    key, ((RenderOutput) response.getOutput()).getContent(),
                                    isCacheGlobal);
    portletMonitor.setCachedMode(portletAppName, portletName,
                                 key, mode, isCacheGlobal);

    portletMonitor.setCachedWindowState(portletAppName, portletName,
                                        key, window, isCacheGlobal);

    String s = (String)response.getOutput().getProperties().get(RenderResponse.EXPIRATION_CACHE);
    if (s != null) {
      int i = Integer.parseInt(s);
      portletMonitor.setCacheExpirationPeriod(portletAppName, portletName, i);
    }
    portletMonitor.setPortletLastAccessTime(portletAppName, portletName, key,
                                            System.currentTimeMillis(), isCacheGlobal);
  }

  private boolean resolveCache(String s){
    if(s == null)   return false;
    if("true".equals(s))  return true;
    return false;
  }
}