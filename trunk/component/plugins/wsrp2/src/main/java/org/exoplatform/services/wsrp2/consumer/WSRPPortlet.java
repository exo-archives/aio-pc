/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp2.consumer;

import org.exoplatform.services.wsrp2.type.PortletContext;

/**
 * Defines a consumer-side representation of a remote portlet. A portlet is
 * uniquely identified by its portlet key. Consumer configured portlets are the
 * result of clone operations on existing portlets (parents).
 * 
 * @author Stephan Laertz
 * @author Benjamin Mestrallet
 */
public interface WSRPPortlet {

  /**
   * Get the portlet key of the portlet. The key can be used to reference to the
   * portlet.
   * 
   * @return a portlet key object
   */
  public PortletKey getPortletKey();

  /**
   * Set the portlet key of the portlet.
   * 
   * @param portletKey The portlet key of the portlet
   */
  public void setPortletKey(PortletKey portletKey);

  /**
   * Get the portlet context object which contains information about the portlet
   * state.
   * 
   * @return the portlet context object of the portlet.
   */
  public PortletContext getPortletContext();

  /**
   * Set the portlet context of the portlet.
   * 
   * @param portletContext The portlet context of the portlet
   */
  public void setPortletContext(PortletContext portletContext);

  /**
   * Checks if a portlet is consumer configured portlet.
   * 
   * @return True if the result <code>getParent()</code> is not equal to the
   *         portlet handle of the portlet key.
   */
  public boolean isConsumerConfigured();

  /**
   * Get the portlet handle of the parent portlet. If the portlet is not a
   * consumer configured portlet the handle returned by this method should be
   * the same as the handle in the portlet key returned by
   * <code>getPortletKey</code>.
   * 
   * @return the portlet handle of the parent portlet.
   */
  public String getParent();

  /**
   * Set the portlet handle of the parent portlet. If the supplied handle is not
   * equal to the handle in the portlet key returned by
   * <code>getPortletKey</code> this method makes the portlet a consumer
   * configured portlet.
   * 
   * @param portletHandle the portlet handle of the parent portlet
   */
  public void setParent(String portletHandle);

  /**
   * @return
   */
  public String getPortletHandle();

  /**
   * @param portletHandle
   */
  public void setPortletHandle(String portletHandle);

}
