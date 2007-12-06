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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Created by The eXo Platform SAS
 * Author : Alexey V. Zavizionov
 *          alexey.zavizionov@exoplatform.com.ua
 * 11.10.2006
 */
public class TestResources extends GenericPortlet {

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
  throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");
    renderResponse.setTitle("TestResources");
    PrintWriter w = renderResponse.getWriter();
    w.println("<center><font size='3'><b><i>This portlet shows an example of loading resources from ResourceBundle with different locales.</i></b></font></center><br>");
    PortletConfig portletConfig = getPortletConfig();
    w.println("<br>Locale.getDefault = " + Locale.getDefault() + "<br>");
    w.println("~~~~~~~~~~~~~~~~~~~~~~~~~<br>");
    Locale locales[] = new Locale[] {
                        new Locale("en"),
                        new Locale("ru")
                        };
    if(portletConfig != null) {
      for(int i = 0; i < locales.length; i++) {
        w.println(locales[i]);
        ResourceBundle resourceBundle =
          portletConfig.getResourceBundle(locales[i]);
        if (resourceBundle != null) {
          String resourceTitle = resourceBundle.getString("javax.portlet.title");
          w.println(" = " + resourceTitle + "<br>");
        } else {
        }
        w.println("~~~~~~~~~~~~~~~~~~~~~~~~~<br>");
      }
    } else {
    }
  }

protected void doEdit(RenderRequest renderRequest, RenderResponse renderResponse)
  throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=UTF-8");
    PrintWriter w = renderResponse.getWriter();
    w.println("<p>test");
  }

public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
  throws PortletException, IOException {

  }


}
