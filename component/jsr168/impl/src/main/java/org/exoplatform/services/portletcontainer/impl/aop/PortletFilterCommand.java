/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletContext;

import org.exoplatform.Constants;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.portletcontainer.filter.PortletFilter;
import org.exoplatform.services.portletcontainer.impl.filter.PortletFilterChainImpl;
import org.exoplatform.services.portletcontainer.impl.filter.PortletFilterConfigImpl;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletRequestImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletResponseImp;
import org.exoplatform.services.portletcontainer.pci.model.Filter;
/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
public class PortletFilterCommand extends BaseCommandUnit {
  protected Object render(RenderExecutionContext rcontext)  throws Throwable {
    log_.debug("--> render method, call portlet filter aspect");
    
    PortalContainer manager = PortalContainer.getInstance();
    PortletRequestImp req = (PortletRequestImp) rcontext.request_;
    PortletResponseImp res = (PortletResponseImp) rcontext.response_;
    PortletContext portletContext = req.getPortletConfig().getPortletContext();
    String portletAppName = req.getPortletWindowInternal().getWindowID().getPortletApplicationName();
    List filters = req.getPortletDatas().getFilter();
    if(filters.isEmpty()) {
      return rcontext.executeNextUnit() ;
    }
    Collection filterInstances = new ArrayList();
    for (Iterator iterator = filters.iterator(); iterator.hasNext();) {
      Filter portletFilter = (Filter) iterator.next();
      String filterName = portletFilter.getFilterName();
      String filterClass = portletFilter.getFilterClass();
      String key = portletAppName + Constants.FILTER_ENCODER + filterName;
      PortletFilter filter = (PortletFilter) manager.getComponentInstance(key);
      if (filter == null){
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        manager.registerComponentImplementation(key, cl.loadClass(filterClass));
        filter = (PortletFilter) manager.getComponentInstance(key);
        filter.init(new PortletFilterConfigImpl(filterName, portletFilter.getInitParam(), portletContext));
      }
      filterInstances.add(filter);
    }
    PortletFilterChainImpl invoker = new PortletFilterChainImpl();
    invoker.invoke(req, res, filterInstances,rcontext.portlet_, false);
    return null ;
  }

  protected Object processAction(ActionExecutionContext acontext)  throws Throwable {
    log_.debug("--> processAction method, call portlet filter aspect");
    
    PortalContainer manager = PortalContainer.getInstance();
    PortletRequestImp req = (PortletRequestImp) acontext.request_;
    PortletResponseImp res = (PortletResponseImp) acontext.response_;
    PortletContext portletContext = req.getPortletConfig().getPortletContext();
    String portletAppName = req.getPortletWindowInternal().getWindowID().getPortletApplicationName();
    List filters = req.getPortletDatas().getFilter();
    if(filters.isEmpty()){
      return acontext.executeNextUnit()  ;
    }
    
    Collection filterInstances = new ArrayList();
    for (Iterator iterator = filters.iterator(); iterator.hasNext();) {
      Filter portletFilter = (Filter) iterator.next();
      String filterName = portletFilter.getFilterName();
      String filterClass = portletFilter.getFilterClass();
      String key = portletAppName + Constants.FILTER_ENCODER + filterName;
      PortletFilter filter = (PortletFilter) manager.getComponentInstance(key);
      if (filter == null){
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        manager.registerComponentImplementation(key, cl.loadClass(filterClass));
        filter = (PortletFilter) manager.getComponentInstance(key);
        filter.init(new PortletFilterConfigImpl(filterName, portletFilter.getInitParam(), portletContext));
      }
      filterInstances.add(filter);
    }
    PortletFilterChainImpl invoker = new PortletFilterChainImpl();
    new Exception().printStackTrace() ;
    invoker.invoke(req, res, filterInstances, acontext.portlet_, true);
    return null ;
  }
}