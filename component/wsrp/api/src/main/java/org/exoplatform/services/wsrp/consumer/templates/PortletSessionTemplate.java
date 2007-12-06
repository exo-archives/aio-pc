/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
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