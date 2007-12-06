/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.impl.servlet;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletApplicationRegister;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.XMLParser;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.xml.resolving.SimpleResolvingService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net
 * Date: 10 nov. 2003
 * Time: 12:58:52
 */
public class PortletApplicationListener implements ServletContextListener {
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    ExoContainer manager = ExoContainerContext.getTopContainer();
    Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
    
    String webXMLFile = "/WEB-INF/web.xml";
    String portletAppName = null;
    
    try {
      portletAppName = (String) ServletContext.class.getMethod("getContextPath", new Class[0]).invoke(servletContextEvent.getServletContext(), new Class[0]);
      portletAppName = portletAppName.substring(portletAppName.lastIndexOf("/") + 1);
    } catch (Exception e) {
      log.warn("Servlet api 2.4 or below detected. Unable to find method getContextPath on ServletContext.");
      //e.printStackTrace();
    }
    if (portletAppName == null) {
      try {
        java.net.URL webXmlUrl = servletContextEvent.getServletContext().getResource(webXMLFile);
        portletAppName = webXmlUrl.toExternalForm();
        portletAppName = portletAppName.substring(0, portletAppName.indexOf(webXMLFile));
        portletAppName = portletAppName.substring(portletAppName.lastIndexOf("/") + 1);
        int id = portletAppName.indexOf(".war");
        if(id > 0) 
          portletAppName = portletAppName.substring(0, id);
      } catch (java.net.MalformedURLException e) {
        log.warn("Erorr getting web.xml from ServletContext.");
        //e.printStackTrace();
      }
    }

    ServletContext servletContext = servletContextEvent.getServletContext();
    log.info("DEPLOY PORTLET APPLICATION: " + portletAppName);
    log.debug("Real path : "+ servletContext.getRealPath(""));   
    InputStream is = null;
    String oldParser = System.getProperty("javax.xml.parsers.DocumentBuilderFactory") ;
    try {
      is = servletContext.getResourceAsStream("/WEB-INF/portlet.xml");
      if (is == null) {
        log.info("PORTLET CONFIGURATION IS NOT FOUND, IGNORE THE PACKAGE");
        return;
      }
      PortletApp portletApp = XMLParser.parse(is);
      
      is = servletContext.getResourceAsStream("/WEB-INF/web.xml");
      Collection<String> roles = new ArrayList<String>();
      
      XPath xpath = XPathFactory.newInstance().newXPath();
      XPathExpression roleNameExp = xpath.compile("/web-app/security-role/role-name") ;
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      SimpleResolvingService serviceXML = 
          (SimpleResolvingService) manager.getComponentInstanceOfType(SimpleResolvingService.class);
      builder.setEntityResolver(serviceXML.getEntityResolver());
      Document document = builder.parse(is);
      NodeList nodes = (NodeList) roleNameExp.evaluate(document , XPathConstants.NODESET);
      for (int i = 0; i <  nodes.getLength(); i++) {
        Node element = nodes.item(i);
        roles.add(element.getFirstChild().getNodeValue());        
      }
            
      log.info("  -- read: " + portletApp.getPortlet().size() + " portlets");
      PortletApplicationRegister service = 
        (PortletApplicationRegister) manager.getComponentInstanceOfType(PortletApplicationRegister.class);
      service.registerPortletApplication(servletContext, portletApp, roles, portletAppName); 
    } catch (Exception e) {
      log.error("Cannot deploy " + servletContext.getServletContextName(), e);
    } finally {
      if(oldParser != null) {
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", oldParser) ;
      }
    }
  }

  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    ServletContext servletContext = servletContextEvent.getServletContext();
    ExoContainer manager = ExoContainerContext.getTopContainer();
    Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
    log.info("UNDEPLOY PORTLET APPLICATION: " + servletContext.getServletContextName());    
    try {
      PortletApplicationRegister service = 
        (PortletApplicationRegister) manager.getComponentInstanceOfType(PortletApplicationRegister.class);
      service.removePortletApplication(servletContext);
    } catch (Exception e) {
      log.error("UNDEPLOY PORTLET APPLICATION: " + e);
    }
  }
}