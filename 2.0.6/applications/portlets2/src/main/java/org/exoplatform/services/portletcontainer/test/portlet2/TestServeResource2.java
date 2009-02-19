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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * Created by The eXo Platform SAS .
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
public class TestServeResource2 extends GenericPortlet {

  /**
   * Overridden method.
   *
   * @param renderRequest request
   * @param renderResponse response
   * @throws PortletException exception
   * @throws IOException exception
   * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
   */
  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    //System.out.println("In doView method of TestPortletNEW...");

    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println("<center><font size='3'><b><i>More functional portlet for test resource serving.</i></b></font></center><br>");
    w.println("Current method: "+((new Exception()).getStackTrace()[0]).getMethodName());
    w.println("<br>");

    ResourceURL resourceURL = renderResponse.createResourceURL();
    resourceURL.setParameter("goal", "image");
    w.println("<p><a href=\"" + resourceURL.toString() + "\">resourceURL</a></p>");

    resourceURL = renderResponse.createResourceURL();
    resourceURL.setParameter("goal", "image2");
    w.println("<p><img src=\"" + resourceURL.toString() + "\"/></p>");

    PortletContext context = getPortletContext();
    PortletRequestDispatcher rd = context.getRequestDispatcher("/WEB-INF/script.jsp");
    rd.include(renderRequest, renderResponse);

    w.println("----------------------------------------<br>");
  }

  protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println(((new Exception()).getStackTrace()[0]).getMethodName());
  }

  protected void doHelp(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println(((new Exception()).getStackTrace()[0]).getMethodName());
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse)
      throws PortletException, IOException {
    super.render(renderRequest, renderResponse);
  }

  public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    String goal = request.getParameter("goal");
    if (goal != null && goal.equals("image")) {
      response.setContentType("image/jpeg");
      Graphics2D graphics;
      java.io.OutputStream stream = response.getPortletOutputStream();
      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(stream);
      BufferedImage bi = new BufferedImage(50, 20, BufferedImage.TYPE_BYTE_INDEXED);
      graphics = bi.createGraphics();
      graphics.setColor(Color.white);
      graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
      graphics.setColor(Color.red);
      char[] data = "\u0422\u0415\u041a\u0421\u0422".toCharArray();
      graphics.drawChars(data, 0, data.length, 0, 10);
      encoder.encode(bi);
    } else if (goal != null && goal.equals("image2")) {
      response.setContentType("image/jpeg");
      Graphics2D graphics;
      java.io.OutputStream stream = response.getPortletOutputStream();
      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(stream);
      BufferedImage bi = new BufferedImage(50, 50, BufferedImage.TYPE_BYTE_INDEXED);
      graphics = bi.createGraphics();
      graphics.setColor(Color.white);
      graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
      graphics.setColor(Color.green);
      graphics.drawLine( 0, 24,  9, 24);
      graphics.drawLine( 9, 24, 19,  0);
      graphics.drawLine(19,  0, 29, 49);
      graphics.drawLine(29, 49, 39, 24);
      graphics.drawLine(39, 24, 49, 24);
      encoder.encode(bi);
    } else {
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter w = response.getWriter();
      w.println("MARKUP FROM SERVEFRAGMENT");
    }

//    MyEventPub sampleAddress = new MyEventPub();
//    sampleAddress.setStreet("myStreet");
//    sampleAddress.setCity("myCity");
//    response.setEvent(new QName("MyEventPub2"), sampleAddress);
  }

}
