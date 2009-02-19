/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.MalformedURLException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	A Negative Test for getResource(path) method where the path doesn't begin 
 *  with a "/"
 */

public class GetResourceMalformedURLExceptionTestPortlet extends GenericPortlet{

	static public String TEST_NAME = "GetResourceMalformedURLExceptionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletContext context = getPortletContext();

        // Illegal path that doesn't begin with 
        String path = "WEB-INF/web.xml"; 

        if (context != null) {
		    try {
        	    URL resourceURL = context.getResource( path );
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Invalid path name that didn't start "
                                     + "with '/'was provided. ");
                resultWriter.addDetail("Test expected a MalformedURLException");
		    } catch (MalformedURLException m) { 
			    resultWriter.setStatus(ResultWriter.PASS);
		    }
        } else {
           resultWriter.setStatus(ResultWriter.FAIL);
           resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString());	
    }
}
