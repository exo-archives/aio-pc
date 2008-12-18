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

package org.exoplatform.services.wsrp1.consumer.impl.helpers;

import org.exoplatform.services.wsrp.consumer.PortletSessionTemplate;
import org.exoplatform.services.wsrp.consumer.PortletWindowSessionAdapter;
import org.exoplatform.services.wsrp1.consumer.PortletWindowSession;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 9 févr. 2004
 * Time: 22:39:52
 */

public class PortletSessionImpl extends PortletSessionTemplate implements java.io.Serializable {

  public PortletSessionImpl(String portletHandle) {
    super.portletHandle = portletHandle;
  }

  public PortletWindowSession getPortletWindowSession(String windowID) {
    PortletWindowSession session = (PortletWindowSession) this.portletWindowSessions.get(windowID);
    if (session == null) {
      session = new PortletWindowSessionAdapter();
      ((PortletWindowSessionAdapter) session).setWindowID(windowID);
      ((PortletWindowSessionAdapter) session).setPortletSession(this);
      this.portletWindowSessions.put(windowID, session);
    }
    return session;
  }

}
