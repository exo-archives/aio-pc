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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Created by The eXo Platform SAS
 * Author : Roman Pedchenko
 *          roman.pedchenko@exoplatform.com.ua
 */
public class TestForm extends GenericPortlet {


  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println("<center><font size='3'><b><i>Portlet for test sending paramethers from web-form.</i></b></font></center><br>");

    if (renderRequest.getParameter("result") != null) {
      w.println("<h3>result:</h3>");
      w.println("<pre>" + renderRequest.getParameter("result") + "</pre>");
    }
    PortletURL actionURL = renderResponse.createActionURL();

    w.println("<form action='" + actionURL.toString() + "' name='portletForm' method='POST'>");
    w.println("&nbsp; Enter test param value here: <input type='text' style='height:23' name='text' value='Test param'>");
    w.println(" or select file :<input type='file' style='height:23' name='file'>");
    w.println("<input type='submit' style='height:23' value='Send'>");
    w.println("</form>");
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)  throws PortletException, IOException {
    String res = "Stream:\n";
    InputStream is = actionRequest.getPortletInputStream();
    res = res + " -- stream: " + is + "\n";
    if (is != null) {
      res = res + " -- available: " + is.available() + "\n -- data:\n    ";
      try {
        int i;
        while((i = is.read()) >= 0)
          res = res + (char) i;
      } catch (EOFException e) {}
    }
    is.close();
/*
    String res = "Reader:<br>\n";
    BufferedReader br = actionRequest.getReader();
    res = res + " -- reader: " + br + "<br>\n";
    if (br != null) {
      res = res + " -- ready: " + br.ready() + "<br>\n";
      res = res + "  <blockquote>\n";
      String s = null;
      while((s = br.readLine()) != null) {
        res = res + s + "\n";
      }
      res = res + "  </blockquote><br>\n";
    }
    br.close();
*/
    actionResponse.setRenderParameter("result", res);
  }

}
