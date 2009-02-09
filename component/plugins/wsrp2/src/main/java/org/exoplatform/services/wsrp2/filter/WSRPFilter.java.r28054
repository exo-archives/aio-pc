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
import java.util.List;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHTTPContainer;
import org.exoplatform.services.wsrp2.type.GetServiceDescription;
import org.exoplatform.container.configuration.ConfigurationManagerImpl;

/**
 * User: Benjamin Mestrallet Date: 26 juil. 2004
 */
public class WSRPFilter implements Filter {

  private String       containerName = "portal";

  private ExoContainer container;
  
  private ServletContext  sc;

  private Log          log           = ExoLogger.getLogger(WSRPFilter.class);

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
      
      String requestURI = ((HttpServletRequest) servletRequest).getRequestURI();
      
      if (requestURI.indexOf("WSRP_v1_Markup_Service") >-1 
          || requestURI.indexOf("WSRP_v2_Markup_Service") >-1 )
      {
      setCurrentContainer();
      WSRPHTTPContainer.createInstance((HttpServletRequest) servletRequest,
                                       (HttpServletResponse) servletResponse);
      filterChain.doFilter(servletRequest, servletResponse);
      } else {
        String path = "";
        ConfigurationManagerImpl cImpl = new ConfigurationManagerImpl(sc);
        if (requestURI.indexOf("WSRPService2") >-1) {
         path = "/wsdl/wsrp-service.wsdl";
        } else if (requestURI.indexOf("WSRPService1") >-1) {
         path = "/wsdl1/wsrp_service.wsdl";
        }
        InputStream stream = cImpl.getInputStream("war:" + path);
        byte[] b;
        b = new byte[stream.available()];
        stream.read(b, 0, stream.available()); 
        servletResponse.getOutputStream().write(b);
        stream.close();
      }
    }catch (Exception e){
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
