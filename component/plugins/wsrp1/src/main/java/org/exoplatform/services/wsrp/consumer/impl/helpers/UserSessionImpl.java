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

package org.exoplatform.services.wsrp.consumer.impl.helpers;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import org.exoplatform.services.wsrp.consumer.GroupSession;
import org.exoplatform.services.wsrp.consumer.GroupSessionMgr;
import org.exoplatform.services.wsrp.consumer.UserSessionMgr;
import org.exoplatform.services.wsrp.exceptions.WSRPException;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 9 févr. 2004
 * Time: 22:31:23
 */

public class UserSessionImpl extends InitCookieImpl
    implements UserSessionMgr {

  protected Map<String, GroupSession> groupSessions = new HashMap<String, GroupSession>();
  private String userID;

  public UserSessionImpl(String markupInterfaceURL) {
    super(markupInterfaceURL);
  }

  public GroupSessionMgr getGroupSession(String groupID) throws WSRPException {
    if (groupID != null) {
      GroupSessionMgr groupSession = (GroupSessionMgr) groupSessions.get(groupID);
      if (groupSession == null) {
        groupSession = new GroupSessionImpl(groupID, getMarkupInterfaceURL());
        addGroupSession(groupSession);
      }
      return groupSession;
    }
    return null;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public Iterator<GroupSession> getAllGroupSessions() {
    return groupSessions.values().iterator();
  }

  public void addGroupSession(GroupSession groupSession) {
    groupSessions.put(groupSession.getGroupID(), groupSession);
  }

  public void removeGroupSession(String groupID) {
    groupSessions.remove(groupID);
  }

  public void removeAllGroupSessions() {
    groupSessions.clear();
  }

}