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
package org.exoplatform.services.portletcontainer.demo.portlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
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
public class BundlesDemo extends GenericPortlet {

  private static final String KEY = "javax.portlet.title";

  protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
  throws PortletException, IOException {
    renderResponse.setContentType("text/html; charset=utf-8");
    PrintWriter w = renderResponse.getWriter();
    w.println("<br/><font align='center' size='3'><b><i>This portlet shows getting resources from resource bundles for different locales.</i></b></font><br/>");
    PortletConfig portletConfig = getPortletConfig();
    Locale defaultLocale = Locale.getDefault();
    w.println("<br/>Default system locale: <b><font color='green'>" + defaultLocale + "</font></b><br/>");
    w.println("<br/><table width='100%' border='1' style='border-collapse:collapse; border-style:solid; border-color:#A7A7AC; font-size: small; '>");
    w.println("<tr bgcolor='#A3A7F6'><th>locale</th><th>key</th><th>value</th><th>country</th><th>language</th><th>default</th></tr>");
    for(Enumeration<Locale> e = portletConfig.getSupportedLocales(); e.hasMoreElements(); ) {
      Locale l = e.nextElement();
      renderLocale(w, portletConfig, l, defaultLocale.getLanguage().equals(l.getLanguage()));
    }
    w.println("</table>");
    w.println("<br/>");
  }

  private void renderLocale(PrintWriter w, PortletConfig portletConfig, Locale locale, boolean hilight) {
    w.print("<tr");
    if (hilight)
      w.print(" bgcolor='#FFF0C0'");
    w.print("><td align='center'>" + locale + "</td><td>" + KEY + "</td><td>");
    ResourceBundle resourceBundle = portletConfig.getResourceBundle(locale);
    if (resourceBundle != null) {
      String resourceTitle = resourceBundle.getString(KEY);
      w.println(resourceTitle);
    } else {
      w.println("<font color='red'>can't get resource bundle!</font>");
    }
    w.println("</td>");
    w.println("<td>" + locale.getCountry() + "</td>");
    w.println("<td align='center'>" + locale.getLanguage() + "</td>");
    w.println("<td align='center'><input type='checkbox'" + (hilight ? " checked='true'" : "") + " disabled='true' /></td>");
    w.println("</tr>");
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
  }

}
