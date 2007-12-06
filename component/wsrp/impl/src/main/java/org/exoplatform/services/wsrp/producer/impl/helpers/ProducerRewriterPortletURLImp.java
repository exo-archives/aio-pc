/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 *
 * Created on 12 janv. 2004
 */
package org.exoplatform.services.wsrp.producer.impl.helpers;


import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.net.URLEncoder;
import java.util.Iterator;
import org.apache.commons.lang.StringUtils;

import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.portletcontainer.helper.BasePortletURL;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class ProducerRewriterPortletURLImp extends BasePortletURL{

  private String sessionID;
  private String portletHandle;
  private String template;
  private PersistentStateManager stateManager;

  public ProducerRewriterPortletURLImp(String type, String markup,
                      List supports, 
                      boolean isCurrentlySecured, 
                      List customWindowStates,
                      Enumeration supportedWindowState,
                      String template, String portletHandle,
                      PersistentStateManager stateManager,
                      String sessionID){
    super(type, markup, supports, isCurrentlySecured, 
          customWindowStates, supportedWindowState);
    this.template = template;
    this.portletHandle = portletHandle;
    this.stateManager = stateManager;
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
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_URL_TYPE + "}", type);
    if(requiredPortletMode != null) {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_MODE + "}", requiredPortletMode.toString());
    } else {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_MODE + "}", "");
    }
    if(requiredWindowState != null){
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_WINDOW_STATE + "}", requiredWindowState.toString());
    } else {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_WINDOW_STATE + "}", "");
    }
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_SECURE_URL + "}", secureInfo);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_PORTLET_HANDLE + "}", portletHandle);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "}",
      navigationalState);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_SESSION_ID + "}", sessionID);

    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_URL + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_REQUIRES_REWRITE + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_INTERACTION_STATE + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_FRAGMENT_ID + "}", type);

    try {
      stateManager.putNavigationalState(navigationalState, parameters);
    } catch (WSRPException e) {
      e.printStackTrace();
    }

    Set names = parameters.keySet();
    for (Iterator iterator = names.iterator(); iterator.hasNext();) {
      String name = (String) iterator.next() ;
      Object obj =  parameters.get(name) ;
      if (obj instanceof String) {
      String value = (String) obj ;
      temp += Constants.AMPERSAND;
      temp += URLEncoder.encode(name);
      temp += "=";
      temp += URLEncoder.encode(value);
      } else {
        String[] values = (String[]) obj ;
        for (int i=0; i < values.length ; i++) {
          temp += Constants.AMPERSAND;
          temp += URLEncoder.encode(name);
          temp += "=";
          temp += URLEncoder.encode(values[i]);
        }
      }
    }

    return temp;
  }

  
}
