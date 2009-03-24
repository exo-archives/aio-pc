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

package org.exoplatform.services.wsrp2.producer.impl.urls;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;

import org.exoplatform.services.portletcontainer.pci.PortletURLFactory;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.wsrp2.producer.PersistentStateManager;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHTTPContainer;
import org.exoplatform.services.wsrp2.producer.impl.urls.ws1.ProducerRewriterPortletURLImp1;
import org.exoplatform.services.wsrp2.producer.impl.urls.ws1.ProducerRewriterResourceURLImp1;
import org.exoplatform.services.wsrp2.producer.impl.urls.ws2.ProducerRewriterPortletURLImp2;
import org.exoplatform.services.wsrp2.producer.impl.urls.ws2.ProducerRewriterResourceURLImp2;
import org.exoplatform.services.wsrp2.type.Templates;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public class WSRPProducerRewriterPortletURLFactory implements PortletURLFactory {

  private String                 sessionID;

  private String                 portletHandle;

  private Templates              templates;

  private boolean                isCurrentlySecured;

  private PersistentStateManager persistentStateManager;

  private boolean                defaultEscapeXml;

  private List<Supports>         supports;                                                // only for PortletURL

  private String                 mimeType;                                                // only for PortletURL

  private String                 cacheLevel;                                              // only for ResourceURL

  private List<String>           supportedPublicRenderParameter = new ArrayList<String>();

  private Portlet                portlet;

  private int                    version;

  private String                 user;

  public WSRPProducerRewriterPortletURLFactory(String mimeType,
                                               Templates templates,
                                               List<Supports> supports,
                                               boolean isCurrentlySecured,
                                               String portletHandle,
                                               PersistentStateManager persistentStateManager,
                                               String sessionID,
                                               boolean defaultEscapeXml,
                                               String cacheLevel,
                                               List<String> supportedPublicRenderParameter,
                                               Portlet portlet,
                                               String user) {
    this.mimeType = mimeType;
    this.supports = supports;
    this.isCurrentlySecured = isCurrentlySecured;
    this.templates = templates;
    this.portletHandle = portletHandle;
    this.persistentStateManager = persistentStateManager;
    this.sessionID = sessionID;
    this.defaultEscapeXml = defaultEscapeXml;
    this.cacheLevel = cacheLevel;
    this.supportedPublicRenderParameter = supportedPublicRenderParameter;
    this.portlet = portlet;
    this.user = user;
    
    this.version = WSRPHTTPContainer.getInstance().getVersion();
  }

  public PortletURL createPortletURL(String type) {
    if (version == 1) {
      return new ProducerRewriterPortletURLImp1(type,
                                                templates,
                                                mimeType,
                                                supports,
                                                isCurrentlySecured,
                                                portletHandle,
                                                persistentStateManager,
                                                sessionID,
                                                user);
    } else {
      return new ProducerRewriterPortletURLImp2(type,
                                               templates,
                                               mimeType,
                                               supports,
                                               isCurrentlySecured,
                                               portletHandle,
                                               persistentStateManager,
                                               sessionID,
                                               defaultEscapeXml,
                                               supportedPublicRenderParameter,
                                               portlet,
                                               user);
    }
  }

  public ResourceURL createResourceURL(String type) {
    if (version == 1) {
      return new ProducerRewriterResourceURLImp1(type,
                                                 templates,
                                                 isCurrentlySecured,
                                                 portletHandle,
                                                 persistentStateManager,
                                                 sessionID,
                                                 user);
    } else {
      return new ProducerRewriterResourceURLImp2(type,
                                                templates,
                                                isCurrentlySecured,
                                                portletHandle,
                                                persistentStateManager,
                                                sessionID,
                                                defaultEscapeXml,
                                                cacheLevel,
                                                supportedPublicRenderParameter,
                                                portlet,
                                                user);
    }
  }

}
