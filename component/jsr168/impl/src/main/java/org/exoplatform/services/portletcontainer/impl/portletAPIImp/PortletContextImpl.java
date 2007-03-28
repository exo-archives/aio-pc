/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.portletcontainer.ExoPortletContext;
import org.exoplatform.services.portletcontainer.impl.PortletApplicationsHolder;
import org.exoplatform.services.portletcontainer.impl.PortletContainerConf;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;


/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 * tuan08@users.sourceforge.net
 * Date: Jul 27, 2003
 * Time: 2:13:09 AM
 */
public class PortletContextImpl implements PortletContext, ExoPortletContext {
  private ServletContext servletContext_;
  private Log log;
  protected ExoContainer cont;

  public PortletContextImpl(ExoContainer cont, ServletContext scontext) {
    this.log = LogUtil.getLog("org.exoplatform.services.portletcontainer");
    servletContext_ = scontext;
    this.cont = cont;
  }

  public ExoContainer getContainer() {
    return cont;
  }

  public String getServerInfo() {
//    return servletContext_.getServerInfo();
    String si = servletContext_.getServerInfo();
    String si2 = "";
    if (si.indexOf("/") < 0) {
      boolean n = false;
      for (int i = 1; i <= si.length(); i++) {
        int c = (int) si.charAt(i - 1);
        if (c >= (int) '0' && c <= (int) '9' && !n) {
          si2  = si2 + "/";
          n = true;
        }
        si2 = si2 + (char) c;
      }
    }
    return si2;
  }

  public PortletRequestDispatcher getRequestDispatcher(String path) {
    if (path == null || !path.startsWith("/"))
      return null;
    RequestDispatcher rD = null;
    try {
      rD = servletContext_.getRequestDispatcher(path);
      if (rD == null)
        return null;
    } catch (IllegalArgumentException e) {
      log.error("Can not lookup request dispatcher", e);
      return null;
    }
    return new PortletRequestDispatcherImp(cont, rD, path);
  }

  public PortletRequestDispatcher getNamedDispatcher(String name) {
    RequestDispatcher rD = null;
    try {
      rD = servletContext_.getNamedDispatcher(name);
      if (rD == null)
        return null;
    } catch (IllegalArgumentException e) {
      log.error("Can not lookup request dispatcher", e);
      return null;
    }
    return new PortletRequestDispatcherImp(cont, rD, name);
  }

  public InputStream getResourceAsStream(String path) {
    return servletContext_.getResourceAsStream(path);
  }

  public int getMajorVersion() {
    return 1;//servletContext_.getMajorVersion() ;
  }

  public int getMinorVersion() {
    return 0;//servletContext_.getMinorVersion() ;
  }

  public String getMimeType(String file) {
    return servletContext_.getMimeType(file);
  }

  public String getRealPath(String path) {
    return servletContext_.getRealPath(path);
  }

  public java.util.Set getResourcePaths(String path) {
    return servletContext_.getResourcePaths(path);
  }

  public java.net.URL getResource(String path) throws MalformedURLException {
    if (!path.startsWith("/"))
      throw new MalformedURLException("path must start with /");
    return servletContext_.getResource(path);
  }

  public java.lang.Object getAttribute(String name) {
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null");
    }
    return servletContext_.getAttribute(name);
  }

  public void removeAttribute(java.lang.String name) {
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null");
    }
    servletContext_.removeAttribute(name);
  }

  public void setAttribute(String name, Object value) {
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null");
    }
    //when the value is null, should have the same effect as removeAttribute (Spec)
    if (value == null) {
      servletContext_.removeAttribute(name);
    } else {
      servletContext_.setAttribute(name, value);
    }
  }

  public Enumeration getAttributeNames() {
    return servletContext_.getAttributeNames();
  }

  public String getInitParameter(String name) {
    if (name == null)
      throw new IllegalArgumentException("argument must not be null");
    return servletContext_.getInitParameter(name);
  }

  public Enumeration getInitParameterNames() {
    return servletContext_.getInitParameterNames();
  }

  public String getPortletContextName() {
    return servletContext_.getServletContextName();
  }

  public ServletContext getWrappedServletContext() {
    return servletContext_;
  }

  public boolean isSessionShared() {
    PortletContainerConf conf = (PortletContainerConf) cont.
        getComponentInstanceOfType(PortletContainerConf.class);
    return conf.isSharedSessionEnable();
  }

  public void log(java.lang.String msg) {
    servletContext_.log(msg);
  }

  public void log(java.lang.String message, java.lang.Throwable throwable) {
    servletContext_.log(message, throwable);
  }
}
