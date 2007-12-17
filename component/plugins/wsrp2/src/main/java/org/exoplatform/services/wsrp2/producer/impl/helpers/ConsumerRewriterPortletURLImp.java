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
import java.util.List;
import java.util.Set;

import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.portletcontainer.pci.PortletURLFactory;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletURLImp;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.utils.Utils;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class ConsumerRewriterPortletURLImp extends PortletURLImp {

  private String                 sessionID;

  private String                 portletHandle;

  private PersistentStateManager stateManager;

  private NamedString[]          navigationalValues;

  public ConsumerRewriterPortletURLImp(String type,
                                       String baseURL,
                                       String markup,
                                       List<Supports> supports,
                                       boolean isCurrentlySecured,
                                       String portletHandle,
                                       PersistentStateManager stateManager,
                                       String sessionID,
                                       boolean defaultEscapeXml) {
    super(type, baseURL, markup, supports, isCurrentlySecured, defaultEscapeXml);
    this.portletHandle = portletHandle;
    this.stateManager = stateManager;
    this.sessionID = sessionID;
  }

  public String toString() {
    if (!setSecureCalled && isCurrentlySecured) {
      isSecure = true;
    }

    // process navigational state
    String navigationalState = IdentifierUtil.generateUUID(this);
    try {
      stateManager.putNavigationalState(navigationalState, parameters);
    } catch (WSRPException e) {
      e.printStackTrace();
    }

    // process interaction state
    String interactionState = "";
    if (type.equalsIgnoreCase(PortletURLFactory.ACTION)) {
      interactionState = IdentifierUtil.generateUUID(this);
      try {
        stateManager.putInteractionState(interactionState, parameters);
      } catch (WSRPException e) {
        e.printStackTrace();
      }
    }

    StringBuffer sB = new StringBuffer();
    sB.append(baseURL);

    sB.append("&");
    sB.append(WSRPConstants.WSRP_URL_TYPE);
    sB.append("=");
    sB.append(Utils.changeUrlTypeFromActionToBlockingaction(type));//type));

    sB.append("&");
    sB.append(WSRPConstants.WSRP_PORTLET_HANDLE);
    sB.append("=");
    sB.append(portletHandle);

    sB.append("&");
    sB.append(WSRPConstants.WSRP_NAVIGATIONAL_STATE);
    sB.append("=");
    sB.append(navigationalState);

    // WSRP_NAVIGATIONAL_VALUES
    if (navigationalValues != null) {
      String navigationalValuesString = null;
      for (NamedString namedString : navigationalValues) {
        if (namedString != null) {
          if (navigationalValuesString == null) {
            // for set param first time
            navigationalValuesString = new String();
            navigationalValuesString.concat(namedString.getName()).concat("=").concat(namedString.getValue());
          } else {
            navigationalValuesString.concat("&").concat(namedString.getName()).concat("=").concat(namedString.getValue());
          }
        }
      }
      sB.append("&");
      sB.append(WSRPConstants.WSRP_NAVIGATIONAL_VALUES);
      sB.append("=");
      sB.append(encode(navigationalValuesString, true));
    }

    sB.append("&");
    sB.append(WSRPConstants.WSRP_INTERACTION_STATE);
    sB.append("=");
    sB.append(interactionState);

    sB.append("&");
    sB.append(WSRPConstants.WSRP_SESSION_ID);
    sB.append("=");
    sB.append(sessionID);

    sB.append("&");
    sB.append(WSRPConstants.WSRP_SECURE_URL);
    sB.append("=");
    sB.append(isSecure);

    sB.append("&");
    sB.append(WSRPConstants.WSRP_FRAGMENT_ID);
    sB.append("=");
    sB.append("");

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
    sB.append(WSRPConstants.WSRP_REWRITE_SUFFFIX);

    Set names = parameters.keySet();
    for (Iterator<String> iterator = names.iterator(); iterator.hasNext();) {
      String name = (String) iterator.next();
      Object obj = parameters.get(name);
      if (obj instanceof String) {
        String value = (String) obj;
        sB.append(Constants.AMPERSAND);
        sB.append(URLEncoder.encode(name));
        sB.append("=");
        sB.append(URLEncoder.encode(value));
      } else {
        String[] values = (String[]) obj;
        for (int i = 0; i < values.length; i++) {
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
