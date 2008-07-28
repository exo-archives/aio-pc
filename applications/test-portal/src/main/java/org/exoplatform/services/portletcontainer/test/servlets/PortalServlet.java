/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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
package org.exoplatform.services.portletcontainer.test.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.exoplatform.frameworks.portletcontainer.portalframework.PortletInfo;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */
public class PortalServlet extends HttpServlet {

  /**
   * Log.
   */
  private final Log log = ExoLogger.getLogger(getClass().getName());

  /**
   * Serves http request. Renders portal page.
   *
   * @param request http request
   * @param response http response
   * @throws IOException declared by superclass
   * @throws ServletException declared by superclass
   */
  public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    try {
      HttpSession session = request.getSession();
      byte[] resource = (byte[]) session.getAttribute("resource");
      if (resource != null) {
        String cntType = (String) session.getAttribute("resourceType");
        if (cntType != null)
          response.setContentType(cntType);
        else
          response.setContentType("text/html");
        Map<String, String> headers = (Map<String, String>) session.getAttribute("resourceHeaders");
        if (headers != null) {
          for (Iterator<String> i = headers.keySet().iterator(); i.hasNext();) {
            String name = i.next();
            response.setHeader(name, headers.get(name));
          }
        }
        response.setStatus((Integer) session.getAttribute("resourceStatus"));
        OutputStream w = response.getOutputStream();
        w.write(resource);
        w.close();
        session.removeAttribute("resource");
        session.removeAttribute("resourceType");
        return;
      }

      response.setContentType("text/html;charset=UTF-8");
      PrintWriter w = response.getWriter();

      w.println("<html>");
        w.println("<head>");
          w.println("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">");
          w.println("<title>Portlet rendering</title>");
        w.println("</head>");
      w.println("<body width='100%' style='font-family: Arial' bgcolor='#FFFFFF'>");
      w.println("<table width='100%'>");
        w.println("<tr>");
          w.println("<td><a href='" + request.getContextPath() + "'><img src=\"../img/logotestportal.png\" style='border:0px none;'></a></td>");
          w.println("<td>&nbsp;</td>");
          w.println("<td align=\"right\">");
            w.println("<form method='post' name='addPortlet' action='.'>");
            w.println("<input type='hidden' name='pAction' value='add'/>");
            w.println("<select name='pApp' id='pApp' onchange='pltListSelectApp();'>");
            w.println("</select>");
            w.println("<select name='pName' id='pName'>");
            w.println("</select>");
            w.println("<input type='submit' value='Add portlet'/>");
            w.println("</form>");
            w.println("<form method='post' name='delPortlet' id='delPortlet' action='.'>");
            w.println("<input type='hidden' name='pAction' value='del'/>");
            w.println("<input type='hidden' name='pId' id='pId' value=''/>");
            w.println("</form>");
            w.println("<script language='JavaScript'>");
            w.println("function pltListSelectApp() {");
            w.println("  var sel = document.getElementById('pApp');");
            w.println("  if (sel.selectedIndex != -1) {");
            w.println("    var sel1 = document.getElementById('pName');");
            w.println("    sel1.options.length = 0;");
            w.println("    var pRow = pList[sel.options[sel.selectedIndex].value];");
            w.println("    for (var i in pRow) {");
            w.println("      sel1.options[sel1.options.length] = new Option(pRow[i], pRow[i]);");
            w.println("    }");
            w.println("  }");
            w.println("}");
            w.println(session.getAttribute("portletNames"));
            w.println("var sel = document.getElementById('pApp');");
            w.println("for (var i in pList) {");
            w.println("  sel.options[sel.options.length] = new Option(i, i);");
            w.println("}");
            w.println("pltListSelectApp();");
            w.println("</script>");
        w.println("</tr>");
      w.println("</table>");

      ArrayList<PortletInfo> portletinfos = (ArrayList<PortletInfo>) session.getAttribute("portletinfos");

      try {
        if (portletinfos != null) {
          Boolean collapsed = (Boolean) session.getAttribute("listCollapsed");
          if (collapsed == null)
            collapsed = new Boolean(false);
          String displayOpen = "";
          String displayClosed = "";
          if (collapsed.booleanValue())
            displayOpen = " style='display: none'";
          else
            displayClosed = " style='display: none'";
          w.println("<script language='JavaScript'>");
          w.println("function listOpen() {");
          w.println("  elCollapsed.style.display = 'none';");
          w.println("  elList.style.display = '';");
          w.println("  checkPortlet.listCollapsed.value = 'false';");
          w.println("  checkPortlet.submit();");
          w.println("}");
          w.println("function listClose() {");
          w.println("  elCollapsed.style.display='';");
          w.println("  elList.style.display='none';");
          w.println("  checkPortlet.listCollapsed.value = 'true';");
          w.println("  checkPortlet.submit();");
          w.println("}");
          w.println("</script>");
          w.println("<form method='POST' name='checkPortlet' action='.'>");
          w.println("<input type='hidden' name='fis' id='fis' value='yes'>");
          w.println("<input type='hidden' name='listCollapsed' id='fis' value='" + collapsed.toString() + "'>");
            w.println("<div id='elCollapsed'" + displayClosed + ">");
              w.println("<table width='100%' border='1' style='border-collapse:collapse;border-style:solid;border-color:#A7A7AC'>");
                w.println("<tr><th valign='center' align='left' bgcolor='#A3A7F6'><img src=\"../img/triangle.gif\" onclick='listOpen()' />" +
                    " <font size='4' face='Verdana,Arial'>Collapsed portlet list</font></th></tr>");
              w.println("</table>");
            w.println("</div>");
            w.println("<div id='elList'" + displayOpen + ">");
            w.println("<table width='100%' border='1' style='border-collapse:collapse;border-style:solid;border-color:#A7A7AC'>");
              w.println("<tr><th valign='center' align='left' bgcolor='#A3A7F6' colspan='5'>"
                  + "<img src=\"../img/opentriangle.gif\" onclick='listClose()' /> <font size='4' face='Verdana,Arial'>Please, select some of the following portlets :</font></th></tr>");
              w.println("<tr><td align='center'><b>Checked</b></td>");
                w.println("<td align='center'><b>Num</b></td>");
                w.println("<td align='center'><b>Portlet Id</b></td>");
              w.println("<td align='center'><b>Portlet Name</b></td>");
              w.println("<td align='center'><b>Portlet Title</b></td>");
              w.println("<td>&nbsp;</td></tr>");
          int i2 = 0;
          for (Iterator<PortletInfo> i = portletinfos.iterator(); i.hasNext();) {
            PortletInfo pinf = i.next();
              String title2 = pinf.getTitle();
              String id2 = pinf.getPortlet();
              String myatr2str = "";
              if (pinf.isToRender())
                myatr2str = " checked";
              i2++;
              w.println("<tr><td align='center'>");
                w.println("<input type='checkbox' name='n" + i2 + "n' ID='n" + i2 + "n'" + myatr2str
                    + " onClick='checkPortlet.submit()'>");
                w.println("</td><td align='center'>" + i2 + "</td><td valign='center' bgcolor=''>" + id2 + "</td>");
                w.println("<td valign='center' bgcolor=''>" + pinf.getName() + "</td>");
              w.println("<td valign='center' bgcolor=''>" + title2 + "</td>");
              w.println("<td>");
                w.println("<input type='button' value='Delete' onclick='document.getElementById(\"pId\").value = \"" + id2 + "\"; document.getElementById(\"delPortlet\").submit();'/>");
              w.println("</td>");
              w.println("</tr>");
            }
            w.println("</table>");
            w.println("</div>");
          w.println("</form>");
        } else {
          w.println("<table width='100%' border='1'><tr><td>There's no portlet data to show</td></tr></table>");
        }
      } catch (Exception e1) {
        e1.printStackTrace();
      }

      String reqURL = new String(request.getRequestURL() + "?");
      String reqContextPath = new String(request.getContextPath().substring(1, request.getContextPath().length()) + ":");

      try {
        if (portletinfos != null) {
          for (Iterator<PortletInfo> i = portletinfos.iterator(); i.hasNext();) {
            PortletInfo pinf = i.next();
            if (pinf.isToRender()) {
              String title = pinf.getTitle();
              String hs = pinf.getOut();
              ArrayList<String> mymodes = pinf.getModes();
              ArrayList<String> mystates = pinf.getStates();
              String portlet = pinf.getPortlet();
              w.println("<table width='100%' border='1' STYLE='border-collapse:collapse;border-style:solid;border-color:#A7A7AC'>");
                w.println("<tr><th valign='center' bgcolor='#A3A7F6'><font size='4' face='Verdana,Arial'><div id=\"p" + pinf.getWid() + "title\">"
                    + title + " (" + portlet + ")"+ "</div></font>");
              String resMode = reqURL;
              resMode += reqContextPath + "componentId=" + portlet;
//              resMode += "&" + reqContextPath + "type=action";
              resMode += "&" + reqContextPath + "isSecure=true";
              if (mymodes!= null)
              for (int j = 0; j < mymodes.size(); j++) {
                w.println("<a href='" + resMode + "&" + reqContextPath + "portletMode=" + mymodes.get(j) + "'>"
                    + mymodes.get(j) + "</a>");
              }
                w.println("<br>");
              if (mystates!= null)
                for (int k = 0; k < mystates.size(); k++) {
                  w.println("<a href='" + resMode + "&" + reqContextPath + "windowState=" + mystates.get(k) + "'>"
                      + mystates.get(k) + "</a>");
                }
                w.println("</th></tr>");
                w.println("<tr><td><div id=\"p" + pinf.getWid() + "content\">" + hs + "</div></td></tr>");
              w.println("</table>");
            }
          }
        } else {
          w.println("<table width='100%' border='1'><tr><th>There's no portlet data to show</th></tr></table>");
        }
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      w.println("</body>");
      w.println("</html>");
      System.out.println("-------------------------------------------------------------");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
