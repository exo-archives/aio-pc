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
 * Created by The eXo Platform SAS Author : Roman Pedchenko .
 * roman.pedchenko@exoplatform.com.ua Apr 19, 2007
 */
public class WindowInfosContainer extends HashMap<Object, Object> {

  /**
   * Internal storage of containers.
   */
  private static ThreadLocal<WindowInfosContainer> threadLocal = new ThreadLocal<WindowInfosContainer>();

  /**
   * Internal map of window infos.
   */
  private static HashMap<String, WindowInfosContainer> map = new HashMap<String, WindowInfosContainer>();

  /**
   * Id.
   */
  private final String id;

  /**
   * Owner.
   */
  private String owner;

  /**
   * @param id id
   * @param owner owner
   */
  public WindowInfosContainer(final String id, final String owner) {
    this.id = id;
  }

  /**
   * @return id
   */
  public final String getId() {
    return id;
  }

  /**
   * @return owner
   */
  public final String getOwner() {
    return owner;
  }

  /**
   * @param key key
   * @param obj infos
   */
  public final void addInfos(final String key, final PortletWindowInternal obj) {
    put(key, obj);
  }

  /**
   * @param key key
   * @return infos
   */
  public final PortletWindowInternal getInfos(final String key) {
    return (PortletWindowInternal) get(key);
  }

  /**
   * @return window infos container
   */
  public static WindowInfosContainer getInstance() {
    return threadLocal.get();
  }

  /**
   * @param scontainer window infos container
   */
  public static void setInstance(final WindowInfosContainer scontainer) {
    threadLocal.set(scontainer);
  }

  /**
   * @param cnt exo container
   * @param id id
   * @param owner owner portal
   */
  public static void createInstance(final ExoContainer cnt, final String id, final String owner) {
    String key = "WINDOW_INFOS_CONTAINER_KEY_ENCODER" + cnt.getContext().getName() + id;
    if (map.get(key) != null)
      threadLocal.set(map.get(key));
    else {
      threadLocal.set(new WindowInfosContainer(id, owner));
      map.put(key, threadLocal.get());
    }
  }

  
  /**
   * @param cnt exo container
   * @param id id
   */
  public static void removeInstance(final ExoContainer cnt, final String id) {
    String key = "WINDOW_INFOS_CONTAINER_KEY_ENCODER" + cnt.getContext().getName() + id;
    if (map.get(key) != null)
      map.remove(key);
  }

}
