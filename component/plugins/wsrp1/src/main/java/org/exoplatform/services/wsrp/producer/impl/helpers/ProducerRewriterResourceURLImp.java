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

// Do we need this implementation feature of JSR286 for WSRP1?
public class ProducerRewriterResourceURLImp extends ResourceURLImp {

  private String                 sessionID;

  private String                 portletHandle;

  private PersistentStateManager stateManager;

  public ProducerRewriterResourceURLImp(String type,
                                        String template,
                                        boolean isCurrentlySecured,
                                        String portletHandle,
                                        PersistentStateManager stateManager,
                                        String sessionID) {
    super(type, template, isCurrentlySecured, true, null);
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

    // process navigational state
    String navigationalState = IdentifierUtil.generateUUID(this);
    try {
      stateManager.putNavigationalState(navigationalState, parameters);
    } catch (WSRPException e) {
      e.printStackTrace();
    }

    String template = baseURL;
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_URL_TYPE + "}", type);
    if (resourceID != null) {
      template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_RESOURCE_ID + "}", resourceID);
    } else {
      template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_RESOURCE_ID + "}", "");
    }
    if (cacheLevel != null) {
      template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_CACHELEVEL + "}", cacheLevel);
    } else {
      template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_CACHELEVEL + "}", "");
    }
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_SECURE_URL + "}", secureInfo);
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_PORTLET_HANDLE + "}", portletHandle);
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "}", navigationalState);
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_SESSION_ID + "}", sessionID);

    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_URL + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_REQUIRES_REWRITE + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_INTERACTION_STATE + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_FRAGMENT_ID + "}", "");

    Set names = parameters.keySet();
    for (Iterator<String> iterator = names.iterator(); iterator.hasNext();) {
      String name = (String) iterator.next();
      Object obj = parameters.get(name);
      if (obj instanceof String) {
        String value = (String) obj;
        template += Constants.AMPERSAND;
        template += URLEncoder.encode(name);
        template += "=";
        template += URLEncoder.encode(value);
      } else {
        String[] values = (String[]) obj;
        for (int i = 0; i < values.length; i++) {
          template += Constants.AMPERSAND;
          template += URLEncoder.encode(name);
          template += "=";
          template += URLEncoder.encode(values[i]);
        }
      }
    }

    return template;
  }

}
