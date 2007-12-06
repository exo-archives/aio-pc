package org.exoplatform.services.portletcontainer.test.filters;

import java.io.*;
import java.util.*;
import java.io.StringWriter;
import javax.servlet.http.*;
import javax.servlet.*;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.io.FileOutputStream;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;

public class PortalServlet extends HttpServlet {

  public void service(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

     try {

       HttpSession session = request.getSession();


       response.setContentType("text/html; charset=UTF-8");
       PrintWriter w = response.getWriter();
       w.println("<html><title>Portlet rendering</title><h1>Portlets</h1>");
       w.println("<body width='100%'>");


       ArrayList portletinfos = (ArrayList) session.getAttribute("portletinfos");
       if (request.getParameter("fis")!=null) {
         //submitted form
         session.removeAttribute("myatr");
         ArrayList myatr = new ArrayList();
         try {
           if (portletinfos != null) {
             for (int i = 1; i <= portletinfos.size(); i++) {
               if (request.getParameter("n"+i+"n")!=null)  {
                 if (request.getParameter("n"+i+"n").equals("on")){
                   // is on
                   myatr.add("checked");
                 } else {
                   //isn't on
                   myatr.add("");
                 }
               } else {
                 //is null
                 myatr.add("");
               }
             }
           } else {
             //session.setAttribute("myatr", null);
           }
         } catch (Exception e1) {
           e1.printStackTrace();
           session.setAttribute("myatr", null);
         }
         session.setAttribute("myatr",myatr);
       } else {
         //not submitted form
         ArrayList myatr0 = (ArrayList) session.getAttribute("myatr");
         if (myatr0 != null) {
           //there is session
           // all ok
         } else {
           //there isn't session
           // haven't session myatr
           ArrayList myatr = new ArrayList();
           try {
             if (portletinfos != null) {
               for (int i = 1; i <= portletinfos.size(); i++) {
                 myatr.add("");
               }
             } else {
               //session.setAttribute("myatr", null);
             }
           } catch (Exception e1) {
             e1.printStackTrace();
             session.setAttribute("myatr", null);
           }
           session.setAttribute("myatr",myatr);
         }
       }


//       ArrayList ids2 = (ArrayList) session.getAttribute("fqTitles");
//       ArrayList titles2 = (ArrayList) session.getAttribute("titles");
//       ArrayList outs2 = (ArrayList) session.getAttribute("outs");
       ArrayList myatr = (ArrayList) session.getAttribute("myatr");
       try {
         if (portletinfos != null) {
           w.println("<form method='POST' name='checkPortlet' action='.'>");
           w.println("<input type='hidden' name='fis' id='fis' value='yes'>");
               w.println("<table width='100%' border='1'>");
                   w.println("<tr><th valign='center' bgcolor='#D0F0D0' colspan='4'><h1>Please, select some of the following portlets</h1></th></tr>");
                       w.println("<tr><td align='center'>Checked</td>");
                           w.println("<td align='center'>Num</td>");
                               w.println("<td align='center'>Portlet Id</td>");
                                   w.println("<td align='center'>Portlet Name</td></tr>");
           int i2 = 0;
           for (Iterator i = portletinfos.iterator(); i.hasNext();) {
               PortletInfo pinf = (PortletInfo) i.next();
               String title2 = pinf.title;
               String id2 = pinf.fqTitle;
               String myatr2str = (String) myatr.get(i2++);
               w.println("<tr><td align='center'>");
                   w.println("<input type='checkbox' name='n"+i2+"n' ID='n"+i2+"n' " + myatr2str + " onClick='checkPortlet.submit()'>");
                       w.println("</td><td align='center'>"+i2+"</td><td valign='center' bgcolor=''>"+id2+"</td>");
                           w.println("<td valign='center' bgcolor=''>"+title2+"</td></tr>");
             }
                                   w.println("</table>");
                                       w.println("</form>");
         } else {
           w.println("<table width='100%' border='1'><tr><td>There's no portlet data to show</td></tr></table>");
         }
       } catch (Exception e1) {
         e1.printStackTrace();
       }


       String reqURL = new String(request.getRequestURL()+"?");
       String reqContextPath = new String(request.getContextPath().substring(1,request.getContextPath().length())+":");

       try {
         if (portletinfos != null) {
           int i2 = 0;
           for (Iterator i = portletinfos.iterator(); i.hasNext();) {
             PortletInfo pinf = (PortletInfo) i.next();
             String myatrstr = (String) myatr.get(i2++);
             if (myatrstr=="checked") {
               String title = pinf.title;
               String hs = pinf.out;
               String mymode = pinf.mode;
               String mystate = pinf.state;
               String portletapp = pinf.portletapp;
               w.println("<table width='100%' border='1'>");
                   w.println("<tr><th valign='center' bgcolor='#D0F0D0'><h1>"+title+"</h1>");
                 String resMode = reqURL;
                 resMode += reqContextPath + "componentId=" + portletapp;
                 resMode += "&" + reqContextPath + "type=action";
                 resMode += "&" + reqContextPath + "isSecure=true";
//                 resMode += "&" + reqContextPath + "portletMode=";
                 String[] ss2 = mymode.split("[.]");
                 String[] sts = mystate.split("[.]");
                 for (int ii = 0; ii < ss2.length; ii++) {
                   w.println("<a href='" + resMode + "&" + reqContextPath + "portletMode=" + ss2[ii] + "'>" + ss2[ii] + "</a>");
                 }
                 w.println("<br>");
                 for (int k = 0; k < sts.length; k++) {
                   w.println("<a href='" + resMode + "&" + reqContextPath + "windowState=" + sts[k] + "'>" + sts[k] + "</a>");
                 }
//                 w.println("<a href='" + resMode + ss2[ii] + "&" + reqContextPath + "windowState=normal'>" + ss2[ii] + "</a>");
                 w.println("</th></tr>");
                     w.println("<tr><td>" + hs + "</td></tr>");
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
       //session.invalidate();
       System.out.println("-------------------------------------------------------------");

     } catch (Exception e) {
       e.printStackTrace();
       //throw new ServletException(e);
     }
  }

}
