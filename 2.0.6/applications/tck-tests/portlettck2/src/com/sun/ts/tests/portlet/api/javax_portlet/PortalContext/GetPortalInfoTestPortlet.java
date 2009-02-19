/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortalContext;

import java.util.StringTokenizer;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortalContext;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;


import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class GetPortalInfoTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetPortalInfoTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME); 
        PortalContext context = request.getPortalContext();

        if (context != null) {
            String info = context.getPortalInfo();

            // it just needs to be a not null value
            if(info != null ) {
			    resultWriter.setStatus(ResultWriter.PASS); 
		    } else {
			    resultWriter.setStatus(ResultWriter.FAIL); 
            	resultWriter.addDetail("Expected: Vender, version number for portal info"); 
            	resultWriter.addDetail("Actual: Obtained null for portal info");
			}
        } else {
			resultWriter.setStatus(ResultWriter.FAIL); 
            resultWriter.addDetail( "Request.getPortalContext() returned null");
        }
		out.println(resultWriter.toString()); 
    }
}
