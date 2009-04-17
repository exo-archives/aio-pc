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

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that if the variable name given to the resourceURL
 * tag already exists in the scope of the page, the new value
 * overwrites the old one.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */
public class ResourceURLVarOverwriteTestPortlet extends GenericPortlet {
	
	private final String TEST_NAME = "ResourceURLVarOverwriteTest";
    
    private final String SERVLET_NAME="ResourceURLVarOverwriteTestServlet";

    
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
    	String expectedResult = TEST_NAME;
        String result = request.getParameter(TEST_NAME);
         
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        if ((result != null) && result.equals(expectedResult)) {
        	resultWriter.setStatus(ResultWriter.PASS);
        } 
        else {
        	resultWriter.setStatus(ResultWriter.FAIL);
        	resultWriter.addDetail("Expected result = " + expectedResult);
        	resultWriter.addDetail("Actual result = " + result);
        }

        PrintWriter out = response.getWriter();
        out.println(resultWriter.toString());  
    }
}
