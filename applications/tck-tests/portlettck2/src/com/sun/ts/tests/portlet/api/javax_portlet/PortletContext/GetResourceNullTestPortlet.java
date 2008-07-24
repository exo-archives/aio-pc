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
import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class tests getResource(path) method for an invalid path.
 */

public class GetResourceNullTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetResourceNullTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletContext context = getPortletContext();

        String path = "/WEB-INF/import javax.portlet.RenderRequest;.xml";

        if (context != null) {
		    try {
        	    URL resourceURL = context.getResource( path );

        	    if(resourceURL != null ) {
			    	resultWriter.setStatus(ResultWriter.FAIL);
			    	resultWriter.addDetail("Expected a non-null value for the " 
                                        + "URL as an invalid path was given as "
                                        + "an argument");
        	    } else {
				    resultWriter.setStatus(ResultWriter.PASS);
        	    }
		    } catch (MalformedURLException m) { 
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("MalformedException thrown while "
                                     + "invoking the getResource() method");
                resultWriter.addDetail(m.getMessage());
		    }
        } else {
           resultWriter.setStatus(ResultWriter.FAIL);
           resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString());	
    }
}
