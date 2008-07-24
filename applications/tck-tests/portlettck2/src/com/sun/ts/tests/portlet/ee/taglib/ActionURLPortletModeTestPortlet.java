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
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 * This class tests the portletMode attribute of the actionURL tag.
 */
public class ActionURLPortletModeTestPortlet extends GenericPortlet {
	
	private final String TEST_NAME = "ActionURLPortletModeTest";
    
    private final String SERVLET_NAME="ActionURLPortletModeTestServlet";
    
    
    @Override
    public void processAction(ActionRequest request,
                              ActionResponse actionResponse)
        throws PortletException, IOException {
    }

    
    @Override
    protected void doEdit(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        resultWriter.setStatus(ResultWriter.PASS);
        out.println(resultWriter.toString());
    }
    
    
    @Override
    protected void doView(RenderRequest request, RenderResponse response)
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
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("doView() is called when the "
                                   + "portlet mode is EDIT.");
            out.println(resultWriter.toString());            
        }
    }
}
