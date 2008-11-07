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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class ProducerRewriterPortletURLImp
                                          extends
                                          org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletURLImp {

  private String                 sessionID;

  private String                 portletHandle;

  private PersistentStateManager stateManager;

  private List<String>           supportedPublicRenderParameter = new ArrayList<String>();

  public ProducerRewriterPortletURLImp(String type,
                                       String template,
                                       String markup,
                                       List<Supports> supports,
                                       boolean isCurrentlySecured,
                                       String portletHandle,
                                       PersistentStateManager stateManager,
                                       String sessionID,
                                       boolean defaultEscapeXml,
                                       List<String> supportedPublicRenderParameter,
                                       Portlet portlet) {
    super(type, template, markup, supports, isCurrentlySecured, defaultEscapeXml, portlet);
    this.portletHandle = portletHandle;
    this.stateManager = stateManager;
    this.sessionID = sessionID;
    this.supportedPublicRenderParameter = supportedPublicRenderParameter;
  }

  public String toString() {

    if (getType().equals(WSRPConstants.URL_TYPE_BLOCKINGACTION))
      invokeFilterActionURL();
    else
      invokeFilterRenderURL();

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

    String template = baseURL;
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_URL_TYPE + "}", getType());
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_FRAGMENT_ID + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_EXTENSIONS + "}", "");

    String secureInfo = "false";
    if (!isSetSecureCalled() && isCurrentlySecured()) {
      setSecure(true);
      secureInfo = "true";
    }
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_SECURE_URL + "}", secureInfo);

    if (requiredPortletMode != null) {
      template = StringUtils.replace(template,
                                     "{" + WSRPConstants.WSRP_MODE + "}",
                                     requiredPortletMode.toString());
    } else {
      template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_MODE + "}", "");
    }
    if (requiredWindowState != null) {
      template = StringUtils.replace(template,
                                     "{" + WSRPConstants.WSRP_WINDOW_STATE + "}",
                                     requiredWindowState.toString());
    } else {
      template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_WINDOW_STATE + "}", "");
    }

    // process navigational state
    String navigationalState = IdentifierUtil.generateUUID(this);
    try {
      stateManager.putNavigationalState(navigationalState, parameters);//was: privateParams
    } catch (WSRPException e) {
      e.printStackTrace();
    }
    template = StringUtils.replace(template,
                                   "{" + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "}",
                                   navigationalState);

    template = StringUtils.replace(template,
                                   "{" + WSRPConstants.WSRP_NAVIGATIONAL_VALUES + "}",
                                   encode(navigationalValuesString));

    // process interaction state
    if (getType().equalsIgnoreCase(PCConstants.ACTION_STRING)) {
      String interactionState = IdentifierUtil.generateUUID(this);
      try {
        stateManager.putInteractionState(interactionState, parameters);//was: privateParams
      } catch (WSRPException e) {
        e.printStackTrace();
      }
      template = StringUtils.replace(template,
                                     "{" + WSRPConstants.WSRP_INTERACTION_STATE + "}",
                                     interactionState);
    } else {
      template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_INTERACTION_STATE + "}", "");
    }

    //clear resource params 
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_URL + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_RESOURCE_ID + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_RESOURCE_STATE + "}", "");
    template = StringUtils.replace(template,
                                   "{" + WSRPConstants.WSRP_RESOURCE_CACHEABILITY + "}",
                                   "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_REQUIRES_REWRITE + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_PREFER_OPERATION + "}", "");

    // other parameters
    template = StringUtils.replace(template,
                                   "{" + WSRPConstants.WSRP_PORTLET_HANDLE + "}",
                                   portletHandle);
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "}", "");
    template = StringUtils.replace(template,
                                   "{" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "}",
                                   "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_SESSION_ID + "}", sessionID);
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_PAGE_STATE + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_PORTLET_STATES + "}", "");

    Collection<String> names = parameters.keySet();
    for (Iterator<String> iterator = names.iterator(); iterator.hasNext();) {
      String name = (String) iterator.next();
      Object obj = parameters.get(name);
      if (obj instanceof String) {
        String value = (String) obj;
        template += WSRPConstants.NEXT_PARAM;
        template += encode(name);
        template += "=";
        template += encode(value);
      } else {
        String[] values = (String[]) obj;
        for (int i = 0; i < values.length; i++) {
          template += WSRPConstants.NEXT_PARAM;
          template += encode(name);
          template += "=";
          template += encode(values[i]);
        }
      }
    }

    return template;
  }

}
