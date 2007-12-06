/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by The eXo Platform SAS 
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 26, 2003
 * Time: 3:38:11 PM
 */
public class HelloWorldPortlet extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    renderResponse.setContentType("text/html;charset=UTF-8");    
    System.out.println("In doView method of HelloWorldPortlet...");
    PrintWriter w = renderResponse.getWriter();
    w.println("Hello World");
    w.println("Request attributes... ");
    Enumeration e = renderRequest.getAttributeNames();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      w.println("attibute : " + s);
      w.println("value : " + renderRequest.getAttribute(s));
    }
    w.println("Request parameters : ");
    e = renderRequest.getParameterNames();
    while (e.hasMoreElements()) {
      String s = (String) e.nextElement();
      w.println("parameter : " + s);
      w.println("value : " + renderRequest.getParameter(s));
    }
    w.println("");
    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setParameter("action_param_1", "action param test");
    actionURL.setParameter("action_param_2", "action param test 2");
    actionURL.setSecure(true);
    actionURL.setWindowState(WindowState.MAXIMIZED);
    actionURL.setPortletMode(PortletMode.EDIT);

    PortletURL renderURL = renderResponse.createRenderURL();
    renderURL.setParameter("render_param", "render param");

    w.println("Create Portlet URL...");
    w.println("action URL : " + actionURL.toString());
    w.println("render URL : " + renderURL.toString());

    w.println("Test object creation...");
    Helper h = new Helper();
    w.println(h.getSomeText());

    w.println("encode in name space : " + renderResponse.getNamespace() + "ahaha");

    renderResponse.setTitle("test title");
    renderResponse.setProperty(RenderResponse.EXPIRATION_CACHE, ""+6);
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
    actionRequest.setAttribute("test_attribute", "benj's test");
    actionResponse.setRenderParameter("test_render_param", "benj's test 2");
    actionResponse.setPortletMode(PortletMode.HELP);
    actionResponse.setWindowState(WindowState.MAXIMIZED);
    
    System.out.println("   **User Principal : "+actionRequest.getUserPrincipal());
    System.out.println("   **Remote User : "+actionRequest.getRemoteUser());
    System.out.println("   **User in role coco : "+actionRequest.isUserInRole("coco"));
    System.out.println("   **User in role trustedUser : "+actionRequest.isUserInRole("trustedUser"));
  }

}
