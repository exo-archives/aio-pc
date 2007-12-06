/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;


import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.exoplatform.Constants;
import org.exoplatform.services.portletcontainer.helper.BasePortletURL;
import org.exoplatform.services.portletcontainer.pci.WindowID;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 11:13:44 PM
 */
public class PortletURLImp extends BasePortletURL {

  private WindowID windowID;

  private String baseURL;
  private String AMPERSAND = "&" ;
  
  public PortletURLImp(String type, String markup,
                      List supports,
                      boolean isCurrentlySecured,
                      List customWindowStates,
                      Enumeration supportedWindowState,
                      String baseURL, WindowID windowID){
    super(type, markup, supports, isCurrentlySecured,
          customWindowStates, supportedWindowState);
    this.baseURL = baseURL;
    this.windowID = windowID;
  }

  private static String _encode(String s) {
    try {
      return URLEncoder.encode(s, "utf-8");
    } catch (java.io.UnsupportedEncodingException e) {
      return s;
    }
  }

  public String toString() {
    if(!setSecureCalled && isCurrentlySecured)
      isSecure = true;

    StringBuffer sB = new StringBuffer();
    sB.append(baseURL);
    //sB.append(Constants.AMPERSAND);
    //sB.append(Constants.COMPONENT_PARAMETER);
    //sB.append("=");
    //sB.append(windowID.generateKey());
    
    sB.append(AMPERSAND);
    sB.append(Constants.TYPE_PARAMETER);
    sB.append("=");
    sB.append(type);
    sB.append(AMPERSAND);
    sB.append(Constants.SECURE_PARAMETER);
    sB.append("=");
    sB.append(isSecure);
    if (requiredPortletMode != null) {
      sB.append(AMPERSAND);
      sB.append(Constants.PORTLET_MODE_PARAMETER);
      sB.append("=");
      sB.append(requiredPortletMode);
    }
    if (requiredWindowState != null) {
      sB.append(AMPERSAND);
      sB.append(Constants.WINDOW_STATE_PARAMETER);
      sB.append("=");
      sB.append(requiredWindowState);
    }
    Set names = parameters.keySet();
    for (Iterator iterator = names.iterator(); iterator.hasNext();) {
      String name = (String) iterator.next() ;
      Object obj =  parameters.get(name) ;
      if (obj instanceof String) {
        String value = (String) obj ;
        sB.append(AMPERSAND);
        sB.append(_encode(name));
        sB.append("=");
        sB.append(_encode(value));
      } else {
        String[] values = (String[]) obj ;
        for (int i=0; i < values.length ; i++) {
          sB.append(AMPERSAND);
          sB.append(_encode(name));
          sB.append("=");
          sB.append(_encode(values[i]));
        }
      }
    }
    return sB.toString();
  }

}
