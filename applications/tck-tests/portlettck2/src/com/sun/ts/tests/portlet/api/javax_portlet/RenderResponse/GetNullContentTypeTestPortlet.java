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
 * This class tests that getContentType() returns null without setting 
 * the content type.
 */
public class GetNullContentTypeTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetNullContentTypeTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String result = response.getContentType();

        if (result == null) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected result = null");
            resultWriter.addDetail("Actual result = " + result);
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(resultWriter.toString());
    }
}
