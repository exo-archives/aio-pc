/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderResponse;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the getContentType() method.
 */
public class GetContentTypeTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetContentTypeTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String expectedResult = "text/html";

        try {
            response.setContentType(expectedResult);
            String result = response.getContentType();

            if ((result != null) && result.equalsIgnoreCase(expectedResult)) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Expected result = " + expectedResult);
                resultWriter.addDetail("Actual result = " + result);
            }
        } catch (IllegalArgumentException e) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(expectedResult + " isn't allowed!");
        }

        PrintWriter out = response.getWriter();
        out.println(resultWriter.toString());
    }
}
