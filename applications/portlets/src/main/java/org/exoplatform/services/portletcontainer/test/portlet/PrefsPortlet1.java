package org.exoplatform.services.portletcontainer.test.portlet;
import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.*;

public class PrefsPortlet1 extends GenericPortlet
{

	/**
	 * A life cycle method. This method gets inovked by the container.
	 *
	 * @param ponfig a object that holds the portlet configuration information
	 *
	 * @throws PortletException an exception that you throw if things go wrong
	 *         while starting the portlet
	 */
	public void init(PortletConfig ponfig) throws PortletException
	{
		super.init(ponfig);
	}

	/**
	 * The GenericPortlet calls this method if the portlet mode is view
	 *
	 * @param pRequest the request
	 * @param pResponse the response
	 *
	 * @throws PortletException
	 * @throws IOException
	 */
	public void doView(RenderRequest request, RenderResponse response)
		throws PortletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter writer=response.getWriter();
		PortletPreferences prefs=request.getPreferences();
		writer.println("<div style='background-color:"+prefs.getValue("color1","black")+"'>color1</div><br>");
		writer.println("<div style='background-color:"+prefs.getValue("color2","black")+"'>color2</div><br>");

    PortletURL renderURL = response.createRenderURL();
//    PortletURL actionURL = renderResponse.createActionURL();
    renderURL.setWindowState(WindowState.NORMAL);
    renderURL.setPortletMode(PortletMode.EDIT);
    writer.println("<p><a href=\"" + renderURL.toString() + "\">EDIT</a></p>");
	}

	/**
	 * The GenericPortlet calls this method if the portlet mode is edit
	 *
	 * @param request the request
	 * @param response the response
	 *
	 * @throws PortletException
	 * @throws IOException
	 */
	public void doEdit(RenderRequest request, RenderResponse response)
		throws PortletException, IOException
	{
		response.setContentType("text/html");
		
		PrintWriter writer=response.getWriter();
		PortletPreferences prefs=request.getPreferences();
		writer.println("<p>current color2: " + prefs.getValue("color2", ""));

    PortletURL actionURL = response.createActionURL();
    actionURL.setParameter("color2", "green");
    writer.println("<p><a href=\"" + actionURL.toString() + "\"><font color=\"green\">change color2 to green</font></a></p>");

    actionURL = response.createActionURL();
    actionURL.setParameter("color2", "red");
    writer.println("<p><a href=\"" + actionURL.toString() + "\"><font color=\"red\">change color2 to red</font></a></p>");

    actionURL = response.createActionURL();
    actionURL.setParameter("color2", "blue");
    writer.println("<p><a href=\"" + actionURL.toString() + "\"><font color=\"blue\">change color2 to blue</font></a></p>");
	}

	//@see javax.portlet.Portlet#processAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
	public void processAction(ActionRequest request, ActionResponse response)
		throws PortletException, IOException
	{
		PortletPreferences prefs=request.getPreferences();
		prefs.setValue("color2",request.getParameter("color2"));
//		prefs.setValue("color2", "green");
		prefs.store();
	}
}
