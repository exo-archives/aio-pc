/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
package org.exoplatform.services.wsrp2.test;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.exoplatform.services.wsrp2.type.Event;
import org.exoplatform.services.wsrp2.type.EventPayload;
import org.exoplatform.services.wsrp2.utils.StandardClasses;
import org.w3c.dom.Document;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Dec 2, 2008
 */
public class TestMarshalledUnmarshalledStandardClasses extends TestCase {

  /**
   * Test marshalling String.
   */
  public void testGetMarshalledDocumentForString() {
    Object value = new String("Hello");
    QName name = new QName("MyEventPub");
    String toenum = value.getClass().getSimpleName();
    StandardClasses t = null;
    try {
      t = StandardClasses.valueOf(toenum.toUpperCase());
    } catch (java.lang.IllegalArgumentException e) {
      e.printStackTrace();
      fail(e.getMessage() + e.getStackTrace()[0] + "\n" + e.getStackTrace()[0] + "\n");
    }
    org.w3c.dom.Document doc = t.getMarshalledDocument(value, name);

//    printDocument(doc);

    org.w3c.dom.Element messageElement = doc.getDocumentElement();

    assertEquals("MyEventPub", messageElement.getNodeName());
    assertEquals("Hello", messageElement.getFirstChild().getNodeValue());

  }

  /**
   * Test marshalling Date with converting after from
   * "2008-12-03T12:46:35.896+02:00" to "Wed Dec 03 12:46:35 GMT+02:00 2008" to
   * compare.
   */
  public void testGetMarshalledDocumentForDate() {

    Object value = new Date();
    QName name = new QName("MyEventPub");
    String toenum = value.getClass().getSimpleName();
    StandardClasses t = null;
    try {
      t = StandardClasses.valueOf(toenum.toUpperCase());
    } catch (java.lang.IllegalArgumentException e) {
      e.printStackTrace();
      fail(e.getMessage() + e.getStackTrace()[0] + "\n" + e.getStackTrace()[0] + "\n");
    }
    org.w3c.dom.Document doc = t.getMarshalledDocument(value, name);

//    printDocument(doc);

    org.w3c.dom.Element messageElement = doc.getDocumentElement();

    assertEquals("MyEventPub", messageElement.getNodeName());

    String source = messageElement.getFirstChild().getNodeValue();
    Date outDate = dateParse(source);
    assertEquals((Date) value, outDate);

  }

  /**
   * Converting from "2008-12-03T12:46:35.896+02:00" to "Wed Dec 03 12:46:35
   * GMT+02:00 2008" to compare.
   * 
   * @param source
   * @return
   */
  private Date dateParse(String source) {
    try {

      System.out.println("MMMMM:" + new Date());
      System.out.println("NNNNNN:" + source);
      
      String dt = "yyyy-MM-dd'T'HH:mm:ss'.'SSSZZZZZ";
      SimpleDateFormat sdf = new SimpleDateFormat(dt);
      String outDateString = source.substring(0, 26) + source.substring(27, 29);
      Date outDate = sdf.parse(outDateString);
      return outDate;
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage() + e.getStackTrace()[0] + "\n" + e.getStackTrace()[0] + "\n");
    }
    return null;
  }

  private void printDocument(Document doc) {
    try {
      // Use a Transformer for output
      TransformerFactory tFactory = TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer();
      DOMSource source2 = new DOMSource(doc);
      StreamResult result = new StreamResult(System.out);
      transformer.transform(source2, result);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage() + e.getStackTrace()[0] + "\n" + e.getStackTrace()[0] + "\n");
    }
  }

  public void testGetUnmarshalledDocumentForDate() {

//    Object value = new String("Hello");
    String eventName = "MyEventPub";

    QName type = new QName(Date.class.getCanonicalName());
    String eventType = type.getLocalPart();

    org.w3c.dom.Element messageElement = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//  <?xml version="1.0" encoding="UTF-8"?><MyEventPub>Hello</MyEventPub>
    String dateString = "2008-12-03T13:24:00.408+02:00";
    String eventXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><" + eventName + ">" + dateString
        + "</" + eventName + ">";
    try {
      StringBuffer stringBuffer1 = new StringBuffer(eventXML);
      ByteArrayInputStream bis1 = new ByteArrayInputStream(stringBuffer1.toString()
                                                                        .getBytes("UTF-8"));
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(bis1);
      messageElement = document.getDocumentElement();
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage() + e.getStackTrace()[0] + "\n" + e.getStackTrace()[0] + "\n");
    }

    EventPayload eventPayload = new EventPayload();
    eventPayload.setAny(messageElement);

    Event newEvent = new Event();
    newEvent.setName(new QName(eventName));
    newEvent.setType(new QName(eventType));
    newEvent.setPayload(eventPayload);

    String toenum2 = eventType.substring(eventType.lastIndexOf(".") + 1);
    StandardClasses t2 = null;
    try {
      t2 = StandardClasses.valueOf(toenum2.toUpperCase());
    } catch (java.lang.IllegalArgumentException e) {
      e.printStackTrace();
      fail(e.getMessage() + e.getStackTrace()[0] + "\n" + e.getStackTrace()[0] + "\n");
    }

    Object result = t2.getUnmarshalledObject(newEvent.getType(), newEvent.getPayload());
    assertEquals(dateParse(dateString), result);
  }

  public void testGetUnmarshalledDocumentForString() {

    String eventName = "MyEventPub";

    QName type = new QName(String.class.getCanonicalName());
    String eventType = type.getLocalPart();

    org.w3c.dom.Element messageElement = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    String stringEvent = "Hello";
    String eventXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><" + eventName + ">" + stringEvent
        + "</" + eventName + ">";
    try {
      StringBuffer stringBuffer1 = new StringBuffer(eventXML);
      ByteArrayInputStream bis1 = new ByteArrayInputStream(stringBuffer1.toString()
                                                                        .getBytes("UTF-8"));
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(bis1);
      messageElement = document.getDocumentElement();
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage() + e.getStackTrace()[0] + "\n" + e.getStackTrace()[0] + "\n");
    }

    EventPayload eventPayload = new EventPayload();
    eventPayload.setAny(messageElement);

    Event newEvent = new Event();
    newEvent.setName(new QName(eventName));
    newEvent.setType(new QName(eventType));
    newEvent.setPayload(eventPayload);

    String toenum2 = eventType.substring(eventType.lastIndexOf(".") + 1);
    StandardClasses t2 = null;
    try {
      t2 = StandardClasses.valueOf(toenum2.toUpperCase());
    } catch (java.lang.IllegalArgumentException e) {
      e.printStackTrace();
      fail(e.getMessage() + e.getStackTrace()[0] + "\n" + e.getStackTrace()[0] + "\n");
    }

    Object result = t2.getUnmarshalledObject(newEvent.getType(), newEvent.getPayload());
    assertEquals(stringEvent, result);
  }

}
