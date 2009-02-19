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

import org.exoplatform.services.wsrp2.intf.WSRPV2MarkupPortType;
import org.exoplatform.services.wsrp2.intf.WSRPV2PortletManagementPortType;

/**
 * WSRP portlet services are all wsrp service interfaces which are session
 * dependent. With the implemented version 0.91 of the WSRP specification the
 * markup interface and the portlet management interface depend on using the
 * same session cookie in an clustered environment. <p/> This interface provides
 * a vehicle to store and retrieve the portTypes of these interfaces.
 * 
 * @author Stephan Laertz
 * @author Benjamin Mestrallet
 */
public interface PortletServices {

  /**
   * Set the portType object of the portlet management service.
   * 
   * @param portletManagementPortType Port type object which describes the
   *          operations of the producers portlet mamagement service endpoint
   */
  public void setWSRPPortletManagementService(WSRPV2PortletManagementPortType portletManagementPortType);

  /**
   * Get a port type object describing the operations of a produces portlet
   * management service endpoint.
   * 
   * @return Portlet management port type object
   */
  public WSRPV2PortletManagementPortType getWSRPPortletManagementService();

  /**
   * Set the portType object of the markup service.
   * 
   * @param markupPortType Port type object which describes the operations of
   *          the producers markup service endpoint
   */
  public void setWSRPMarkupService(WSRPV2MarkupPortType markupPortType);

  /**
   * Get a port type object describing the operations of a produces markup
   * service endpoint.
   * 
   * @return Markup port type object
   */
  public WSRPV2MarkupPortType getWSRPMarkupService();

  /**
   * Get the URL of the producers markup and portlet management service
   * interface. Since both services can depend on a producer session they need
   * to have the same accessPoint.
   * 
   * @return The URL of the producers markup and portlet management service
   *         interface.
   */
  public String getPortletServicesURL();
}
