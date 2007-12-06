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

package org.exoplatform.services.wsrp.consumer.templates;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import org.exoplatform.services.wsrp.consumer.PortletSession;
import org.exoplatform.services.wsrp.consumer.PortletWindowSession;
import org.exoplatform.services.wsrp.type.SessionContext;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 9 févr. 2004
 * Time: 15:50:57
 */

public abstract class PortletSessionTemplate implements PortletSession{

  protected String portletHandle;
  private SessionContext sessionContext;
  protected Map portletWindowSessions = new HashMap();

  public String getPortletHandle() {
    return portletHandle;
  }

  public void setPortletHandle(String portletHandle) {
    this.portletHandle = portletHandle;
  }

  public SessionContext getSessionContext() {
    return sessionContext;
  }

  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  public abstract PortletWindowSession getPortletWindowSession(String windowID);

  public Iterator getAllPorletWindowSessions() {
    return portletWindowSessions.entrySet().iterator();
  }

  public PortletWindowSession removePortletWindowSession(String windowID) {
    portletWindowSessions.remove(windowID);
    return null;
  }

  public void removeAllPortletWindowSessions() {
    portletWindowSessions.clear();
  }


}