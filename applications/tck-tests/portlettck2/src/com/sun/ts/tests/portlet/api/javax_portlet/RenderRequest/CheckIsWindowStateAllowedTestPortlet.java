/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.WindowState;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 * A Test for PortletRequest.isWindowStateAllowed(windowState)
 * The NORMAL window state must be supported , so we'll
 * call this method with NORMAL window state and make sure
 * it returns true.
 */


public class CheckIsWindowStateAllowedTestPortlet extends GenericPortlet {

    public static String TEST_NAME="CheckIsWindowStateAllowedTest";
    
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        boolean isAllowed = request.isWindowStateAllowed(WindowState.NORMAL);

        if (isAllowed) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(
                " Every Portlet should allow the NORMAL window state");
        }
        out.println(resultWriter.toString());
    }
}
