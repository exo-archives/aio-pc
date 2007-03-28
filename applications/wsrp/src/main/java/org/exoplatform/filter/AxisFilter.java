/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.container.*;

import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpServletRequest;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpServletResponse;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpSession;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 26 juil. 2004
 */
public class AxisFilter implements Filter {

  public static String WSRP_CONTAINER = "portal";  

  public void init(FilterConfig filterConfig) throws ServletException {
    String containerName = filterConfig.getInitParameter("portal-container-name");
    if ( containerName != null ) 
      WSRP_CONTAINER = containerName;
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    WSRPHttpServletRequest wsrpHttpServletRequest = new WSRPHttpServletRequest((HttpServletRequest)servletRequest);
//    WSRPHttpServletResponse wsrpHttpServletResponse = new WSRPHttpServletResponse((HttpServletResponse)servletResponse);
    HttpSession httpSession = ((HttpServletRequest)servletRequest).getSession();
    WSRPHttpSession wsrpHttpSession = new WSRPHttpSession( httpSession.getId(), httpSession.getMaxInactiveInterval());
    if ( ExoContainerContext.getTopContainer() instanceof RootContainer) {
      //PORTALCONTAINER
      PortalContainer container = RootContainer.getInstance().getPortalContainer(WSRP_CONTAINER);
      PortalContainer.setInstance(container);
      WSRPHttpServletRequest wsrpHttpServletRequest1 = 
        (WSRPHttpServletRequest) container.getComponentInstanceOfType(WSRPHttpServletRequest.class);
      if ( wsrpHttpServletRequest1 == null ) {
        container.registerComponentInstance(wsrpHttpServletRequest);
        System.out.println("PortalContainer: injecting " + WSRPHttpServletRequest.class);
        container.registerComponentInstance(wsrpHttpSession);
        System.out.println("PortalContainer: injecting " + WSRPHttpSession.class);
      }
      try {
        filterChain.doFilter(servletRequest, servletResponse);
      } finally {      
        List<HibernateService> list = 
           container.getComponentInstancesOfType(HibernateService.class) ;
        for(HibernateService hservice : list)  hservice.closeSession() ;
        PortalContainer.setInstance(null);
      }
    } else {
      //STANDALONECONTAINER
      ExoContainer container = ExoContainerContext.getTopContainer();
      WSRPHttpServletRequest wsrpHttpServletRequest2 = 
        (WSRPHttpServletRequest) container.getComponentInstanceOfType(WSRPHttpServletRequest.class);
      if ( wsrpHttpServletRequest2 == null ) {
        container.registerComponentInstance(wsrpHttpServletRequest);
        System.out.println("StandaloneContainer: injecting " + WSRPHttpServletRequest.class);
        container.registerComponentInstance(wsrpHttpSession);
        System.out.println("PortalContainer: injecting " + WSRPHttpSession.class);        
      }
      try {
        filterChain.doFilter(servletRequest, servletResponse);
      } finally {      
        List<HibernateService> list = 
           container.getComponentInstancesOfType(HibernateService.class) ;
        for(HibernateService hservice : list)  hservice.closeSession() ;
      }      
    }
    
  }
  
  public void destroy() {
  }
}