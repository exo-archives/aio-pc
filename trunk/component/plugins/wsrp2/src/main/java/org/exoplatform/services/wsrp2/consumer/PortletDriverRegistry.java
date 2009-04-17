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
 * This interface defines a registry which can be used to store portlet driver
 * objects.
 * 
 * @author Stephan Laertz
 * @author Benjamin Mestrallet
 */
public interface PortletDriverRegistry {

  /**
   * Get an portlet driver for the given portlet. If there is no portlet driver
   * object cached a new portlet driver will be created and returned.
   * 
   * @param portlet The portlet the returned portlet driver is bind to
   * @return The portlet driver for this portlet
   */
  public PortletDriver getPortletDriver(WSRPPortlet portlet) throws WSRPException;

  /**
   * Get all cached portlet drivers.
   * 
   * @return Iterator with all portlet drivers in the registry
   */
  public Iterator getAllPortletDrivers();
}
