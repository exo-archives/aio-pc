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
package org.exoplatform.services.portletcontainer.test.portlet2;

import javax.portlet.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

//JAXB-2.0
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.ContentHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import org.w3c.dom.Node;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.bind.JAXBElement;
import org.exoplatform.portlet2.nov.*;
import org.exoplatform.portlet2.*;

import javax.xml.namespace.*;

public class TestJAXBPortlet extends GenericPortlet {

  
  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In doView method of TestPortletNEW...");
    
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println(((new Exception()).getStackTrace()[0]).getMethodName());
    w.println("----------------------------------------<br>");
    
    String xsd = new String();
    String packagename = new String();

    xsd = "/WEB-INF/classes/schema.xsd";
    packagename = "org.exoplatform.portlet2";
      
    try {
      // The most significant
      JAXBContext jc = JAXBContext.newInstance(packagename);
      
      Unmarshaller u = jc.createUnmarshaller();

      SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
      try {
          Source schemaFile = new StreamSource(getPortletContext().getResourceAsStream(xsd));
          Schema schema = sf.newSchema(schemaFile);
          u .setSchema(schema);
          /*
          u.setEventHandler(
              new ValidationEventHandler() {
                  public boolean handleEvent(ValidationEvent ve) {
                      // ignore warnings
                      if (ve.getSeverity() != ValidationEvent.WARNING) {
                          ValidationEventLocator vel = ve.getLocator();
                          System.out.println("Line:Col[" + vel.getLineNumber() +
                              ":" + vel.getColumnNumber() +
                              "]:" + ve.getMessage());
                      }
                      return true;
                  }
              }
          );
          */
      } catch (org.xml.sax.SAXException se) {
          System.out.println("Unable to validate due to following error.");
          se.printStackTrace();
      }
      
      //- MESSAGE -//
      w.println("<br>MESSAGE");
      InputStream is = getPortletContext().getResourceAsStream("/WEB-INF/message.xml");
      Object ob = u.unmarshal(is);
      System.out.println(">>> EXOMAN TestPortlet.doView ob = " + ob);
      
      // create Message and Object - described in xsd
      Message message = new Message();
      message.setNum(12345);
      message.setSms("Good bye");      
      Object objmes = message;

      // create Call - not described in xsd
      Call call = new Call();
      call.setPhone(610015);
      call.setVoice("Hello Alex!");

      Marshaller m = jc.createMarshaller();
      
      /*
      m.setEventHandler(
        new ValidationEventHandler() {
          public boolean handleEvent(ValidationEvent ve) {
            // ignore warnings
            if (ve.getSeverity() != ValidationEvent.WARNING) {
              ValidationEventLocator vel = ve.getLocator();
              System.out.println("Line:Col[" + vel.getLineNumber() +
                        ":" + vel.getColumnNumber() +
                        "]:" + ve.getMessage());
            }
            return true;
          }
        }
      );
      */
      
      // marshal Call - not described in xsd
      try {
        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        System.out.println("-----------------------");w.println("<br>-----------------------");
        m.marshal(call,System.out);
        w.println("<br><pre><xmp>");
        m.marshal(call,w);
        w.println("</xmp></pre>");
        System.out.println("-----------------------");w.println("<br>-----------------------");
      } catch (Exception e) {
        System.out.println(">>> EXOMAN TestPortlet.doView e.getCause() =call= " + e.getCause());
        System.out.println(">>> EXOMAN TestPortlet.doView e.getMessage() = " + e.getMessage());
      }
      
      // marshal Message - described in xsd
      try {
        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        System.out.println("-----------------------");w.println("<br>-----------------------");
        m.marshal(message,System.out);
        w.println("<br><pre><xmp>");
        m.marshal(message,w);
        w.println("</xmp></pre>");
        System.out.println("-----------------------");w.println("<br>-----------------------");
      } catch (Exception e) {
        System.out.println(">>> EXOMAN TestPortlet.doView e.getCause() =message= " + e.getCause());
        System.out.println(">>> EXOMAN TestPortlet.doView e.getMessage() = " + e.getMessage());
      }
      
      // marshal Object (Message) - described in xsd
      try {
        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        System.out.println("-----------------------");w.println("<br>-----------------------");
        m.marshal(objmes,System.out);
        w.println("<br><pre><xmp>");
        m.marshal(objmes,w);
        w.println("</xmp></pre>");
        System.out.println("-----------------------");w.println("<br>-----------------------");
      } catch (Exception e) {
        System.out.println(">>> EXOMAN TestPortlet.doView e.getCause() =message= " + e.getCause());
        System.out.println(">>> EXOMAN TestPortlet.doView e.getMessage() = " + e.getMessage());
      }
      
      
      
      // test unmarshal/edit/marshal Mail - described in xsd
      //- MAIL -//
      w.println("<br><br>MAIL");
      is = getPortletContext().getResourceAsStream("/WEB-INF/mail.xml");
      JAXBElement<Mail> mailElement = (JAXBElement<Mail>) (
          new JAXBElement(
              new QName("","mail"),Mail.class,u.unmarshal(is)
              )
          );
      
      Mail mail = (Mail)mailElement.getValue();
      System.out.println(">>> EXOMAN TestPortlet.doView mail.getFrom() = " + mail.getFrom());
      w.println("<br>mail.getFrom() = " + mail.getFrom());
      System.out.println(">>> EXOMAN TestPortlet.doView mail.getTo() = " + mail.getTo());
      w.println("<br>mail.getTo() = " + mail.getTo());
      System.out.println(">>> EXOMAN TestPortlet.doView mail.getSubject() = " + mail.getSubject());
      w.println("<br>mail.getSubject() = " + mail.getSubject());
      System.out.println(">>> EXOMAN TestPortlet.doView mail.getText() = " + mail.getText());
      w.println("<br>mail.getText() = " + mail.getText());

      mail.setFrom("f");
      mail.setTo("t");
      mail.setSubject("s");
      mail.setText("text");

      m = jc.createMarshaller();
      m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
      System.out.println("-----------------------");w.println("<br>-----------------------");
      m.marshal(mailElement,System.out);
      w.println("<br><pre><xmp>");
      m.marshal(mailElement,w);
      w.println("</xmp></pre>");
      System.out.println("-----------------------");w.println("<br>-----------------------");
      
      
      /*
      // Simple: create Message and marshal 
      //works
      org.exoplatform.services.portletcontainer.test.portlet2.jaxb.Message p = new org.exoplatform.services.portletcontainer.test.portlet2.jaxb.Message();
      p.setNum(1);
      p.setSms("A");
      JAXBContext context = JAXBContext.newInstance(org.exoplatform.services.portletcontainer.test.portlet2.jaxb.Message.class);
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
      m.marshal(p,System.out);
      */
      
    } catch( UnmarshalException ue ) {
        System.out.println( "Caught UnmarshalException" );
        ue.printStackTrace();
    } catch( JAXBException je ) {
        System.out.println( "Caught JAXBException" );
        je.printStackTrace();
    }
    
  }

  protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In doEdit method of TestPortletNEW...");
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println(((new Exception()).getStackTrace()[0]).getMethodName());
  }

  protected void doHelp(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In doHelp method of TestPortletNEW...");
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println(((new Exception()).getStackTrace()[0]).getMethodName());
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    super.render(renderRequest, renderResponse);
  }

}
