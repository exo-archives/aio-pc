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
package org.exoplatform.services.portletcontainer.impl.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
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
import org.exoplatform.services.xml.resolving.XMLResolvingService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.dom.DOMInputImpl;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net
 * Date: 10 nov. 2003
 * Time: 12:58:52
 */
public class PortletApplicationListener implements ServletContextListener {

  private static Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
  private static String fileWebXML = "/WEB-INF/web.xml";
  private static String filePortletXML = "/WEB-INF/portlet.xml";
  private static String fileXMLXSD = "javax/servlet/resources/" + "xml.xsd";
  private static String fileXMLSchemaDTD = "javax/servlet/resources/" + "XMLSchema.dtd";
  private static String fileDatatypesDTD = "javax/servlet/resources/" + "datatypes.dtd";

  protected ServletContext _servletContext;

  public void contextInitialized(ServletContextEvent servletContextEvent) {

    ExoContainer manager = ExoContainerContext.getTopContainer();

    if (log.isDebugEnabled())
      log.debug("Getting context path");

    String portletAppName = getContextPath(servletContextEvent);

    if (log.isDebugEnabled())
      log.debug("Context path:" + portletAppName);

    ServletContext servletContext = servletContextEvent.getServletContext();
    _servletContext = servletContextEvent.getServletContext();
    log.info("DEPLOY PORTLET APPLICATION: " + portletAppName);
    if (log.isDebugEnabled())
      log.debug("Real path : "+ servletContext.getRealPath(""));

    InputStream is = null;
    String oldParser = System.getProperty("javax.xml.parsers.DocumentBuilderFactory") ;

    try {

      String spec = validationPortletXML(servletContext);
      if (spec == null)
        return;

      is = servletContext.getResourceAsStream(filePortletXML);
      log.info("The portlet.xml file valid portlet spec " + spec);
      PortletApp portletApp = XMLParser.parse(is,spec.equalsIgnoreCase("2")?Boolean.TRUE:Boolean.FALSE);
      is = servletContext.getResourceAsStream(fileWebXML);
      Collection<String> roles = new ArrayList<String>();

      XPath xpath = XPathFactory.newInstance().newXPath();
      XPathExpression roleNameExp = xpath.compile("/web-app/security-role/role-name") ;
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      XMLResolvingService serviceXML =
          (XMLResolvingService) manager.getComponentInstanceOfType(XMLResolvingService.class);
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
      log.error("Cannot deploy " + portletAppName, e);
    } finally {
      if(oldParser != null) {
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", oldParser) ;
      }
    }
  }

  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    ServletContext servletContext = servletContextEvent.getServletContext();
    String portletAppName = getContextPath(servletContextEvent);
    ExoContainer manager = ExoContainerContext.getTopContainer();
    log.info("UNDEPLOY PORTLET APPLICATION: " + portletAppName);
    try {
      PortletApplicationRegister service =
        (PortletApplicationRegister) manager.getComponentInstanceOfType(PortletApplicationRegister.class);
      service.removePortletApplication(servletContext, portletAppName);
    } catch (Exception e) {
      log.error("UNDEPLOY PORTLET APPLICATION: " + e);
    }
  }

  private String getContextPath(ServletContextEvent servletContextEvent) {

    String portletAppName = null;
    try {
      portletAppName = (String) ServletContext.class.getMethod("getContextPath", new Class[0]).invoke(servletContextEvent.getServletContext(), new Class[0]);
      portletAppName = portletAppName.substring(portletAppName.lastIndexOf("/") + 1);
      return portletAppName;
    } catch (Exception e) {
      log.warn("Servlet api 2.4 or below detected. Unable to find method getContextPath on ServletContext.");
      //e.printStackTrace();
    }
    if (portletAppName == null) {
      try {
        java.net.URL webXmlUrl = servletContextEvent.getServletContext().getResource(fileWebXML);
        portletAppName = webXmlUrl.toExternalForm();
        portletAppName = portletAppName.substring(0, portletAppName.indexOf(fileWebXML));
        int id = portletAppName.indexOf(".war");
        if(id > 0)
          portletAppName = portletAppName.substring(0, id);
        portletAppName = portletAppName.substring(portletAppName.lastIndexOf("/") + 1);
        return portletAppName;
      } catch (java.net.MalformedURLException e) {
        log.error("Erorr getting web.xml from ServletContext.");
        //e.printStackTrace();
      }
    }
    return portletAppName;
  }

  private String validationPortletXML(ServletContext servletContext) {

    InputStream is = servletContext.getResourceAsStream(filePortletXML);
    if (is == null) {
      log.info("PORTLET CONFIGURATION IS NOT FOUND, IGNORE THE PACKAGE");
      return null;
    }
    try {
      is.close();
    } catch (IOException e) {
      log.error(e.getCause());
    }

    String specErr1 = null;
    String specErr2 = null;

    if (log.isDebugEnabled())
      log.debug("Defining portlet specification");
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    StreamSource source = null;
//      long time = java.lang.System.currentTimeMillis();
    try {
      if (log.isDebugEnabled())
        log.debug("validation 1-st portlet spec");
      source = new StreamSource(servletContext.getResourceAsStream(filePortletXML));
      SchemaFactory factory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
      StreamSource schemaFile = new StreamSource(cl.getResourceAsStream("portlet-app_1_0.xsd"));
      factory.setResourceResolver(new PCResourceResolverImpl());
      Schema schema = factory.newSchema(schemaFile);
      Validator validator = schema.newValidator();
      validator.validate(source);
      return "1";
    } catch (SAXException e) {
      specErr1 = e.getMessage();
    } catch (IOException e) {
      specErr1 = e.getMessage();
    }
//      System.out.println(java.lang.System.currentTimeMillis()-time + " - time 1-st portlet spec validation");
//      time = java.lang.System.currentTimeMillis();
    try {
      if (log.isDebugEnabled())
        log.debug("validation 2-nd portlet spec");
      source = new StreamSource(servletContext.getResourceAsStream(filePortletXML));
      SchemaFactory factory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
      StreamSource schemaFile = new StreamSource(cl.getResourceAsStream("portlet-app_2_0.xsd"));
      factory.setResourceResolver(new PCResourceResolverImpl());
      Schema schema = factory.newSchema(schemaFile);
      Validator validator = schema.newValidator();
      validator.validate(source);
      return "2";
    } catch (SAXException e) {
      specErr2 = e.getMessage();
    } catch (IOException e) {
      specErr2 = e.getMessage();
    }
//      System.out.println(java.lang.System.currentTimeMillis()-time + " - time 2-nd portlet spec validation");

    if (log.isDebugEnabled())
      log.debug("Checking that was the error reason internet connection problem.");
    String errWithoutInet = "src-resolve: Cannot resolve the name 'xml:lang' to a(n) 'attribute declaration' component.";
    if (specErr1.contains(errWithoutInet) && specErr2.contains(errWithoutInet)) {
      log.info("Validation portlet.xml warning: please check your internet connection.");
      StringBuffer out = new StringBuffer();
      byte[] b = new byte[4096];
      try {
        is = servletContext.getResourceAsStream(filePortletXML);
        for (int n; (n = is.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      String str = out.toString();
      int i1 = str.indexOf("portlet-app");
      int i2 = str.indexOf(">",i1);
      String s = str.substring(i1,i2);
      int res1 = s.indexOf("portlet-app_1_0.xsd");
      if (res1 > 0 ) return "1";
      int res2 = s.indexOf("portlet-app_2_0.xsd");
      if (res2 > 0 ) return "2";
    }

    log.error("The portlet.xml doesn't valid portlet spec 1 and 2");
    if (specErr1 != null )
      log.error("Validation 1-st portlet spec: " + specErr1);
    if (specErr2 != null )
      log.error("Validation 2-nd portlet spec: " + specErr2);
    return null;
  }



  class PCResourceResolverImpl implements LSResourceResolver {

    private Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");

    public PCResourceResolverImpl() {
    }

    public LSInput resolveResource(String type,         // 'http://www.w3.org/2001/XMLSchema'
                                   String namespaceURI, // 'http://www.w3.org/XML/1998/namespace'
                                   String publicId,     // 'null'
                                   String systemId,     // 'http://www.w3.org/2001/xml.xsd' <- this one require for us
                                   String baseURI)      // 'null'
                                   {
      try {

        if (systemId.equals("http://www.w3.org/2001/xml.xsd")) {
          // return a special input source for http://www.w3.org/2001/xml.xsd
          LSInput lsInput = new DOMInputImpl();
          InputStream is = null;
          ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
          Collection c = Collections.list(cl.getResources(fileXMLXSD)) ;
          Iterator i = c.iterator();
          while(i.hasNext()) {
            URL url = (URL) i.next() ;
            is = url.openStream();
            if (is != null) {
              if (log.isDebugEnabled())
                log.debug("Have got the : " + fileXMLXSD + ", url = " + url + ", key = " + url.toString());
              break;
            }
          }
          lsInput.setByteStream(is);
          return lsInput;
        }

        if (systemId.equals("XMLSchema.dtd")) {
          // return a special input source for XMLSchema.dtd
          LSInput lsInput = new DOMInputImpl();
          InputStream is = null;
          ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
          Collection c = Collections.list(cl.getResources(fileXMLSchemaDTD)) ;
          Iterator i = c.iterator();
          while(i.hasNext()) {
            URL url = (URL) i.next() ;
            is = url.openStream();
            if (is != null) {
              if (log.isDebugEnabled())
                log.debug("Have got the : " + fileXMLSchemaDTD + ", url = " + url + ", key = " + url.toString());
              break;
            }
          }
          lsInput.setByteStream(is);
          return lsInput;
        }

        if (systemId.equals("datatypes.dtd")) {
          // return a special input source for datatypes.dtd
          LSInput lsInput = new DOMInputImpl();
          InputStream is = null;
          ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
          Collection c = Collections.list(cl.getResources(fileDatatypesDTD)) ;
          Iterator i = c.iterator();
          while(i.hasNext()) {
            URL url = (URL) i.next() ;
            is = url.openStream();
            if (is != null) {
              if (log.isDebugEnabled())
                log.debug("Have got the : " + fileDatatypesDTD + ", url = " + url + ", key = " + url.toString());
              break;
            }
          }
          lsInput.setByteStream(is);
          return lsInput;
        }

      } catch (Exception e) {
        System.out.println(e.getCause());
      }

      return null;
    }

  }

}
