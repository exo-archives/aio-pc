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
 *	This class uses the getMinorVersion() method to get the minor version
 *  number and compares it with the expected value.
 */

public class GetMinorVersionTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetMinorVersionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletContext context = getPortletContext();
		int minorVersion = 0;
        int result = 0;

        if (context != null) {
            result = context.getMinorVersion();
            if(result == minorVersion ) {
			    resultWriter.setStatus(ResultWriter.PASS);
            } else {
		        resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("getMinorVersion() returned incorrect "
                                      + "result");
                resultWriter.addDetail("Expected result = " + minorVersion); 
                resultWriter.addDetail("Actual result = " + result);
            }
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString());
    }
}
