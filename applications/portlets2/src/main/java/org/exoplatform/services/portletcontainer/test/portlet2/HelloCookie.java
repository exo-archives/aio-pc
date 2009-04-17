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
package org.exoplatform.services.portletcontainer.test.portlet2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Test portlet.
 */
public class HelloCookie extends GenericPortlet {

  /**
   * Overridden method.
   *
   * @param renderRequest render request
   * @param renderResponse render response
   * @throws PortletException something may go wrong
   * @throws IOException something may go wrong
   * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
   */
  public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    
    // get cookies
    Cookie[] cookies = renderRequest.getCookies();
    w.println("\n<br />   cookies = " + cookies);
    
    if (cookies!=null) {
      for (int i = 0; i < cookies.length; i++) {
        Cookie cookie = cookies[i];
        w.println("\n<br /><b> i = " + i + "</b>");
        w.println("\n<br />   cookie.getComment() = " + cookie.getComment());
        w.println("\n<br />   cookie.getDomain()  = " + cookie.getDomain());
        w.println("\n<br />   cookie.getMaxAge()  = " + cookie.getMaxAge());
        w.println("\n<br />   cookie.getName()    = " + cookie.getName());
        w.println("\n<br />   cookie.getPath()    = " + cookie.getPath());
        w.println("\n<br />   cookie.getValue()   = " + cookie.getValue());
        w.println("\n<br />   cookie.getVersion() = " + cookie.getVersion());
        w.println("\n<br />   cookie.getSecure()  = " + cookie.getSecure());
        w.println("\n<br />");
      }
    }
    
    w.println("<center><font size='3'><b><i>Simple portlet shows cookies</i></b></font></center><br>");
    w.println("<h2 align=\"center\">Hello World</h2>");

    //URLS
    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setParameter("action_param_1", "action param test");
    actionURL.setParameter("action_param_2", "action param test 2");
    actionURL.setSecure(true);
    actionURL.setWindowState(WindowState.MAXIMIZED);
    actionURL.setPortletMode(PortletMode.VIEW);

    PortletURL renderURL = renderResponse.createRenderURL();
    renderURL.setParameter("render_param", "render param");

    w.println("<p><b>Portlet URLs</b></p>");
    w.println("<p><a href=\"" + actionURL.toString() + "\">action URL</a>");
    w.println("&nbsp;&nbsp; and &nbsp;&nbsp;");
    w.println("<a href=\"" + renderURL.toString() + "\">render URL</a></p>");

    renderResponse.setTitle("test title");
    renderResponse.setProperty(RenderResponse.EXPIRATION_CACHE, "" + 6);
  }

  public void doEdit(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    
    // get cookies
    Cookie[] cookies = renderRequest.getCookies();
    w.println("\n<br />   cookies = " + cookies + "\n<br />");
    
    //set cookies
    Cookie cookie = new Cookie("my-name", "my-value");
    renderResponse.addProperty(cookie);

    w.println("<h2 align=\"center\">If you want so much to edit something try this =)</h2>");
    w.println("<form><input type=\"text\" value=\"something to edit =)\"/></form>");

    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setWindowState(WindowState.NORMAL);
    actionURL.setPortletMode(PortletMode.HELP);
    w.println("<p><a href=\"" + actionURL.toString() + "\">do you need help?..</a>");
    actionURL = renderResponse.createActionURL();
    actionURL.setWindowState(WindowState.NORMAL);
    actionURL.setPortletMode(PortletMode.VIEW);
    w.println("<p><a href=\"" + actionURL.toString() + "\">back to view</a>");
  }

  public void doHelp(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println("<h2 align=\"center\">Help, I need somebody's help! &copy;</h2>");

    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setWindowState(WindowState.NORMAL);
    actionURL.setPortletMode(PortletMode.VIEW);
    w.println("<p><a href=\"" + actionURL.toString() + "\">back to view</a>");
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
    actionRequest.setAttribute("test_attribute", "test");
    actionResponse.setRenderParameter("test_render_param", "test 2");
    
    // get cookies
    Cookie[] cookies = actionRequest.getCookies();
    System.out.println(">>> HelloCookie.processAction() cookies = " + cookies);
    System.out.println(">>> HelloCookie.processAction() cookies.length = " + cookies.length);
    
    //set cookies
    Cookie cookie = new Cookie("my-name", "my-value");
    actionResponse.addProperty(cookie);
    
  }

  
}
