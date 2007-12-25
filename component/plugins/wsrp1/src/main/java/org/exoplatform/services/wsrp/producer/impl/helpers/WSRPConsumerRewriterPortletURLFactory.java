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

import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;

import org.exoplatform.services.portletcontainer.pci.PortletURLFactory;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;
import org.exoplatform.services.wsrp.utils.Utils;

/**
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 23 janv. 2004
 * Time: 18:20:54
 */
public class WSRPConsumerRewriterPortletURLFactory implements PortletURLFactory {

  private String                 markup;

  private String                 sessionID;

  private boolean                isCurrentlySecured;

  private PersistentStateManager stateManager;

  private String                 portletHandle;

  private String                 template;

  private List<Supports>         supports;

  public WSRPConsumerRewriterPortletURLFactory(String markup,
                                               String template,
                                               List<Supports> supports,
                                               boolean isCurrentlySecured,
                                               String portletHandle,
                                               PersistentStateManager stateManager,
                                               String sessionID) {
    this.markup = markup;
    this.supports = supports;
    this.isCurrentlySecured = isCurrentlySecured;
    this.template = template;
    this.portletHandle = portletHandle;
    this.stateManager = stateManager;
    this.sessionID = sessionID;
  }

  public PortletURL createPortletURL(String type) {
    return new ConsumerRewriterPortletURLImp(Utils.changeUrlTypeFromJSRPortletToWSRP(type),
                                             template,
                                             markup,
                                             supports,
                                             isCurrentlySecured,
                                             portletHandle,
                                             stateManager,
                                             sessionID);
  }

  public ResourceURL createResourceURL(String type) {
    return new ConsumerRewriterResourceURLImp(Utils.changeUrlTypeFromJSRPortletToWSRP(type),
                                              template,
                                              isCurrentlySecured,
                                              portletHandle,
                                              stateManager,
                                              sessionID);
  }

}
