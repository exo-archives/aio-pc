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

package org.exoplatform.services.wsrp2.consumer.adapters;

import org.exoplatform.services.wsrp2.consumer.PortletKey;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 8 févr. 2004
 * Time: 22:56:54
 */

public class PortletKeyAdapter implements PortletKey {

  /**
   * Portlet handle.
   */
  private String portletHandle;

  /**
   * Producer ID.
   */
  private String producerId;

  /**
   * Get portlet handle.
   * @return portletHandle
   */
  public String getPortletHandle() {
    return portletHandle;
  }

  /**
   * Set portlet handle.
   * @param portletHandle String
   */
  public void setPortletHandle(String portletHandle) {
    this.portletHandle = portletHandle;
  }

  /**
   * Get producer ID.
   * @return producerId
   */
  public String getProducerId() {
    return producerId;
  }

  /**
   * Set producer ID.
   * @parem producerId String
   */
  public void setProducerId(String producerId) {
    this.producerId = producerId;
  }

}
