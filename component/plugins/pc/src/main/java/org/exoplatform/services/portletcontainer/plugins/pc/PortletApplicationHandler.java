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

import org.exoplatform.services.log.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.PortletContainerConf;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletProcessingException;
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
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.DummyCcppProfile;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net Date: 10 nov. 2003 Time: 13:02:54
 */
public class PortletApplicationHandler {

  /**
   * Portal context.
   */
  private final PortalContext               portalContext;

  /**
   * Portlet application holder.
   */
  private final PortletApplicationsHolder   holder;

  /**
   * Portlet container configurtaion.
   */
  private final PortletContainerConf        conf;

  /**
   * Logger.
   */
  private static Log                        log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");

  /**
   * Portlet container monitor.
   */
  private final PortletContainerMonitorImpl monitor;

  /**
   * Resource bundle manager.
   */
  private final ResourceBundleManager       resourceBundleManager;

  /**
   * Exo container.
   */
  private ExoContainer                      cont;

  /**
   * @param portalContext portal context
   * @param holder holder
   * @param conf conf
   * @param portletMonitor portlet monitor
   * @param manager bundle manager
   * @param context exo container context
   */
  public PortletApplicationHandler(final PortalContext portalContext,
                                   final PortletApplicationsHolder holder,
                                   final PortletContainerConf conf,
                                   final PortletContainerMonitorImpl portletMonitor,
                                   final ResourceBundleManager manager,
                                   final ExoContainerContext context) {
    this.portalContext = portalContext;
    this.holder = holder;
    this.conf = conf;
    this.monitor = portletMonitor;
    this.resourceBundleManager = manager;
    this.cont = context.getContainer();
  }

  /**
   * @param servletContext servlet context
   * @param request request
   * @param response response
   * @param input input
   * @param output output
   * @param windowInfos window infos
   * @param methodCalled action type
   * @throws PortletContainerException exception
   */
  public final void process(final ServletContext servletContext,
                            final HttpServletRequest request,
                            final HttpServletResponse response,
                            final Input input,
                            final Output output,
                            final PortletWindowInternal windowInfos,
                            final int methodCalled) throws PortletContainerException {
    long startTime = System.currentTimeMillis();
    log.debug("process() method in PortletApplicationHandler entered");
    PortletSessionImp session = null;
    CustomRequestWrapper requestWrapper = null;
    CustomResponseWrapper responseWrapper = null;
    PortletRequestImp portletRequest = null;
    PortletResponseImp portletResponse = null;
    String portletAppName = windowInfos.getWindowID().getPortletApplicationName();
    String portletName = windowInfos.getWindowID().getPortletName();
    try {
      ExoContainer manager = cont;
      PortletApplicationProxy proxy = (PortletApplicationProxy) manager.getComponentInstance(portletAppName
          + PCConstants.PORTLET_APP_ENCODER);

      if (!holder.isModeSuported(portletAppName,
                                 portletName,
                                 input.getMarkup(),
                                 input.getPortletMode()))
        throw new PortletContainerException("The portlet mode " + input.getPortletMode().toString()
            + " is not supported for the " + input.getMarkup() + " markup language.");

      if (!holder.isStateSupported(portletAppName,
                                   portletName,
                                   input.getMarkup(),
                                   input.getWindowState())) {
        log.debug("Window state : " + input.getWindowState()
            + " not supported, set the window state to normal");
        input.setWindowState(WindowState.NORMAL);
      }

      String exception_key = PCConstants.EXCEPTION + portletAppName + portletName;

      PortletContext portletContext = PortletAPIObjectFactory.getInstance()
                                                             .createPortletContext(cont,
                                                                                   servletContext,
                                                                                   holder.getPortletMetaData(portletAppName,
                                                                                                             portletName));

      log.debug("Create new object");

      long portletAppVersionNumber = 1;
      portletAppVersionNumber = monitor.getPortletVersionNumber(portletAppName);
      log.debug("Get portlet version number : " + portletAppVersionNumber);

      // create a PortletSession object
      session = new PortletSessionImp(cont,
                                      request.getSession(false),
                                      portletContext,
                                      windowInfos.getWindowID().getUniqueID());

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
                                                 holder.getPortletApplication(portletAppName)
                                                       .getSecurityConstraint(),
                                                 holder.getPortletApplication(portletAppName)
                                                       .getUserAttribute(),
                                                 holder.getPortletApplication(portletAppName)
                                                       .getCustomPortletMode(),
                                                 holder.getPortletApplication(portletAppName)
                                                       .getCustomWindowState(),
                                                 holder.getRoles(portletAppName),
                                                 conf.getSupportedContent());

      // TODO sort the attributes

      if (methodCalled == PCConstants.ACTION_INT) {
        portletRequest = new ActionRequestImp(reqCtx);
        processActionRequest(portletRequest);
      } else if (methodCalled == PCConstants.EVENT_INT) {
        portletRequest = new EventRequestImp(reqCtx);
        processEventRequest(portletRequest);
      } else if (methodCalled == PCConstants.RESOURCE_INT) {
        portletRequest = new ResourceRequestImp(reqCtx);
        processResourceRequest(portletRequest);
      } else {
        portletRequest = new RenderRequestImp(reqCtx);
        processRenderRequest(portletRequest);
      }

      ResponseContext resCtx = new ResponseContext(responseWrapper,
                                                   cont,
                                                   windowInfos.getWindowID().getUniqueID(),
                                                   input,
                                                   holder.getPortletMetaData(portletAppName,
                                                                             portletName),
                                                   request.isSecure(),
                                                   conf.getSupportedContent(),
                                                   Collections.enumeration(holder.getWindowStates(portletAppName,
                                                                                                  portletName,
                                                                                                  input.getMarkup())),
                                                   holder.getPortletApplication(portletAppName)
                                                         .getCustomWindowState(),
                                                   output,
                                                   portalContext,
                                                   portletRequest);

      if (methodCalled == PCConstants.ACTION_INT)
        portletResponse = new ActionResponseImp(resCtx);
      else if (methodCalled == PCConstants.EVENT_INT)
        portletResponse = new EventResponseImp(resCtx);
      else if (methodCalled == PCConstants.RESOURCE_INT)
        portletResponse = new ResourceResponseImp(resCtx);
      else
        portletResponse = new RenderResponseImp(resCtx);

      portletRequest.setAttribute(PortletRequest.USER_INFO, input.getUserAttributes());
      portletRequest.setAttribute(PortletRequest.CCPP_PROFILE, getCcppProfile(request));

      monitor.setLastAccessTime(portletAppName, portletName, startTime);
      boolean isBroken = monitor.isBroken(portletAppName, portletName);
      boolean isAvailable = monitor.isAvailable(portletAppName, portletName, startTime);
      boolean isDestroyed = monitor.isDestroyed(portletAppName, portletName);

      if (isDestroyed) {
        log.debug("Portlet is destroyed");
        processPortletException(null, portletRequest, methodCalled, null, output);
        return;
      } else if (isBroken || !isAvailable || (portletRequest.getAttribute(exception_key) != null)) {
        log.debug("Portlet is borken, not available or the request contains an associated error");
        processPortletException(null, portletRequest, methodCalled, exception_key, output);
        return;
      } else {
        Portlet portlet = null;
        try {
          portlet = proxy.getPortlet(portletContext, portletName);
        } catch (PortletException e) {
          log.error("unable to get portlet :  " + portletName, e);
          portletRequest.setAttribute(exception_key, e);
          processPortletException(e, portletRequest, methodCalled, exception_key, output);
          return;
        }
        try {
          PortletCommandChain chain = (PortletCommandChain) cont.getComponentInstanceOfType(PortletCommandChain.class);
          if (methodCalled == PCConstants.ACTION_INT) {
            chain.doProcessAction(portlet,
                                  (ActionRequest) portletRequest,
                                  (ActionResponse) portletResponse);
            // portlet.processAction((ActionRequest) portletRequest,
            // (ActionResponse) portletResponse);
            if (((ActionResponseImp) portletResponse).isSendRedirectAlreadyOccured()) {
              String location = ((ActionResponseImp) portletResponse).getLocation();
              log.debug("need to redirect to " + location);
              output.addProperty(Output.SEND_REDIRECT, location);
            }
          } else if (methodCalled == PCConstants.EVENT_INT) {
            chain.doProcessEvent(portlet,
                                 (EventRequest) portletRequest,
                                 (EventResponse) portletResponse);
          } else if (methodCalled == PCConstants.RESOURCE_INT) {
            chain.doServeResource(portlet,
                                  (ResourceRequest) portletRequest,
                                  (ResourceResponse) portletResponse);
          } else {
            chain.doRender(portlet,
                           (RenderRequest) portletRequest,
                           (RenderResponse) portletResponse);
            if (((RenderInput) input).getTitle() != null) {
              log.debug("overide default title");
              ((RenderOutput) output).setTitle(((RenderInput) input).getTitle());
            }
          }
        } catch (Throwable t) {
          log.error("exception returned by processAction() or render() methods", t);
          monitor.setLastFailureAccessTime(portletAppName, portletName, startTime);
          if (t instanceof RuntimeException) {
            log.debug("It is a runtime exception");
            portletRequest.setAttribute(exception_key, t);
            processPortletException(t, portletRequest, methodCalled, exception_key, output);
            return;
          }
          if (t instanceof PortletException) {
            log.debug("It is a portlet exception");
            PortletException e = (PortletException) t;
            if (t instanceof UnavailableException) {
              log.debug("It is an unavailable exception");
              UnavailableException ex = (UnavailableException) e;
              if (!ex.isPermanent()) {
                log.debug("but a non permanent one");
                monitor.setUnavailabilityPeriod(portletAppName,
                                                portletName,
                                                ex.getUnavailableSeconds());
              } else {
                log.debug("a permanent one, so destroy the portlet and broke it");
                proxy.destroy(portletName);
                monitor.brokePortlet(portletAppName, portletName);
              }
            }
            portletRequest.setAttribute(exception_key, e);
            processPortletException(t, portletRequest, methodCalled, exception_key, output);
            return;
          }
          log.debug("It is not a portlet exception");
          portletRequest.setAttribute(exception_key, t);
          processPortletException(t, portletRequest, methodCalled, exception_key, output);
          return;
        }
      }
    } finally {
      long endTime = System.currentTimeMillis();
      PortletRuntimeDatasImpl rtd = monitor.getPortletRuntimeData(portletAppName, portletName);
      if (portletRequest != null)
        if (portletRequest.getPortletSession(false) == null)
          output.addProperty(Output.INVALIDATE_SESSION, "0");
        else
          output.addProperty(Output.INVALIDATE_SESSION, ""
              + portletRequest.getPortletSession(false).getMaxInactiveInterval());
      if (rtd != null)
        // portlet is broken
        if (methodCalled == PCConstants.ACTION_INT)
          rtd.logProcessActionRequest(startTime, endTime);
        else if (methodCalled == PCConstants.EVENT_INT)
          rtd.logProcessEventRequest(startTime, endTime);
        else if (methodCalled == PCConstants.RESOURCE_INT) {
          boolean cacheHit = ((ResourceOutput) output).isCacheHit();
          rtd.logServeResourceRequest(startTime, endTime, cacheHit);
        } else {
          boolean cacheHit = ((RenderOutput) output).isCacheHit();
          rtd.logRenderRequest(startTime, endTime, cacheHit);
        }
    }
  }

  /**
   * @param request request
   * @return CC/PP profile
   */
  private Object getCcppProfile(final HttpServletRequest request) {
    // jsr-188 (CC/PP)
    // ProfileFactory pf = ProfileFactoryImpl.getInstance();
    // Profile profile =
    // pf.newProfile(request,ValidationMode.VALIDATIONMODE_NONE);
    // return profile;
    return new DummyCcppProfile();
  }

  /**
   * @param throwable TODO
   * @param request request
   * @param methodCalled action type
   * @param key key
   * @param output output
   * @throws PortletContainerException exception
   */
  private void processPortletException(Throwable throwable,
                                       final PortletRequestImp request,
                                       final int methodCalled,
                                       final String key,
                                       final Output output) throws PortletContainerException {
    if (conf.isHookPortletExceptions())
      generateOutputForException(request, methodCalled, key, output);
    else
      throw new PortletProcessingException("", throwable);
  }

  private void generateOutputForException(final PortletRequestImp request,
                                          final int methodCalled,
                                          final String key,
                                          final Output output) {
    String prop_key = "";
    String prop_output = "";
    String title = "";
    String content = "";
    log.debug("generate the exception message");
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
        log.debug("Exception associated : " + e.toString());
        content = e.toString();
        while (e.getCause() != null) {
          e = e.getCause();
          String ce = "Cause: " + e.toString();
          if (content == null)
            content = ce;
          else
            content = content + "<br>" + ce;
        }
        if (content == null)
          content = prop_output;
        prop_output = content;
      } else {
        log.debug("No exception associated");
        content = "There is a problem";
      }
    }
    if ((methodCalled == PCConstants.ACTION_INT) || (methodCalled == PCConstants.EVENT_INT))
      output.addProperty(prop_key, prop_output);
    else {
      ((RenderOutput) output).setTitle(title);
      try {
        ((RenderOutput) output).setContent(content.getBytes("utf-8"));
      } catch (java.io.UnsupportedEncodingException e) {
        ((RenderOutput) output).setContent(content.getBytes());
      }

      output.addProperty(prop_key, prop_output);
    }
  }

  /**
   * @param portletAppName portlet app name
   * @param portletName portlet name
   * @param locale locale
   * @return resource bundle
   */
  public final ResourceBundle getBundle(final String portletAppName,
                                        final String portletName,
                                        final Locale locale) {
    org.exoplatform.services.portletcontainer.pci.model.Portlet portlet = holder.getPortletMetaData(portletAppName,
                                                                                                    portletName);
    try {
      return resourceBundleManager.lookupBundle(portlet, locale);
    } catch (Exception e) {
      return null;
    }
  }

  // can be overriden for specific Action request processing
  protected void processActionRequest(PortletRequestImp request) {

  }

  // can be overriden for specific Event request processing
  protected void processEventRequest(PortletRequestImp request) {

  }

  // can be overriden for specific Resource request processing
  protected void processResourceRequest(PortletRequestImp request) {

  }

  // can be overriden for specific Render request processing
  protected void processRenderRequest(PortletRequestImp request) {

  }
}
