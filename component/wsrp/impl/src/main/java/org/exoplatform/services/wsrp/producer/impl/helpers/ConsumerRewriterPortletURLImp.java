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
public class ConsumerRewriterPortletURLImp extends BasePortletURL{

  private String sessionID;
  private String portletHandle;
  private String baseURL;
  private PersistentStateManager stateManager;

  public ConsumerRewriterPortletURLImp(String type, String markup,
                      List supports,
                      boolean isCurrentlySecured,
                      List customWindowStates,
                      Enumeration supportedWindowState,
                      String baseURL, String portletHandle,
                      PersistentStateManager stateManager,
                      String sessionID){
    super(type, markup, supports, isCurrentlySecured,
          customWindowStates, supportedWindowState);
    this.baseURL = baseURL;
    this.portletHandle = portletHandle;
    this.stateManager = stateManager;
    this.sessionID = sessionID;
  }

  public String toString() {
    if(!setSecureCalled && isCurrentlySecured){
      isSecure = true;
    }

    String navigationalState = IdentifierUtil.generateUUID(this);
    try {
      stateManager.putNavigationalState(navigationalState, parameters);
    } catch (WSRPException e) {
      e.printStackTrace();
    }

    StringBuffer sB = new StringBuffer();
    sB.append(baseURL);
    sB.append("&");
    sB.append(WSRPConstants.WSRP_URL_TYPE);
    sB.append("=");
    sB.append(type);
    sB.append("&");
    sB.append(WSRPConstants.WSRP_PORTLET_HANDLE);
    sB.append("=");
    sB.append(portletHandle);
    sB.append("&");
    sB.append(WSRPConstants.WSRP_NAVIGATIONAL_STATE);
    sB.append("=");
    sB.append(navigationalState);
    sB.append("&");
    sB.append(WSRPConstants.WSRP_SESSION_ID);
    sB.append("=");
    sB.append(sessionID);
    sB.append("&");
    sB.append(WSRPConstants.WSRP_SECURE_URL);
    sB.append("=");
    sB.append(isSecure);

    if (requiredPortletMode != null) {
      sB.append("&");
      sB.append(WSRPConstants.WSRP_MODE);
      sB.append("=");
      sB.append(requiredPortletMode);
    }
    if (requiredWindowState != null) {
      sB.append("&");
      sB.append(WSRPConstants.WSRP_WINDOW_STATE);
      sB.append("=");
      sB.append(requiredWindowState);
    }
    sB.append(WSRPConstants.WSRP_REWITE_SUFFFIX);

    Set names = parameters.keySet();
    for (Iterator iterator = names.iterator(); iterator.hasNext();) {
      String name = (String) iterator.next() ;
      Object obj =  parameters.get(name) ;
      if (obj instanceof String) {
      String value = (String) obj ;
      sB.append(Constants.AMPERSAND);
      sB.append(URLEncoder.encode(name));
      sB.append("=");
      sB.append(URLEncoder.encode(value));
      } else {
        String[] values = (String[]) obj ;
        for (int i=0; i < values.length ; i++) {
          sB.append(Constants.AMPERSAND);
          sB.append(URLEncoder.encode(name));
          sB.append("=");
          sB.append(URLEncoder.encode(values[i]));
        }
      }
    }

    return sB.toString();
  }

  
}
