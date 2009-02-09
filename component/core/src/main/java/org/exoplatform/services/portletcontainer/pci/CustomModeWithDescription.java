/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.pci;

import java.util.List;
import javax.portlet.PortletMode;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net .
 */
public class CustomModeWithDescription {

  /**
   * Portlet mode.
   */
  private final PortletMode portletMode;

  /**
   * Descriptions.
   */
  private final List<LocalisedDescription> descriptions;

  /**
   * @param portletMode portlet mode
   * @param descriptions descriptions
   */
  public CustomModeWithDescription(final PortletMode portletMode,
      final List<LocalisedDescription> descriptions) {
    this.portletMode = portletMode;
    this.descriptions = descriptions;
  }

  /**
   * @return a List of LocalisedDescription objects
   */
  public final List<LocalisedDescription> getDescriptions() {
    return descriptions;
  }

  /**
   * @return portlet mode object
   */
  public final PortletMode getPortletMode() {
    return portletMode;
  }

}
