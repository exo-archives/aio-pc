/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.portletcontainer.impl.servlet;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.impl.PortletApplicationHandler;
import org.exoplatform.services.portletcontainer.impl.PortletContainerDispatcher;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.Output;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 10 nov. 2003
 * Time: 13:01:42
 */
public class ServletWrapper extends HttpServlet{

  HashMap session_data = new HashMap();
  
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);

  }

  protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
                  throws ServletException, java.io.IOException{
    
    ExoContainer manager = ExoContainerContext.getContainerByName((String) servletRequest.getAttribute(PortletContainerDispatcher.CONTAINER));
    
    PortletApplicationHandler handler = (PortletApplicationHandler) manager.
        getComponentInstanceOfType(PortletApplicationHandler.class);
    Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");    
    log.debug("Service method of ServletWrapper entered");
    log.debug("Encoding used : " + servletRequest.getCharacterEncoding());
    if (servletRequest.getAttribute(PortletContainerDispatcher.ATTRS) != null) {
      
      HttpSession ss = servletRequest.getSession();
      //System.out.println("--->Session : "+ss);
      HashMap attrs = (HashMap) servletRequest.getAttribute(PortletContainerDispatcher.ATTRS);

      for (Iterator i = attrs.keySet().iterator(); i.hasNext();) {
        String an = (String) i.next();
        ss.setAttribute(an, attrs.get(an));
        System.out.println("Received status for : "+an+" = "+attrs.get(an));
      }
      
      return;
    }
    boolean isToGetBundle = false;
    Boolean b = (Boolean)servletRequest.getAttribute(PortletContainerDispatcher.IS_TO_GET_BUNDLE);
    if(b != null)                         
      isToGetBundle = b.booleanValue();                
    if(isToGetBundle){
      log.debug("Get bundle");
      String portletAppName = (String) servletRequest.getAttribute(PortletContainerDispatcher.PORTLET_APPLICATION_NAME);
      String portletName = (String) servletRequest.getAttribute(PortletContainerDispatcher.PORTLET_NAME);
      ResourceBundle bundle = handler.getBundle(portletAppName, portletName,                                                   
                                                ((Locale)servletRequest.getAttribute(PortletContainerDispatcher.LOCALE_FOR_BUNDLE)));
      servletRequest.setAttribute(PortletContainerDispatcher.BUNDLE, bundle);
      return; 
    }
    PortletWindowInternal windowInfo = (PortletWindowInternal) servletRequest.getAttribute(PortletContainerDispatcher.WINDOW_INFO);
    Input input = (Input) servletRequest.getAttribute(PortletContainerDispatcher.INPUT);
    Output output = (Output) servletRequest.getAttribute(PortletContainerDispatcher.OUTPUT);
    
  	boolean isAction = ((Boolean)servletRequest.getAttribute(PortletContainerDispatcher.IS_ACTION)).booleanValue();
		try {			      
			handler.process(getServletContext(), servletRequest,
							        servletResponse, input, output, windowInfo, isAction);
           
		} catch (PortletContainerException e) {
      log.error("An error occured while processing the portlet request", e);
			throw new ServletException("An error occured while processing the portlet request", e);
		}
    try {
    //session_data.clear();  
    Enumeration en = servletRequest.getSession().getAttributeNames();
    while (en.hasMoreElements()){
      String attrname = (String)en.nextElement();
      Object obj = (Object)servletRequest.getSession().getAttribute(attrname);
      session_data.put(attrname, obj);
      output.setSessionMap(session_data);
    }
  
    }catch (Exception e){
      e.printStackTrace();
    }
	}
}
