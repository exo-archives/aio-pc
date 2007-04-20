/**
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.helper;

import java.util.HashMap;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;

/**
 * Created by The eXo Platform SARL
 * Author : Roman Pedchenko
 *          roman.pedchenko@exoplatform.com.ua
 * Apr 19, 2007  
 */

public class WindowInfosContainer extends HashMap <Object, Object> {

  private static ThreadLocal threadLocal_ = new ThreadLocal();
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
    String key = "WINDOW_INFOS_CONTAINER_KEY_ENCODER" + id;
    if (cnt.getComponentInstance(key) != null)
      threadLocal_.set(cnt.getComponentInstance(key));
    else {
      threadLocal_.set(new WindowInfosContainer(id, owner));
      cnt.registerComponentInstance(key, threadLocal_.get());
    }
  }

  static public void removeInstance(ExoContainer cnt, String id) {
    String key = "WINDOW_INFOS_CONTAINER_KEY_ENCODER" + id;
    if (cnt.getComponentInstance(key) != null)
      cnt.unregisterComponent(key);
  }

}
