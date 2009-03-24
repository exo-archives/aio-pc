/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

package org.exoplatform.services.wsrp2.producer.impl.urls.ws1;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletURLImp;
import org.exoplatform.services.wsrp2.WSRPConstants;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.type.Templates;
import org.exoplatform.services.wsrp2.utils.Modes;
import org.exoplatform.services.wsrp2.utils.TemplatesFactory;
import org.exoplatform.services.wsrp2.utils.URLUtils;
import org.exoplatform.services.wsrp2.utils.WindowStates;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class ProducerRewriterPortletURLImp1 extends PortletURLImp {

  private String                 sessionID;

  private String                 portletHandle;

  private PersistentStateManager stateManager;

  private Templates              templates;

  private String                 user;

  public ProducerRewriterPortletURLImp1(String type,
                                        Templates templates,
                                        String mimeType,
                                        List<Supports> supports,
                                        boolean isCurrentlySecured,
                                        String portletHandle,
                                        PersistentStateManager persistentStateManager,
                                        String sessionID,
                                        String user) {
    super(type, null, mimeType, supports, isCurrentlySecured, true, null);
    this.portletHandle = portletHandle;
    this.stateManager = persistentStateManager;
    this.sessionID = sessionID;
    this.templates = templates;
    this.user = user;
  }

  public String toString() {
    String secureInfo = "false";
    if (!isSetSecureCalled() && isCurrentlySecured()) {
      setSecure(true);
      secureInfo = "true";
    }

    String navigationalState = IdentifierUtil.generateUUID(this);
    try {
      stateManager.putNavigationalState(navigationalState, parameters);
    } catch (WSRPException e) {
      e.printStackTrace();
    }

    String template = TemplatesFactory.getTemplate(templates,
                                                   isSecure(),
                                                   URLUtils.getWSRPType(getType()));

    template = StringUtils.replace(template,
                                   "{" + WSRPConstants.WSRP_URL_TYPE + "}",
                                   URLUtils.getWSRPType(getType()));

    // WSRP_MODE 
    if (requiredPortletMode != null) {
      template = StringUtils.replace(template,
                                     "{" + WSRPConstants.WSRP_MODE + "}",
                                     Modes.addPrefixWSRP(requiredPortletMode.toString()));
    } else {
      template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_MODE + "}", "");
    }

    // WSRP_WINDOW_STATE
    if (requiredWindowState != null) {
      template = StringUtils.replace(template,
                                     "{" + WSRPConstants.WSRP_WINDOW_STATE + "}",
                                     WindowStates.addPrefixWSRP(requiredWindowState.toString()));
    } else {
      template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_WINDOW_STATE + "}", "");
    }

    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_SECURE_URL + "}", secureInfo);
    template = StringUtils.replace(template,
                                   "{" + WSRPConstants.WSRP_PORTLET_HANDLE + "}",
                                   portletHandle);
    template = StringUtils.replace(template,
                                   "{" + WSRPConstants.WSRP_NAVIGATIONAL_STATE + "}",
                                   navigationalState);
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_SESSION_ID + "}", sessionID);

    template = StringUtils.replace(template,
                                   "{" + WSRPConstants.WSRP_PORTLET_INSTANCE_KEY + "}",
                                   "");
    template = StringUtils.replace(template,
                                   "{" + WSRPConstants.WSRP_USER_CONTEXT_KEY + "}",
                                   user != null ? user : "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_URL + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_REQUIRES_REWRITE + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_INTERACTION_STATE + "}", "");
    template = StringUtils.replace(template, "{" + WSRPConstants.WSRP_FRAGMENT_ID + "}", "");

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
