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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by The eXo Platform SAS .
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: $
*/
public class LayoutNode extends LayoutItem {

  /**
   * Child nodes.
   */
  private List<LayoutItem> children = new ArrayList<LayoutItem>();

  /**
   * @param name node name
   */
  public LayoutNode(String name) {
    super(name);
  }

  public void addChild(LayoutItem child) {
    children.add(child);
  }

  public List<LayoutItem> getChildren() {
    if (children != null)
      return Collections.unmodifiableList(children);
    return null;
  }

  public void setChildren(List<LayoutItem> children) {
    this.children = children;
  }

  public void removeChild(LayoutItem item) {
    children.remove(item);
  }

}
