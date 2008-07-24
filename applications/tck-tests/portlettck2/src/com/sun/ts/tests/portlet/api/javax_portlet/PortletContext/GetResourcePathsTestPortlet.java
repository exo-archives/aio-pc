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
import java.util.Set;
import java.util.Iterator;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class test getResourcePaths(path) method.
 */
public class GetResourcePathsTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetResourcePathsTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletContext context = getPortletContext();

        String path = "/WEB-INF";
        int count = 0;
        int expectedCount = 2;

        if (context != null) {
            Set resourcePaths = context.getResourcePaths( path );

            if(resourcePaths != null && resourcePaths.size() > 0 ) {
		    	for (Iterator it=resourcePaths.iterator(); it.hasNext();) {
		    		String value = (String) it.next();
                    if ((value.equals("/WEB-INF/resource.xml")) ||
                         (value.equals("/WEB-INF/web.xml"))) {
                            count++;
                    }
			    }
                if (count == expectedCount) {
		    	   resultWriter.setStatus(ResultWriter.PASS);
                } else {
		    	   resultWriter.setStatus(ResultWriter.FAIL);
		    	   resultWriter.addDetail(
                                "Expected to find /WEB-INF/resource.xml "
                                 + "and /WEB-INF/web.xml in the list returned.");
                }
            } else {
		    	resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( " Could not get resource paths");     
            }
        } else {
           resultWriter.setStatus(ResultWriter.FAIL);
           resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString());	
    }
}
