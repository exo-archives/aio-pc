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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * Created by The eXo Platform SAS .
 * Author : Alexey Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 */
public class ImageServlet extends HttpServlet {

  /**
   * Overridden method.
   *
   * @param request request
   * @param response response
   * @throws IOException exception
   * @throws ServletException exception
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    try {
       response.setContentType("image/jpeg");

       ByteArrayOutputStream output;
       output = new ByteArrayOutputStream();

       Graphics2D graphics;
       JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);//response.getOutputStream()
       BufferedImage bi = new BufferedImage( 50,
                                             20,
                                             BufferedImage.TYPE_BYTE_INDEXED);
       graphics = bi.createGraphics();
       graphics.setColor(Color.white);
       graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
       graphics.setColor(Color.red);
//       Font fo = new Font("Times New Roman",1,30);
//       graphics.setFont(fo);
       graphics.drawChars("\u041f\u0418\u0417\u0414\u0415\u0426".toCharArray(),0,6,0,10);
       encoder.encode(bi);

       char[] c = (new String(output.toByteArray())).toCharArray();
       String s = new String(c);
       OutputStream os = response.getOutputStream();
       os.write(s.getBytes());

       //PrintWriter w = response.getWriter();
       //w.println(s.getBytes());

       /*
       char[] c = (new String(output.toByteArray())).toCharArray();
       String s = new String(c);
       w.println(s);
       */


     } catch (Exception e) {
       throw new ServletException(e);
     }
  }
}
