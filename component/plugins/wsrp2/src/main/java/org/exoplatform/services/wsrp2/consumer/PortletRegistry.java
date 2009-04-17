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

package org.exoplatform.services.wsrp2.consumer;

import java.util.Iterator;

import org.exoplatform.services.wsrp2.exceptions.WSRPException;

/**
 * This interface defines a registry which holds portlet objects.
 * 
 * @author Stephan Laertz
 * @author Benjamin Mestrallet
 */
public interface PortletRegistry {

  /**
   * Add a portlet to the registry.
   * 
   * @param portlet The portlet to add
   */
  public void addPortlet(WSRPPortlet portlet) throws WSRPException;

  /**
   * Get the portlet for the given producer and portlet handle.
   * 
   * @param portletKey The portlet key identifying the portlet
   * @return The portlet with the given portlet key
   */
  public WSRPPortlet getPortlet(PortletKey portletKey);
  
  
  /**
   * Get the portlet.
   * 
   * @param producerID
   * @param portletHandle
   * @return
   */
  public WSRPPortlet getPortlet(String producerID, String portletHandle);
  
  /**
   * Get Portlet Key.
   * 
   * @param producerID
   * @param portletHandle
   * @return
   */
  public PortletKey getPortletKey(String producerID, String portletHandle);

  /**
   * Remove the portlet with the given portlet key.
   * 
   * @param portletKey The portlet key identifying the portlet
   * @return The portlet which has been removed or null
   */
  public WSRPPortlet removePortlet(PortletKey portletKey);

  /**
   * Tests if a portlet with the given portlet key.
   * 
   * @param portletKey The portlet key identifying the portlet
   * @return True if portlet exists with this portlet key
   */
  public boolean existsPortlet(PortletKey portletKey);

  /**
   * Get all the portlets in the register.
   * 
   * @return Iterator with all portlets in the registry
   */
  public Iterator<WSRPPortlet> getAllPortlets();

  /**
   * Remove all portlets from the registry.
   */
  public void removeAllPortlets();

}
