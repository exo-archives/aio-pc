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

  private Log            log           = ExoLogger.getLogger(WSRPFilter.class);

  public void init(FilterConfig filterConfig) throws ServletException {
    this.log = ExoLogger.getLogger(this.getClass().getName());
    String containerName = filterConfig.getInitParameter("portal-container-name");
    this.sc = filterConfig.getServletContext();
    if (containerName != null)
      this.containerName = containerName;
    if (log.isDebugEnabled())
      log.debug("this.containerName = " + this.containerName);
  }

  public synchronized void doFilter(ServletRequest servletRequest,
                                    ServletResponse servletResponse,
                                    FilterChain filterChain) throws IOException, ServletException {
    try {

      HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
      String pathInfo = httpRequest.getPathInfo();


      if (!doSelfHaldle(httpRequest)) {
        setCurrentContainer();
        WSRPHTTPContainer.createInstance((HttpServletRequest) servletRequest,
                                         (HttpServletResponse) servletResponse);
        filterChain.doFilter(servletRequest, servletResponse);

      } else {

        //SELF
        String path = "";

        int version = 2;
//        ConfigurationManagerImpl cImpl = new ConfigurationManagerImpl(sc);

        if (pathInfo.startsWith("/WSRPService2/")) {
          if (httpRequest.getQueryString() != null
              && httpRequest.getQueryString().equalsIgnoreCase("wsdl"))
            path += "/wsrp-service.wsdl";
          else
            path += pathInfo.substring("/WSRPService2".length());
        } else if (pathInfo.startsWith("/WSRPService1/")) {
          version = 1;
          path += "/wsdl1";
          if (httpRequest.getQueryString() != null
              && httpRequest.getQueryString().equalsIgnoreCase("wsdl"))
            path += "/wsrp_service.wsdl";
          else
            path += pathInfo.substring("/WSRPService1".length());
        }


        URL url = sc.getResource("/WEB-INF" + path);

        InputStream stream = url.openStream();//cImpl.getInputStream("war:" + path);
//        InputStream stream = sc.getResourceAsStream("/WEB-INF"+"/wsrp-service.wsdl");//cImpl.getInputStream("war:" + path);

        byte[] b;
        b = new byte[stream.available()];
        stream.read(b, 0, stream.available());
        String out = new String(b, "UTF-8");



        if (path.endsWith("service.wsdl")) {
          String serverName = servletRequest.getServerName();
          int serverPort = servletRequest.getServerPort();
          String scheme = servletRequest.getScheme();
          String contextPath = httpRequest.getContextPath();
          String servletPath = httpRequest.getServletPath();
          String urlPrefix = scheme + "://" + serverName + ":" + serverPort + contextPath
              + servletPath;
          out = out.replaceAll("http://my.service:808" + version, urlPrefix);
//        out = out.replaceAll("<soap:address location=\"(\\S+)\"/>", "<soap:address location=\"" + urlPrefix + "\"/>");
        }

        servletResponse.getOutputStream().write(out.getBytes());
        stream.close();

      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (container != null) {
        try {
          hibernateCloseSession();
        } catch (Exception e) {
          log.warn("An error occured while closing the hibernate sessions", e);
        }
      }
      try {
        WSRPHTTPContainer.removeInstance();
      } catch (Exception e) {
        log.warn("An error occured while cleaning the ThreadLocal", e);
      }
      try {
        ExoContainerContext.setCurrentContainer(null);
      } catch (Exception e) {
        log.warn("An error occured while cleaning the ThreadLocal", e);
      }
    }
  }

  private boolean doSelfHaldle(HttpServletRequest httpServletRequest) {
    String pathInfo = httpServletRequest.getPathInfo();
    if (pathInfo == null)
      return false;
    if (pathInfo.equalsIgnoreCase("/WSRPService1/") || pathInfo.equalsIgnoreCase("/WSRPService2/"))
      return true;
    if (pathInfo.startsWith("/WSRPService1/wsrp_") || pathInfo.startsWith("/WSRPService2/wsrp-"))
      return true;
    if (pathInfo.startsWith("/WSRPService1/xml.xsd")
        || pathInfo.startsWith("/WSRPService2/xml.xsd"))
      return true;
    return false;
  }

  private void hibernateCloseSession() {
    List<HibernateService> list = container.getComponentInstancesOfType(HibernateService.class);
    for (HibernateService hservice : list) {
      try {
        hservice.closeSession();
      } catch (Exception e) {
        System.out.println(">>> ERROR      WSRPFilter.hibernateCloseSession() e.getMessage() = "
            + e.getMessage());
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
