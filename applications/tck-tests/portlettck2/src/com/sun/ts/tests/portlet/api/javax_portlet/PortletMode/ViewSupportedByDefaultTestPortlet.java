/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletMode;

import java.io.PrintWriter;
import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the VIEW portlet mode is supported by portlet even 
 * though it is not specified in the descriptor file. One way to test is that
 * doView() method is invoked even though the VIEW portlet mode is not defined
 * for this portlet.
 */
public class ViewSupportedByDefaultTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "ViewSupportedByDefaultTest";

    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        resultWriter.setStatus(ResultWriter.PASS);
        out.println(resultWriter.toString());
    }
}
