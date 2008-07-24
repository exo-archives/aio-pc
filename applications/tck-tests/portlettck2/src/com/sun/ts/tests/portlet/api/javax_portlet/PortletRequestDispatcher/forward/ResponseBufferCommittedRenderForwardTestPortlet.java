/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseBufferCommittedRenderForwardTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME = 
		"ResponseBufferCommittedRenderForwardTest";
	
	private static final String SERVLET_PATH = 
		"/ResponseBufferCommittedForwardTestServlet";


	protected String getServletPath() {
		return SERVLET_PATH;
	}


	protected String getTestName() {
		return TEST_NAME;
	}


	@Override
    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");

        //first Request checks forward(RenderRequest req, RenderResponse res)
        PortletRequestDispatcher dispatcher
        = getPortletContext().getRequestDispatcher(getServletPath());

        if (dispatcher == null) {
        	PrintWriter out = response.getWriter();
        	ResultWriter resultWriter = new ResultWriter(getTestName());
        	resultWriter.setStatus(ResultWriter.FAIL);
        	resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
        			+ getServletPath());
        	out.println(resultWriter.toString());
        } 
        else {

        	dispatcher.forward(request, response);
        	
        	try{	            	 
        		response.resetBuffer();

        		PrintWriter out = response.getWriter();
        		ResultWriter resultWriter = new ResultWriter(getTestName());
        		resultWriter.setStatus(ResultWriter.FAIL);
        		resultWriter.addDetail("Response buffer not committed and closed.");
        		out.println(resultWriter.toString());

        	}catch(IllegalStateException e){
        	}
        	
        }

    }
}
