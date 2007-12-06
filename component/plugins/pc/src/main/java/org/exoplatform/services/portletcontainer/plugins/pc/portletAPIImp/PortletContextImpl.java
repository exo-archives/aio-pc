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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.portletcontainer.ExoPortletContext;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Date: Jul 27, 2003 Time: 2:13:09 AM
 */
public class PortletContextImpl implements PortletContext, ExoPortletContext {
  private ServletContext servletContext_;

  private Log            log;

  protected ExoContainer cont;

  private Portlet        portlet;

  public PortletContextImpl(ExoContainer cont,
                            ServletContext scontext,
                            Portlet portlet) {
    this.log = LogUtil.getLog("org.exoplatform.services.portletcontainer");
    servletContext_ = scontext;
    this.cont = cont;
    this.portlet = portlet;
  }

  public ExoContainer getContainer() {
    return cont;
  }

  public String getServerInfo() {
    return servletContext_.getServerInfo();
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
    return portlet.getApplication().getVer2() ? 2 : 1;
  }

  public int getMinorVersion() {
    return 0;
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

  public void setAttribute(String name,
                           Object value) {
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null");
    }
    // when the value is null, should have the same effect as removeAttribute
    // (Spec)
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

  public void log(java.lang.String msg) {
    servletContext_.log(msg);
  }

  public void log(java.lang.String message,
                  java.lang.Throwable throwable) {
    servletContext_.log(message, throwable);
  }

  // A PC spec Draft 20 changes:

  // public Map getPortletRuntimeOptions() {
  // return portlet.getContainerRuntimeOption();
  // }
  //
  // public Map getApplicationRuntimeOptions() {
  // return portlet.getApplication().getContainerRuntimeOption();
  // }

  public Map getContainerRuntimeOptions() {
    Map a = portlet.getContainerRuntimeOption();
    Map b = portlet.getApplication().getContainerRuntimeOption();

    // // If a container
    // runtime option is set on the portlet application level and on the portlet
    // level with the same name the setting on the portlet level takes
    // precedence
    // and overwrites the one set on the portal application level.

    b.putAll(a);
    return b;
  }

  public Portlet getPortlet() {
    return portlet;
  }

}
