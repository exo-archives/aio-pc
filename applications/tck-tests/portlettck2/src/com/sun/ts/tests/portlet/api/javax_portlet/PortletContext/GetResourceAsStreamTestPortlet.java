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
import java.io.InputStream;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class tests for getResourceAsStream() method.
 */
public class GetResourceAsStreamTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetResourceAsStreamTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletContext context = getPortletContext();

        String path = "/WEB-INF/web.xml";

        if (context != null) {
            InputStream in = context.getResourceAsStream( path );

            if(in != null ) {
			    resultWriter.setStatus(ResultWriter.PASS);
            } else {
		    	resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("PortletContext.getResourceAsStream(" 
                                           + path + ") returned a null" );
            }
        } else {
           resultWriter.setStatus(ResultWriter.FAIL);
           resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString());
    }
}
