/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp2.filter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHTTPContainer;

/**
 * User: Benjamin Mestrallet Date: 26 juil. 2004
 */
public class WSRPFilter implements Filter {

  private String         containerName = "portal";

  private ExoContainer   container;

  private ServletContext sc;

  private static Log     LOG           = ExoLogger.getLogger(WSRPFilter.class);

  public void init(FilterConfig filterConfig) throws ServletException {
    this.LOG = ExoLogger.getLogger(this.getClass().getName());
    String containerName = filterConfig.getInitParameter("portal-container-name");
    this.sc = filterConfig.getServletContext();
    if (containerName != null)
      this.containerName = containerName;
    if (LOG.isDebugEnabled())
      LOG.debug("this.containerName = " + this.containerName);
  }

  public synchronized void doFilter(ServletRequest servletRequest,
                                    ServletResponse servletResponse,
                                    FilterChain filterChain) throws IOException, ServletException {
    try {

      HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

      if (!doSelfHaldle(httpRequest)) {
        setCurrentContainer();
        WSRPHTTPContainer.createInstance((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        filterChain.doFilter(servletRequest, servletResponse);

      } else {

        //SELF handle
        String path = processResourcePath(httpRequest);
        if (LOG.isDebugEnabled())
          LOG.debug("path is " + path);
        InputStream stream = getResourceStream(path);
        String out = streamToString(stream);
        if (doesRequiredRewriting(path)) {
          out = wsdlRewriting(out, httpRequest);
        }

        servletResponse.getOutputStream().write(out.getBytes());
        stream.close();

      }
    } finally {
      if (container != null) {
        try {
          hibernateCloseSession();
        } catch (Exception e) {
          LOG.warn("An error occured while closing the hibernate sessions", e);
        }
      }
      try {
        WSRPHTTPContainer.removeInstance();
      } catch (Exception e) {
        LOG.warn("An error occured while cleaning the ThreadLocal", e);
      }
      try {
        ExoContainerContext.setCurrentContainer(null);
      } catch (Exception e) {
        LOG.warn("An error occured while cleaning the ThreadLocal", e);
      }
    }
  }

  private String streamToString(InputStream stream) throws IOException, ServletException {
    byte[] b;
    b = new byte[stream.available()];
    stream.read(b, 0, stream.available());
    String out = new String(b, "UTF-8");
    return out;
  }

  private int getServiceVersion(HttpServletRequest httpRequest) {
    int version = httpRequest.getPathInfo().startsWith("/WSRPService1/") ? 1 : 2;
    return version;
  }

  private String processResourcePath(HttpServletRequest httpRequest) {
    String path = "";
    String pathInfo = httpRequest.getPathInfo();
    if (pathInfo.startsWith("/WSRPService2/")) {
      path += "/wsdl";
      if (httpRequest.getQueryString() != null && httpRequest.getQueryString().equalsIgnoreCase("wsdl"))
        path += "/wsrp-service.wsdl";
      else
        path += pathInfo.substring("/WSRPService2".length());
    } else if (pathInfo.startsWith("/WSRPService1/")) {
      path += "/wsdl1";
      if (httpRequest.getQueryString() != null && httpRequest.getQueryString().equalsIgnoreCase("wsdl"))
        path += "/wsrp_service.wsdl";
      else
        path += pathInfo.substring("/WSRPService1".length());
    }
    return path;
  }

  private InputStream getResourceStream(String path) throws IOException, ServletException {
    URL url = sc.getResource("/WEB-INF" + path);
    InputStream stream = url.openStream();
    //      ConfigurationManagerImpl cImpl = new ConfigurationManagerImpl(sc);
    //        InputStream stream = cImpl.getInputStream("war:" + path);
    return stream;
  }

  private String wsdlRewriting(String out, HttpServletRequest httpRequest) {//, int version) {
    String serverName = httpRequest.getServerName();
    int serverPort = httpRequest.getServerPort();
    String scheme = httpRequest.getScheme();
    String contextPath = httpRequest.getContextPath();
    String servletPath = httpRequest.getServletPath();
    String urlPrefix = scheme + "://" + serverName + ":" + serverPort + contextPath + servletPath;
    int version = getServiceVersion(httpRequest);
    out = out.replaceAll("http://my.service:808" + version, urlPrefix);
    //  out = out.replaceAll("<soap:address location=\"(\\S+)\"/>", "<soap:address location=\"" + urlPrefix + "\"/>");
    return out;
  }

  private boolean doesRequiredRewriting(String path) {
    return path.endsWith("service.wsdl");
  }

  private boolean doSelfHaldle(HttpServletRequest httpServletRequest) {
    String pathInfo = httpServletRequest.getPathInfo();
    //whether we are going to get CXF services' list
    if (pathInfo == null)
      return false;
    //whether required service description wsdl
    if (pathInfo.equalsIgnoreCase("/WSRPService1/") || pathInfo.equalsIgnoreCase("/WSRPService2/"))
      return true;
    //whether required other wsdl with prefix
    if (pathInfo.startsWith("/WSRPService1/wsrp_") || pathInfo.startsWith("/WSRPService2/wsrp-"))
      return true;
    //whether required xml.xsd
    if (pathInfo.equalsIgnoreCase("/WSRPService1/xml.xsd") || pathInfo.equalsIgnoreCase("/WSRPService2/xml.xsd"))
      return true;
    //otherwise do not self handle
    return false;
  }

  private void hibernateCloseSession() {
    List<HibernateService> list = container.getComponentInstancesOfType(HibernateService.class);
    for (HibernateService hservice : list) {
      try {
        hservice.closeSession();
      } catch (Exception e) {
        System.out.println(">>> ERROR      WSRPFilter.hibernateCloseSession() e.getMessage() = " + e.getMessage());
      }
    }
  }

  private void setCurrentContainer() {
    container = ExoContainerContext.getContainerByName(containerName);
    if (container == null)
      container = ExoContainerContext.getTopContainer();
    ExoContainerContext.setCurrentContainer(container);
  }

  public void destroy() {
  }

}
