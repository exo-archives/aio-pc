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
public class LayoutPlt extends LayoutItem {

  /**
   * @param app portlet application name
   * @param name portlet name
   * @param id window id
   */
  public LayoutPlt(String app, String name, String id) {
    super(name);
    this.app = app;
    this.id = id;
  }

  /**
   * Portlet application name.
   */
  private String app;

  /**
   * Portlet window ID.
   */
  private String id;

  /**
   * @return the app
   */
  public String getApp() {
    return app;
  }

  /**
   * @param app the app to set
   */
  public void setApp(String app) {
    this.app = app;
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

}
