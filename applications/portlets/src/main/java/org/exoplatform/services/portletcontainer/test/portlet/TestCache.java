package org;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.portlet.*;

public class TestCache extends GenericPortlet 
{

	/**
	 * The GenericPortlet calls this method if the portlet mode is view
	 *
	 * @param request the request
	 * @param response the response
	 *
	 * @throws PortletException
	 * @throws IOException
	 */
	public void doView(RenderRequest request, RenderResponse response)
		throws PortletException, IOException
	{
		response.setContentType("text/html;charset=UTF-8");
		Date d=new Date();
		response.setTitle("TestCache: "+ d.toString());
		response.getWriter().println("TestCache: "+ d.toString());
	}

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
    throws PortletException, IOException {
  
  }  
  
}
