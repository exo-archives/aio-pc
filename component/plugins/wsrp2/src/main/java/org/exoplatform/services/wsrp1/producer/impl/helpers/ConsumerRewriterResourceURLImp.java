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

package org.exoplatform.services.wsrp1.producer.impl.helpers;

import java.util.Iterator;
import java.util.Set;

import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ResourceURLImp;
import org.exoplatform.services.wsrp.PersistentStateManager;
import org.exoplatform.services.wsrp.WSRPException;
import org.exoplatform.services.wsrp1.WSRPConstants;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */

//Do we need this implementation feature of JSR286 for WSRP1?
public class ConsumerRewriterResourceURLImp extends ResourceURLImp {

  private String                 sessionID;

  private String                 portletHandle;

  private PersistentStateManager stateManager;

  public ConsumerRewriterResourceURLImp(String type,
                                        String baseURL,
                                        boolean isCurrentlySecured,
                                        String portletHandle,
                                        PersistentStateManager stateManager,
                                        String sessionID) {
    super(type, baseURL, isCurrentlySecured, true, null, null, null);
    this.portletHandle = portletHandle;
    this.stateManager = stateManager;
    this.sessionID = sessionID;
  }

  public String toString() {
    if (!isSetSecureCalled() && isCurrentlySecured()) {
      setSecure(true);
    }

    // process navigational state
    String navigationalState = IdentifierUtil.generateUUID(this);
    try {
      stateManager.putNavigationalState(navigationalState, parameters);
    } catch (WSRPException e) {
      e.printStackTrace();
    }

    StringBuffer sB = new StringBuffer();
    sB.append(WSRPConstants.WSRP_REWRITE_PREFIX);

    sB.append(WSRPConstants.WSRP_URL_TYPE);
    sB.append("=");
    sB.append(getType());

    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_PORTLET_HANDLE);
    sB.append("=");
    sB.append(portletHandle);

    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_NAVIGATIONAL_STATE);
    sB.append("=");
    sB.append(navigationalState);

    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_SESSION_ID);
    sB.append("=");
    sB.append(sessionID);

    sB.append(WSRPConstants.NEXT_PARAM);
    sB.append(WSRPConstants.WSRP_SECURE_URL);
    sB.append("=");
    sB.append(isSecure());

    if (resourceID != null) {
      sB.append(WSRPConstants.NEXT_PARAM);
      sB.append(WSRPConstants.WSRP_RESOURCE_ID);
      sB.append("=");
      sB.append(resourceID);
    }
    if (cacheLevel != null) {
      sB.append(WSRPConstants.NEXT_PARAM);
      sB.append(WSRPConstants.WSRP_CACHELEVEL);
      sB.append("=");
      sB.append(cacheLevel);
    }
    sB.append(WSRPConstants.WSRP_REWRITE_SUFFFIX);

    Set names = parameters.keySet();
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
