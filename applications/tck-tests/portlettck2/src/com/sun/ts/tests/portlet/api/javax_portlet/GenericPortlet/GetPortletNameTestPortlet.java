/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.GenericPortlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the getPortletName() method.
 */
public class GetPortletNameTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetPortletNameTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String expectedResult = TEST_NAME + "Portlet";
        String result = getPortletName();

        if ((result != null) && result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }

        out.println(resultWriter.toString());
    }
}
