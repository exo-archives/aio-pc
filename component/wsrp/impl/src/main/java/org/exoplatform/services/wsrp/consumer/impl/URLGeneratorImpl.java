/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.impl;


import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.exoplatform.Constants;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.consumer.URLGenerator;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 6 févr. 2004
 * Time: 13:19:37
 */

public class URLGeneratorImpl implements URLGenerator{

  public String getBlockingActionURL(String baseURL, Map params) {
    return getURL(baseURL, params);
  }

  public String getRenderURL(String baseURL, Map params) {
    return getURL(baseURL, params);
  }

  public String getResourceURL(String baseURL, Map params) {
    return getURL(baseURL, params);
  }

  private String getURL(String baseURL, Map params){
    StringBuffer sB = new StringBuffer();
    sB.append(baseURL);
    return computeParameters(sB, params);
  }

  public String getNamespacedToken(String token) {
    return token;
  }

  private String computeParameters(StringBuffer sB, Map parameters){
    Set names = parameters.keySet();
    for (Iterator iterator = names.iterator(); iterator.hasNext();) {
      String name = (String) iterator.next() ;
      Object obj =  parameters.get(name) ;
      if (obj instanceof String) {
        String value = (String) obj ;
        sB.append("&");
        sB.append(URLEncoder.encode(replaceName(name)));
        sB.append("=");
        sB.append(URLEncoder.encode(replaceValue(value)));
      } else {
        String[] values = (String[]) obj ;
        for (int i=0; i < values.length ; i++) {
          name = replaceName(name);
          sB.append("&");
          sB.append(URLEncoder.encode(name));
          sB.append("=");
          sB.append(URLEncoder.encode(values[i]));
        }
      }
    }
    return sB.toString();
  }

  private String replaceName(String name){
    if(WSRPConstants.WSRP_MODE.equals(name))
      return Constants.PORTLET_MODE_PARAMETER;
    else if(WSRPConstants.WSRP_WINDOW_STATE.equals(name))
      return Constants.WINDOW_STATE_PARAMETER;
    else if(WSRPConstants.WSRP_PORTLET_HANDLE.equals(name))
      return Constants.COMPONENT_PARAMETER;
    else if(WSRPConstants.WSRP_SECURE_URL.equals(name))
      return Constants.SECURE_PARAMETER;
    else if(WSRPConstants.WSRP_URL_TYPE.equals(name))
      return Constants.TYPE_PARAMETER;
    return name;
  }

  private String replaceValue(String value){
    if(value.startsWith(WSRPConstants.WSRP_PREFIX))
      value =  value.substring(WSRPConstants.WSRP_PREFIX.length());
    if(WSRPConstants.URL_TYPE_BLOCKINGACTION.equals(value))
      value = "action";
    return value;
  }

}