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

package org.exoplatform.services.wsrp2.consumer.adapters;

import java.util.List;

import org.exoplatform.services.wsrp2.consumer.PortletSession;
import org.exoplatform.services.wsrp2.consumer.PortletWindowSession;
import org.exoplatform.services.wsrp2.type.MarkupContext;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.ResourceContext;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 9
 *         févr. 2004 Time: 15:49:27
 */

public class PortletWindowSessionAdapter implements PortletWindowSession {

  /**
   * Window ID.
   */
  private String            windowID;

  /**
   * Portlet session.
   */
  private PortletSession    portletSession;

  /**
   * Markup context.
   */
  private MarkupContext     markupContext;

  /**
   * Navigational state.
   */
  private String            navigationalState;

  /**
   * Resource context.
   */
  private ResourceContext   resourceContext;

  /**
   * Navigational values.
   */
  private List<NamedString> navigationalValues;

  /**
   * Interaction state.
   */
  private String            interactionState;

  /**
   * Resource state.
   */
  private String            resourceState;

  private String            mode;

  private String            windowState;

  /**
   * Get window ID.
   * 
   * @return windowID
   */
  public String getWindowID() {
    return windowID;
  }

  /**
   * Set window ID.
   * 
   * @param windowID String
   */
  public void setWindowID(String windowID) {
    this.windowID = windowID;
  }

  /**
   * Get markup context.
   * 
   * @return markupContext
   */
  public MarkupContext getCachedMarkup() {
    return markupContext;
  }

  /**
   * Set markup context.
   * 
   * @param markupContext MarkupContext
   */
  public void updateMarkupCache(MarkupContext markupContext) {
    this.markupContext = markupContext;
  }

  /**
   * Get portlet session.
   * 
   * @return portletSession
   */
  public PortletSession getPortletSession() {
    return portletSession;
  }

  /**
   * Set portlet session.
   * 
   * @param portletSession PortletSession
   */
  public void setPortletSession(PortletSession portletSession) {
    this.portletSession = portletSession;
  }

//  public MarkupContext getMarkupContext() {
//    return markupContext;
//  }
//
//  public void setMarkupContext(MarkupContext markupContext) {
//    this.markupContext = markupContext;
//  }

  /**
   * Get navigational state.
   * 
   * @return navigationalState
   */
  public String getNavigationalState() {
    return navigationalState;
  }

  /**
   * Set navigational state.
   * 
   * @param navigationalState String
   */
  public void setNavigationalState(String navigationalState) {
    this.navigationalState = navigationalState;
  }

  // WSRP 2

  /**
   * Get cached resource.
   * 
   * @return resourceContext
   */
  public ResourceContext getCachedResource() {
    return this.resourceContext;
  }

  /**
   * Update resource cache.
   * 
   * @param resourceContext ResourceContext
   */
  public void updateResourceCache(ResourceContext resourceContext) {
    this.resourceContext = resourceContext;
  }

  /**
   * Get resource context.
   * 
   * @return resourceContext
   */
  public ResourceContext getResourceContext() {
    return resourceContext;
  }

  /**
   * Set resource context.
   * 
   * @param resourceContext ResourceContext
   */
  public void setResourceContext(ResourceContext resourceContext) {
    this.resourceContext = resourceContext;
  }

  /**
   * Get navigational values.
   * 
   * @return navigationalValues
   */
  public List<NamedString> getNavigationalValues() {
    return navigationalValues;
  }

  /**
   * Set navigational values.
   * 
   * @param navigationalValues List
   */
  public void setNavigationalValues(List<NamedString> navigationalValues) {
    this.navigationalValues = navigationalValues;
  }

  /**
   * Get interaction state.
   * 
   * @return interactionState
   */
  public String getInteractionState() {
    return interactionState;
  }

  /**
   * Set interaction state.
   * 
   * @param interactionState String
   */
  public void setInteractionState(String interactionState) {
    this.interactionState = interactionState;
  }

  /**
   * Get resource state.
   * 
   * @return resourceState
   */
  public String getResourceState() {
    return resourceState;
  }

  /**
   * Set resource state.
   * 
   * @param resourceState String
   */
  public void setResourceState(String resourceState) {
    this.resourceState = resourceState;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getMode() {
    return mode;
  }

  public void setWindowState(String windowState) {
    this.windowState = windowState;
  }

  public String getWindowState() {
    return windowState;
  }

}
