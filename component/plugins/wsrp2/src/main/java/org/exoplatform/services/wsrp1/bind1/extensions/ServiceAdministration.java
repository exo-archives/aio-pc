package org.exoplatform.services.wsrp1.bind1.extensions;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ServiceAdministration complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceAdministration", propOrder = { "properties" })
public class ServiceAdministration implements Serializable {

  protected java.util.HashMap<java.lang.String, java.lang.String> properties;

  public java.util.HashMap<java.lang.String, java.lang.String> getProperties() {
    if (properties == null) {
      properties = new java.util.HashMap<java.lang.String, java.lang.String>();
    }
    return this.properties;
  }
  
  public String getPropertiesAsString(){
    return getProperties().toString();
  }

}
