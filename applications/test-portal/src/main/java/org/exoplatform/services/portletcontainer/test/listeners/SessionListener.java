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
package org.exoplatform.services.portletcontainer.test.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.services.portletcontainer.test.filters.PortletFilter;

/**
 * Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @date 2007-04-20
 */

/**
 * Http session listener class is intended to catch session destroying and to remove window infos
 * container dedicated to given session.
 */
public class SessionListener implements HttpSessionListener {

  /**
   * Default constructor.
   *
   * @param se session event
   */
  public void sessionCreated(final HttpSessionEvent se) { }

  /**
   * Drops window info objects right before session is about to be destroyed.
   *
   * @param se session event
   */
  public final void sessionDestroyed(final HttpSessionEvent se) {
    WindowInfosContainer.removeInstance(ExoContainerContext.getCurrentContainer(), se.getSession().getId());
    PortletFilter.FRAMEWORKS.remove(se.getSession().getId());
  }

}
