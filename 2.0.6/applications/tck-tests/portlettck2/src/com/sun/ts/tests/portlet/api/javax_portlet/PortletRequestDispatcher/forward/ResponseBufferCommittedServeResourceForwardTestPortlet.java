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
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseBufferCommittedServeResourceForwardTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME = 
		"ResponseBufferCommittedServeResourceForwardTest";
	
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
		
		PrintWriter out = response.getWriter();		
		
		//create ResourceURL
		PortletURLTag urlTag = new PortletURLTag();
		ResourceURL resourceURL = response.createResourceURL();
		urlTag.setTagContent(resourceURL.toString());

		out.println(urlTag.toString());		
		
    }

	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
		//third Request
		response.setContentType("text/html");
		
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
	             resultWriter.addDetail("Response buffer not committed");
	             out.println(resultWriter.toString());
             }
             catch(IllegalStateException e){}
	     }
	}
}
