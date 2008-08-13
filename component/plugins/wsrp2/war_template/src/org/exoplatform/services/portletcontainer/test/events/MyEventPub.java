/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
package org.exoplatform.services.portletcontainer.test.events;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * test event class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "clazz", "string" })
@XmlRootElement(name = "myeventpub")
public class MyEventPub implements Serializable {

//  private final String[] jaxb8_5_2List = new String[] { "java.lang.String", "java.math.BigInteger",
//      "java.math.BigDecimal", "java.util.Calendar", "java.util.Date", "javax.xml.namespace.QName",
//      "java.net.URI", "javax.xml.datatype.XMLGregorianCalendar", "javax.xml.datatype.Duration",
//      "java.awt.Image", "javax.activation.DataHandler", "javax.xml.transform.Source",
//      "java.util.UUID" };
  
  /**
   * street.
   */
  @XmlElement(required = true)
  private String clazz;

  /**
   * city.
   */
  @XmlElement(required = false)
  private String string;

  /**
   * @param value clazz
   */
  public final void setClazz(final String value) {
    clazz = value;
  }

  /**
   * @return clazz
   */
  public final String getClazz() {
    return clazz;
  }

  /**
   * @param value string
   */
  public final void setString(final String value) {
    string = value;
  }

  /**
   * @return string
   */
  public final String getString() {
    return string;
  }

}
