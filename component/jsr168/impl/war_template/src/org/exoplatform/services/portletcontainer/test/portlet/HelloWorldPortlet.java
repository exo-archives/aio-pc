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
