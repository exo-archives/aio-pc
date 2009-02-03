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
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.ExoPortletContext;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;

/**
 * Created by The eXo Platform SAS.
 * Author : Tuan Nguyen tuan08@users.sourceforge.net
 * Date: Jul 27, 2003
 * Time: 2:13:09 AM
 */
public class PortletContextImpl implements PortletContext, ExoPortletContext {

  /**
   * Servlet context.
   */
  private final ServletContext servletContext;

  /**
   * Logger.
   */
  private final Log log;

  /**
   * Exo container.
   */
  private ExoContainer cont;

  /**
   * Portlet object.
   */
  private final Portlet portlet;

  /**
   * @param cont exo container
   * @param scontext servlet context
   * @param portlet portlet object
   */
  public PortletContextImpl(final ExoContainer cont,
      final ServletContext scontext,
      final Portlet portlet) {
    this.log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
    servletContext = scontext;
    this.cont = cont;
    this.portlet = portlet;
  }

  /**
   * @return exo container
   */
  public final ExoContainer getContainer() {
    return cont;
  }

  /**
   * Overridden method.
   *
   * @return server info
   * @see javax.portlet.PortletContext#getServerInfo()
   */
  public final String getServerInfo() {
    return servletContext.getServerInfo();
  }

  /**
   * Overridden method.
   *
   * @param path path
   * @return request dispatcher
   * @see javax.portlet.PortletContext#getRequestDispatcher(java.lang.String)
   */
  public final PortletRequestDispatcher getRequestDispatcher(final String path) {
    if ((path == null) || !path.startsWith("/"))
      return null;
    RequestDispatcher rD = null;
    try {
      rD = servletContext.getRequestDispatcher(path);
      if (rD == null)
        return null;
    } catch (IllegalArgumentException e) {
      log.error("Can not lookup request dispatcher", e);
      return null;
    }
    return new PortletRequestDispatcherImp(cont, rD, path);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @return named dispatcher
   * @see javax.portlet.PortletContext#getNamedDispatcher(java.lang.String)
   */
  public final PortletRequestDispatcher getNamedDispatcher(final String name) {
    RequestDispatcher rD = null;
    try {
      rD = servletContext.getNamedDispatcher(name);
      if (rD == null)
        return null;
    } catch (IllegalArgumentException e) {
      log.error("Can not lookup request dispatcher", e);
      return null;
    }
    return new PortletRequestDispatcherImp(cont, rD, name);
  }

  /**
   * Overridden method.
   *
   * @param path path
   * @return resource stream
   * @see javax.portlet.PortletContext#getResourceAsStream(java.lang.String)
   */
  public final InputStream getResourceAsStream(final String path) {
    return servletContext.getResourceAsStream(path);
  }

  /**
   * Overridden method.
   *
   * @return major version
   * @see javax.portlet.PortletContext#getMajorVersion()
   */
  public final int getMajorVersion() {
    if (portlet.getApplication().getVer2())
      return 2;
    return 1;
  }

  /**
   * Overridden method.
   *
   * @return minor version
   * @see javax.portlet.PortletContext#getMinorVersion()
   */
  public final int getMinorVersion() {
    return 0;
  }

  /**
   * Overridden method.
   *
   * @param file file
   * @return mime type
   * @see javax.portlet.PortletContext#getMimeType(java.lang.String)
   */
  public final String getMimeType(final String file) {
    return servletContext.getMimeType(file);
  }

  /**
   * Overridden method.
   *
   * @param path path
   * @return real path
   * @see javax.portlet.PortletContext#getRealPath(java.lang.String)
   */
  public final String getRealPath(final String path) {
    return servletContext.getRealPath(path);
  }

  /**
   * Overridden method.
   *
   * @param path path
   * @return set of paths
   * @see javax.portlet.PortletContext#getResourcePaths(java.lang.String)
   */
  public final java.util.Set getResourcePaths(final String path) {
    return servletContext.getResourcePaths(path);
  }

  /**
   * Overridden method.
   *
   * @param path path
   * @return url
   * @throws MalformedURLException exception
   * @see javax.portlet.PortletContext#getResource(java.lang.String)
   */
  public final java.net.URL getResource(final String path) throws MalformedURLException {
    if (!path.startsWith("/"))
      throw new MalformedURLException("path must start with /");
    return servletContext.getResource(path);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @return value
   * @see javax.portlet.PortletContext#getAttribute(java.lang.String)
   */
  public final java.lang.Object getAttribute(final String name) {
    if (name == null)
      throw new IllegalArgumentException("The attribute name cannot be null");
    return servletContext.getAttribute(name);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @see javax.portlet.PortletContext#removeAttribute(java.lang.String)
   */
  public final void removeAttribute(final java.lang.String name) {
    if (name == null)
      throw new IllegalArgumentException("The attribute name cannot be null");
    servletContext.removeAttribute(name);
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @param value value
   * @see javax.portlet.PortletContext#setAttribute(java.lang.String, java.lang.Object)
   */
  public final void setAttribute(final String name, final Object value) {
    if (name == null)
      throw new IllegalArgumentException("The attribute name cannot be null");
    // when the value is null, should have the same effect as removeAttribute
    // (Spec)
    if (value == null)
      servletContext.removeAttribute(name);
    else
      servletContext.setAttribute(name, value);
  }

  /**
   * Overridden method.
   *
   * @return attribute names
   * @see javax.portlet.PortletContext#getAttributeNames()
   */
  public final Enumeration getAttributeNames() {
    return servletContext.getAttributeNames();
  }

  /**
   * Overridden method.
   *
   * @param name name
   * @return init parameter value
   * @see javax.portlet.PortletContext#getInitParameter(java.lang.String)
   */
  public final String getInitParameter(final String name) {
    if (name == null)
      throw new IllegalArgumentException("argument must not be null");
    return servletContext.getInitParameter(name);
  }

  /**
   * Overridden method.
   *
   * @return init parameter names
   * @see javax.portlet.PortletContext#getInitParameterNames()
   */
  public final Enumeration getInitParameterNames() {
    return servletContext.getInitParameterNames();
  }

  /**
   * Overridden method.
   *
   * @return portlet context name
   * @see javax.portlet.PortletContext#getPortletContextName()
   */
  public final String getPortletContextName() {
    return servletContext.getServletContextName();
  }

  /**
   * Overridden method.
   *
   * @return encapsulated servlet contex
   * @see org.exoplatform.services.portletcontainer.ExoPortletContext#getWrappedServletContext()
   */
  public final ServletContext getWrappedServletContext() {
    return servletContext;
  }

  /**
   * Overridden method.
   *
   * @param msg message to log
   * @see javax.portlet.PortletContext#log(java.lang.String)
   */
  public final void log(final java.lang.String msg) {
    servletContext.log(msg);
  }

  /**
   * Overridden method.
   *
   * @param message message to log
   * @param throwable exception
   * @see javax.portlet.PortletContext#log(java.lang.String, java.lang.Throwable)
   */
  public final void log(final java.lang.String message, final java.lang.Throwable throwable) {
    servletContext.log(message, throwable);
  }

  /**
   * Overridden method.
   *
   * @return container runtime options
   * @see javax.portlet.PortletContext#getContainerRuntimeOptions()
   */
  public final Enumeration<String> getContainerRuntimeOptions() {
    Map<String, String[]> a = portlet.getContainerRuntimeOption();
    Map<String, String[]> b = portlet.getApplication().getContainerRuntimeOption();

    // If a container runtime option is set on the portlet application level and on the portlet
    // level with the same name the setting on the portlet level takes precedence
    // and overwrites the one set on the portal application level.

    b.putAll(a);
    return java.util.Collections.enumeration(b.keySet());
  }

  /**
   * @return portlet object
   */
  public final Portlet getPortlet() {
    return portlet;
  }

}
