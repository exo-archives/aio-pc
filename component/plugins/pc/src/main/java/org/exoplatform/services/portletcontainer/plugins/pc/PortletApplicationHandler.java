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
package org.exoplatform.services.portletcontainer.plugins.pc;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortalContext;
import javax.portlet.Portlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.UnavailableException;
import javax.portlet.WindowState;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.Output;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.services.portletcontainer.plugins.pc.aop.PortletCommandChain;
import org.exoplatform.services.portletcontainer.plugins.pc.monitor.PortletContainerMonitorImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.monitor.PortletRuntimeDatasImpl;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ActionRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ActionResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.EventRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.EventResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletAPIObjectFactory;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletSessionImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RenderRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RenderResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.RequestContext;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceRequestImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResponseContext;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.bundle.ResourceBundleManager;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.CustomRequestWrapper;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.CustomResponseWrapper;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: 10 nov. 2003 Time: 13:02:54
 */
public class PortletApplicationHandler {

  private PortalContext               portalContext;

  // private int nbInstances = 0;

  private PortletApplicationsHolder   holder;

  private PortletContainerConf        conf;

  static private Log                  log_ = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");

  private PortletContainerMonitorImpl monitor;

  private ResourceBundleManager       resourceBundleManager;

  protected ExoContainer              cont;

  public PortletApplicationHandler(PortalContext portalContext,
                                   PortletApplicationsHolder holder,
                                   PortletContainerConf conf,
                                   PortletContainerMonitorImpl portletMonitor,
                                   ResourceBundleManager manager,
                                   ExoContainerContext context) {
    this.portalContext = portalContext;
    this.holder = holder;
    this.conf = conf;
    this.monitor = portletMonitor;
    this.resourceBundleManager = manager;
    this.cont = context.getContainer();
  }

  public void process(ServletContext servletContext,
                      HttpServletRequest request,
                      HttpServletResponse response,
                      Input input,
                      Output output,
                      PortletWindowInternal windowInfos,
                      int isAction) throws PortletContainerException {
    long startTime = System.currentTimeMillis();
    log_.debug("process() method in PortletApplicationHandler entered");
    PortletSessionImp session = null;
    CustomRequestWrapper requestWrapper = null;
    CustomResponseWrapper responseWrapper = null;
    PortletRequestImp portletRequest = null;
    PortletResponseImp portletResponse = null;
    String portletAppName = windowInfos.getWindowID().getPortletApplicationName();
    String portletName = windowInfos.getWindowID().getPortletName();
    try {
      ExoContainer manager = cont;
      PortletApplicationProxy proxy = (PortletApplicationProxy) manager.getComponentInstance(portletAppName);

      if (!holder.isModeSuported(portletAppName, portletName, input.getMarkup(), input.getPortletMode())) {
        throw new PortletContainerException("The portlet mode " + input.getPortletMode().toString() + " is not supported for the "
            + input.getMarkup() + " markup language.");
      }

      if (!holder.isStateSupported(portletAppName, portletName, input.getMarkup(), input.getWindowState())) {
        log_.debug("Window state : " + input.getWindowState() + " not supported, set the window state to normal");
        input.setWindowState(WindowState.NORMAL);
      }

      String exception_key = PCConstants.EXCEPTION + portletAppName + portletName;

      PortletContext portletContext = PortletAPIObjectFactory.getInstance().createPortletContext(cont,
                                                                                                 servletContext,
                                                                                                 holder.getPortletMetaData(portletAppName,
                                                                                                                           portletName));

      log_.debug("Create new object");

      long portletAppVersionNumber = 1;
      portletAppVersionNumber = monitor.getPortletVersionNumber(portletAppName);
      log_.debug("Get portlet version number : " + portletAppVersionNumber);

      // create a PortletSession object
      session = new PortletSessionImp(cont, request.getSession(false), portletContext, windowInfos.getWindowID().getUniqueID());

      // create a servlet request wrapper
      requestWrapper = new CustomRequestWrapper(request, windowInfos.getWindowID().getUniqueID());

      // create a servlet response wrapper
      responseWrapper = new CustomResponseWrapper(response);

      RequestContext reqCtx = new RequestContext(requestWrapper,
                                                 portalContext,
                                                 portletContext,
                                                 session,
                                                 input,
                                                 windowInfos,
                                                 holder.getPortletApplication(portletAppName).getSecurityConstraint(),
                                                 holder.getPortletApplication(portletAppName).getUserAttribute(),
                                                 holder.getPortletApplication(portletAppName).getCustomPortletMode(),
                                                 holder.getPortletApplication(portletAppName).getCustomWindowState(),
                                                 holder.getRoles(portletAppName),
                                                 conf.getSupportedContent());

      // @todo sort the attributes

      if (isAction == PCConstants.actionInt) {
        portletRequest = new ActionRequestImp(reqCtx);
      } else if (isAction == PCConstants.eventInt) {
        portletRequest = new EventRequestImp(reqCtx);
      } else if (isAction == PCConstants.resourceInt) {
        portletRequest = new ResourceRequestImp(reqCtx);
      } else {
        portletRequest = new RenderRequestImp(reqCtx);
      }

      ResponseContext resCtx = new ResponseContext(responseWrapper,
                                                   cont,
                                                   windowInfos.getWindowID().getUniqueID(),
                                                   input,
                                                   holder.getPortletMetaData(portletAppName, portletName),
                                                   request.isSecure(),
                                                   conf.getSupportedContent(),
                                                   Collections.enumeration(holder.getWindowStates(portletAppName, portletName, input.getMarkup())),
                                                   holder.getPortletApplication(portletAppName).getCustomWindowState(),
                                                   output,
                                                   portalContext,
                                                   portletRequest);

      if (isAction == PCConstants.actionInt) {
        portletResponse = new ActionResponseImp(resCtx);
      } else if (isAction == PCConstants.eventInt) {
        portletResponse = new EventResponseImp(resCtx);
      } else if (isAction == PCConstants.resourceInt) {
        portletResponse = new ResourceResponseImp(resCtx);
      } else {
        portletResponse = new RenderResponseImp(resCtx);
      }

      portletRequest.setAttribute(PortletRequest.USER_INFO, input.getUserAttributes());
      portletRequest.setAttribute(PortletRequest.CCPP_PROFILE, getCcppProfile(request));

      monitor.setLastAccessTime(portletAppName, portletName, startTime);
      boolean isBroken = monitor.isBroken(portletAppName, portletName);
      boolean isAvailable = monitor.isAvailable(portletAppName, portletName, startTime);
      boolean isDestroyed = monitor.isDestroyed(portletAppName, portletName);

      if (isDestroyed) {
        log_.debug("Portlet is destroyed");
        generateOutputForException(portletRequest, isAction, null, output);
        return;
      } else if (isBroken || !isAvailable || portletRequest.getAttribute(exception_key) != null) {
        log_.debug("Portlet is borken, not available or the request contains an associated error");
        generateOutputForException(portletRequest, isAction, exception_key, output);
        return;
      } else {
        Portlet portlet = null;
        try {
          portlet = proxy.getPortlet(portletContext, portletName);
        } catch (PortletException e) {
          log_.error("unable to get portlet :  " + portletName, e);
          portletRequest.setAttribute(exception_key, e);
          generateOutputForException(portletRequest, isAction, exception_key, output);
          return;
        }
        try {
          PortletCommandChain chain = (PortletCommandChain) cont.getComponentInstanceOfType(PortletCommandChain.class);
          if (isAction == PCConstants.actionInt) {
            chain.doProcessAction(portlet, (ActionRequest) portletRequest, (ActionResponse) portletResponse);
            // portlet.processAction((ActionRequest) portletRequest,
            // (ActionResponse) portletResponse);
            if (((ActionResponseImp) portletResponse).isSendRedirectAlreadyOccured()) {
              String location = ((ActionResponseImp) portletResponse).getLocation();
              log_.debug("need to redirect to " + location);
              output.addProperty(Output.SEND_REDIRECT, location);
            }
          } else if (isAction == PCConstants.eventInt) {
            chain.doProcessEvent(portlet, (EventRequest) portletRequest, (EventResponse) portletResponse);
          } else if (isAction == PCConstants.resourceInt) {
            chain.doServeResource(portlet, (ResourceRequest) portletRequest, (ResourceResponse) portletResponse);
          } else {
            chain.doRender(portlet, (RenderRequest) portletRequest, (RenderResponse) portletResponse);
            if (((RenderInput) input).getTitle() != null) {
              log_.debug("overide default title");
              ((RenderOutput) output).setTitle(((RenderInput) input).getTitle());
            }
          }
        } catch (Throwable t) {
          log_.error("exception returned by processAction() or render() methods", t);
          monitor.setLastFailureAccessTime(portletAppName, portletName, startTime);
          if (t instanceof RuntimeException) {
            log_.debug("It is a runtime exception");
            portletRequest.setAttribute(exception_key, t);
            generateOutputForException(portletRequest, isAction, exception_key, output);
            return;
          }
          if (t instanceof PortletException) {
            log_.debug("It is a portlet exception");
            PortletException e = (PortletException) t;
            if (t instanceof UnavailableException) {
              log_.debug("It is an unavailable exception");
              UnavailableException ex = (UnavailableException) e;
              if (!ex.isPermanent()) {
                log_.debug("but a non permanent one");
                monitor.setUnavailabilityPeriod(portletAppName, portletName, ex.getUnavailableSeconds());
              } else {
                log_.debug("a permanent one, so destroy the portlet and broke it");
                proxy.destroy(portletName);
                monitor.brokePortlet(portletAppName, portletName);
              }
            }
            portletRequest.setAttribute(exception_key, e);
            generateOutputForException(portletRequest, isAction, exception_key, output);
            return;
          }
          log_.debug("It is not a portlet exception");
          portletRequest.setAttribute(exception_key, t);
          generateOutputForException(portletRequest, isAction, exception_key, output);
          return;
        }
      }
    } finally {
      long endTime = System.currentTimeMillis();
      PortletRuntimeDatasImpl rtd = monitor.getPortletRuntimeData(portletAppName, portletName);
      if (rtd != null) { // should fix this later , this can happen if the
        // portlet is broken
        if (isAction == PCConstants.actionInt) {
          rtd.logProcessActionRequest(startTime, endTime);
        } else if (isAction == PCConstants.eventInt) {
          rtd.logProcessEventRequest(startTime, endTime);
        } else if (isAction == PCConstants.resourceInt) {
          boolean cacheHit = ((ResourceOutput) output).isCacheHit();
          rtd.logServeResourceRequest(startTime, endTime, cacheHit);
        } else {
          boolean cacheHit = ((RenderOutput) output).isCacheHit();
          rtd.logRenderRequest(startTime, endTime, cacheHit);
        }
      }
    }
  }

  private Object getCcppProfile(HttpServletRequest request) {
    // jsr-188 (CC/PP)
    // ProfileFactory pf = ProfileFactoryImpl.getInstance();
    // Profile profile =
    // pf.newProfile(request,ValidationMode.VALIDATIONMODE_NONE);
    // return profile;
    return null;
  }

  private void generateOutputForException(PortletRequestImp request,
                                          int isAction,
                                          String key,
                                          Output output) {
    String prop_key = "";
    String prop_output = "";
    String title = "";
    String content = "";
    log_.debug("generate the exception message");
    if (key == null) {
      prop_key = PCConstants.DESTROYED;
      prop_output = "output generated because of a destroyed portlet access";
      title = "Portlet destroyed";
      content = "Portlet unvailable";
    } else {
      Throwable e = (Throwable) request.getAttribute(key);
      prop_key = PCConstants.EXCEPTION;
      prop_output = "output generated because of an exception";
      title = "Exception occured";
      if (e != null) {
        log_.debug("Exception associated : " + e.toString());
        content = e.toString();
        while (e.getCause() != null) {
          e = e.getCause();
          String ce = "Cause: " + e.toString();
          if (content == null)
            content = ce;
          else
            content = content + "<br>" + ce;
        }
        if (content == null) {
          content = prop_output;
        }
        prop_output = content;
      } else {
        log_.debug("No exception associated");
        content = "There is a problem";
      }
    }
    if (isAction == PCConstants.actionInt || isAction == PCConstants.eventInt) {
      output.addProperty(prop_key, prop_output);
    } else {
      ((RenderOutput) output).setTitle(title);
      try {
        ((RenderOutput) output).setContent(content.getBytes("utf-8"));
      } catch (java.io.UnsupportedEncodingException e) {
        ((RenderOutput) output).setContent(content.getBytes());
      }

      output.addProperty(prop_key, prop_output);
    }
  }

  public ResourceBundle getBundle(String portletAppName,
                                  String portletName,
                                  Locale locale) {
    org.exoplatform.services.portletcontainer.pci.model.Portlet portlet = holder.getPortletMetaData(portletAppName, portletName);
    try {
      return resourceBundleManager.lookupBundle(portlet, locale);
    } catch (Exception e) {
      return null;
    }
  }
}
