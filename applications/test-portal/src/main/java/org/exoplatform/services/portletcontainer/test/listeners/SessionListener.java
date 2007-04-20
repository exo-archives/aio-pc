/**
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.portletcontainer.test.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;

/**
 * Created by The eXo Platform SARL
 * Author : Roman Pedchenko
 *          roman.pedchenko@exoplatform.com.ua
 * Apr 20, 2007  
 */

public class SessionListener implements HttpSessionListener {
  
  public void sessionCreated(HttpSessionEvent se) {
    WindowInfosContainer.createInstance(ExoContainerContext.getCurrentContainer(), se.getSession().getId(),
      se.getSession().getRemoteUser());
  }

  public void sessionDestroyed(HttpSessionEvent se) {
    WindowInfosContainer.removeInstance(ExoContainerContext.getCurrentContainer(), se.getSession().getId());
  }
  
}
