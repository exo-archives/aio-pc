/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 12 janv. 2004
 */
package org.exoplatform.services.wsrp.producer.impl.helpers;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.portletcontainer.helper.BasePortletURL;
import org.exoplatform.services.wsrp.WSRPConstants;


/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class PortletURLImp extends BasePortletURL{

  private String sessionID;
  private Map mapToStoreRenderParameters;
  private String windowID;  
  private String template;
  
  public PortletURLImp(String type, String markup, 
                      List supports, 
                      boolean isCurrentlySecured, 
                      List customWindowStates,
                      Enumeration supportedWindowState,
                      String template, String windowID,
                      Map mapToStoreRenderParameters,
                      String sessionID){
    super(type, markup, supports, isCurrentlySecured, 
          customWindowStates, supportedWindowState);
    this.template = template;
    this.windowID = windowID;     
    this.mapToStoreRenderParameters = mapToStoreRenderParameters;
    this.sessionID = sessionID;    
  }  

  public String toString() {
    String secureInfo ="false";
    if(!setSecureCalled && isCurrentlySecured){
      isSecure = true;
      secureInfo = "true";
    }          
    
    String navigationalState = IdentifierUtil.generateUUID(this);
    
    String temp = template;
    String[] key = StringUtils.split(windowID, "/") ;
        
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_URL_TYPE + "}", type);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_MODE + "}", requiredPortletMode.toString());
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_WINDOW_STATE + "}", requiredWindowState.toString());    
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_SECURE_URL + "}", secureInfo);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_PORTLET_HANDLE + "}", key[0] + "/" + key[1]);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "}", key[2]);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "}", navigationalState);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_SESSION_ID + "}", sessionID);
        
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "}", "");        
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_URL + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_REQUIRES_REWRITE + "}", "");    
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_INTERACTION_STATE + "}", "");    
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_FRAGMENT_ID + "}", type);        
    
    mapToStoreRenderParameters.put(navigationalState, parameters);
    
    return temp;
  }
  
  

}
