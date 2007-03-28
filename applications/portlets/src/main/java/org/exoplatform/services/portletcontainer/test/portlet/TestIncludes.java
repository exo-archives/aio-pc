package org;
import java.io.IOException;

import javax.portlet.*;


public class TestIncludes extends GenericPortlet
{
	private static final String HELLO_TEMPLATE =
		"/WEB-INF/HelloWorld.jsp";
	private static final String HELLO_TEMPLATE_HTML =
		"/WEB-INF/HelloWorld.html";

	/**
	 * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	protected void doView(RenderRequest request, RenderResponse response)
		throws PortletException, IOException
	{
		response.setContentType("text/html;charset=UTF-8");
		response.setTitle("HelloWorld Runtime TITLE");
		
		//include HTML
		PortletContext context = getPortletContext();
		
		//include an html page
		int htmltimes=Integer.parseInt(getInitParameter("htmltimes"));
		if(htmltimes>0)
		{
			PortletRequestDispatcher rd =
				context.getRequestDispatcher(HELLO_TEMPLATE_HTML);
//System.out.println("TestIncludes: about to include html " + htmltimes + " times");
			for(int i=0;i<htmltimes;i++) {
				rd.include(request, response);
//System.out.println("TestIncludes: html " + i);
			}
		}
		
		//include a jsp page
		int jsptimes=Integer.parseInt(getInitParameter("jsptimes"));
		if(jsptimes>0)
		{
			PortletRequestDispatcher rd =
				context.getRequestDispatcher(HELLO_TEMPLATE);
//System.out.println("TestIncludes: about to include jsp " + jsptimes + " times");
			for(int i=0;i<jsptimes;i++) {
				rd.include(request, response);
//System.out.println("TestIncludes: jsp " + i);
			}
		}
		
		//include a servlet that includes HELLO_TEMPLATE
		int servlettimes=Integer.parseInt(getInitParameter("servlettimes"));
		if(servlettimes>0)
		{
			PortletRequestDispatcher rd =
				context.getRequestDispatcher("/TestServlet");
//System.out.println("TestIncludes: about to include servlet " + servlettimes + " times");
			for(int i=0;i<servlettimes;i++) {
				rd.include(request, response);
//System.out.println("TestIncludes: servlet " + i);
			}
		}
	}
}
