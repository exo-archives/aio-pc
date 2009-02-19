/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.io.PrintWriter;


import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	Test for PortletSession.getPortletContext() method
 */



public class GetPortletContextTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetPortletContextTest";

    /**
     *	Get the minor version from the portlet context object 
     */


    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME); 
        PortletSession portletSession = request.getPortletSession( true );


        if(portletSession != null ) {
        	PortletContext context = portletSession.getPortletContext();
		    int expectedMinorVersion = 0; // Dummy value
        	int minorVersion = context.getMinorVersion(); 

        	if (minorVersion == expectedMinorVersion) { 
                resultWriter.setStatus(ResultWriter.PASS); 
            } else {
                resultWriter.setStatus(ResultWriter.FAIL); 

                resultWriter.addDetail(
                    "PortletSession.getPortletContext did not ");
                resultWriter.addDetail(
                    "return the correct minor version number " );
                resultWriter.addDetail(
                    "Expected result = " + expectedMinorVersion);
                resultWriter.addDetail("Actual result = " + minorVersion);
            }

        } else {
            resultWriter.setStatus(ResultWriter.FAIL); 
            resultWriter.addDetail("getPortletSession returned null?" );
		}
        out.println(resultWriter.toString()); 
	}
}
