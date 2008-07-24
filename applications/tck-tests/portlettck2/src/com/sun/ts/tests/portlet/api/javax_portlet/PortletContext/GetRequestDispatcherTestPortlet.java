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
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.GenericPortlet;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class uses the getRequestDispatcher() method to create a 
 *  PortletRequestDispatcher object for a given jsp and invokes the
 *  include method.
 */

public class GetRequestDispatcherTestPortlet extends GenericPortlet {

    static public String TEST_NAME = "GetRequestDispatcherTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletContext context = getPortletContext();

        String path = "/GetIncluded.jsp"; 

        if (context != null) {
            PortletRequestDispatcher rd = context.getRequestDispatcher(path);

            if(rd != null ) {
                rd.include(request, response);
            } else {
			    resultWriter.setStatus(ResultWriter.FAIL);
			    resultWriter.addDetail("Cannot get PortletRequestDispatcher" 
                                   + " for " +  path);
            }
        } else {
           resultWriter.setStatus(ResultWriter.FAIL);
           resultWriter.addDetail("getPortletContext() returned null");
        }
       out.println(resultWriter.toString());
    }
}
