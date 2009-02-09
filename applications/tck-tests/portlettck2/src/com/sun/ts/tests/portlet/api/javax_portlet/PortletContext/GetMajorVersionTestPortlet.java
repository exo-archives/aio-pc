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

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class uses the getMajorVersion() method to get the major version
 *  number and compares it with the expected value.
 */

public class GetMajorVersionTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetMajorVersionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletContext context = getPortletContext();
		int majorVersion = 2;

        if (context != null) {
            if (context.getMajorVersion() == majorVersion ) {
			    resultWriter.setStatus(ResultWriter.PASS);
            } else {
			    resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("getMajorVersion() returned "
                                            + context.getMajorVersion());
                resultWriter.addDetail("Expected getMajorVersion() = " 
                                            + majorVersion);
            }
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString());
    }
}
