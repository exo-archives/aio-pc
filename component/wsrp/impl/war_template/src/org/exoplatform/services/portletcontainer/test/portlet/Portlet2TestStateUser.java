/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;
import java.io.IOException;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 27 janv. 2004
 * Time: 19:42:24
 */

public class Portlet2TestStateUser extends GenericPortlet{

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
    PortletSession session = actionRequest.getPortletSession();
    PortletPreferences prefs = actionRequest.getPreferences();
    if(session.getAttribute("counter") == null){
      session.setAttribute("counter", "1");
    } else {      
      prefs.setValue("attName2", "attValue2");
    }
    prefs.store();
  }

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
  }

}