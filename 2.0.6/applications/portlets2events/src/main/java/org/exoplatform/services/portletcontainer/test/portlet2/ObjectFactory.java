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

/**
 * JAXB object factory.
 */
@XmlRegistry
public class ObjectFactory {

  /**
   * street element name.
   */
  private static final QName STREET_QNAME = new QName("", "street");

  /**
   * city element name.
   */
  private static final QName CITY_QNAME = new QName("", "city");

  /**
   * simple constructor.
   */
  public ObjectFactory() {
  }

  /**
   * @return demo event
   */
  public final MyEventPub createMyEventPub() {
    return new MyEventPub();
  }

  /**
   * @param value title
   * @return JAXB element
   */
  @XmlElementDecl(namespace = "", name = "street")
  public final JAXBElement<String> createTitle(final String value) {
    return new JAXBElement<String>(STREET_QNAME, String.class, null, value);
  }

  /**
   * @param value author
   * @return JAXB element
   */
  @XmlElementDecl(namespace = "", name = "city")
  public final JAXBElement<String> createAuthor(final String value) {
    return new JAXBElement<String>(CITY_QNAME, String.class, null, value);
  }

}
