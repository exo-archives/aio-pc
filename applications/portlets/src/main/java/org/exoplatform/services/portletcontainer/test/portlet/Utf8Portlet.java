package org.exoplatform.services.portletcontainer.test.portlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.portlet.*;

public class Utf8Portlet extends GenericPortlet {

	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		response.setContentType("text/html;charset=utf-8");

    PrintWriter w = response.getWriter();
    String text = (String) request.getAttribute("text");
    if (text == null)
      text = "--";
    w.println("text: " + text);
    
    PortletURL url = response.createActionURL();
    w.println("<p>form:<form action=\"" + url.toString() + "\" method=\"post\" enctype=\"application/x-www-form-urlencoded\">");
    w.println("<input type=\"text\" name=\"text\"/>");
    w.println("</form></p>");

    url = response.createActionURL();
    url.setParameter("text", "тест");
    w.println("<p><a href=\"" + url.toString() + "\">link</a></p>");
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
System.out.println(" --- Utf8Portlet.processAction: got parameter 'text' == " + actionRequest.getParameter("text"));
    actionRequest.setAttribute("text", actionRequest.getParameter("text"));
  }
}
