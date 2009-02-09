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
public class ResponseBufferPortletCommitForwardTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME = 
		"ResponseBufferPortletCommitForwardTest";
	
	private static final String SERVLET_PATH = 
		"/ResponseBufferPortletCommitForwardTestServlet";

	@Override
    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");

        PortletRequestDispatcher dispatcher =
        	getPortletContext().getRequestDispatcher(SERVLET_PATH);

        if (dispatcher == null) {
        	PrintWriter out = response.getWriter();
        	ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        	resultWriter.setStatus(ResultWriter.FAIL);
        	resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
        			+ SERVLET_PATH);
        	out.println(resultWriter.toString());
        } 
        else {
        	PrintWriter out = response.getWriter();
        	PortletURLTag urlTag = new PortletURLTag();
        	ResourceURL resourceURL = response.createResourceURL();
        	urlTag.setTagContent(resourceURL.toString());

        	out.println(urlTag.toString());
        	response.flushBuffer();
        	try{
        		dispatcher.forward(request, response);
        	}
        	catch(IllegalStateException e){
        		out = response.getWriter();
        		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        		resultWriter.setStatus(ResultWriter.PASS);
        		out.println(resultWriter.toString());
        	}
        }
    }

	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response) 
	throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PortletRequestDispatcher dispatcher =
	     	getPortletContext().getRequestDispatcher(SERVLET_PATH);
	
	     if (dispatcher == null) {
	         PrintWriter out = response.getWriter();
	         ResultWriter resultWriter = new ResultWriter(TEST_NAME);
	         resultWriter.setStatus(ResultWriter.FAIL);
	         resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
	                                + SERVLET_PATH);
	         out.println(resultWriter.toString());
	     } 
	     else {
			 PrintWriter out = response.getWriter();
	         out.println("Write something, that can be flush.");
	         response.flushBuffer();
	         try{
	        	 dispatcher.forward(request, response);
	         }
	         catch(IllegalStateException e){
	        	 out = response.getWriter();
	    		 ResultWriter resultWriter = new ResultWriter(TEST_NAME);
	    		 resultWriter.setStatus(ResultWriter.PASS);
	    		 out.println(resultWriter.toString());
	         }
	     }
	}
}
