/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp.producer.impl.helpers;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceURLImp;
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class ProducerRewriterResourceURLImp extends ResourceURLImp {

  private String                 sessionID;

  private String                 portletHandle;

  private PersistentStateManager stateManager;

  public ProducerRewriterResourceURLImp(String type,
                                        String baseURL,
                                        boolean isCurrentlySecured,
                                        String portletHandle,
                                        PersistentStateManager stateManager,
                                        String sessionID) {
    super(type, baseURL, isCurrentlySecured, true, null);
    this.portletHandle = portletHandle;
    this.stateManager = stateManager;
    this.sessionID = sessionID;
  }

  public String toString() {
    String secureInfo = "false";
    if (!setSecureCalled && isCurrentlySecured) {
      isSecure = true;
      secureInfo = "true";
    }
    String navigationalState = IdentifierUtil.generateUUID(this);
    String temp = baseURL;
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_URL_TYPE + "}", type);
    if (resourceID != null) {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_RESOURCE_ID + "}", resourceID);
    } else {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_RESOURCE_ID + "}", "");
    }
    if (cacheLevel != null) {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_CACHELEVEL + "}", cacheLevel);
    } else {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_CACHELEVEL + "}", "");
    }
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_SECURE_URL + "}", secureInfo);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_PORTLET_HANDLE + "}", portletHandle);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "}", navigationalState);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_SESSION_ID + "}", sessionID);

    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_URL + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_REQUIRES_REWRITE + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_INTERACTION_STATE + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_FRAGMENT_ID + "}", "");

    try {
      stateManager.putNavigationalState(navigationalState, parameters);
    } catch (WSRPException e) {
      e.printStackTrace();
    }

    Set names = parameters.keySet();
    for (Iterator iterator = names.iterator(); iterator.hasNext();) {
      String name = (String) iterator.next();
      Object obj = parameters.get(name);
      if (obj instanceof String) {
        String value = (String) obj;
        temp += Constants.AMPERSAND;
        temp += URLEncoder.encode(name);
        temp += "=";
        temp += URLEncoder.encode(value);
      } else {
        String[] values = (String[]) obj;
        for (int i = 0; i < values.length; i++) {
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
