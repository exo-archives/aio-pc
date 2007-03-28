/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.portlets.wsrp;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.exoplatform.services.wsrp.consumer.GroupSessionMgr;
import org.exoplatform.services.wsrp.consumer.PortletSession;
//import org.exoplatform.services.wsrp.consumer.PortletSession;


/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 9 févr. 2004
 * Time: 22:34:33
 */

public class GroupSessionImpl extends InitCookieImpl implements GroupSessionMgr {

  protected String groupID;
  protected Map portletSessions = new HashMap();

  public GroupSessionImpl(String groupID, String markupURL) {
    super(markupURL);
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

  public Iterator getAllPortletSessions() {
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