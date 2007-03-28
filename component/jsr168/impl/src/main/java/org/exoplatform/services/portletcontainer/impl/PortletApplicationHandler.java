/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.impl;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortalContext;
import javax.portlet.Portlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.UnavailableException;
import javax.portlet.WindowState;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerConstants;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.impl.aop.PortletCommandChain;
import org.exoplatform.services.portletcontainer.impl.monitor.PortletContainerMonitorImpl;
import org.exoplatform.services.portletcontainer.impl.monitor.PortletRuntimeDatasImpl;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.ActionRequestImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.ActionResponseImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletAPIObjectFactory;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletRequestImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletResponseImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletSessionImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.RenderRequestImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.RenderResponseImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.bundle.ResourceBundleManager;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomRequestWrapper;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomResponseWrapper;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.SharedSessionWrapper;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool.PortletObjectsWrapper;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool.PortletObjectsWrapperFactory;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.Output;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net
 * Date: 10 nov. 2003
 * Time: 13:02:54
 */
public class PortletApplicationHandler {

  private PortalContext portalContext;
  private int nbInstances = 0;

  private PortletApplicationsHolder holder;
  private PortletContainerConf conf;
//  private Log log_;
  static private Log log_ = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
  private PortletContainerMonitorImpl monitor;
  private ResourceBundleManager resourceBundleManager;  

  protected ExoContainer cont;

  public PortletApplicationHandler(PortalContext portalContext,
                                   PortletApplicationsHolder holder,
                                   PortletContainerConf conf,
                                   LogService logService,
                                   PortletContainerMonitorImpl portletMonitor,
                                   ResourceBundleManager manager,
                                   ExoContainerContext context) {
    this.portalContext = portalContext;
    this.holder = holder;
    this.conf = conf;
    this.monitor = portletMonitor;
    this.resourceBundleManager= manager;
    this.cont = context.getContainer();
//    log_ = logService.getLog("org.exoplatform.services.portletcontainer");
  }

  public void process(ServletContext servletContext, HttpServletRequest request,
                      HttpServletResponse response, Input input, Output output,
                      PortletWindowInternal windowInfos, boolean isAction) 
    throws PortletContainerException  {
    
    long startTime = System.currentTimeMillis() ;
    log_.debug("process() method in PortletApplicationHandler entered");
    PortletObjectsWrapper portletObjectsWrapper = null;
    PortletSessionImp session = null;
    SharedSessionWrapper sharedSession = null;
    CustomRequestWrapper requestWrapper = null;
    CustomResponseWrapper responseWrapper = null;
    PortletRequestImp portletRequest = null;
    PortletResponseImp portletResponse = null;
    String portletAppName = windowInfos.getWindowID().getPortletApplicationName();
    String portletName = windowInfos.getWindowID().getPortletName();
    try {
      ExoContainer manager = cont;
      PortletApplicationProxy proxy = 
        (PortletApplicationProxy) manager.getComponentInstance(portletAppName);

      if (!holder.isModeSuported(portletAppName, portletName, input.getMarkup(), input.getPortletMode())) {
        throw new PortletContainerException("The portlet mode " + input.getPortletMode().toString() +
            " is not supported for the " + input.getMarkup() + " markup language.");
      }

      if (!holder.isStateSupported(input.getWindowState(), portletAppName)) {
        log_.debug("Window state : " + input.getWindowState() + 
                   " not supported, set the window state to normal");
        input.setWindowState(WindowState.NORMAL);
      }

      String exception_key = PortletContainerConstants.EXCEPTION + portletAppName + portletName;

      //PortletApplicationProxy portletApp = getPortletApplication(windowInfos.getPortletApplicationName());
      PortletContext portletContext = 
        PortletAPIObjectFactory.getInstance().createPortletContext(cont, servletContext);

      log_.debug("Create new object");
      PortletObjectsWrapperFactory.createInstance(cont);
      portletObjectsWrapper = PortletObjectsWrapperFactory.getInstance().createObject();

      if (conf.isSharedSessionEnable()) {
        log_.debug("shared session enable");
        sharedSession = portletObjectsWrapper.getSharedSessionWrapper();
      }
      session = (PortletSessionImp) portletObjectsWrapper.getPortletSession();
      requestWrapper = portletObjectsWrapper.getCustomRequestWrapper();
      responseWrapper =  portletObjectsWrapper.getCustomResponseWrapper();
      if (isAction) {
        portletRequest = (ActionRequestImp) portletObjectsWrapper.getActionRequest();
        portletResponse = (ActionResponseImp) portletObjectsWrapper.getActionResponse();
      } else {
        portletRequest = (RenderRequestImp) portletObjectsWrapper.getRenderRequest();
        portletResponse = (RenderResponseImp) portletObjectsWrapper.getRenderResponse();
      }

      long portletAppVersionNumber = 1;
      portletAppVersionNumber = monitor.getPortletVersionNumber(portletAppName);
      log_.debug("Get portlet version number : " + portletAppVersionNumber);

      //create a PortletSession object
      if (conf.isSharedSessionEnable()) {
        sharedSession.fillSharedSessionWrapper(request.getSession(), portletAppName);
        sharedSession.init();
        session.fillPortletSession(sharedSession,
            portletContext, windowInfos.getWindowID().getUniqueID());
      } else {
        session.fillPortletSession(request.getSession(), portletContext, windowInfos.getWindowID().
            getUniqueID());
      }

      //create a servlet request wrapper
      requestWrapper.fillCustomRequestWrapper(request, windowInfos.getWindowID().
          getUniqueID());

      //create a servlet response wrapper
      responseWrapper.fillResponseWrapper(response);

      //create an ActionRequestImp object
      portletRequest.fillPortletRequest(requestWrapper, portalContext, portletContext, session,
          holder.getPortletMetaData(portletAppName, portletName),
          input, windowInfos,
          holder.getPortletApplication(portletAppName).getSecurityConstraint(),
          holder.getPortletApplication(portletAppName).getUserAttribute(),
          holder.getPortletApplication(portletAppName).getCustomPortletMode(),
          holder.getPortletApplication(portletAppName).getCustomWindowState(),
          holder.getRoles(portletAppName),
          conf.getSupportedContent());
      //@todo sort the attributes
      portletRequest.setAttribute(PortletRequest.USER_INFO, input.getUserAttributes());

      portletResponse.fillPortletResponse(responseWrapper, output,
          holder.getPortletApplication(portletAppName).getCustomWindowState());

      if (isAction) {
        ((ActionResponseImp) portletResponse).fillActionResponse(input, holder.getPortletMetaData(portletAppName, portletName));
      } else {
        ((RenderRequestImp) portletRequest).fillRenderRequest(((RenderInput) input).getRenderParameters(),
            ((RenderInput) input).isUpdateCache());
        ((RenderResponseImp) portletResponse).fillRenderResponse(windowInfos.getWindowID().getUniqueID(), input,
            holder.getPortletMetaData(portletAppName, portletName), request.isSecure(),
            conf.getSupportedContent(), Collections.enumeration(holder.getWindowStates(portletAppName)));
      }

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
          if (isAction) {
            PortletCommandChain  chain = 
              (PortletCommandChain)cont.getComponentInstanceOfType(PortletCommandChain.class) ;
            chain.doProcessAction(portlet, (ActionRequest) portletRequest, (ActionResponse) portletResponse) ;
            //portlet.processAction((ActionRequest) portletRequest, (ActionResponse) portletResponse);
            if (((ActionResponseImp) portletResponse).isSendRedirectAlreadyOccured()) {
              String location = ((ActionResponseImp) portletResponse).getLocation();
              log_.debug("need to redirect to " + location);
              output.addProperty(Output.SEND_REDIRECT, location);
            }
          } else {
            PortletCommandChain  chain = 
              (PortletCommandChain)cont.getComponentInstanceOfType(PortletCommandChain.class) ;
            chain.doRender(portlet, (RenderRequest) portletRequest, (RenderResponse) portletResponse) ;
            //portlet.render((RenderRequest) portletRequest, (RenderResponse) portletResponse);
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
          //monitor.brokePortlet(portletAppName, portletName);
          //proxy.destroy(portletName);
          portletRequest.setAttribute(exception_key, t);
          generateOutputForException(portletRequest, isAction, exception_key, output);
          return;
        }
      }
    } finally {
      long endTime = System.currentTimeMillis() ;
      PortletRuntimeDatasImpl rtd =  monitor.getPortletRuntimeData(portletAppName, portletName) ;
      if(rtd != null) { //should fix this later ,  this can happen if the portlet is broken
      	if(isAction) {
      		rtd.logProcessActionRequest(startTime, endTime) ; 
      	} else {
      	  boolean cacheHit = ((RenderOutput) output).isCacheHit() ;
      		rtd.logRenderRequest(startTime, endTime, cacheHit) ; 
      	}
      }
    }
  }

  private void generateOutputForException(PortletRequestImp request,
                                          boolean isAction,
                                          String key,
                                          Output output) {
    String prop_key = "";
    String prop_output = "";
    String title = "";
    String content = "";
    log_.debug("generate the exception message");
    if (key == null) {
      prop_key = PortletContainerConstants.DESTROYED;
      prop_output = "output generated because of a destroyed portlet access";
      title = "Portlet destroyed";
      content = "Portlet unvailable";
    } else {
      Throwable e = (Throwable) request.getAttribute(key);
      prop_key = PortletContainerConstants.EXCEPTION;
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
    if (isAction) {
      //output = new ActionOutput();
      output.addProperty(prop_key, prop_output);
    } else {
      //output = new RenderOutput();
      ((RenderOutput) output).setTitle(title);
      ((RenderOutput) output).setContent(content.toCharArray());
      output.addProperty(prop_key, prop_output);
    }
  }

  public ResourceBundle getBundle(String portletAppName, String portletName, Locale locale) {
    org.exoplatform.services.portletcontainer.pci.model.Portlet type = 
      holder.getPortletMetaData(portletAppName, portletName);
    try {
      return resourceBundleManager.lookupBundle(type, locale);
    } catch (Exception e) {
      return null;
    }
  }
}