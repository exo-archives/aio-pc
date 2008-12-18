/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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

/*
 * Created by IntelliJ IDEA.
 * User: azer
 * Date: 25 janv. 2004
 * Time: 17:47:02
 */
package org.exoplatform.services.wsrp.producer;

import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpSession;
import org.exoplatform.services.wsrp.type.CacheControl;
import org.exoplatform.services.wsrp.type.Templates;
import org.exoplatform.services.wsrp.type.UserContext;

public interface TransientStateManager {

  public static int SESSION_TIME_PERIOD = 900; // seconds

  public WSRPHttpSession resolveSession(String sessionID, String user, Integer sessiontimeperiod) throws WSRPException;

  public void releaseSession(String sessionID);

  public CacheControl getCacheControl(PortletData portletDatas) throws WSRPException;

  public boolean validateCache(String validateTag) throws WSRPException;

  public Templates getTemplates(WSRPHttpSession session);

  public void storeTemplates(Templates templates, WSRPHttpSession session);

  public UserContext resolveUserContext(UserContext userContext, WSRPHttpSession session);
}
