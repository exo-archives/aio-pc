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
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net .
 */
public class CustomWindowStateWithDescription {

  /**
   * Window state.
   */
  private final javax.portlet.WindowState windowState;

  /**
   * Descriptions.
   */
  private final List<LocalisedDescription> descriptions;

  /**
   * @param windowState window state
   * @param descriptions descriptions
   */
  public CustomWindowStateWithDescription(final javax.portlet.WindowState windowState,
      final List<LocalisedDescription> descriptions) {
    this.windowState = windowState;
    this.descriptions = descriptions;
  }

  /**
   * @return a List of LocalisedDescription objects
   */
  public final List<LocalisedDescription> getDescriptions() {
    return descriptions;
  }

  /**
   * @return window state
   */
  public final javax.portlet.WindowState getWindowState() {
    return windowState;
  }

  /**
   * Description class.
   */
  public class Description {

    /**
     * Language.
     */
    private final String lang;

    /**
     * Description.
     */
    private final String description;

    /**
     * @param lang language
     * @param description description
     */
    public Description(final String lang, final String description) {
      this.lang = lang;
      this.description = description;
    }

    /**
     * @return description
     */
    public final String getDescription() {
      return description;
    }

    /**
     * @return language
     */
    public final String getLang() {
      return lang;
    }

  }

}
