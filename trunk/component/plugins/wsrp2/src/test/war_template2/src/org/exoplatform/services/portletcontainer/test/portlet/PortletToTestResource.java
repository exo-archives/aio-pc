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
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * Created by The eXo Platform SAS Author : Alexey Zavizionov
 * alexey.zavizionov@exoplatform.com.ua
 */
public class PortletToTestResource extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException,
                                                                                   IOException {

    renderResponse.setContentType("text/html; charset=utf-8");
    PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException,
                                                                                       IOException {
  }

  public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException,
                                                                               IOException {

    String goal = request.getParameter("goal");
    if (goal != null && goal.equals("image")) {
      response.setContentType("image/jpeg");
      Graphics2D graphics;
      java.io.OutputStream stream = response.getPortletOutputStream();
      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(stream);
      BufferedImage bi = new BufferedImage(50, 50, BufferedImage.TYPE_BYTE_INDEXED);
      graphics = bi.createGraphics();
      graphics.setColor(Color.white);
      graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
      graphics.setColor(Color.green);
      graphics.drawLine(0, 24, 9, 24);
      graphics.drawLine(9, 24, 19, 0);
      graphics.drawLine(19, 0, 29, 49);
      graphics.drawLine(29, 49, 39, 24);
      graphics.drawLine(39, 24, 49, 24);
      encoder.encode(bi);
    } else if (goal != null && goal.equals("preferences")) {
      PortletPreferences prefs = request.getPreferences();
      prefs.setValue("attName2", "attValue2");
      prefs.store();
      response.setContentType("text/html; charset=utf-8");
      PrintWriter w = response.getWriter();
      w.print("preferences");
    } else {
      response.setContentType("text/html; charset=utf-8");
      PrintWriter w = response.getWriter();
      w.print("Everything is ok");
    }
  }

}
