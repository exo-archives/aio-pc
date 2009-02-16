/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the getScheme() method.
 */
public class GetSchemeTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetSchemeTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String expectedResult = "http";
        String result = request.getScheme();

        if ((result != null) && result.equalsIgnoreCase(expectedResult)) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }

        out.println(resultWriter.toString());
    }
}
