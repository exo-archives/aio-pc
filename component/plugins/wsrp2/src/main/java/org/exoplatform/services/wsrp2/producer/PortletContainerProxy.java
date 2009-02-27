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

package org.exoplatform.services.wsrp2.producer;

import java.util.Collection;
import java.util.Map;

import javax.portlet.WindowState;

import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.EventInput;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpServletRequest;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpServletResponse;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.ResourceList;

// TODO: Auto-generated Javadoc
/**
 * The Interface PortletContainerProxy.
 * 
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */
public interface PortletContainerProxy {

  /**
   * Checks if is portlet offered.
   * 
   * @param portletHandle the portlet handle
   * 
   * @return true, if is portlet offered
   */
  public boolean isPortletOffered(String portletHandle);

  /**
   * Gets the resource list.
   * 
   * @param desiredLocales the desired locales
   * 
   * @return the resource list
   */
  public ResourceList getResourceList(String[] desiredLocales);

  /**
   * Gets the portlet description.
   * 
   * @param portletHandle the portlet handle
   * @param desiredLocales the desired locales
   * 
   * @return the portlet description
   */
  public PortletDescription getPortletDescription(String portletHandle, String[] desiredLocales);

  /**
   * Sets the portlet properties.
   * 
   * @param portletHandle the portlet handle
   * @param userID the user id
   * @param propertyList the property list
   * 
   * @throws WSRPException the WSRP exception
   */
  public void setPortletProperties(String portletHandle, String userID, PropertyList propertyList) throws WSRPException;

  /**
   * Gets the portlet properties.
   * 
   * @param portletHandle the portlet handle
   * @param userID the user id
   * 
   * @return the portlet properties
   * 
   * @throws WSRPException the WSRP exception
   */
  public Map<String, String[]> getPortletProperties(String portletHandle, String userID) throws WSRPException;

  /**
   * Gets the all portlet meta data.
   * 
   * @return the all portlet meta data
   */
  public Map<String, PortletData> getAllPortletMetaData();

  /**
   * Gets the supported window states.
   * 
   * @return the supported window states
   */
  public Collection<WindowState> getSupportedWindowStates();

  /**
   * Render.
   * 
   * @param request the request
   * @param response the response
   * @param input the input
   * 
   * @return the render output
   * 
   * @throws WSRPException the WSRP exception
   */
  public RenderOutput render(WSRPHttpServletRequest request,
                             WSRPHttpServletResponse response,
                             RenderInput input) throws WSRPException;

  /**
   * Process action.
   * 
   * @param request the request
   * @param response the response
   * @param input the input
   * 
   * @return the action output
   * 
   * @throws WSRPException the WSRP exception
   */
  public ActionOutput processAction(WSRPHttpServletRequest request,
                                    WSRPHttpServletResponse response,
                                    ActionInput input) throws WSRPException;

  /**
   * Serve resource.
   * 
   * @param request the request
   * @param response the response
   * @param input the input
   * 
   * @return the resource output
   * 
   * @throws WSRPException the WSRP exception
   */
  public ResourceOutput serveResource(WSRPHttpServletRequest request,
                                      WSRPHttpServletResponse response,
                                      ResourceInput input) throws WSRPException;

  /**
   * Process event.
   * 
   * @param request the request
   * @param response the response
   * @param input the input
   * 
   * @return the event output
   * 
   * @throws WSRPException the WSRP exception
   */
  public EventOutput processEvent(WSRPHttpServletRequest request,
                                  WSRPHttpServletResponse response,
                                  EventInput input) throws WSRPException;

}
