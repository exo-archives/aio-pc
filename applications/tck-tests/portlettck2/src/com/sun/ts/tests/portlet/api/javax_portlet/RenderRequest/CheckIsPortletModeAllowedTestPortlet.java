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
import javax.portlet.PortletMode;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 *	A  Test for PortletRequest.isPortletModeAllowed(portletMode)
 *   The VIEW portlet mode must be supported , so we'll call
 *                 this method with VIEW portlet mode and make sure it
 *                 returns true.
 */


public class CheckIsPortletModeAllowedTestPortlet extends GenericPortlet {

    public static String TEST_NAME="CheckIsPortletModeAllowedTest";
    
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        boolean isAllowed = request.isPortletModeAllowed(PortletMode.VIEW);

        if (isAllowed) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(
                " Every Portlet should allow the VIEW portlet mode");
        }
        out.println(resultWriter.toString());
    }
}
