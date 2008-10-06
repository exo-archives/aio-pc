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
@XmlType(name = "", propOrder = { "city" })
@XmlRootElement(name = "myeventpub")
public class MyEventPub implements Serializable {

  /**
   * city.
   */
  @XmlElement(required = true)
  private String city;

  /**
   * @param value city
   */
  public final void setCity(final String city) {
    this.city = city;
  }

  /**
   * @return city
   */
  public final String getCity() {
    return city;
  }

}
