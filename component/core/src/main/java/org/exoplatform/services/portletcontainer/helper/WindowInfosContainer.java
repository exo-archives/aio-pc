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
package org.exoplatform.services.portletcontainer.helper;

import java.util.HashMap;

import org.exoplatform.container.ExoContainer;

/**
 * Created by The eXo Platform SAS
 * Author : Roman Pedchenko
 *          roman.pedchenko@exoplatform.com.ua
 * Apr 19, 2007
 */

public class WindowInfosContainer extends HashMap <Object, Object> {

  private static ThreadLocal threadLocal_ = new ThreadLocal();
  private static HashMap map = new HashMap();
  private String id;
  private String owner;

  public WindowInfosContainer(String id, String owner) {
    this.id = id;
  }

  public String getId() { return id; }

  public String getOwner() { return owner; }

  final public void addInfos(String key, PortletWindowInternal obj) { put(key, obj); }

  final public PortletWindowInternal getInfos(String key) { return (PortletWindowInternal) get(key); }

  static public WindowInfosContainer getInstance() { return (WindowInfosContainer) threadLocal_.get(); }

  static public void setInstance(WindowInfosContainer scontainer) { threadLocal_.set(scontainer); }

  static public void createInstance(ExoContainer cnt, String id, String owner) {
    String key = "WINDOW_INFOS_CONTAINER_KEY_ENCODER" + cnt.getContext().getName() + id;
    if (map.get(key) != null)
      threadLocal_.set(map.get(key));
    else {
      threadLocal_.set(new WindowInfosContainer(id, owner));
      map.put(key, threadLocal_.get());
    }
  }

  static public void removeInstance(ExoContainer cnt, String id) {
    String key = "WINDOW_INFOS_CONTAINER_KEY_ENCODER" + cnt.getContext().getName() + id;
    if (map.get(key) != null)
      map.remove(key);
  }

}
