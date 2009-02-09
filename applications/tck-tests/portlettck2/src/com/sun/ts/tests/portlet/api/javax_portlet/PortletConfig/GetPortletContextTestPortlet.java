/**
 * Copyright 2007 IBM Corporation.
 */
/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletConfig;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class uses the getPortletContext() method to retreive the portlet
 *  context object and from that object it retrieves the minor version number 
 *  and compares it with the expected result.
 */

public class GetPortletContextTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetPortletContextTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME); 
        PortletConfig portletConfig = getPortletConfig();

        if(portletConfig != null ) {
        	PortletContext context = portletConfig.getPortletContext();

            if (context != null) {
        	    int minorVersion = context.getMinorVersion(); 

        	    if (minorVersion == 0) { 
                   resultWriter.setStatus(ResultWriter.PASS); 
                } else {
                   resultWriter.setStatus(ResultWriter.FAIL); 

                   resultWriter.addDetail("getPortletContext() did not return");
                   resultWriter.addDetail(" the correct minor version number.");
                   resultWriter.addDetail("Expected result = 0" );
                   resultWriter.addDetail("Actual result = " + minorVersion);
                }
            } else {
                resultWriter.setStatus(ResultWriter.FAIL); 
                resultWriter.addDetail("getPortletContext() returned null" );
            }
         } else {
            resultWriter.setStatus(ResultWriter.FAIL); 
            resultWriter.addDetail("getPortletConfig() returned null" );
		 }
     out.println(resultWriter.toString()); 
	}
}
