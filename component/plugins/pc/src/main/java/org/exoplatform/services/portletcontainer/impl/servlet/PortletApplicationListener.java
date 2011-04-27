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
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.xml.ExoXPPParser;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.portletcontainer.PortletApplicationRegister;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.XMLParser;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xmlpull.v1.XmlPullParser;

import com.sun.org.apache.xerces.internal.dom.DOMInputImpl;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net
 * Date: 10 nov. 2003
 * Time: 12:58:52
 */
public class PortletApplicationListener implements ServletContextListener {

  /**
   * Logger.
   */
  private static Log       log              = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");

  /**
   * WebXML.
   */
  private static String    fileWebXML       = "/WEB-INF/web.xml";

  /**
   * PortletXML.
   */
  private static String    filePortletXML   = "/WEB-INF/portlet.xml";

  /**
   * XMLXSD.
   */
  private static String    fileXMLXSD       = "javax/servlet/resources/" + "xml.xsd";

  /**
   * XMLSchemaDTD.
   */
  private static String    fileXMLSchemaDTD = "javax/servlet/resources/" + "XMLSchema.dtd";

  /**
   * DatatypesDTD.
   */
  private static String    fileDatatypesDTD = "javax/servlet/resources/" + "datatypes.dtd";

  /**
   * Servlet context.
   */
  protected ServletContext hServletContext;

  /**
   * Overridden method.
   *
   * @param servletContextEvent servlet context event
   * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
   */
  public final void contextInitialized(final ServletContextEvent servletContextEvent) {

    ExoContainer manager = ExoContainerContext.getTopContainer();
    if (log.isDebugEnabled())
      log.debug("Getting context path");

    String portletAppName = getContextPath(servletContextEvent);
    if (log.isDebugEnabled())
      log.debug("Context path:" + portletAppName);

    ServletContext servletContext = servletContextEvent.getServletContext();
    hServletContext = servletContextEvent.getServletContext();
    log.info("DEPLOYING PORTLET APPLICATION: " + portletAppName);
    if (log.isDebugEnabled())
      log.debug("Real path : " + servletContext.getRealPath(""));

    InputStream is = null;
    String oldParser = System.getProperty("javax.xml.parsers.DocumentBuilderFactory");

    try {

      String spec = validationPortletXML(servletContext);

      if (spec == null)
        return;
      is = servletContext.getResourceAsStream(filePortletXML);
      log.info("The portlet.xml file valid portlet spec " + spec);
      PortletApp portletApp = XMLParser.parse(is, spec.equalsIgnoreCase("2") ? Boolean.TRUE : Boolean.FALSE);
      is = servletContext.getResourceAsStream(fileWebXML);
      Collection<String> roles = new ArrayList<String>();
      ExoXPPParser xpp = ExoXPPParser.getInstance();
      xpp.setInput(is, "UTF8");
      xpp.mandatoryNode("web-app");
      boolean isEnd = false;
      while (!xpp.node("security-role")) {
        if (xpp.getEventType() == XmlPullParser.END_DOCUMENT) {
          isEnd = true;
          break;
        }
        xpp.next();
      }
      if (!isEnd) {
        while (xpp.node("description")) {xpp.next();}
        roles.add(xpp.mandatoryNodeContent("role-name"));
        xpp.next();
        while (xpp.node("security-role")) {
          while (xpp.node("description")) {xpp.next();}
          roles.add(xpp.mandatoryNodeContent("role-name"));
          xpp.next();
        }
      }
      log.info("  -- read: " + portletApp.getPortlet().size() + " portlets");
      PortletApplicationRegister service = (PortletApplicationRegister) manager.getComponentInstanceOfType(PortletApplicationRegister.class);
      service.registerPortletApplication(servletContext, portletApp, roles, portletAppName);
      log.info("DEPLOYED PORTLET APPLICATION SUCCESSFUL: " + portletAppName);
    } catch (Throwable e) {
      log.error("Cannot deploy " + portletAppName, e);
    } finally {
      if (oldParser != null)
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", oldParser);
    }
  }

  /**
   * Overridden method.
   *
   * @param servletContextEvent servlet context event
   * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
   */
  public final void contextDestroyed(final ServletContextEvent servletContextEvent) {

    ServletContext servletContext = servletContextEvent.getServletContext();
    String portletAppName = getContextPath(servletContextEvent);
    ExoContainer manager = ExoContainerContext.getTopContainer();
    log.info("UNDEPLOY PORTLET APPLICATION: " + portletAppName);
    try {
      PortletApplicationRegister service = (PortletApplicationRegister) manager.getComponentInstanceOfType(PortletApplicationRegister.class);
      service.removePortletApplication(servletContext, portletAppName);
    } catch (Exception e) {
      log.error("UNDEPLOY PORTLET APPLICATION: " + e);
    }
  }

  /**
   * @param servletContextEvent servlet context event
   * @return context path
   */
  private String getContextPath(final ServletContextEvent servletContextEvent) {

    String portletAppName = null;
    try {
      portletAppName = (String) ServletContext.class.getMethod("getContextPath", new Class[0]).invoke(servletContextEvent.getServletContext(),
                                                                                                      new Class[0]);
      portletAppName = portletAppName.substring(portletAppName.lastIndexOf("/") + 1);
      return portletAppName;
    } catch (Exception e) {
      log.warn("Servlet api 2.4 or below detected. Unable to find method getContextPath on ServletContext.");
      //e.printStackTrace();
    }
    if (portletAppName == null)
      try {
        java.net.URL webXmlUrl = servletContextEvent.getServletContext().getResource(fileWebXML);
        portletAppName = webXmlUrl.toExternalForm();
        portletAppName = portletAppName.substring(0, portletAppName.indexOf(fileWebXML));
        int id = portletAppName.indexOf(".war");
        if (id > 0)
          portletAppName = portletAppName.substring(0, id);
        portletAppName = portletAppName.substring(portletAppName.lastIndexOf("/") + 1);
        return portletAppName;
      } catch (java.net.MalformedURLException e) {
        log.error("Erorr getting web.xml from ServletContext.");
        //e.printStackTrace();
      }
    return portletAppName;
  }

  /**
   * @param servletContext servlet context
   * @return version of portlet app
   */
  private String validationPortletXML(final ServletContext servletContext) {

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

    try {
//    long time = java.lang.System.currentTimeMillis();
      if (log.isDebugEnabled())
        log.debug("validation 1-st portlet spec");
      source = new StreamSource(servletContext.getResourceAsStream(filePortletXML));
      SchemaFactory factory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
      StreamSource schemaFile = new StreamSource(cl.getResourceAsStream("portlet-app_1_0.xsd"));
      factory.setResourceResolver(new PCResourceResolverImpl());
      ErrorHandler aHandler = new MyErrorHandler();
      factory.setErrorHandler(aHandler);
      Schema schema = factory.newSchema(schemaFile);
      Validator validator = schema.newValidator();
      validator.validate(source);
      return "1";
    } catch (SAXException e) {
      specErr1 = e.getMessage();
    } catch (IOException e) {
      specErr1 = e.getMessage();
    } catch (Exception e) {
      log.info(e.getMessage());
    } finally {
//    System.out.println(java.lang.System.currentTimeMillis()-time + " - time 1-st portlet spec validation");
    }

    try {
//    time = java.lang.System.currentTimeMillis();
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
    } catch (Exception e) {
      log.info(e.getMessage());
    } finally {
//    System.out.println(java.lang.System.currentTimeMillis()-time + " - time 2-nd portlet spec validation");
    }

    if (log.isDebugEnabled())
      log.debug("Checking that was the error reason internet connection problem.");
    String errWithoutInet = "src-resolve: Cannot resolve the name 'xml:lang' to a(n) 'attribute declaration' component.";
    if (specErr1.contains(errWithoutInet) && specErr2.contains(errWithoutInet)) {
      log.info("Validation portlet.xml warning: please check your internet connection.");
      StringBuffer out = new StringBuffer();
      byte[] b = new byte[4096];
      try {
        is = servletContext.getResourceAsStream(filePortletXML);
        for (int n; (n = is.read(b)) != -1;)
          out.append(new String(b, 0, n));
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      String str = out.toString();
      int i1 = str.indexOf("portlet-app");
      int i2 = str.indexOf(">", i1);
      String s = str.substring(i1, i2);
      int res1 = s.indexOf("portlet-app_1_0.xsd");
      if (res1 > 0)
        return "1";
      int res2 = s.indexOf("portlet-app_2_0.xsd");
      if (res2 > 0)
        return "2";
    }

    log.error("The portlet.xml doesn't valid portlet spec 1 and 2");
    if (specErr1 != null)
      log.error("Validation 1-st portlet spec: " + specErr1);
    if (specErr2 != null)
      log.error("Validation 2-nd portlet spec: " + specErr2);
    return null;
  }

  /**
   * Class to resolve resources.
   */
  class PCResourceResolverImpl implements LSResourceResolver {

    /**
     * Logger.
     */
    private final Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");

    /**
     * Simple constructor.
     */
    public PCResourceResolverImpl() {
    }

    /**
     * Overridden method.
     *
     * @param type type
     * @param namespaceURI namespace uri
     * @param publicId public id
     * @param systemId system id
     * @param baseURI base uri
     * @return ls input
     * @see org.w3c.dom.ls.LSResourceResolver#resolveResource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public final LSInput resolveResource(final String type, // 'http://www.w3.org/2001/XMLSchema'
                                         final String namespaceURI, // 'http://www.w3.org/XML/1998/namespace'
                                         final String publicId, // 'null'
                                         final String systemId, // 'http://www.w3.org/2001/xml.xsd' <- this one require for us
                                         final String baseURI) // 'null'
    {
      try {

        if (systemId.equals("http://www.w3.org/2001/xml.xsd")) {
          // return a special input source for http://www.w3.org/2001/xml.xsd
          LSInput lsInput = new DOMInputImpl();
          InputStream is = null;
          ClassLoader cl = Thread.currentThread().getContextClassLoader();
          Collection c = Collections.list(cl.getResources(fileXMLXSD));
          Iterator i = c.iterator();
          while (i.hasNext()) {
            URL url = (URL) i.next();
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
          ClassLoader cl = Thread.currentThread().getContextClassLoader();
          Collection c = Collections.list(cl.getResources(fileXMLSchemaDTD));
          Iterator i = c.iterator();
          while (i.hasNext()) {
            URL url = (URL) i.next();
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
          ClassLoader cl = Thread.currentThread().getContextClassLoader();
          Collection c = Collections.list(cl.getResources(fileDatatypesDTD));
          Iterator i = c.iterator();
          while (i.hasNext()) {
            URL url = (URL) i.next();
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

class MyErrorHandler implements ErrorHandler {

  public void warning(SAXParseException exception) throws SAXException {
    // Bring things to a crashing halt
    System.out.println("**Parsing Warning**" + "  Line:    " + exception.getLineNumber() + "" + "  URI:     " + exception.getSystemId() + ""
        + "  Message: " + exception.getMessage());
    throw new SAXException("Warning encountered");
  }

  public void error(SAXParseException exception) throws SAXException {
    // Bring things to a crashing halt
    System.out.println("**Parsing Error**" + "  Line:    " + exception.getLineNumber() + "" + "  URI:     " + exception.getSystemId() + ""
        + "  Message: " + exception.getMessage());
    throw new SAXException("Error encountered");
  }

  public void fatalError(SAXParseException exception) throws SAXException {
    // Bring things to a crashing halt
    System.out.println("**Parsing Fatal Error**" + "  Line:    " + exception.getLineNumber() + "" + "  URI:     " + exception.getSystemId() + ""
        + "  Message: " + exception.getMessage());
    throw new SAXException("Fatal Error encountered");
  }
}
