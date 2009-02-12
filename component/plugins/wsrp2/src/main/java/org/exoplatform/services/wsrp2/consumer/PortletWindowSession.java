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

import java.util.List;

import org.exoplatform.services.wsrp2.type.MarkupContext;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.ResourceContext;

/**
 * The portlet window session is a session which is unique for every window of a
 * portlet instance.
 */
public interface PortletWindowSession {

  /**
   * Get the ID of the portlets window this session belongs to.
   * 
   * @return The ID of the portlet window.
   */
  public String getWindowID();

  /**
   * Set the ID of the portlets window this sessions belongs to.
   * 
   * @param windowID The ID of the portlet window.
   */
  public void setWindowID(String windowID);

  /**
   * Get the markup context which has been cached. This might be useful to
   * retrieve the markup which was returned
   * <code>performBlockingInteraction</code> calls in order to save an
   * additional <code>getMarkup</code> call.
   * 
   * @return The cached markup context or null in case the cache is empty.
   */
  public MarkupContext getCachedMarkup();

  /**
   * Update the cache which holds the markup context. This might be useful to
   * store the markup which was returned by
   * <code>performBlockingInteraction</code> calls in order to save an
   * additional <code>getMarkup</code> call. Updateing the cache with a null
   * value clears the markup cache.
   */
  public void updateMarkupCache(MarkupContext markupContext);

  /**
   * Get the portlet session this window session belongs to.
   * 
   * @return The <code>PortletSession</code> this window session belongs to.
   */
  public PortletSession getPortletSession();

  public void setNavigationalState(String navigationalState);

  public String getNavigationalState();

  // WSRP 2

  public ResourceContext getCachedResource();

  public void updateResourceCache(ResourceContext resourceContext);

  public void setNavigationalValues(List<NamedString> navigationalValues);

  public List<NamedString> getNavigationalValues();

  public void setInteractionState(String interactionState);

  public String getInteractionState();

  public void setResourceState(String resourceState);

  public String getResourceState();

}
