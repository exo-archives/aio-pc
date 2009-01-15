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
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;

import org.exoplatform.services.portletcontainer.pci.PortletURLFactory;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.producer.impl.helpers.ws1.ProducerRewriterPortletURLImp1;
import org.exoplatform.services.wsrp2.producer.impl.helpers.ws1.ProducerRewriterResourceURLImp1;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRPProducerRewriterPortletURLFactory implements PortletURLFactory {

  private String                 sessionID;

  private String                 portletHandle;

  private String                 template;

  private boolean                isCurrentlySecured;

  private PersistentStateManager persistentStateManager;

  private boolean                defaultEscapeXml;

  private List<Supports>         supports;                                                // only for PortletURL

  private String                 mimeType;                                                // only for PortletURL

  private String                 cacheLevel;                                              // only for ResourceURL

  private List<String>           supportedPublicRenderParameter = new ArrayList<String>();

  private Portlet                portlet;

  private int                    version;

  public WSRPProducerRewriterPortletURLFactory(String mimeType,
                                               String template,
                                               List<Supports> supports,
                                               boolean isCurrentlySecured,
                                               String portletHandle,
                                               PersistentStateManager persistentStateManager,
                                               String sessionID,
                                               boolean defaultEscapeXml,
                                               String cacheLevel,
                                               List<String> supportedPublicRenderParameter,
                                               Portlet portlet) {
    this.mimeType = mimeType;
    this.supports = supports;
    this.isCurrentlySecured = isCurrentlySecured;
    this.template = template;
    this.portletHandle = portletHandle;
    this.persistentStateManager = persistentStateManager;
    this.sessionID = sessionID;
    this.defaultEscapeXml = defaultEscapeXml;
    this.cacheLevel = cacheLevel;
    this.supportedPublicRenderParameter = supportedPublicRenderParameter;
    this.portlet = portlet;
    this.version = WSRPHTTPContainer.getInstance().getVersion();
  }

  public PortletURL createPortletURL(String type) {
    if (version == 1) {
      return new ProducerRewriterPortletURLImp1(type,
                                                template,
                                                mimeType,
                                                supports,
                                                isCurrentlySecured,
                                                portletHandle,
                                                persistentStateManager,
                                                sessionID);
    } else {
      return new ProducerRewriterPortletURLImp(type,
                                               template,
                                               mimeType,
                                               supports,
                                               isCurrentlySecured,
                                               portletHandle,
                                               persistentStateManager,
                                               sessionID,
                                               defaultEscapeXml,
                                               supportedPublicRenderParameter,
                                               portlet);
    }
  }

  public ResourceURL createResourceURL(String type) {
    if (version == 1) {
      return new ProducerRewriterResourceURLImp1(type,
                                                 template,
                                                 isCurrentlySecured,
                                                 portletHandle,
                                                 persistentStateManager,
                                                 sessionID);
    } else {
      return new ProducerRewriterResourceURLImp(type,
                                                template,
                                                isCurrentlySecured,
                                                portletHandle,
                                                persistentStateManager,
                                                sessionID,
                                                defaultEscapeXml,
                                                cacheLevel,
                                                supportedPublicRenderParameter,
                                                portlet);
    }
  }

}
