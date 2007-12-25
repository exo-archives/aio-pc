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

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class CustomWindowStateWithDescription {

  private javax.portlet.WindowState  windowState;

  private List<LocalisedDescription> descriptions;

  public CustomWindowStateWithDescription(javax.portlet.WindowState windowState, List descriptions) {
    this.windowState = windowState;
    this.descriptions = descriptions;
  }

  /**
   * @return a List of LocalisedDescription objects
   */
  public List<LocalisedDescription> getDescriptions() {
    return descriptions;
  }

  public javax.portlet.WindowState getWindowState() {
    return windowState;
  }

  public class Description {
    private String lang;

    private String description;

    public Description(String lang, String description) {
      this.lang = lang;
      this.description = description;
    }

    public String getDescription() {
      return description;
    }

    public String getLang() {
      return lang;
    }

  }

}
