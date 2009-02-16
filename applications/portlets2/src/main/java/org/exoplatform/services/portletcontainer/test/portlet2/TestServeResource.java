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
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

/**
 * Created by The eXo Platform SAS .
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
public class TestServeResource extends GenericPortlet {

  /**
   * Overridden method.
   *
   * @param renderRequest request
   * @param renderResponse response
   * @throws PortletException exception
   * @throws IOException exception
   * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
   */
  protected void doView(RenderRequest renderRequest,
                        RenderResponse renderResponse) throws PortletException,
                                                      IOException {
    System.out.println("In doView method of TestServeResource...");

    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println("<center><font size='3'><b><i>Portlet for test resource serving.</i></b></font></center><br>");
    w.println("Current method: " + ((new Exception()).getStackTrace()[0]).getMethodName());

    printParams(renderRequest, w);

    PortletURL renderURL = renderResponse.createRenderURL();
    renderURL.setParameter("renderURL_param_name_1", "render_value_1");
    renderURL.setParameter("renderURL_param_name_2", "render_value_2");
    renderURL.setParameter("same_param_name", "render");
    w.println("<p><a href=\"" + renderURL.toString() + "\">render URL</a>");

    PortletURL renderURLnop = renderResponse.createRenderURL();
    renderURLnop.removePublicRenderParameter("");
    w.println("<p><a href=\"" + renderURLnop.toString() + "\">render URL no params</a>");

    PortletURL actionURL = renderResponse.createActionURL();
    actionURL.setParameter("actionURL_param_name_1", "action_value_1");
    actionURL.setParameter("actionURL_param_name_2", "action_value_2");
    actionURL.setParameter("same_param_name", "action");
    w.println("<p><a href=\"" + actionURL.toString() + "\">action URL</a>");

    PortletURL actionURLnop = renderResponse.createActionURL();
    w.println("<p><a href=\"" + actionURLnop.toString() + "\">action URL no params</a>");

    ResourceURL resourceURL = renderResponse.createResourceURL();
    resourceURL.setParameter("resourceURL_param_name_1", "resource_value_1");
    resourceURL.setParameter("resourceURL_param_name_2", "resource_value_2");
    resourceURL.setParameter("same_param_name", "resource_1");
    resourceURL.setCacheability(ResourceURL.PAGE);
    w.println("<p><a href=\"" + resourceURL.toString() + "\">resource URL with cache '" + resourceURL.getCacheability() + "'</a>");

    ResourceURL resourceURL2 = renderResponse.createResourceURL();
    resourceURL2.setParameter("resourceURL_param_name_1", "resource_value_1");
    resourceURL2.setParameter("resourceURL_param_name_2", "resource_value_2");
    resourceURL2.setParameter("same_param_name", "resource_2");
    resourceURL2.setCacheability(ResourceURL.FULL);
    w.println("<p><a href=\"" + resourceURL2.toString() + "\">resource URL 2 with cache '" + resourceURL2.getCacheability() + "'</a>");

    ResourceURL resourceURLnop = renderResponse.createResourceURL();
    w.println("<p><a href=\"" + resourceURLnop.toString() + "\">resource URL no params</a>");

  }

  private void printParams(PortletRequest request,
                           PrintWriter w) {
    System.out.println("In printParams method of TestServeResource...");
    w.println("<br><br>");
    if (request.getParameterNames() == null)
      return;
    List<String> names = Collections.list(request.getParameterNames());
    for (String string : names) {
      w.println(" = " + string);
      String[] values = request.getParameterValues(string);
      for (String string2 : values) {
        w.println("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - " + string2);
      }
      w.println("<br>");
    }
    w.println("<br>");
  }

  public void processAction(ActionRequest actionRequest,
                            ActionResponse actionResponse) throws PortletException,
                                                          IOException {
    System.out.println("In processAction method of TestServeResource...");
    actionResponse.setRenderParameters(actionRequest.getParameterMap());
  }

  public void render(RenderRequest renderRequest,
                     RenderResponse renderResponse) throws PortletException,
                                                   IOException {
    System.out.println("In render method of TestServeResource...");
    super.render(renderRequest, renderResponse);
  }

  public void serveResource(ResourceRequest resourceRequest,
                            ResourceResponse resourceResponse) throws PortletException,
                                                              IOException {
    System.out.println("In serveResource method of TestServeResource... ");

    resourceResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = resourceResponse.getWriter();
    w.println("This is the content from serveResource." + "<br>");

    String name = resourceRequest.getAttribute("name").toString();
    w.println(name + "<br>");

    printParams(resourceRequest, w);

    PortletURL renderURL = resourceResponse.createRenderURL();
    renderURL.setParameter("renderURL_param_name_1", "resource_value_1");
    renderURL.setParameter("renderURL_param_name_2", "resource_value_2");
    renderURL.setParameter("same_param_name", "resource");
    w.println("<p><a href=\"" + renderURL.toString() + "\">render URL</a>");

    PortletURL renderURLnop = resourceResponse.createRenderURL();
    w.println("<p><a href=\"" + renderURLnop.toString() + "\">render URL no params</a>");

    //on console
    Enumeration parnames = resourceRequest.getParameterNames();
    while (parnames.hasMoreElements()) {
      String par = (String) parnames.nextElement();
      System.out.println(" >>> Parameters in RESOURCE: '" + par + "' = '" + resourceRequest.getParameter(par) + "'");
    }
    /*
    resourceResponse.setContentType("image/jpeg");
    PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher("/ImageServlet");
    rd.include(resourceRequest, resourceResponse);
    */
    /*
    resourceResponse.setContentType("image/jpeg");
    Graphics2D graphics;
    java.io.OutputStream stream = resourceResponse.getPortletOutputStream();
    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(stream);
    BufferedImage bi = new BufferedImage( 50,20,BufferedImage.TYPE_BYTE_INDEXED);
    graphics = bi.createGraphics();
    graphics.setColor(Color.white);
    graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
    graphics.setColor(Color.red);
    //Font fo = new Font("Times New Roman",1,30);
    //graphics.setFont(fo);
    graphics.drawChars("\u041f\u0418\u0417\u0414\u0415\u0426".toCharArray(),0,6,0,10);
    encoder.encode(bi);
    */
  }

}
