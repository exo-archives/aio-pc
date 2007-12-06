/**
 * Created by The eXo Platform SAS
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
package org.exoplatform.services.portletcontainer.test.portlet2;

import java.lang.String;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "street",
    "city"})
@XmlRootElement(name = "myeventpub")
public class MyEventPub implements Serializable {

  @XmlElement(required = true)
  private String street;
  @XmlElement(required = true)
  private String city;

  public void setStreet(String s) {
    street = s;
  }
  public String getStreet() {
    return street;
  }
  public void setCity(String c) {
    city = c;
  }
  public String getCity() {
    return city;
  }

}

