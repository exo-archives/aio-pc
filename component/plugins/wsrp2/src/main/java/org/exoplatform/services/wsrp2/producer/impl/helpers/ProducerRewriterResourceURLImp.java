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
 
package org.exoplatform.services.wsrp2.producer.impl.helpers;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceURLImp;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.utils.Utils;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class ProducerRewriterResourceURLImp extends ResourceURLImp {

  private String                 sessionID;

  private String                 portletHandle;

  private PersistentStateManager stateManager;

  private NamedString[]          navigationalValues;// TODO EXOMAN

  private Extension[]            extensions;

  private String                 resourceState;     // TODO EXOMAN

  public ProducerRewriterResourceURLImp(String type,
                                        String baseURL,
                                        boolean isCurrentlySecured,
                                        String portletHandle,
                                        PersistentStateManager stateManager,
                                        String sessionID,
                                        boolean defaultEscapeXml,
                                        String cacheLevel) {
    super(type, baseURL, isCurrentlySecured, defaultEscapeXml, cacheLevel);
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
    if (resourceState != null) {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_RESOURCE_STATE + "}", resourceState);
    } else {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_RESOURCE_STATE + "}", "");
    }
    if (cacheLevel != null) {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_RESOURCE_CACHEABILITY + "}", cacheLevel);
    } else {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_RESOURCE_CACHEABILITY + "}", "");
    }
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_SECURE_URL + "}", secureInfo);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_PORTLET_HANDLE + "}", portletHandle);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "}", navigationalState);

    // WSRP_NAVIGATIONAL_VALUES
    if (navigationalValues != null) {
      String navigationalValuesString = null;
      for (NamedString namedString : navigationalValues) {
        if (namedString != null) {
          if (navigationalValuesString == null) {
            //for set param first time
            navigationalValuesString = new String();
            navigationalValuesString.concat(namedString.getName()).concat("=").concat(namedString.getValue());
          } else {
            navigationalValuesString.concat("&").concat(namedString.getName()).concat("=").concat(namedString.getValue());  
          }
        }
      }
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_NAVIGATIONAL_VALUES + "}", encode(navigationalValuesString, true));
    } else {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_NAVIGATIONAL_VALUES + "}", "");
    }

    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_SESSION_ID + "}", sessionID);

    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_URL + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_REQUIRES_REWRITE + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_PREFER_OPERATION + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_INTERACTION_STATE + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_FRAGMENT_ID + "}", "");

    Utils.fillExtensions(temp, extensions);

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
