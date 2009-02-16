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

/**
 * Defines the information which uniquely identifies an portlet provided by a
 * producer. The portlet key does not identify a use of an portlet
 * 
 * @author <a href='mailto:Stephan.Laertz@de.ibm.com'>Stephan Laertz</a>
 * @author Benjamin Mestrallet
 */
public interface PortletKey {

  /**
   * Get the portlet handle which identifies an portlet in the scope of one
   * producer
   * 
   * @return The portlet handle
   */
  public String getPortletHandle();

  /**
   * Set the portlet handle which identifies an portlet in the scope of one
   * producer
   * 
   * @param portletHandle The portlet handle
   */
  public void setPortletHandle(String portletHandle);

  /**
   * Get the ID of the producer providing the portlet
   * 
   * @return The ID of the producer
   */
  public String getProducerId();

  /**
   * Set the ID of the producer providing the portlet
   * 
   * @param id The ID of the producer
   */
  public void setProducerId(String id);
}
