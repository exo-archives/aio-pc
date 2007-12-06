/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.portlets.wsrp;


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

  protected Map groupSessions = new HashMap();
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

  public Iterator getAllGroupSessions() {
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