/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 30, 2003
 * Time: 9:09:25 PM
 */
package org.exoplatform.services.portletcontainer.pci;


import java.util.Map;
import java.util.HashMap;
import org.exoplatform.services.portletcontainer.PortletContainerConstants;

public class Output {

  final static public String SEND_REDIRECT = "_send_redirect_" ;
  final static public String LOGIN = "_login_" ;
  final static public String PASSWORD = "_password_" ;
  final static public String LOGOUT = "_logout_";

  private Map properties = new HashMap();
  private HashMap sessionMap = new HashMap();


  public Map getProperties() {
    return properties;
  }

  public void addProperty(String key, Object o) {
    properties.put(key, o);
  }

  public void setProperties(Map properties) {
    this.properties = properties;
  }

  public boolean hasError(){
    if(properties.get(PortletContainerConstants.DESTROYED) != null ||
        properties.get(PortletContainerConstants.EXCEPTION) != null)
      return true;
    return false;
  }
  
  public HashMap getSessionMap() {
    return sessionMap;
  }

  public void setSessionMap(HashMap map) {
    this.sessionMap = map;
  }

}
