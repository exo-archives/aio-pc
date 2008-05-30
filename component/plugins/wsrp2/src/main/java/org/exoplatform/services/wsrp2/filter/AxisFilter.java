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
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
//import org.exoplatform.container.PortalContainer;
//import org.exoplatform.container.RootContainer;
import org.exoplatform.services.database.HibernateService;

import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpServletRequest;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpSession;

/**
 * User: Benjamin Mestrallet
 * Date: 26 juil. 2004
 */
public class AxisFilter implements Filter {

  private String containerName = "portal";
  private Integer retriesToRegister = 2;
  private ExoContainer container;
  private WSRPHttpServletRequest wsrpHttpServletRequest;
  private WSRPHttpSession wsrpHttpSession;

  public void init(FilterConfig filterConfig) throws ServletException {
    String containerName = filterConfig.getInitParameter("portal-container-name");
    if (containerName != null) 
      this.containerName = containerName;
    Integer retriesToRegister = new Integer(filterConfig.getInitParameter("retries-to-register"));
    if (retriesToRegister != null) 
      this.retriesToRegister = retriesToRegister;
  }

  public synchronized void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    wsrpHttpServletRequest = new WSRPHttpServletRequest((HttpServletRequest)servletRequest);
    HttpSession httpSession = ((HttpServletRequest)servletRequest).getSession();
    wsrpHttpSession = new WSRPHttpSession( httpSession.getId(), httpSession.getMaxInactiveInterval());
    int i = retriesToRegister;
    while (i != 0) {
      try {
        register();
        i = 0; 
      } catch (ConcurrentModificationException e) {
        if (i > 0) i--;
      } 
    }
    i = retriesToRegister;
    while (i != 0) {
      try {
        hibernateCloseSession();
        i = 0; 
      } catch (ConcurrentModificationException e) {
        if (i > 0) i--;
      } 
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }
  
  private void register() {
    container = ExoContainerContext.getContainerByName(containerName);
    if (container == null)
      container = ExoContainerContext.getTopContainer();
    ExoContainerContext.setCurrentContainer(container);
    
    WSRPHttpServletRequest checkwsrpHttpServletRequest = 
      (WSRPHttpServletRequest) container.getComponentInstanceOfType(WSRPHttpServletRequest.class);
    if (checkwsrpHttpServletRequest == null) {
      container.registerComponentInstance(wsrpHttpServletRequest);
      System.out.println("StandaloneContainer: injecting " + WSRPHttpServletRequest.class);
      container.registerComponentInstance(wsrpHttpSession);
      System.out.println("StandaloneContainer: injecting " + WSRPHttpSession.class);
    }
  }
  
  private void hibernateCloseSession() {
    List<HibernateService> list = container.getComponentInstancesOfType(HibernateService.class) ;
    for(HibernateService hservice : list)  
      hservice.closeSession() ;
    //PortalContainer.setInstance(null);
  }
  
  public void destroy() {
  }
  
}