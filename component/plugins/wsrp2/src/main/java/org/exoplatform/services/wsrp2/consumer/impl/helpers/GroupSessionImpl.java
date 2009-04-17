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

package org.exoplatform.services.wsrp2.consumer.impl.helpers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.exoplatform.services.wsrp2.consumer.GroupSessionMgr;
import org.exoplatform.services.wsrp2.consumer.PortletSession;

/**
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 9
 *         févr. 2004 Time: 22:34:33
 */

public class GroupSessionImpl extends InitCookieImpl implements GroupSessionMgr, Serializable {

  /**
   * 
   */
  private static final long             serialVersionUID = 3380422934423030970L;

  protected String                      groupID;

  protected Map<String, PortletSession> portletSessions  = new HashMap<String, PortletSession>();

  public GroupSessionImpl() {
  }

  public GroupSessionImpl(String groupID, String producerID) {
    super(producerID);
    this.groupID = groupID;
  }

  public PortletSession getPortletSession(String portletHandle) {
    if (portletHandle == null) {
      return null;
    }
    PortletSession portletSession = (PortletSession) this.portletSessions.get(portletHandle);
    if (portletSession == null) {
      portletSession = new PortletSessionImpl(portletHandle);
      addPortletSession(portletSession);
    }
    return portletSession;
  }

  public String getGroupID() {
    return groupID;
  }

  public void setGroupID(String groupID) {
    this.groupID = groupID;
  }

  public Iterator<PortletSession> getAllPortletSessions() {
    return portletSessions.values().iterator();
  }

  public boolean existsPortletSession(String instanceKey) {
    return portletSessions.containsKey(instanceKey);
  }

  public void addPortletSession(PortletSession portletSession) {
    portletSessions.put(portletSession.getPortletHandle(), portletSession);
  }

  public void removePortletSession(String instanceKey) {
    portletSessions.remove(instanceKey);
  }

  public void removeAllPortletSessions() {
    portletSessions.clear();
  }

}
