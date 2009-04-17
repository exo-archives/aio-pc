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

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class PathNotSetRenderForwardTestPortlet 
	extends GenericPortlet {
   
	private final String TEST_NAME = 
		"PathNotSetRenderForwardTest";
	
	private final String SERVLET_NAME = 
		"PathNotSetForwardTestServlet";
	
    //session attribute names
	private final String TEST_NAME_ATTR = "testname";	
	private final String PRINT_RESULT_ATTR = "printResult";
	
	   
    /**
     * Perpares the session before the servlet will be included.
     * 
     * @param session 
     * @param request 
     */
    private void prepareForward(PortletSession session, 
    		RenderRequest request, RenderResponse response){
    	
    	//write the test name to the session
        session.setAttribute(TEST_NAME_ATTR, TEST_NAME, 
        		PortletSession.APPLICATION_SCOPE);
        
        //servlet writes the result  
        session.setAttribute(PRINT_RESULT_ATTR, true, 
        		PortletSession.APPLICATION_SCOPE);    	
    }
    
    

       
    @Override
    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {
		
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
			prepareForward(session, request, response);

			dispatcher.forward(request, response);

		}

       
    }
}
