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
package org.exoplatform.services.portletcontainer.pci.model;

/**
 * Jul 7, 2004 .
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: DisplayName.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class DisplayName {

  /**
   * Language.
   */
  private String lang;

  /**
   * Display name.
   */
  private String displayName;

  /**
   * @return display name
   */
  public final String getDisplayName() {
    return displayName;
  }

  /**
   * @param name name
   */
  public final void setDisplayName(final String name) {
    this.displayName = name;
  }

  /**
   * @return language
   */
  public final String getLang() {
    return lang;
  }

  /**
   * @param lang language
   */
  public final void setLang(final String lang) {
    this.lang = lang;
  }
}
