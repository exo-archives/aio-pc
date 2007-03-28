/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 26, 2003
 * Time: 3:38:11 PM
 */
package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;


public class WSRPHelloWorldPortlet extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");        
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
  }

}
