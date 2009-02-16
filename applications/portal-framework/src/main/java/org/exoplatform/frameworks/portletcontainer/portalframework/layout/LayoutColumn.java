/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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
package org.exoplatform.frameworks.portletcontainer.portalframework.layout;

/**
 * Created by The eXo Platform SAS .
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: $
*/
public class LayoutColumn extends LayoutNode {

  /**
   * Column width in percents.
   */
  private String width = "0";

  /**
   * Column id.
   */
  private String id = "";

  /**
   * Is the column default.
   */
  private boolean defaultCol = false;

  /**
   * @param width column width in percents
   * @param string
   */
  public LayoutColumn(String id, String width, boolean def) {
    super("col");
    this.id = id;
    this.width = width;
    this.defaultCol = def;
  }

  /**
   * @return the width
   */
  public String getWidth() {
    return (width != null) ? width : "";
  }

  /**
   * @param width the width to set
   */
  public void setWidth(String width) {
    this.width = width;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the defaultCol
   */
  public boolean isDefaultCol() {
    return defaultCol;
  }

  /**
   * @param defaultCol the defaultCol to set
   */
  public void setDefaultCol(boolean defaultCol) {
    this.defaultCol = defaultCol;
  }

}
