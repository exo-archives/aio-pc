/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Sep 11, 2003
 * Time: 8:29:24 PM
 */
package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloWorldPortlet2 extends GenericPortlet {

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
    System.out.println("In processAction method of HelloWorldPortlet2...");
  }

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
    renderResponse.setContentType("text/html");    
    PrintWriter w = renderResponse.getWriter();
    w.println("Hello World 2");
  }

  protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
    renderResponse.setContentType("text/html");
    System.out.println("In doEdit method of HelloWorldPortlet2...");
  }

}
