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

/**
 * Created by The eXo Platform SAS Author : Mestrallet Benjamin .
 */
public class RenderInput extends Input {

  /**
   * Title.
   */
  private String title;

  /**
   * Update cache.
   */
  private boolean updateCache;

  /**
   * @return title
   */
  public final String getTitle() {
    return title;
  }

  /**
   * For UIPortletLifecycle.processRender, the title getting from UIPortlet.
   *
   * @param title title
   */
  public final void setTitle(final String title) {
    this.title = title;
  }

  /**
   * @return update cache
   */
  public final boolean isUpdateCache() {
    return updateCache;
  }

  /**
   * @param updateCache update cache
   */
  public final void setUpdateCache(final boolean updateCache) {
    this.updateCache = updateCache;
  }

}
