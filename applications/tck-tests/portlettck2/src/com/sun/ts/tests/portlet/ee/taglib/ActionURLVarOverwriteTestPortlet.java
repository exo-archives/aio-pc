/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.ee.taglib;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 * This class tests that if the variable name given to the actionURL
 * tag already exists in the scope of the page, the new value
 * overwrites the old one.
 */
public class ActionURLVarOverwriteTestPortlet extends GenericPortlet {
	
    private final String TEST_NAME = "ActionURLVarOverwriteTest";
    
    private final String SERVLET_NAME="ActionURLVarOverwriteTestServlet";

    
    @Override
    public void processAction(ActionRequest request, ActionResponse actionResponse)
        throws PortletException, IOException {

        PortletSession session = request.getPortletSession();
        session.setAttribute(TEST_NAME, request.getParameter(TEST_NAME));
    }

    
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
            PortletSession session = request.getPortletSession();
            String result = (String)session.getAttribute(TEST_NAME);
            session.removeAttribute(TEST_NAME);

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
