/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.portlets.wsrp;

import org.exoplatform.services.wsrp.consumer.PortletWindowSession;
import org.exoplatform.services.wsrp.consumer.adapters.PortletWindowSessionAdapter;
import org.exoplatform.services.wsrp.consumer.templates.PortletSessionTemplate;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 9 févr. 2004
 * Time: 22:39:52
 */

public class PortletSessionImpl extends PortletSessionTemplate {

  public PortletSessionImpl(String portletHandle) {
    super.portletHandle = portletHandle;
  }

  public PortletWindowSession getPortletWindowSession(String windowID) {
    PortletWindowSession session = (PortletWindowSession) this.portletWindowSessions.get(windowID);
    if (session == null) {
      session = new PortletWindowSessionAdapter();
      ((PortletWindowSessionAdapter)session).setWindowID(windowID);
      ((PortletWindowSessionAdapter)session).setPortletSession(this);
      this.portletWindowSessions.put(windowID, session);
    }
    return session;
  }

}