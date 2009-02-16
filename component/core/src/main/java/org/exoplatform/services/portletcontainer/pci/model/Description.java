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
 * @version: $Id: Description.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Description {

  /**
   * Language.
   */
  private String lang;

  /**
   * Description.
   */
  private String description;

  /**
   * @return description
   */
  public final String getDescription() {
    return description;
  }

  /**
   * @param description description
   */
  public final void setDescription(final String description) {
    this.description = description;
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

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (!(obj instanceof Description))
      return false;
    Description other = (Description) obj;
    if (this.description != other.description)
      return false;
    if (this.lang != other.lang)
      return false;
    return true;
  }
}
