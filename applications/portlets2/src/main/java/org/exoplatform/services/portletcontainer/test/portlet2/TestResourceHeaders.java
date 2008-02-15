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
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: $
 */
public class TestResourceHeaders extends GenericPortlet {

  /**
   * Overridden method.
   *
   * @param request request
   * @param response response
   * @throws PortletException exception
   * @throws IOException exception
   * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
   */
  protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    response.setContentType("text/html; charset=utf-8");
    PrintWriter w = response.getWriter();
    w.println("<center><font size='3'><b><i>Demo for setting http headers in serveResource()</i></b></font></center><br/>");
    w.println("<br/>Click to the link below to download a picture<br/>");

    ResourceURL resourceURL = response.createResourceURL();
    w.println("<p><a href=\"" + resourceURL.toString() + "\">Click to download</a></p>");
  }

  public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
  }

  public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    response.setProperty("Content-Disposition", "attachment; filename=picture.jpeg");
    response.setContentType("application/octet-stream");
    java.io.OutputStream stream = response.getPortletOutputStream();
    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(stream);
    BufferedImage bi = new BufferedImage(50, 50, BufferedImage.TYPE_BYTE_INDEXED);
    Graphics2D graphics = bi.createGraphics();
    graphics.setColor(Color.white);
    graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
    graphics.setColor(Color.green);
    graphics.drawLine( 0, 24,  9, 24);
    graphics.drawLine( 9, 24, 19,  0);
    graphics.drawLine(19,  0, 29, 49);
    graphics.drawLine(29, 49, 39, 24);
    graphics.drawLine(39, 24, 49, 24);
    encoder.encode(bi);
  }

}
