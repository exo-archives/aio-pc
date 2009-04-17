/*
 * Copyright 2007 IBM Corporation.
 */

package com.sun.ts.tests.portlet.ee.taglib;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.sun.ts.tests.portlet.common.util.ParameterCheck;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 * This class tests the resourceURL and param tag.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */

public class ResourceURLTestPortlet extends GenericPortlet {
	
	private final String TEST_NAME = "ResourceURLTest";
	
	private final String SERVLET_NAME="ResourceURLTestServlet";

	
	@Override
	public void render(RenderRequest request, RenderResponse response)
    	throws PortletException, IOException {
		
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletRequestDispatcher dispatcher
            = getPortletContext().getNamedDispatcher(SERVLET_NAME);
        
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
        
    	String[] result = request.getParameterValues(TEST_NAME);
    	
        ParameterCheck check = new ParameterCheck(TEST_NAME, resultWriter);
        check.checkParameter(result);
    	
    	out.println(resultWriter.toString());
	}
}

