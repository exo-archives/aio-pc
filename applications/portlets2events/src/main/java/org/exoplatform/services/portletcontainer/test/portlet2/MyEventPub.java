/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
package org.exoplatform.services.portletcontainer.test.portlet2;

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
@XmlType(name = "", propOrder = { "street", "city" })
@XmlRootElement(name = "myeventpub")
public class MyEventPub implements Serializable {

  /**
   * street.
   */
  @XmlElement(required = true)
  private String street;

  /**
   * city.
   */
  @XmlElement(required = true)
  private String city;

  /**
   * @param s street
   */
  public final void setStreet(final String s) {
    street = s;
  }

  /**
   * @return street
   */
  public final String getStreet() {
    return street;
  }

  /**
   * @param c city
   */
  public final void setCity(final String c) {
    city = c;
  }

  /**
   * @return city
   */
  public final String getCity() {
    return city;
  }

}
