/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.ee.taglib;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 * This class tests the var attribute of the renderURL tag.
 */
public class RenderURLVarTestPortlet extends GenericPortlet {
	
	private final String TEST_NAME = "RenderURLVarTest";
    
	private final String SERVLET_NAME="RenderURLVarTestServlet";
	

	@Override
    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        RequestCount requestCount = 
        	new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (requestCount.isFirstRequest()) {

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
        else {
            String expectedResult = TEST_NAME;
            String result = request.getParameter(TEST_NAME);

            if ((result != null) && result.equals(expectedResult)) {
                resultWriter.setStatus(ResultWriter.PASS);
            } 
            else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Expected result = " + expectedResult);
                resultWriter.addDetail("Actual result = " + result);
            }

            out.println(resultWriter.toString());            
        }
    }
}
