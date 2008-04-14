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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceURLImp;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class ConsumerRewriterResourceURLImp extends ResourceURLImp {

  private String                 sessionID;

  private String                 portletHandle;

  private PersistentStateManager stateManager;

  private List<String>           supportedPublicRenderParameter;

  public ConsumerRewriterResourceURLImp(String type,
                                        String baseURL,
                                        boolean isCurrentlySecured,
                                        String portletHandle,
                                        PersistentStateManager stateManager,
                                        String sessionID,
                                        boolean defaultEscapeXml,
                                        String cacheLevel,
                                        List<String> supportedPublicRenderParameter, Portlet portlet) {
    super(type, baseURL, isCurrentlySecured, defaultEscapeXml, cacheLevel, portlet, null);
    this.portletHandle = portletHandle;
    this.stateManager = stateManager;
    this.sessionID = sessionID;
    this.supportedPublicRenderParameter = supportedPublicRenderParameter;
  }

  public String toString() {
    
    invokeFilterResourceURL();

    Map<String, String[]> publicParams = new HashMap<String, String[]>();
    Map<String, String[]> privateParams = new HashMap<String, String[]>();
    String navigationalValuesString = new String();
    if (parameters != null) {
      Collection<String> keys = parameters.keySet();
      for (String key : keys) {
        String[] value = parameters.get(key);
        if (supportedPublicRenderParameter != null && supportedPublicRenderParameter.contains(key)) {
          //PUBLIC
          publicParams.put(key, value);
          // process navigationalValuesString
          for (String param : value) {
            if (navigationalValuesString != "")
              navigationalValuesString += WSRPConstants.NEXT_PARAM;
            navigationalValuesString += key + "=" + param;
          }
        } else {
          //PRIVATE
          privateParams.put(key, value);
        }
      }
    }

    StringBuffer sB = new StringBuffer();

    sB.append(WSRPConstants.WSRP_REWRITE_PREFIX);

    sB.append(WSRPConstants.WSRP_URL_TYPE);
    sB.append("=");
    sB.append(getType());

    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_FRAGMENT_ID);
    sB.append("=");
    sB.append("");

    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_EXTENSIONS);
    sB.append("=");
    sB.append("");

    if (!isSetSecureCalled() && isCurrentlySecured()) {
      setSecure(true);
    }
    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_SECURE_URL);
    sB.append("=");
    sB.append(isSecure());

    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_URL);
    sB.append("=");
    sB.append("");

    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_RESOURCE_ID);
    sB.append("=");
    sB.append(resourceID);

    // process resource state
    String resourceState = IdentifierUtil.generateUUID(this);
    try {
      stateManager.putResourceState(resourceState, parameters);//was: privateParams
    } catch (WSRPException e) {
      e.printStackTrace();
    }
    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_RESOURCE_STATE);
    sB.append("=");
    sB.append(resourceState);

    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_RESOURCE_CACHEABILITY);
    sB.append("=");
    sB.append(cacheLevel);

    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_REQUIRES_REWRITE);
    sB.append("=");
    sB.append("");

    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_PREFER_OPERATION);
    sB.append("=");
    sB.append("");

    sB.append(WSRPConstants.WSRP_REWRITE_SUFFFIX);

    Collection<String> names = parameters.keySet();
    for (Iterator<String> iterator = names.iterator(); iterator.hasNext();) {
      String name = (String) iterator.next();
      Object obj = parameters.get(name);
      if (obj instanceof String) {
        String value = (String) obj;
        sB.append(WSRPConstants.NEXT_PARAM);
        sB.append(encode(name));
        sB.append("=");
        sB.append(encode(value));
      } else {
        String[] values = (String[]) obj;
        for (int i = 0; i < values.length; i++) {
          sB.append(WSRPConstants.NEXT_PARAM);
          sB.append(encode(name));
          sB.append("=");
          sB.append(encode(values[i]));
        }
      }
    }

    return sB.toString();
  }

}
