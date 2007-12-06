/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
package org.exoplatform.services.portletcontainer.test.portlet2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


@XmlRegistry
public class ObjectFactory {

  private final static QName _Street_QNAME = new QName("", "street");
  private final static QName _City_QNAME = new QName("", "city");


  public ObjectFactory() {
  }

  public MyEventPub createMyEventPub() {
    return new MyEventPub();
  }

  @XmlElementDecl(namespace = "", name = "street")
  public JAXBElement<String> createTitle(String value) {
    return new JAXBElement<String>(_Street_QNAME, String.class, null, value);
  }

  @XmlElementDecl(namespace = "", name = "city")
  public JAXBElement<String> createAuthor(String value) {
    return new JAXBElement<String>(_City_QNAME, String.class, null, value);
  }

}
