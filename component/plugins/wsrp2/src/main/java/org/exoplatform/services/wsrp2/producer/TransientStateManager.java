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

package org.exoplatform.services.wsrp2.producer;

import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.intf.InvalidSession;
import org.exoplatform.services.wsrp2.producer.impl.helpers.WSRPHttpSession;
import org.exoplatform.services.wsrp2.type.CacheControl;
import org.exoplatform.services.wsrp2.type.Templates;
import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * The Interface TransientStateManager.
 * 
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 */

public interface TransientStateManager {

  /** The SESSIO n_ tim e_ period. */
  public static int SESSION_TIME_PERIOD = 900; // seconds

  /**
   * Resolve session.
   * 
   * @param sessionID the session id
   * @param sessiontimeperiod the sessiontimeperiod
   * 
   * @return the wSRP http session
   * 
   * @throws InvalidSession the invalid session
   */
  public WSRPHttpSession resolveSession(String sessionID, Integer sessiontimeperiod) throws InvalidSession;

  /**
   * Release session.
   * 
   * @param sessionID the session id
   */
  public void releaseSession(String sessionID);

  /**
   * Gets the cache control.
   * 
   * @param portletDatas the portlet datas
   * 
   * @return the cache control
   * 
   * @throws WSRPException the WSRP exception
   */
  public CacheControl getCacheControl(PortletData portletDatas) throws WSRPException;

  /**
   * Validate cache.
   * 
   * @param validateTag the validate tag
   * 
   * @return true, if successful
   * 
   * @throws WSRPException the WSRP exception
   */
  public boolean validateCache(String validateTag) throws WSRPException;

  /**
   * Gets the templates.
   * 
   * @param session the session
   * 
   * @return the templates
   */
  public Templates getTemplates(WSRPHttpSession session);

  /**
   * Store templates.
   * 
   * @param templates the templates
   * @param session the session
   */
  public void storeTemplates(Templates templates, WSRPHttpSession session);

  /**
   * Resolve user context.
   * 
   * @param userContext the user context
   * @param session the session
   * 
   * @return the user context
   */
  public UserContext resolveUserContext(UserContext userContext, WSRPHttpSession session);
}
