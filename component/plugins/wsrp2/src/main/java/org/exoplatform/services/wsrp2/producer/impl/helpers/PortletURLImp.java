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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.type.Extension;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.utils.Utils;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class PortletURLImp extends org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletURLImp {

  private String        windowID;

  private Map           mapToStoreRenderParameters;

  private String        sessionID;

  private NamedString[] navigationalValues;// TODO EXOMAN

  private Extension[]   extensions;

  public PortletURLImp(String type,
                       String baseURL,
                       String markup,
                       List<Supports> supports,
                       boolean isCurrentlySecured,
                       String windowID,
                       Map mapToStoreRenderParameters,
                       String sessionID,
                       boolean defaultEscapeXml) {
    super(type, baseURL, markup, supports, isCurrentlySecured, defaultEscapeXml);
    this.windowID = windowID;
    this.mapToStoreRenderParameters = mapToStoreRenderParameters;
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
    String[] key = StringUtils.split(windowID, "/");

    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_URL_TYPE + "}", type);
    if (requiredPortletMode != null) {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_MODE + "}", requiredPortletMode.toString());
    } else {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_MODE + "}", "");
    }
    if (requiredWindowState != null) {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_WINDOW_STATE + "}", requiredWindowState.toString());
    } else {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_WINDOW_STATE + "}", "");
    }
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_SECURE_URL + "}", secureInfo);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_PORTLET_HANDLE + "}", key[0] + "/" + key[1]);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "}", key[2]);
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "}", navigationalState);

    // WSRP_NAVIGATIONAL_VALUES
    if (navigationalValues != null) {
      for (NamedString namedString : navigationalValues) {
        temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_NAVIGATIONAL_VALUES + "}", namedString.getValue()); // TODO
                                                                                                                      // EXOMAN
      }
    } else {
      temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_NAVIGATIONAL_VALUES + "}", "");
    }

    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_SESSION_ID + "}", sessionID);

    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_URL + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_REQUIRES_REWRITE + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_PREFER_OPERATION + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_INTERACTION_STATE + "}", "");
    temp = StringUtils.replace(temp, "{" + WSRPConstants.WSRP_FRAGMENT_ID + "}", "");
    Utils.fillExtensions(temp, extensions);

    mapToStoreRenderParameters.put(navigationalState, parameters);

    return temp;
  }

}
