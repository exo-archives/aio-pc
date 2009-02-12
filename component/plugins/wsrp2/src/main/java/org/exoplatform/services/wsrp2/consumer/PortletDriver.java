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

import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.ReturnAny;

/**
 * The portlet driver is a wrapper for all action which can be performed on an
 * portlet. There is one portlet driver for all instances of an portlet.
 * 
 * @author Benjamin Mestrallet
 */
public interface PortletDriver {

  /**
   * Get the portlet this driver is bound to.
   * 
   * @return The enity
   */
  public WSRPPortlet getPortlet();

  /**
   * This method is used to retrieve the markup generated by the portlet
   * instance.
   * 
   * @return The markup response generated by portlet
   */
  public MarkupResponse getMarkup(WSRPMarkupRequest markupRequest,
                                  UserSessionMgr userSession,
                                  String path) throws WSRPException;

  /**
   * This method is used to perform a blocking interaction on the portlet
   * instance.
   */
  public BlockingInteractionResponse performBlockingInteraction(WSRPInteractionRequest actionRequest,
                                                                UserSessionMgr userSession,
                                                                String path) throws WSRPException;

  /**
   * Clone the portlet
   * 
   * @return The new portlet context
   */
  public PortletContext clonePortlet(UserSessionMgr userSession) throws WSRPException;

  /**
   *
   **/
  public void initCookie(UserSessionMgr userSession) throws WSRPException;

  /**
   * Destroy the producer portlets specified in the entiyHandles array.
   */
  public DestroyPortletsResponse destroyPortlets(List<String> portletHandles, UserSessionMgr userSession) throws WSRPException;

  /**
   * Inform the producer that the sessions specified in the sessionIDs array
   * will no longer be used by the consumer and can therefor be released.
   */
  public ReturnAny releaseSessions(List<String> sessionIDs, UserSessionMgr userSession) throws WSRPException;

  public PortletDescriptionResponse getPortletDescription(UserSessionMgr userSession,
                                                          List<String> desiredLocales) throws WSRPException;

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(UserSessionMgr userSession) throws WSRPException;

  public PropertyList getPortletProperties(List<String> names, UserSessionMgr userSession) throws WSRPException;

  public PortletContext setPortletProperties(PropertyList properties, UserSessionMgr userSession) throws WSRPException;

  // WSRP2 spec

  public ResourceResponse getResource(WSRPResourceRequest resourceRequest,
                                      UserSessionMgr userSession,
                                      String path) throws WSRPException;

  public HandleEventsResponse handleEvents(WSRPEventsRequest eventRequest,
                                           UserSessionMgr userSession,
                                           String path) throws WSRPException;

}
