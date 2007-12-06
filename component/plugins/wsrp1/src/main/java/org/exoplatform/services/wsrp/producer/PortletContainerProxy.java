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

package org.exoplatform.services.wsrp.producer;

import java.util.Collection;
import java.util.Map;

import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpServletRequest;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpServletResponse;
import org.exoplatform.services.wsrp.type.PortletDescription;
import org.exoplatform.services.wsrp.type.PropertyList;
import org.exoplatform.services.wsrp.type.ResourceList;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public interface PortletContainerProxy {

  public boolean isPortletOffered(String portletHandle);

  public ResourceList getResourceList(String[] desiredLocales);

  public PortletDescription getPortletDescription(String portletHandle,
                                                  String[] desiredLocales);

  public void setPortletProperties(String portletHandle,
                                   String userID,
                                   PropertyList propertyList) throws WSRPException;

  public Map getPortletProperties(String portletHandle,
                                  String userID) throws WSRPException;

  public Map getAllPortletMetaData();

  // public Collection getWindowStates(String s);
  public Collection getSupportedWindowStates();

  public RenderOutput render(WSRPHttpServletRequest request,
                             WSRPHttpServletResponse response,
                             RenderInput input) throws WSRPException;

  public ActionOutput processAction(WSRPHttpServletRequest request,
                                    WSRPHttpServletResponse response,
                                    ActionInput input) throws WSRPException;

  // public Collection getSupportedPortletModesWithDescriptions();
  // public Collection getSupportedWindowStatesWithDescriptions();

}
