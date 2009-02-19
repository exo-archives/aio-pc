/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class PathNotSetServeResourceForwardTestPortlet extends GenericPortlet {
    
	private final String TEST_NAME = 
		"PathNotSetServeResourceForwardTest";
	
	private final String SERVLET_NAME = 
		"PathNotSetForwardTestServlet";
	
    //session attribute names
	private final String TEST_NAME_ATTR = "testname";	
	private final String PRINT_RESULT_ATTR = "printResult";
	
    
    /**
     * Prepares the forward e.g. sets attribute in the session.
     * 
     * @param session 
     * @param request 
     * @param response 
     */
    protected void prepareForward(PortletSession session, 
    		ResourceRequest request, ResourceResponse response){
    	
		//write the test name to the session
        session.setAttribute(TEST_NAME_ATTR, TEST_NAME,
        		PortletSession.APPLICATION_SCOPE);
        
        //servlet writes the result  
        session.setAttribute(PRINT_RESULT_ATTR, true,
        		PortletSession.APPLICATION_SCOPE);
    }
    
 
    
    /*
     * Creates and writes out an ResourceURL with the 
     * parameters supplied by getParametersMap().
     */
    @Override
    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		//create ResourceURL
		PortletURLTag urlTag = new PortletURLTag();
		ResourceURL resourceURL = response.createResourceURL();
		urlTag.setTagContent(resourceURL.toString());

		out.println(urlTag.toString());
		
    }

    
    /*
     * Obtains a PortletRequestDispatcher. If this succeeded invokes 
     * prepareForward(), dispatcher.forward() and checkForwardResults().
     * If the servlet does not print the result an extra trip is needed
     * to print results!
     */
	@Override
	public void serveResource(ResourceRequest request, 
			ResourceResponse response) throws PortletException, IOException {
		
		PortletSession session = request.getPortletSession(true);
		
		PortletRequestDispatcher dispatcher =
        	getPortletContext().getNamedDispatcher(SERVLET_NAME);

		if (dispatcher == null) {
			response.setContentType("text/html");

			PrintWriter out = response.getWriter();

			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			resultWriter.setStatus(ResultWriter.FAIL);

			resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
					+ SERVLET_NAME);

			out.println(resultWriter.toString());
		} 
		else {

			//save data for the servlet in the session
			prepareForward(session, request, response);

			dispatcher.forward(request, response);


		}	

	}
    
    
}
