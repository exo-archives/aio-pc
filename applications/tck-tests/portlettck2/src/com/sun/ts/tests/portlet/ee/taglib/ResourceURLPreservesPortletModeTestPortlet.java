/*
 * Copyright 2007 IBM Corporation.
 */

package com.sun.ts.tests.portlet.ee.taglib;

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

import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 * This class tests whether the resourceURL tag preserves the current portled mode.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */
public class ResourceURLPreservesPortletModeTestPortlet extends GenericPortlet {
	
	private final String TEST_NAME = "ResourceURLPreservesPortletModeTest";
	
	private final String SERVLET_NAME = "ResourceURLPreservesPortletModeTestServlet";

	
	@Override
	public void render(RenderRequest request, RenderResponse response)
    	throws PortletException, IOException {
		
        response.setContentType("text/html");
        
        String portletMode = request.getPortletMode().toString();
        PortletSession session = request.getPortletSession();
        session.setAttribute(TEST_NAME,portletMode);
        
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletRequestDispatcher dispatcher = 
        	getPortletContext().getNamedDispatcher(SERVLET_NAME);
        
        if (dispatcher == null) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
                                   + SERVLET_NAME);
            out.println(resultWriter.toString());
        } 
        else {
        	dispatcher.include(request, response);
        }
	}
	
	
	@Override
    public void serveResource(ResourceRequest request, ResourceResponse response)
		throws PortletException, IOException {
    	
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        
        PortletSession session = request.getPortletSession();
        String expectedPortletMode = (String)session.getAttribute(TEST_NAME);
        
        String portletMode = request.getPortletMode().toString();
        
        if((portletMode != null) && (expectedPortletMode != null) && 
        		(portletMode.equals(expectedPortletMode))){
        	resultWriter.setStatus(ResultWriter.PASS);
        }
        else{
        	resultWriter.setStatus(ResultWriter.FAIL);
        	resultWriter.addDetail("The portlet mode has changed illegally!");
        	resultWriter.addDetail("Expected portlet mode: "+expectedPortletMode);
        	resultWriter.addDetail("Actual portlet mode: "+portletMode);
        }
    	
    	out.println(resultWriter.toString());
	}
}

