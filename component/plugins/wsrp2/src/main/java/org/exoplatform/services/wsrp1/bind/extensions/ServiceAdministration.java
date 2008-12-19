package org.exoplatform.services.wsrp1.bind.extensions;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ServiceDescription complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;ServiceDescription&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;requiresRegistration&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot;/&gt;
 *         &lt;element name=&quot;offeredPortlets&quot; type=&quot;{urn:oasis:names:tc:wsrp:v1:types}PortletDescription&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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
